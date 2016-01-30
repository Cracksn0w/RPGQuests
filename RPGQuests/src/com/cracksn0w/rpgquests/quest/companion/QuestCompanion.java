package com.cracksn0w.rpgquests.quest.companion;

import org.bukkit.entity.Player;

import com.cracksn0w.rpgquests.quest.Quest;
import com.cracksn0w.rpgquests.quest.task.Task;

public class QuestCompanion {

	private Player player;
	private Quest quest;
	private Task current_task;
	private Object progress;
	
	public QuestCompanion(Player player, Quest quest) {
		this.player = player;
		this.quest = quest;
		
		current_task = quest.getTask(0);
	}
	
	public QuestCompanion(Player player, Quest quest, Task current_task, Object progress) {
		this.player = player;
		this.quest = quest;
		this.current_task = current_task;
		this.progress = progress;
	}
	
	public void setTask(Task task) {
		current_task = task;
	}
	
	public Task getCurrentTask() {
		return current_task;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setProgress(Object obj) {
		progress = obj;
	}
	
	public void registerQCListener() {
		switch(current_task.getTaskType()) {
		case COLLECT:
			
			break;
		case KILL:
			
			break;
		default:
			break;
		}
	}
	
}