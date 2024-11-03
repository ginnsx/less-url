package com.github.xioshe.less.url.task;

import com.github.xioshe.less.url.entity.Task;
import com.github.xioshe.less.url.repository.AccessRecordRepository;
import com.github.xioshe.less.url.repository.TaskRepository;
import com.github.xioshe.less.url.service.link.LinkService;
import com.github.xioshe.less.url.util.lock.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class VisitCountTask {

    private final Clock globalClock;
    private final TaskRepository taskRepository;
    private final AccessRecordRepository accessRecordRepository;
    private final TransactionTemplate tx;
    private final SqlSessionFactory sqlSessionFactory;
    private final LinkService linkService;

    @Scheduled(fixedDelay = 5 * 60 * 1000) // 5min
    @DistributedLock(key = "update-link-visits", waitTime = 5)
    public void updateVisitCount() {
        log.info("Updating link visit count");
        var optionalTask = taskRepository.findByTaskName("update-link-visits");
        var lastExecutedAt = optionalTask.map(Task::getLastExecutedAt)
                .orElse(LocalDateTime.of(1970, 1, 1, 0, 0));
        var now = LocalDateTime.now(globalClock);

        var records = accessRecordRepository.countByAccessTime(lastExecutedAt, now);
        if (records.isEmpty()) {
            log.debug("No link visit record found");
            return;
        }

        tx.execute(status -> {
            var result = linkService.batchUpdateVisitCount(records);

            var task = optionalTask.orElseGet(() -> {
                var t = new Task();
                t.setTaskName("update-link-visits");
                return t;
            });
            task.setLastExecutedAt(now);
            taskRepository.saveOrUpdate(task);
            log.debug("Updated task");
            return result;
        });
        log.info("Updated {} link visit count", records.size());
    }
}
