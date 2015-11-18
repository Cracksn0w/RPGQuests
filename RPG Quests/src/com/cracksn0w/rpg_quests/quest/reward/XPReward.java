package com.cracksn0w.rpg_quests.quest.reward;

public class XPReward extends Reward {

	public double amount;
	
	public XPReward(double amount) {
		rewardtype = RewardType.XP;
		this.amount = amount;
	}
	
	@Override
	public RewardType getRewardType() {
		return rewardtype;
	}

	public double getActualReward() {
		return amount;
	}
	
}
