package com.cracksn0w.rpgquests.quest.requirement;

import com.cracksn0w.rpgquests.quest.Quest;

public class QuestRequirement extends Requirement {

	Quest quest;
	
	public QuestRequirement(Quest quest) {
		this.requirementtype = RequirementType.QUEST;
		this.quest = quest;
	}
	
	@Override
	public RequirementType getRequirementType() {
		return requirementtype;
	}

	public Quest getActualRequirement() {
		return quest;
	}
	
}
