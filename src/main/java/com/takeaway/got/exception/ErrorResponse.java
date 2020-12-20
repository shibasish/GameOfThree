package com.takeaway.got.exception;

public class ErrorResponse {
    private int status;
    private String title;
    private String description;
    private long timeStamp;

    public ErrorResponse(){}

    public ErrorResponse(int status, String title, String description, long timeStamp) {
        this.status = status;
        this.title = title;
        this.description = description;
        this.timeStamp = timeStamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}

