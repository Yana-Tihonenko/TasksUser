package de.ait.listtask.service.impl;

import de.ait.listtask.dto.UsersRequest;
import de.ait.listtask.dto.task.TasksDto;
import de.ait.listtask.model.Task;
import de.ait.listtask.repository.TaskRepository;
import de.ait.listtask.service.TaskService;
import de.ait.listtask.utils.PageRequestsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static de.ait.listtask.dto.task.TasksDto.convertToListTaskDto;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;
  private final PageRequestsUtil pageRequestsUtil;

  @Value("${users.sort.fields}")
  private List<String> sortFields;

  @Value("${users.filter.fields}")
  private List<String> filterFields;


  @Override
  public TasksDto getTasks(UsersRequest usersRequest) {
    PageRequest pageRequest = pageRequestsUtil.getPageRequest(usersRequest.getPage(),
        usersRequest.getOrderBy(), usersRequest.getDesc(), sortFields);
    Page<Task> page = getUsersPage(usersRequest.getFilterBy(), usersRequest.getFilterValue(), pageRequest);
    return TasksDto.builder()
        .tasks(convertToListTaskDto(page.getContent()))
        .count(page.getTotalElements())
        .pagesCount(page.getTotalPages())
        .build();
  }

  //startDate,finishDate
  private Page<Task> getUsersPage(String filterBy, String filterValue, PageRequest pageRequest) {
    Page<Task> page = Page.empty();
    if (filterBy == null || filterBy.equals("")) {
      page = taskRepository.findAll(pageRequest);
    } else {
      pageRequestsUtil.checkField(filterFields, filterBy);
      if (filterBy.equals("startDate")) {
        page = taskRepository.findAllByStartDate(LocalDate.parse(filterValue), pageRequest);
      } else {
        page = taskRepository.findAllByFinishDate(LocalDate.parse(filterValue), pageRequest);
      }
    }
    return page;
  }
}
