package de.ait.listtask.Utils;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class DatabaseUtils {
  private final JdbcTemplate jdbcTemplate;

  public DatabaseUtils(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public <T> List<T> executeQueryAndConvertToList(String sql, RowMapper<T> rowMapper) {
    return jdbcTemplate.query(sql, rowMapper);
  }


}
