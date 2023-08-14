package de.ait.listtask.controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.ait.listtask.controllers.Utils.DatabaseUtils;
import de.ait.listtask.dto.task.NewTaskDto;
import de.ait.listtask.dto.task.TaskDto;
import de.ait.listtask.dto.user.NewUserDto;
import de.ait.listtask.dto.user.UserDto;
import de.ait.listtask.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static de.ait.listtask.controllers.Utils.JsonUtils.getJsonNode;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("UsersController is works: ")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
@ActiveProfiles("test")
public class UserIntegrationTest {

  @Autowired
  JdbcTemplate jdbcTemplate;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  ObjectMapper objectMapper;


  @Nested
  @DisplayName("Post/user is work:")
  class AddUser {
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void add_user_positive() throws Exception {

      String body = objectMapper.writeValueAsString(NewUserDto.builder()
          .password("1234qwe!")
          .email("tihonenkoyana@gmail.com").build());
      mockMvc.perform(post("/users")
              .header("Content-Type", "application/json")
              .content(body))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.id", is(1)))
          .andExpect(jsonPath("$.email", is("tihonenkoyana@gmail.com")))
          .andExpect(jsonPath("$.role", is("USER")))
          .andExpect(jsonPath("$.state", is("NOT_CONFIRMED")));

    }
  }

  @Nested
  @DisplayName("Post /{user-id}/tasks is work:")
  class AddTask {

    @Test
    @Sql(scripts = "/sql/users.sql")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void add_new_task_positive() throws Exception {
      String dateStart = LocalDate.now().plusDays(2).toString();
      String dateFinish = LocalDate.now().plusWeeks(1).toString();
      String randomUserIdForTest = getRandomUserId();
      String body = objectMapper.writeValueAsString(NewTaskDto.builder()
          .description("description")
          .title("title")
          .startDate(dateStart)
          .finishDate(dateFinish)
          .build());

      mockMvc.perform(post("/users/{user_id}/tasks", randomUserIdForTest)
              .param("user_id", randomUserIdForTest)
              .header("Content-Type", "application/json")
              .content(body))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.id", is(1)))
          .andExpect(jsonPath("$.description", is("description")))
          .andExpect(jsonPath("$.title", is("title")))
          .andExpect(jsonPath("$.startDate", is(dateStart)))
          .andExpect(jsonPath("$.finishDate", is(dateFinish)))
          .andExpect(jsonPath("$.userId", is(randomUserIdForTest)));
    }

    @Test
    @Sql(scripts = "/sql/users.sql")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void addTask_not_exit_user() throws Exception {
      String dateStart = LocalDate.now().plusDays(2).toString();
      String dateFinish = LocalDate.now().plusWeeks(1).toString();
      String notExitUserId = getNotExitUserId();
      String body = objectMapper.writeValueAsString(NewTaskDto.builder()
          .description("description")
          .title("title")
          .startDate(dateStart)
          .finishDate(dateFinish)
          .build());

      mockMvc.perform(post("/users/{user_id}/tasks", notExitUserId)
              .param("user_id", notExitUserId)
              .header("Content-Type", "application/json")
              .content(body))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.errorCode", is("100")))
          .andExpect(jsonPath("$.message", is("Not found user")))
          .andExpect(jsonPath("$.rejectedValue", is(notExitUserId)));

    }

    @Test
    @Sql(scripts = "/sql/users.sql")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void addTask_with_invalid_format_startDate() throws Exception {
      String dateFinish = LocalDate.now().plusMonths(2).toString();
      String randomUserIdForTest = getRandomUserId();
      String body = objectMapper.writeValueAsString(NewTaskDto.builder()
          .description("description")
          .title("title")
          .startDate("20223-08-15")
          .finishDate(dateFinish)
          .build());

      mockMvc.perform(post("/users/{user_id}/tasks", randomUserIdForTest)
              .param("user_id", randomUserIdForTest)
              .header("Content-Type", "application/json")
              .content(body))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errorCode", is("101")))
          .andExpect(jsonPath("$.message", is("Wrong date format")))
          .andExpect(jsonPath("$.rejectedValue", is("20223-08-15")));

    }

