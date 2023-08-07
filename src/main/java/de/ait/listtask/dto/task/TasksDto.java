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
  private Integer count;

  public static TasksDto convertTasksLisToDto(List<Task> taskList) {
    return TasksDto.builder()
        .tasks(taskList.stream().map(TaskDto::convertToTaskDto).collect(Collectors.toList()))
        .count(taskList.size())
        .build();

  }
}
