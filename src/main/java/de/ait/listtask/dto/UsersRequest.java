package de.ait.listtask.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "User request")
public class UsersRequest {

    @Parameter(description = "Page number", example = "1")
    private Integer page;
    @Parameter(description = "The field by which sorting is available. Available: description,title,startDate,finishDate")
    private String orderBy;
    @Parameter(description = "Set true if sorting in reverse order is desired")
    private Boolean desc;
    @Parameter(description = "The field by which filter is available. Available: startDate,finishDate")
    private String filterBy;
    @Parameter(description = "Filter value", example = "2023-12-25")
    private String filterValue;
}
