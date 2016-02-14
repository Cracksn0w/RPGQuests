package com.cracksn0w.rpgquests.quest.task;

import org.bukkit.Material;

public class CollectTask extends Task {
	
	private Material material;
	private int amount;
	private int material_data;
	
	public CollectTask(Material material, int amount, int metadata) {
		super(TaskType.COLLECT);
		
		this.material = material;
		this.amount = amount;
		this.material_data = metadata;
	}
	
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void setMaterialData(int material_data) {
		this.material_data = material_data;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public int getMaterialData() {
		return material_data;
	}
	
}