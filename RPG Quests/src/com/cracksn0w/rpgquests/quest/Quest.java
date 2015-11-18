package com.cracksn0w.rpgquests.quest;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import com.cracksn0w.rpgquests.quest.npc.QuestNPC;
import com.cracksn0w.rpgquests.quest.requirement.Requirement;
import com.cracksn0w.rpgquests.quest.reward.Reward;

public class Quest {

	private String name;
	private int id;
	private QuestNPC questnpc;
	private List<ITask> tasks;
	private List<Reward> rewards;
	private List<Requirement> requirements;
	private boolean enabled;
	
	public Quest(String name, int id, QuestNPC questnpc) {
		this.name = name;
		this.id = id;
		this.questnpc = questnpc;
		
		tasks = new ArrayList<>();
		rewards = new ArrayList<>();
		requirements = new ArrayList<>();
		enabled = false;
	}
	
	public Quest(String name, int id, String questnpcname, List<ITask> tasks, List<Reward> rewards, List<Requirement> requirements, boolean enabled) {
		this.name = name;
		this.id = id;
		this.questnpc = this.createQuestNPC(questnpcname);
		this.tasks = tasks;
		this.rewards = rewards;
		this.requirements = requirements;
		this.enabled = enabled;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	public QuestNPC getQuestNPC() {
		return questnpc;
	}
	
	public QuestNPC createQuestNPC(String name) {
		NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
		
		return new QuestNPC(npc);
	}
	
	public void spawnQuestNPC(Location loc) {
		questnpc.getNPC().spawn(loc);
	}
	
	public void addTask(ITask task) {
		tasks.add(task);
	}
	
	public List<ITask> getTasks() {
		return tasks;
	}
	
	public void addReward(Reward rwd) {
		rewards.add(rwd);
	}
	
	public List<Reward> getRewards() {
		return rewards;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
}