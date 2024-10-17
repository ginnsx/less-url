package com.github.xioshe.less.url.api.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;

import java.util.List;

import static com.github.xioshe.less.url.util.constants.RegexPatterns.SORT_STR_PATTERN;


@ParameterObject
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagination {

    @Parameter(description = "页码，从 1 开始", example = "1")
    private Integer page = 1;
    @Parameter(description = "每页大小", example = "20")
    private Integer size = 20;
    @Parameter(description = "按游标分页，上一页的最后一个元素 id，会忽略 page")
    private Long after;

    @Parameter(name = "sort_by", description = "排序条件", example = "email,-last_modify,+name")
    @Pattern(regexp = SORT_STR_PATTERN, message = "排序参数格式不正确")
    private String sortBy;

    public void setSort_by(@Pattern(regexp = SORT_STR_PATTERN, message = "排序参数格式不正确") String sort_by) {
        this.sortBy = sort_by;
    }

    public <T> IPage<T> toPage() {
        Page<T> pageParam = new Page<>(page, size);
        List<OrderItem> orders = SortParamParser.parse(sortBy, true);
        if (!orders.isEmpty()) {
            pageParam.setOrders(orders);
        }
        return pageParam;
    }

}
