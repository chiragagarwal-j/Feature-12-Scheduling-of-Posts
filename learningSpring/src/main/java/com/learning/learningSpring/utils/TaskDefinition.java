package com.learning.learningSpring.utils;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class TaskDefinition {
    private String cronExpression;
    private String actionType;
    private String data;
}
