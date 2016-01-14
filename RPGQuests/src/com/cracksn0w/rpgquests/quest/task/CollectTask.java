package com.cracksn0w.rpgquests.quest.task;

import org.bukkit.inventory.ItemStack;

public class CollectTask extends Task {
	
	private ItemStack item;
	
	public CollectTask(ItemStack item) {
		super(TaskType.COLLECT);
		
		this.item = item;
	}
	
	public void setItemStack(ItemStack stack) {
		item = stack;
	}
	
	public ItemStack getItemStack() {
		return item;
	}
	
}