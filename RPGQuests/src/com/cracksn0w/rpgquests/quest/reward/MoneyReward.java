package com.cracksn0w.rpgquests.quest.reward;

public class MoneyReward extends Reward {

	double amount;
	
	public MoneyReward(double amount) {
		rewardtype = RewardType.MONEY;
		this.amount = amount;
	}
	
	@Override
	public RewardType getRewardType() {
		return rewardtype;
	}

	//returns the actual reward --> here: money
	public double getActualReward() {
		return amount;
	}
	
}
