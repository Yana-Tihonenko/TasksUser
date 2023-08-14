package de.ait.listtask.controller;

import de.ait.listtask.dto.UsersRequest;
import de.ait.listtask.dto.task.TasksDto;
import de.ait.listtask.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TaskController implements ApiTask{

 private final TaskService taskService;
  @Override
  public ResponseEntity<TasksDto> getTasks(UsersRequest usersRequest) {
    return ResponseEntity.ok()
        .body(taskService.getTasks(usersRequest));
  }
}
