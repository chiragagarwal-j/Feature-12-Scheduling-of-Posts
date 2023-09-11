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
![create post](https://github.com/chiragagarwal-j/Feature-12-Scheduling-of-Posts/blob/main/Documentation/create%20scheduled%20post.png)

# How to delete scheduled post?

 

After logging into your forum and entering the post details page, then click on **date and time** prompt and set the date and time you wish to delete your post.

 

If you don't wish to schedule for deleting the post, you can simply disregard the date and time option and proceed with deleting the post.


 

# The Work Flow

 

**TaskDefinitionBean Creation:**  Task Definition Bean is designed to execute scheduled actions within a Spring-based application. It implements the **Runnable** interface, where the specific action is defined within the overridden **run** method. This bean is responsible for performing tasks such as saving and deleting posts.

 

**Scheduling Logic:**
The `TaskSchedulingService` is a Spring service that facilitates task scheduling in a Spring-based application. It utilizes a `TaskScheduler` for scheduling and a `jobsMap` to manage scheduled tasks. The `scheduleATask` method schedules tasks based on a unique job ID, a `Runnable` task, and a specified cron expression, storing the associated `ScheduledFuture` in the `jobsMap`. On the other hand, the `removeScheduledTask` method allows the removal of scheduled tasks by their job ID, canceling the task if it exists and marking the corresponding entry in the `jobsMap` as `null`. This service streamlines the process of automating tasks at specified intervals or times within a Spring application, making it a valuable tool for managing scheduled activities.

**The Cron Expression**
Cron Expression Format : * * * * * *

Example: <br />
**Normal Date Time Expression: 2023-09-07 19:54:00** <br />
**Cron Expression : 0 54 19 7 9 ?**

* 0 2 * * *: This cron expression runs a task every day at 2:00 AM.

* 0 0 * * 1: This expression runs a task every Monday (day of the week 1) at midnight.

* 30 3,6,9,12 * * *: This expression runs a task at 3:30 AM, 6:30 AM, 9:30 AM, and 12:30 PM every day.


**Task Activation:** When the current time matches a scheduled publication time, the scheduler activates the task associated with that post. This task typically involves posting content to a designated platform, such as a social media platform, blog, or website.


## Conclusion
Dynamic task scheduling, whether implemented in Java Spring or any other context, plays a crucial role in optimizing resource utilization and efficiency. It allows for the flexible assignment of tasks or jobs based on real-time conditions and priorities, making it essential in various domains, including operating systems, cloud computing, and application development. Dynamic scheduling ensures that tasks are executed when needed, adapts to changing workloads, and can be adjusted at runtime to meet evolving requirements, ultimately improving system performance and responsiveness.

# Team Members: 
-  **Chirag Agarwal**
-  **Modali Harshitha**
