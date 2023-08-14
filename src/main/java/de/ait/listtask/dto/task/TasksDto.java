package de.ait.listtask.dto.task;

import de.ait.listtask.model.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "List of tasks")
public class TasksDto {
  @Schema(description = "List of tasks")
  private List<TaskDto> tasks;
  @Schema(description = "Tasks count")
  private Long count;
  @Schema(description = "Total count page", example = "3")
  private Integer pagesCount;

  public static TasksDto convertToTasksDto(List<Task> taskList) {
    return TasksDto.builder()
        .tasks(taskList.stream().map(TaskDto::convertToTaskDto).collect(Collectors.toList()))
        .build();

  }

  public static List<TaskDto> convertToListTaskDto(List<Task> taskList) {
    return taskList.stream()
        .map(TaskDto::convertToTaskDto)
        .collect(Collectors.toList());


  }

}