    @Test
    @Sql(scripts = "/sql/users.sql")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void addTask_with_invalid_format_finishDate() throws Exception {
      String dateStart = LocalDate.now().plusMonths(2).toString();
      String randomUserIdForTest = getRandomUserId();
      String body = objectMapper.writeValueAsString(NewTaskDto.builder()
          .description("description")
          .title("title")
          .startDate(dateStart)
          .finishDate("20223-08-15")
          .build());

      mockMvc.perform(post("/users/{user_id}/tasks", randomUserIdForTest)
              .param("user_id", randomUserIdForTest)
              .header("Content-Type", "application/json")
              .content(body))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errorCode", is("101")))
          .andExpect(jsonPath("$.message", is("Wrong date format")))
          .andExpect(jsonPath("$.rejectedValue", is("20223-08-15")));
    }

    @Test
    @Sql(scripts = "/sql/users.sql")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void addTask_with_startDate_lessThan_dataNow() throws Exception {
      String dateStart = LocalDate.now().minusDays(2).toString();
      String dateFinish = LocalDate.now().plusMonths(1).toString();
      String randomUserIdForTest = getRandomUserId();
      String body = objectMapper.writeValueAsString(NewTaskDto.builder()
          .description("description")
          .title("title")
          .startDate(dateStart)
          .finishDate(dateFinish)
          .build());

      mockMvc.perform(post("/users/{user_id}/tasks", randomUserIdForTest)
              .param("user_id", randomUserIdForTest)
              .header("Content-Type", "application/json")
              .content(body))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errors[0].field", is("startDate")))
          .andExpect(jsonPath("$.errors[0].message", is("The date cannot be less than the current date")))
          .andExpect(jsonPath("$.errors[0].rejectedValue", is(dateStart)));

    }

    //TODO подумать как обработать и проверить массив ошибок
//    @Test
//    void addTask_with_finishDate_lessThan_dataNow() throws Exception {
//      String body = objectMapper.writeValueAsString(NewTaskDto.builder()
//          .description("description")
//          .title("title")
//          .startDate("2023-08-11")
//          .finishDate("2023-08-11")
//          .build());
//
//      mockMvc.perform(post("/users/1/tasks")
//              .header("Content-Type", "application/json")
//              .content(body))
//          .andExpect(status().isBadRequest())
//          .andExpect(jsonPath("$.errors[0].field", is("startDate")))
//          .andExpect(jsonPath("$.errors[0].message", is("The date cannot be less than the current date")))
//          .andExpect(jsonPath("$.errors[0].rejectedValue", is("2023-08-10")));
//
//    }
    @Test
    @Sql(scripts = "/sql/users.sql")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void addTask_with_finishDate_lessThan_startDate() throws Exception {

      String dateStart = LocalDate.now().plusDays(2).toString();
      String dateFinish = LocalDate.now().plusDays(1).toString();
      String randomUserIdForTest = getRandomUserId();
      String body = objectMapper.writeValueAsString(NewTaskDto.builder()
          .description("description")
          .title("title")
          .startDate(dateStart)
          .finishDate(dateFinish)
          .build());

      mockMvc.perform(post("/users/{user_id}/tasks", randomUserIdForTest)
              .param("user_id", randomUserIdForTest)
              .header("Content-Type", "application/json")
              .content(body))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errors[0].message", is("Start date cannot be later than end date")));

    }
  }

  @Nested
  @DisplayName("GET /users is works:")
  class GetAllUsers {
    @Test
    @Sql(scripts = "/sql/users.sql")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void get_all_users() throws Exception {
      MvcResult resultRequest = mockMvc.perform(get("/users"))
          .andExpect(status().isOk())
          .andReturn();

      JsonNode jsonNode = getJsonNode(resultRequest);
      JsonNode usersNode = jsonNode.get("users");
      List<UserDto> expectedUsers = createExpectedUsersDto("SELECT * FROM ACCOUNT");
      for (int i = 0; i < usersNode.size(); i++) {
        JsonNode userNode = usersNode.get(i);
        UserDto expectedUser = expectedUsers.get(i);
        assertThat(userNode.get("id").asLong(), is(expectedUser.getId()));
        assertThat(userNode.get("email").asText(), is(expectedUser.getEmail()));
        assertThat(User.Role.valueOf(userNode.get("role").asText()), is(expectedUser.getRole()));
        assertThat(User.State.valueOf(userNode.get("state").asText()), is(expectedUser.getState()));
      }

      assertEquals(expectedUsers.size(), jsonNode.get("count").asInt());

    }
  }

