package de.ait.listtask.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDTO {
  @Schema(description = "Code error of system")
  private String errorCode;
  @Schema (description = "Description of errors")
  private String message;
  @Schema (description = "Incorrect parameter")
  private String field;
  @Schema (description = "Incorrect value")
  private String rejectedValue;

}
