package com.cracksn0w.rpgquests.quest.task;

import org.bukkit.Material;

public class CollectTask extends Task {
	
	private Material material;
	private int amount;
	private int metadata;
	
	public CollectTask(Material material, int amount) {
		super(TaskType.COLLECT);
		
		this.material = material;
		this.amount = amount;
		metadata = 0;
	}
	
	public CollectTask(Material material, int amount, int metadata) {
		super(TaskType.COLLECT);
		
		this.material = material;
		this.amount = amount;
		this.metadata = metadata;
	}
	
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void setMetadata(int metadata) {
		this.metadata = metadata;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public int getMetadata() {
		return metadata;
	}
	
}