  @Nested
  @DisplayName("GET /{user_id}/tasks is works:")
  class GetAllTaskByUser {
    @Test
    @Sql(scripts = "/sql/users.sql")
    @Sql(scripts = "/sql/tasks.sql")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void get_all_tasks_by_users_positiv() throws Exception {

      String randomUserIdForTest = getRandomUserId();

      MvcResult resultRequest = mockMvc.perform(get("/users/{user_id}/tasks", randomUserIdForTest)
              .param("user_id", randomUserIdForTest))
          .andExpect(status().isOk())
          .andReturn();


      JsonNode jsonNode = getJsonNode(resultRequest);
      JsonNode tasksNode = jsonNode.get("tasks");
      String sql = "SELECT * FROM TASK WHERE USER_ID=" + randomUserIdForTest;
      List<TaskDto> expectedTasksByUser = createExpectedTaskByUser(sql);
      for (int i = 0; i < tasksNode.size(); i++) {
        JsonNode taskNode = tasksNode.get(i);
        TaskDto expectedTaskByUser = expectedTasksByUser.get(i);
        assertThat(taskNode.get("id").asLong(), is(expectedTaskByUser.getId()));
        assertThat(taskNode.get("description").asText(), is(expectedTaskByUser.getDescription()));
        assertThat(taskNode.get("title").asText(), is(expectedTaskByUser.getTitle()));
        assertThat(taskNode.get("startDate").asText(), is(expectedTaskByUser.getStartDate()));
        assertThat(taskNode.get("finishDate").asText(), is(expectedTaskByUser.getFinishDate()));

      }

      assertEquals(expectedTasksByUser.size(), jsonNode.get("count").asInt());

    }

    @Test
    @Sql(scripts = "/sql/users.sql")
    @Sql(scripts = "/sql/tasks.sql")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void get_all_tasks_by_not_exit_users() throws Exception {
      String notExitUserId = getNotExitUserId();
       mockMvc.perform(get("/users/{user_id}/tasks", notExitUserId)
          .param("user_id", notExitUserId))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath("$.errorCode", is("100")))
           .andExpect(jsonPath("$.message", is("Not found user")))
           .andExpect(jsonPath("$.rejectedValue", is(notExitUserId)));
    }
  }

  public List<UserDto> createExpectedUsersDto(String sql) {
    return new DatabaseUtils(jdbcTemplate)
        .executeQueryAndConvertToList(sql, (resultSet, rowNum) -> UserDto.builder()
            .id(resultSet.getLong("id"))
            .email(resultSet.getString("email"))
            .role(User.Role.valueOf(resultSet.getString("role")))
            .state(User.State.valueOf(resultSet.getString("state")))
            .build());
  }

  public List<TaskDto> createExpectedTaskByUser(String sql) {
    return new DatabaseUtils(jdbcTemplate)
        .executeQueryAndConvertToList(sql, (resultSet, rowNum) -> TaskDto.builder()
            .id(resultSet.getLong("id"))
            .description(resultSet.getString("description"))
            .title(resultSet.getString("title"))
            .startDate(resultSet.getString("start_date"))
            .finishDate(resultSet.getString("finish_date"))
            .userId(resultSet.getString("user_id"))
            .build());
  }

  public String getRandomUserId() {
    return jdbcTemplate.queryForObject(
        "SELECT ID FROM ACCOUNT ORDER BY RANDOM() LIMIT 1",
        (resultSet, rowNum) -> resultSet.getString("ID")
    );
  }

  public String getNotExitUserId() {
    return jdbcTemplate.queryForObject(
        "SELECT max(ID) AS max_id FROM ACCOUNT",
        (resultSet, rowNum) -> {
          int maxIdUser = resultSet.getInt("max_id");
          return String.valueOf(maxIdUser + 1);
        });

  }
}




