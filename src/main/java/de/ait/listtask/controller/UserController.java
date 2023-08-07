package de.ait.listtask.controller;

import de.ait.listtask.dto.task.NewTaskDto;
import de.ait.listtask.dto.task.TaskDto;
import de.ait.listtask.dto.task.TasksDto;
import de.ait.listtask.dto.user.NewUserDto;
import de.ait.listtask.dto.user.UserDto;
import de.ait.listtask.dto.user.UsersDto;
import de.ait.listtask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController implements ApiUser {

  private final UserService userService;

  @Override
  public ResponseEntity<UserDto> addUser(NewUserDto newUser) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(userService.addUser(newUser));

  }

  @Override
  public ResponseEntity<TaskDto> addTaskForUser(Long userId, NewTaskDto newTaskForUser) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(userService.addTaskForUser(userId, newTaskForUser));
  }

  @Override
  public ResponseEntity<UsersDto> getAllUsers() {

    return ResponseEntity.ok(userService.getAllUser());
  }

  @Override
  public ResponseEntity<TasksDto> getUserTasks(Long userId) {
    return ResponseEntity.ok().body(
        userService.getAllTaskUser(userId));
  }
}
