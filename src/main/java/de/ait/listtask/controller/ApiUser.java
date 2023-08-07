package de.ait.listtask.controller;

import de.ait.listtask.dto.task.NewTaskDto;
import de.ait.listtask.dto.task.TaskDto;
import de.ait.listtask.dto.task.TasksDto;
import de.ait.listtask.dto.user.NewUserDto;
import de.ait.listtask.dto.user.UserDto;
import de.ait.listtask.dto.user.UsersDto;
import de.ait.listtask.validation.dto.ValidationErrorDto;
import de.ait.listtask.validation.dto.ValidationErrorsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

@Tags(value = {
    @Tag(name = "Users")
})
@Validated
@RequestMapping("/users")
public interface ApiUser {
  @Operation(summary = "Add new user", description = "Api for add new user")
  @PostMapping()
  ResponseEntity<UserDto> addUser(@RequestBody NewUserDto newUser);

  @Operation(summary = "Add new task for user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "New user is added", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = TasksDto.class))
      }),
      @ApiResponse(responseCode = "400", description = "Validation errors", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorsDto.class))
      }),
      @ApiResponse(responseCode = "404", description = "Not found user", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorDto.class))
      })
  })
  @PostMapping("/{user-id}/tasks")
  ResponseEntity<TaskDto> addTaskForUser(@Parameter(required = true, description = "Id user", example = "1")
                                         @PathVariable("user-id") Long userId,
                                         @Parameter(required = true, description = "Parameter new task")
                                         @RequestBody @Valid NewTaskDto newTaskForUser);


  @GetMapping()
  @Operation(summary = "Get all user")
  ResponseEntity<UsersDto> getAllUsers();

  @Operation(summary = "Get user tasks")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Get user tasks", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = TasksDto.class))
      }),
      @ApiResponse(responseCode = "404", description = "Not found user", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorDto.class))
      })
  })
  @GetMapping("/{user-id}/tasks")
  ResponseEntity<TasksDto> getUserTasks(
      @Parameter(required = true, description = "Id user", example = "1")
      @PathVariable("user-id") Long userId);
}