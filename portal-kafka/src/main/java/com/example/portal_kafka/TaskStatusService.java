package com.example.portal_kafka;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class TaskStatusService {
    private final Map<String, String> statuses = new ConcurrentHashMap<>();

    public void setStatus(String taskId, String status) {
        statuses.put(taskId, status);
    }

    public String getStatus(String taskId) {
        return statuses.getOrDefault(taskId, "NIEZNANE");
    }
}