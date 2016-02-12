package com.cracksn0w.rpgquests.quest.reward;

public class Reward {
	
	private RewardType rewardtype;
	private Object reward;
	
	public Reward(RewardType rewardtype, Object reward) {
		this.rewardtype = rewardtype;
		this.reward = reward;
	}
	
	public Object getActualReward() {
		return reward;
	}
	
	public RewardType getRewardType() {
		return rewardtype;
	}
	
}