package com.cracksn0w.rpgquests.quest.task;

import org.bukkit.Material;

public class CollectTask extends Task {
	
	private Material material;
	private int amount;
	
	public CollectTask(Material material, int amount) {
		super(TaskType.COLLECT);
		
		this.material = material;
		this.amount = amount;
	}
	
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public int getAmount() {
		return amount;
	}
	
}