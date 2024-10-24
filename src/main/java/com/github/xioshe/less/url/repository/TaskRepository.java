package com.github.xioshe.less.url.repository;

import com.github.xioshe.less.url.entity.Task;
import com.github.xioshe.less.url.repository.mapper.TaskMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TaskRepository extends BaseRepository<TaskMapper, Task> {

    public Optional<Task> findByTaskName(String taskName) {
        return lambdaQuery().eq(Task::getTaskName, taskName).oneOpt();
    }
}
