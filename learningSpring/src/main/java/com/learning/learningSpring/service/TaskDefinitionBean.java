package com.learning.learningSpring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.learningSpring.entity.Post;
import com.learning.learningSpring.repository.PostRepository;
import com.learning.learningSpring.utils.TaskDefinition;

@Service
public class TaskDefinitionBean implements Runnable {

    @Autowired
    private PostRepository postRepository;

    private Post post;
    private TaskDefinition taskDefinition;

    public TaskDefinitionBean(Post post, TaskDefinition taskDefinition, PostRepository postRepository) {
        this.post = post;
        this.taskDefinition = taskDefinition;
        this.postRepository = postRepository;
    }

    @Override
    public void run() {
        if (post != null) {
            postRepository.save(post);
        }
    }

    public TaskDefinition getTaskDefinition() {
        return taskDefinition;
    }

    public void setTaskDefinition(TaskDefinition taskDefinition) {
        this.taskDefinition = taskDefinition;
    }
}
