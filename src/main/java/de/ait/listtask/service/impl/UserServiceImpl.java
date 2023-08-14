package de.ait.listtask.service.impl;

import de.ait.listtask.dto.task.NewTaskDto;
import de.ait.listtask.dto.task.TaskDto;
import de.ait.listtask.dto.task.TasksDto;
import de.ait.listtask.dto.user.NewUserDto;
import de.ait.listtask.dto.user.UserDto;
import de.ait.listtask.dto.user.UsersDto;
import de.ait.listtask.exception.RestException;
import de.ait.listtask.model.Task;
import de.ait.listtask.model.User;
import de.ait.listtask.repository.TaskRepository;
import de.ait.listtask.repository.UserRepository;
import de.ait.listtask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static de.ait.listtask.dto.task.TaskDto.convertToTaskDto;
import static de.ait.listtask.dto.task.TasksDto.convertToTasksDto;
import static de.ait.listtask.dto.user.UsersDto.convertToUsersListDto;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final TaskRepository taskRepository;

  @Autowired
  MessageSource messageSource;


  @Override
  public UserDto addUser(NewUserDto newUser) {
    User user = User.builder()
        .email(newUser.getEmail())
        .password(newUser.getPassword())
        .state(User.State.NOT_CONFIRMED)
        .role(User.Role.USER)
        .build();
    userRepository.save(user);
    return UserDto.convertToUserDto(user);
  }

  @Override
  public TaskDto addTaskForUser(Long userId, NewTaskDto newTaskForUser) {
    User user = getUserOrElseThrow(userId);
    Task task = Task.builder()
        .description(newTaskForUser.getDescription())
        .title(newTaskForUser.getTitle())
        .executor(user)
        .build();
    try {
      task.setStartDate(LocalDate.parse(newTaskForUser.getStartDate()));
      task.setFinishDate(LocalDate.parse(newTaskForUser.getFinishDate()));

    } catch (DateTimeParseException ex) {
      throw new RestException("101", null, BAD_REQUEST, messageSource);
    }


    taskRepository.save(task);


    return convertToTaskDto(task);
  }

  @Override
  public UsersDto getAllUser() {
    return convertToUsersListDto(userRepository.findAll());
  }

  @Override
  public TasksDto getAllTaskUser(Long userId) {
    User user = getUserOrElseThrow(userId);
    return convertToTasksDto(user.getTasks());

  }

  private User getUserOrElseThrow(Long userId) {
    return userRepository.findById(userId).orElseThrow(
        () -> new RestException("100",
            userId.toString(),
            NOT_FOUND, messageSource));
  }
}
