package de.ait.listtask.service;

import de.ait.listtask.dto.task.NewTaskDto;
import de.ait.listtask.dto.task.TaskDto;
import de.ait.listtask.dto.task.TasksDto;
import de.ait.listtask.dto.user.NewUserDto;
import de.ait.listtask.dto.user.UserDto;
import de.ait.listtask.dto.user.UsersDto;

public interface UserService {

  UserDto addUser(NewUserDto newUser);

  TaskDto addTaskForUser(Long userId, NewTaskDto newTaskForUser);

  UsersDto getAllUser();

  TasksDto getAllTaskUser(Long userId);
}
