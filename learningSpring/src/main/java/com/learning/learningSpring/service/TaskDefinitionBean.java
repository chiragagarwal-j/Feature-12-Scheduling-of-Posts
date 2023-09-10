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

    @Autowired
    private PostService postService;

    private Integer id;
    private Post post;
    private TaskDefinition taskDefinition;

    public TaskDefinitionBean() {
    }

    public TaskDefinitionBean(Post post, TaskDefinition taskDefinition, PostRepository postRepository) {
        this.post = post;
        this.taskDefinition = taskDefinition;
        this.postRepository = postRepository;
    }

    public TaskDefinitionBean(Integer id, TaskDefinition taskDefinition, PostService postService) {
        this.id = id;
        this.taskDefinition = taskDefinition;
        this.postService=postService;
    }

    @Override
    public void run() {
        if (post != null) {
            postRepository.save(post);
        }

        if (id != null) {
            postService.deleteLikeAndComment(id);
            postService.deletePostById(id);
        }
    }

    public TaskDefinition getTaskDefinition() {
        return taskDefinition;
    }

    public void setTaskDefinition(TaskDefinition taskDefinition) {
        this.taskDefinition = taskDefinition;
    }
}
