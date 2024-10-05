package com.github.xioshe.less.url.api.dto;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;

import java.util.List;

@ParameterObject
@Data
public class Sort {

    public static final String SORT_SEPARATOR = ",";
    public static final String SORT_STR_PATTERN = "^[+-]?[a-zA-Z_]*(?:,[+-]?[a-zA-Z_]*)*$";


    @Parameter(description = "排序条件", example = "email,-last_modify,+name")
    @Pattern(regexp = SORT_STR_PATTERN)
    private String sortBy;

    public List<OrderItem> toOrderItems() {
        return SortParamParser.parse(sortBy, true);
    }
}
