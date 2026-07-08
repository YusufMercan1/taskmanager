package com.yusufmercan.taskmanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskResponse {
    private Long id;
    private String description;
    private String title;
    private boolean completed;
}
