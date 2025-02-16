package com.example.processserver;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProcessedDataRepository processedDataRepository;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    private final BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();

    @Transactional
    public void submitTask(Task task) {
        task.setStatus("QUEUED");
        taskRepository.save(task);
        taskQueue.offer(task);
        processNextTask();
    }

    private void processNextTask() {
        if (taskExecutor.getActiveCount() < taskExecutor.getMaxPoolSize()) {
            Task task = taskQueue.poll();
            if (task != null) {
                taskExecutor.execute(() -> executeTask(task));
            }
        }
    }

    @Transactional
    public void executeTask(Task task) {
        long startTime = System.currentTimeMillis();
        try {
            task.setStatus("IN_PROGRESS");
            taskRepository.save(task);

            processRange(task.getStartRow(), task.getEndRow());

            task.setStatus("COMPLETED");
        } catch (Exception e) {
            task.setStatus("FAILED");
            task.setErrorMessage(e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            task.setExecutionTime(endTime - startTime);
            taskRepository.save(task);
            processNextTask();
        }
    }

    private void processRange(int startRow, int endRow) {
        for (int i = startRow; i <= endRow; i++) {
            ProcessedData data = new ProcessedData();
            data.setValue("Value " + i);
            processedDataRepository.save(data);
        }
    }
}
