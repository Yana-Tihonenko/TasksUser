package de.ait.listtask.controller;

import de.ait.listtask.dto.UsersRequest;
import de.ait.listtask.dto.task.TasksDto;
import de.ait.listtask.exception.dto.ErrorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tags(value = {
    @Tag(name = "Tasks")
})
@RequestMapping("/api/tasks")
public interface ApiTask {
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "List of task",
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = TasksDto.class))
          }),
      @ApiResponse(responseCode = "403", description = "Trying to sort by a forbidden field",
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
          })
  })
  @Operation(summary = "List of task")
  @GetMapping
  ResponseEntity<TasksDto> getTasks(UsersRequest usersRequest);
}
