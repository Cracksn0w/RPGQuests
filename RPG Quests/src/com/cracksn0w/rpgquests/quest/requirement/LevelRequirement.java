package com.cracksn0w.rpgquests.quest.requirement;

public class LevelRequirement extends Requirement {

	int level;
	
	public LevelRequirement(int level) {
		this.requirementtype = RequirementType.LEVEL;
		this.level = level;
	}
	
	@Override
	public RequirementType getRequirementType() {
		return requirementtype;
	}

	//gets the actual requirement --> here: the level
	public int getActualRequirement() {
		return level;
	}
	
}
