package com.github.xioshe.less.url.api.dto;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;

import java.util.List;

import static com.github.xioshe.less.url.util.constants.RegexPatterns.SORT_STR_PATTERN;

@ParameterObject
@Data
public class Sort {

    public static final String SORT_SEPARATOR = ",";


    @Parameter(name = "sort_by", description = "排序条件", example = "email,-last_modify,+name")
    @Pattern(regexp = SORT_STR_PATTERN, message = "排序参数格式不正确")
    private String sortBy;

    public void setSort_by(@Pattern(regexp = SORT_STR_PATTERN, message = "排序参数格式不正确") String sort_by) {
        this.sortBy = sort_by;
    }

    public List<OrderItem> toOrderItems() {
        return SortParamParser.parse(sortBy, true);
    }
}
