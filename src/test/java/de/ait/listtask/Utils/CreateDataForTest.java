package de.ait.listtask.Utils;

import de.ait.listtask.dto.task.TaskDto;
import de.ait.listtask.dto.user.UserDto;
import de.ait.listtask.model.User;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class CreateDataForTest {

  public static List<TaskDto> createExpectedTask(JdbcTemplate jdbcTemplate, String sql) {
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

  public static List<UserDto> createExpectedUsersDto(JdbcTemplate jdbcTemplate,String sql) {
    return new DatabaseUtils(jdbcTemplate)
        .executeQueryAndConvertToList(sql, (resultSet, rowNum) -> UserDto.builder()
            .id(resultSet.getLong("id"))
            .email(resultSet.getString("email"))
            .role(User.Role.valueOf(resultSet.getString("role")))
            .state(User.State.valueOf(resultSet.getString("state")))
            .build());
  }

  public static Integer expectedTotalCountRow(JdbcTemplate jdbcTemplate, String sql) {
    return jdbcTemplate.queryForObject(sql, Integer.class);
  }

  public  static String getRandomDateStart(JdbcTemplate jdbcTemplate) {
    return jdbcTemplate.queryForObject(
        "SELECT START_DATE FROM TASK ORDER BY RANDOM() LIMIT 1",
        (resultSet, rowNum) -> resultSet.getString("START_DATE")
    );
  }
  public static  String getRandomUserId(JdbcTemplate jdbcTemplate) {
    return jdbcTemplate.queryForObject(
        "SELECT ID FROM ACCOUNT ORDER BY RANDOM() LIMIT 1",
        (resultSet, rowNum) -> resultSet.getString("ID")
    );
  }

  public  static  String getNotExitUserId(JdbcTemplate jdbcTemplate) {
    return jdbcTemplate.queryForObject(
        "SELECT max(ID) AS max_id FROM ACCOUNT",
        (resultSet, rowNum) -> {
          int maxIdUser = resultSet.getInt("max_id");
          return String.valueOf(maxIdUser + 1);
        });

  }
}
