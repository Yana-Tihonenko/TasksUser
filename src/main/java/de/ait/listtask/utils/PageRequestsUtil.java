package de.ait.listtask.utils;

import de.ait.listtask.exception.RestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Component
public class PageRequestsUtil {

  @Value("${global.page.size}")
  private Integer pageSize;
  @Autowired
  MessageSource messageSource;

  public PageRequest getPageRequest(Integer pageNumber, String orderByField, Boolean desc, List<String> sortFields) {
    if (orderByField == null && !orderByField.equals("")) {
      checkField(sortFields, orderByField);
      Sort.Direction direction = Sort.Direction.ASC;
      if (desc != null && desc) {
        direction = Sort.Direction.DESC;
      }
      Sort sort = Sort.by(direction, orderByField);
      return PageRequest.of(pageNumber, pageSize, sort);
    }
    return getDefaultPageRequest(pageNumber);
  }

  public void checkField(List<String> allowedFields, String field) {
    if (!allowedFields.contains(field)) {
      throw new RestException("102",
          field,
          FORBIDDEN, messageSource);
    }
  }

  private PageRequest getDefaultPageRequest(Integer pageNumber) {
    return PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "id"));
  }
}
