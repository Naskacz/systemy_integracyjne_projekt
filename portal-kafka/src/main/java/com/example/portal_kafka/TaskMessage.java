package com.example.portal_kafka;

public class TaskMessage {
    private String taskId;
    private int sleepTime;

    public TaskMessage() {}

    public TaskMessage(String taskId, int sleepTime) {
        this.taskId = taskId;
        this.sleepTime = sleepTime;
    }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public int getSleepTime() { return sleepTime; }
    public void setSleepTime(int sleepTime) { this.sleepTime = sleepTime; }
}