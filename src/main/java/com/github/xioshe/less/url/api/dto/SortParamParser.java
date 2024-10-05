package com.github.xioshe.less.url.api.dto;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.xioshe.less.url.api.dto.Sort.SORT_SEPARATOR;

public class SortParamParser {


    /**
     * 1. 从 Sort 字符串中提取出排除条件
     * 2. 将驼峰转下划线
     *
     * @param sortStr 排序字符串，如：email,-last_modify,+name
     * @return list of OrderItem
     */
    public static List<OrderItem> parse(String sortStr, boolean mapCamel) {
        if (StringUtils.isBlank(sortStr)) {
            return Collections.emptyList();
        }

        List<OrderItem> orderItems = new ArrayList<>();

        for (String sort : sortStr.trim().split(SORT_SEPARATOR)) {
            OrderItem order = new OrderItem();
            if (sort.startsWith("-")) {
                order.setAsc(false);
                sort = sort.substring(1);
            } else if (sort.startsWith("+")) {
                sort = sort.substring(1);
            }

            if (mapCamel) {
                sort = StringUtils.camelToUnderline(sort);
            }
            order.setColumn(sort);
            orderItems.add(order);
        }
        return orderItems;
    }
}