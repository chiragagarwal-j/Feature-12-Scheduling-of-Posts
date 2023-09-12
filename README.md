# Feature 12

# Scheduling for Addition and Deletion of Posts

- [Introduction](#introduction)
- [The Work Flow](#the-work-flow)
- [Conclusion](#conclusion)

## Introduction

Introducing our project that focuses on **scheduling posts**, a feature-rich solution designed to empower users with the ability to plan. Our project streamlines the process of creating, timing, and publishing posts.

![Feature Work Flow](https://github.com/chiragagarwal-j/Feature-12-Scheduling-of-Posts/blob/main/Documentation/Feature%20Work%20Flow.png)

# How to add a scheduled post?

After logging into your forum and enter the content, then click on **date and time** prompt and set the date and time you wish to publish your post.

If you don't wish to schedule a post, you can simply disregard the date and time option and proceed with publishing the post.
![create post](https://github.com/chiragagarwal-j/Feature-12-Scheduling-of-Posts/blob/main/Documentation/new%20scheduled%20add%20post.png)

# How to delete scheduled post?

After logging into your forum and entering the post details page, then click on **date and time** prompt and set the date and time you wish to delete your post.

If you don't wish to schedule for deleting the post, you can simply disregard the date and time option and proceed with deleting the post.

![delete scheduled post](https://github.com/chiragagarwal-j/Feature-12-Scheduling-of-Posts/blob/main/Documentation/new%20scheduled%20del%20post.png)

# The Work Flow

1.**TaskDefinitionBean Creation:** Task Definition Bean is designed to execute scheduled actions within a Spring-based application. It implements the `Runnable` interface, where the specific action is defined within the overridden `run` method. This bean is responsible for performing tasks such as saving and deleting posts.

2.**Scheduling Logic:**
The `TaskSchedulingService` is a Spring service class designed for scheduling and managing tasks in a Spring-based application. Here are the main aspects:

- **Task Scheduler Injection:**

  - The class injects a `TaskScheduler` bean provided by Spring for task scheduling.

- **Scheduled Tasks Storage:**

  - It maintains a `Map<String, ScheduledFuture<?>>` named `jobsMap` to track scheduled tasks. The keys represent job IDs, and the values are `ScheduledFuture` objects representing the scheduled tasks.

- **Scheduling a Task (`scheduleATask` method):**
  - This method allows you to schedule a task for execution with specific parameters:
    - `jobId`: A unique identifier for the job/task.
    - `tasklet`: A `Runnable` task to be executed when scheduling conditions are met.
    - `cronExpression`: A cron expression specifying when the task should run.
  - The method schedules the task using the provided `cronExpression` and stores the corresponding `ScheduledFuture` in the `jobsMap`.

When the current time matches a scheduled publication time, the scheduler activates the task associated with that post.

**The Cron Expression** <br />
Cron Expression Format : \* \* \* \* \* \*

Example: <br />
**Normal Date Time Expression: 2023-09-07 19:54:00** <br />
**Cron Expression : 0 54 19 7 9 ?**

## Conclusion
- Within the Spring framework, when tasks are scheduled for execution using TaskScheduler, they are typically executed on a separate thread.
- Spring manages a pool of worker threads to execute scheduled tasks asynchronously.These worker threads are separate from the main application thread and are responsible for executing tasks according to their specified schedules (defined using cron expressions here).
- When a task is scheduled, an instance of TaskDefinitionBean is created and executed on one of the worker threads managed by Spring's task scheduler.

# Team Members:

- **Chirag Agarwal**
- **Modali Harshitha**
