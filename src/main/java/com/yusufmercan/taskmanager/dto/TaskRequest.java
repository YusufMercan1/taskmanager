package com.yusufmercan.taskmanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {
    private String description;
    private String title;
    private boolean completed;
}
