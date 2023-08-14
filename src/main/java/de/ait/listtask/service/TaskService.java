package de.ait.listtask.service;

import de.ait.listtask.dto.UsersRequest;
import de.ait.listtask.dto.task.TasksDto;

public interface TaskService {
  TasksDto getTasks(UsersRequest usersRequest);
}
