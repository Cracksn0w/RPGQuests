package com.cracksn0w.rpgquests.quest.task;

public abstract class Task {
	
	private TaskType type;
	
	public Task(TaskType type) {
		this.type = type;
	}
	
	public TaskType getTaskType() {
		return type;
	}
	
}