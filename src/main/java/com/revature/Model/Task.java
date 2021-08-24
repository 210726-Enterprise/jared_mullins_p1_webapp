package com.revature.Model;

import com.revature.annotations.Column;
import com.revature.annotations.Entity;
import com.revature.utils.BaseClass;

@Entity(tableName = "tasks")
public class Task extends BaseClass {

    /**
     * The general task that needs to be completed
     */
    @Column(columnName = "task")
    private String task;

    /**
     * Describes the task in greater detail
     */
    @Column(columnName = "description")
    private String description;

    /**
     * Sets the priority level of the task
     */
    @Column(columnName = "priority")
    private int priority;

    /**
     * Boolean for if the task has been completed
     */
    @Column(columnName = "completed")
    private boolean completed;

    /**
     * No-args constructor
     */
    public Task() {
    }

    /**
     * All arg constructor
     * @param task general task
     * @param description detailed description of task
     * @param priority priority level of task
     * @param completed if it is completed
     */
    public Task(String task, String description, int priority, boolean completed) {
        this.task = task;
        this.description = description;
        this.priority = priority;
        this.completed = completed;
    }

    /**
     * Gets priority level
     * @return int priority level
     */
    public int getPriority() {
        return priority;
    }

    /**
     * sets priority level
     * @param priority int new priority level
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Gets whether task is completed
     * @return True if completed; false if not
     */
    public boolean getCompleted() {
        return completed;
    }

    /**
     * Sets completed state of task
     * @param completed true or false
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Gets general task
     * @return String task
     */
    public String getTask() {
        return task;
    }

    /**
     * Sets task name
     * @param task new name of task
     */
    public void setTask(String task) {
        this.task = task;
    }

    /**
     * Description of task
     * @return desciption of task
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description of task
     * @param description new description of task
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
