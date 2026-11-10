package com.example.portal_kafka;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TaskController {

    private final KafkaTemplate<String, TaskMessage> kafkaTemplate;
    private final TaskStatusService statusService;
    private static final String TOPIC = "portal-tasks-topic";

    public TaskController(KafkaTemplate<String, TaskMessage> kafkaTemplate, TaskStatusService statusService) {
        this.kafkaTemplate = kafkaTemplate;
        this.statusService = statusService;
    }
    @PostMapping("/task")
    public ResponseEntity<String> submitTask(@RequestParam(defaultValue = "3000") int sleepTime) {
        String taskId = UUID.randomUUID().toString();
        TaskMessage task = new TaskMessage(taskId, sleepTime);

        statusService.setStatus(taskId, "OCZEKUJE W KOLEJCE");
        kafkaTemplate.send(TOPIC, taskId, task);

        return ResponseEntity.accepted().body(taskId);
    }

    @GetMapping("/task/{id}/status")
    public ResponseEntity<String> getTaskStatus(@PathVariable String id) {
        String status = statusService.getStatus(id);
        return ResponseEntity.ok(status);
    }
}