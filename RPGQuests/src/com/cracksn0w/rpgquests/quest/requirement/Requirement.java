package com.cracksn0w.rpgquests.quest.requirement;

public class Requirement {

	private RequirementType requirement_type;
	private Object requirement;
	private int addition;
	
	public Requirement(RequirementType requirement_type, Object requirement) {
		this.requirement_type = requirement_type;
		this.requirement = requirement;
	}
	
	public Requirement(RequirementType requirement_type, Object requirement, int addition) {
		this.requirement_type = requirement_type;
		this.requirement = requirement;
		this.addition = addition;
	}
	
	public RequirementType getRequirementType() {
		return requirement_type;
	}
	
	public Object getRequirement() {
		return requirement;
	}
	
	public int getAddition() {
		return addition;
	}
	
}