package com.cracksn0w.rpgquests.quest.task;

import org.bukkit.entity.EntityType;

public class KillTask extends Task {

	private EntityType entity_type;
	private int amount;
	
	public KillTask(EntityType entity_type, int amount) {
		super(TaskType.KILL);
		
		this.entity_type = entity_type;
		this.amount = amount;
	}

	public void setEntity(EntityType entity_type) {
		this.entity_type = entity_type;
	}
	
	public EntityType getEntityType() {
		return entity_type;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public int getAmount() {
		return amount;
	}
	
}