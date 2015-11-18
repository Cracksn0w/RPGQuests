package com.cracksn0w.rpgquests.quest.reward;

import org.bukkit.inventory.ItemStack;

public class ItemReward extends Reward {

	ItemStack itemstack;
	
	public ItemReward(ItemStack itemstack) {
		rewardtype = RewardType.ITEM;
		this.itemstack = itemstack;
	}
	
	@Override
	public RewardType getRewardType() {
		return rewardtype;
	}

	public ItemStack getActualReward() {
		return itemstack;
	}
	
}
