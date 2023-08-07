package de.ait.listtask.dto.user;

import de.ait.listtask.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static de.ait.listtask.dto.user.UserDto.convertToUserDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "List of user data")
public class UsersDto {
  @Schema(description = "List of user data")
  private List<UserDto> users;
  @Schema(description = "User count")
  private Integer count;


  public static UsersDto convertToUsersListDto(List<User> userList) {
    return UsersDto.builder()
        .users(userList.stream().map(UserDto::convertToUserDto).collect(Collectors.toList()))
        .count(userList.size())
        .build();
  }

}
