package de.ait.listtask.dto.user;

import de.ait.listtask.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "User data")
public class UserDto {
  @Schema(description = "Id user", example = "1")
  private Long id;
  @Schema(description = "User email", example = "exaple@gmail.com")
  private String email;
  @Schema(description = "User role", example = "User")
  private User.Role role;
  @Schema(description = "User state", example = "Banned")
  private User.State state;

  public static UserDto convertToUserDto(User user) {
    return UserDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .role(user.getRole())
        .state(user.getState())
        .build();
  }

}
