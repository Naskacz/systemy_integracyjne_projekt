package com.example.portal_kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TaskConsumer {

    private final TaskStatusService statusService;

    public TaskConsumer(TaskStatusService statusService) {
        this.statusService = statusService;
    }

    @KafkaListener(topics = "portal-tasks-topic", groupId = "portal-consumer-group")
    public void consumeTask(TaskMessage task) {
        String taskId = task.getTaskId();
        System.out.println("--> [KAFKA] Rozpoczęto przetwarzanie zadania: " + taskId);
        
        statusService.setStatus(taskId, "W TRAKCIE PRZETWARZANIA");
        
        try {
            Thread.sleep(task.getSleepTime());
            statusService.setStatus(taskId, "ZAKOŃCZONE");
            System.out.println("--> [KAFKA] Zakończono pomyślnie zadanie: " + taskId);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            statusService.setStatus(taskId, "BŁĄD");
            System.err.println("Przerwano zadanie: " + taskId);
        }
    }
}