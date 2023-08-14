package de.ait.listtask.dto.task;

import de.ait.listtask.validation.constraints.BeforeCurrentDate;
import de.ait.listtask.validation.constraints.FinishDateLessThanStartDate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@Schema(description = "Data for adding a new task")
@FinishDateLessThanStartDate
public class NewTaskDto {
  @NotNull(message = "Field Description can't be empty")
  @NotBlank(message = "Field Description can't be null")
  @Schema(description = "Description of task", example = "Develop a data model")
  String description;

  @NotNull(message = "Field Title can't be empty")
  @NotBlank(message = "Field Title can't be null")
  @Schema(description = "Title of task", example = "A data model")
  String title;

  @Schema(description = "Date of start", example = "2023-09-23")
  @BeforeCurrentDate
  String startDate;

  @Schema(description = "Date of finish", example = "2023-09-25")
  @BeforeCurrentDate
  String finishDate;

}
