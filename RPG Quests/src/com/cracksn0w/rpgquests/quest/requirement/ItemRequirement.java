package com.cracksn0w.rpgquests.quest.requirement;

import org.bukkit.inventory.ItemStack;

public class ItemRequirement extends Requirement {

	ItemStack itemstack;
	
	public ItemRequirement(ItemStack itemstack) {
		this.requirementtype = RequirementType.ITEM;
		this.itemstack = itemstack;
	}
	
	@Override
	public RequirementType getRequirementType() {
		return requirementtype;
	}

	public ItemStack getActualRequirement() {
		return itemstack;
	}
	
}
