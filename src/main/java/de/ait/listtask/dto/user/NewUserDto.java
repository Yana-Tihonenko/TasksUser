package de.ait.listtask.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Data for adding a new user")
@Data
@Builder
public class NewUserDto {
  @Schema(description = "User email", example = "exaple@gmail.com")
  private String email;
  @Schema(description = "User password", example = "123123")
  private String password;
}
