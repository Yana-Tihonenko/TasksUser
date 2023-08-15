package de.ait.listtask.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.ait.listtask.dto.task.TaskDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static de.ait.listtask.controllers.Utils.CreateDataForTest.*;
import static de.ait.listtask.controllers.Utils.JsonUtils.getJsonNode;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TaskController is works: ")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
@ActiveProfiles("test")
public class TaskIntegrationTest {
  private final JdbcTemplate jdbcTemplate;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  ObjectMapper objectMapper;
  @Value("${global.page.size}")
  private Integer pageSize;

  @Autowired
  public TaskIntegrationTest(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Test
  @Sql(scripts = "/sql/users.sql")
  @Sql(scripts = "/sql/tasks.sql")
    //@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  void get_tasks_firstPage_sortByDescription_desc() throws Exception {
    MvcResult resultRequest = mockMvc.perform(get("/api/tasks?page=0&orderBy=description&desc=true"))
        .andExpect(status().isOk())
        .andReturn();

    JsonNode jsonNode = getJsonNode(resultRequest);
    JsonNode tasksNode = jsonNode.get("tasks");
    String sqlTasks = "SELECT * FROM TASK ORDER BY DESCRIPTION DESC LIMIT " + pageSize;
    List<TaskDto> expectedTasks = createExpectedTask(jdbcTemplate, sqlTasks);
    String sqlTotalCountRow = "SELECT count(*) FROM TASK";
    Integer totalCountRow = expectedTotalCountRow(jdbcTemplate, sqlTotalCountRow);
    for (int i = 0; i < tasksNode.size(); i++) {
      JsonNode taskNode = tasksNode.get(i);
      TaskDto expectedTask = expectedTasks.get(i);
      assertThat(taskNode.get("id").asLong(), is(expectedTask.getId()));
      assertThat(taskNode.get("description").asText(), is(expectedTask.getDescription()));
      assertThat(taskNode.get("title").asText(), is(expectedTask.getTitle()));
      assertThat(taskNode.get("startDate").asText(), is(expectedTask.getStartDate()));
      assertThat(taskNode.get("finishDate").asText(), is(expectedTask.getFinishDate()));
    }
    assertEquals(jsonNode.get("count").asInt(), totalCountRow);
  }

  @Test
  void get_tasks_firstPage_sortByTitle_filterByStartDate() throws Exception {
    String getRandomStartDate = getRandomDateStart(jdbcTemplate);
    MvcResult resultRequest = mockMvc.perform
            (get("/api/tasks?page=0&orderBy=title&filterBy=startDate&filterValue={start_date},getRandomStartDate ")
                .param("start_date", getRandomStartDate))
        .andExpect(status().isOk())
        .andReturn();

    JsonNode jsonNode = getJsonNode(resultRequest);
    JsonNode tasksNode = jsonNode.get("tasks");
    String sqlTasks = "SELECT * FROM TASK WHERE START_DATE="+getRandomStartDate+" ORDER BY TITLE LIMIT " + pageSize;
    List<TaskDto> expectedTasks = createExpectedTask(jdbcTemplate, sqlTasks);
    String sqlTotalCountRow = "SELECT count(*) FROM TASK WHERE START_DATE="+getRandomStartDate;
    Integer totalCountRow = expectedTotalCountRow(jdbcTemplate, sqlTotalCountRow);
    for (int i = 0; i < tasksNode.size(); i++) {
      JsonNode taskNode = tasksNode.get(i);
      TaskDto expectedTask = expectedTasks.get(i);
      assertThat(taskNode.get("id").asLong(), is(expectedTask.getId()));
      assertThat(taskNode.get("description").asText(), is(expectedTask.getDescription()));
      assertThat(taskNode.get("title").asText(), is(expectedTask.getTitle()));
      assertThat(taskNode.get("startDate").asText(), is(expectedTask.getStartDate()));
      assertThat(taskNode.get("finishDate").asText(), is(expectedTask.getFinishDate()));
    }
    assertEquals(jsonNode.get("count").asInt(), totalCountRow);
  }
}
