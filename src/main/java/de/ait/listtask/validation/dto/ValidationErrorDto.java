package de.ait.listtask.validation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Validation error")
public class ValidationErrorDto {
  @Schema(description = "Incorrect field value")
  private String field;
  @Schema(description = "Text of error")
  private String message;
  @Schema(description = "Incorrect value")
  private String rejectedValue;


}
