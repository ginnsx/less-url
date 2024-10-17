package com.github.xioshe.less.url.util.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class QueryWrapperBuilder {

    private static final Map<Class<?>, Map<String, String>> columnNameCacheMap = new ConcurrentHashMap<>();
    private static final Map<Class<?>, List<Field>> validFieldsCacheMap = new ConcurrentHashMap<>();

    public static <T> QueryWrapper<T> build(Object query) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        Class<?> queryClass = query.getClass();

        List<Field> validFields = getValidFields(queryClass);

        for (Field field : validFields) {
            try {
                field.setAccessible(true);
                Object value = field.get(query);
                if (value != null && notBlankString(value)) {
                    String fieldName = field.getName();
                    applyCondition(queryWrapper, queryClass, fieldName, value);
                }
            } catch (IllegalAccessException e) {
                log.error("Error accessing field: " + field.getName(), e);
            }
        }

        return queryWrapper;
    }

    private static List<Field> getValidFields(Class<?> clazz) {
        return validFieldsCacheMap.computeIfAbsent(clazz, QueryWrapperBuilder::computeValidFields);
    }

    private static List<Field> computeValidFields(Class<?> clazz) {
        List<Field> validFields = new ArrayList<>();
        ReflectionUtils.doWithFields(clazz, validFields::add, QueryWrapperBuilder::isValidQueryField);
        return validFields;
    }

    private static boolean isValidQueryField(Field field) {
        // 过滤静态字段
        if (Modifier.isStatic(field.getModifiers())) {
            return false;
        }

        // 过滤被 @NotAutoQuery 注解标记的字段
        if (field.isAnnotationPresent(NotAutoQuery.class)) {
            return false;
        }

        // 过滤 transient 字段
        if (Modifier.isTransient(field.getModifiers())) {
            return false;
        }

        // 过滤 synthetic 字段（编译器生成的字段）
        return !field.isSynthetic();
    }

    private static boolean notBlankString(Object value) {
        return !(value instanceof String s) || !s.isBlank();
    }

    private static <T> void applyCondition(
            QueryWrapper<T> queryWrapper, Class<?> queryClass, String fieldName, Object value) {
        if (fieldName.contains("_")) {
            String[] parts = fieldName.split("_");
            String column = getColumnName(queryClass, parts[0]);
            String operator = parts[1];
            applyOperator(queryWrapper, column, operator, value);
        } else {
            String columnName = getColumnName(queryClass, fieldName);
            queryWrapper.eq(columnName, value);
        }
    }

    private static String getColumnName(Class<?> queryClass, String fieldName) {
        return columnNameCacheMap
                .computeIfAbsent(queryClass, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(fieldName, QueryWrapperBuilder::camelToUnderscore);
    }

    private static <T> void applyOperator(QueryWrapper<T> queryWrapper,
            String column, String operator, Object value) {
        switch (operator) {
            case "eq" -> queryWrapper.eq(column, value);
            case "ne" -> queryWrapper.ne(column, value);
            case "gt" -> queryWrapper.gt(column, value);
            case "ge" -> queryWrapper.ge(column, value);
            case "lt" -> queryWrapper.lt(column, value);
            case "le" -> queryWrapper.le(column, value);
            case "like" -> queryWrapper.like(column, value);
            case "likeLeft" -> queryWrapper.likeLeft(column, value);
            case "likeRight" -> queryWrapper.likeRight(column, value);
            case "in" -> {
                if (value instanceof Collection<?> collection) {
                    queryWrapper.in(column, collection);
                } else if (value instanceof Object[] array) {
                    queryWrapper.in(column, array);
                } else {
                    queryWrapper.in(column, value);
                }
            }
            default -> log.warn("Unsupported operator: {}", operator);
        }
    }

    private static String camelToUnderscore(String input) {
        // return input.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
        return StringUtils.camelToUnderline(input);
    }
}
