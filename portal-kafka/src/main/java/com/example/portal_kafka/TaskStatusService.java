package com.example.portal_kafka;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class TaskStatusService {
    private static final String KEY_PREFIX = "task:status:";
    private static final Duration STATUS_TTL = Duration.ofHours(1);
    private final StringRedisTemplate redisTemplate;
    public TaskStatusService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    public void setStatus(String taskId, String status) {
        redisTemplate.opsForValue().set(KEY_PREFIX + taskId, status, STATUS_TTL);
    }

    public String getStatus(String taskId) {
        String status = redisTemplate.opsForValue().get(KEY_PREFIX + taskId);
        return status != null ? status : "NIEZNANE";
    }
}