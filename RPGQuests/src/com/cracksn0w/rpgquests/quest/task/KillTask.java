package com.cracksn0w.rpgquests.quest.task;

import org.bukkit.entity.Entity;

public class KillTask extends Task {

	private Entity entity;
	private int amount;
	
	public KillTask(Entity entity, int amount) {
		super(TaskType.KILL);
		
		this.entity = entity;
		this.amount = amount;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public int getAmount() {
		return amount;
	}
	
}