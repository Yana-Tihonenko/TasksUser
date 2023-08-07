package de.ait.listtask.dto.task;

import de.ait.listtask.model.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data task")
public class TaskDto {
  @Schema(description = "Id of task", example = "1")
  Long id;
  @Schema(description = "Description of task", example = "Develop a data model")
  String description;
  @Schema(description = "Title of task", example = "A data model")
  String title;
  @Schema(description = "Date of start", example = "2023-09-23")
  String startDate;
  @Schema(description = "Date of finish", example = "2023-09-25")
  String finishDate;
  @Schema(description = "Id user", example = "1")
  String userId;

  public static TaskDto convertToTaskDto(Task task) {
    return TaskDto.builder()
        .id(task.getId())
        .description(task.getDescription())
        .title(task.getTitle())
        .startDate(task.getStartDate().toString())
        .finishDate(task.getFinishDate().toString())
        .userId(task.getExecutor().getId().toString())
        .build();

  }
}
