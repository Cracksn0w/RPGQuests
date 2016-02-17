package com.cracksn0w.rpgquests.quest;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.cracksn0w.rpgquests.QuestRegistry;
import com.cracksn0w.rpgquests.companion.QuestCompanion;
import com.cracksn0w.rpgquests.quest.npc.QuestNPC;
import com.cracksn0w.rpgquests.quest.requirement.Requirement;
import com.cracksn0w.rpgquests.quest.reward.Reward;
import com.cracksn0w.rpgquests.quest.task.Task;

public class Quest {

	private QuestRegistry quest_registry;
	
	private String name;
	private int id;
	private QuestNPC questnpc;
	private List<Task> tasks;
	private List<Reward> rewards;
	private List<Requirement> requirements;
	private boolean enabled;
	
	/**
	 * Erstelle eine neue Quest.
	 * 
	 * @param name Der Name der Quest.
	 * @param id Eine eizigartige ID für die Quest.
	 * @param npc_name Der Name den der QuestNPC bekommt.
	 */
	public Quest(QuestRegistry quest_registry, String name, int id, String npc_name) {
		this.quest_registry = quest_registry;
		this.name = name;
		this.id = id;
		this.questnpc = this.createQuestNPC(npc_name);
		
		tasks = new ArrayList<>();
		rewards = new ArrayList<>();
		requirements = new ArrayList<>();
		enabled = false;
	}
	
	/**
	 * Wenn eine Quest aus dem Datei-System geladen wird, muss dieser Konstruktor verwendet werden.
	 * 
	 * @param name Name
	 * @param id ID
	 * @param questnpcname QuestNPC Name
	 * @param tasks Aufgaben
	 * @param rewards Belohnungen
	 * @param requirements Vorraussetzungen
	 * @param enabled Aktiviert
	 */
	public Quest(QuestRegistry quest_registry, String name, int id, int npc_id, Location npc_location, List<String> message, List<Task> tasks, List<Reward> rewards, List<Requirement> requirements, boolean enabled) {
		this.quest_registry = quest_registry;
		this.name = name;
		this.id = id;
		this.questnpc = this.loadQuestNPC(npc_id);
		this.spawnQuestNPC(npc_location);
		questnpc.setMessage(message);
		this.tasks = tasks;
		this.rewards = rewards;
		this.requirements = requirements;
		this.enabled = enabled;
	}
	
	/**
	 * Gibt den Namen der Quest zurück.
	 * 
	 * @return Name der Quest.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gibt die ID der Quest zurück.
	 * 
	 * @return ID der Quest.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gibt den QuestNPC der Quest zurück.
	 * 
	 * @return QuestNPC der Quest.
	 */
	public QuestNPC getQuestNPC() {
		return questnpc;
	}
	
	/**
	 * Erstell einen QuestNPC für diese Quest.
	 * 
	 * @param name Name für den QuestNPC.
	 * @return Gibt den QuestNPC zurück.
	 */
	private QuestNPC createQuestNPC(String name) {
		NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
		
		return new QuestNPC(this, npc);
	}
	
	private QuestNPC loadQuestNPC(int id) {
		NPC npc = CitizensAPI.getNPCRegistry().getById(id);
		
		return new QuestNPC(this, npc);
	}
	
	public void spawnQuestNPC(Location loc) {
		questnpc.getNPC().spawn(loc);
	}
	
	public void addTask(Task task) {
		tasks.add(task);
	}
	
	public void removeTask(Task task) {
		for(QuestCompanion qc : quest_registry.getQCsForCurrentTask(task)) {
			qc.onCurrentTaskRemoved();
		}
		
		tasks.remove(task);
		
		if(tasks.size() == 0) this.setEnabled(false);
	}
	
	public List<Task> getTasks() {
		return tasks;
	}
	
	public boolean hasTasks() {
		return !tasks.isEmpty();
	}
	
	public Task getNextTask(Task task) {
		if(tasks.size() == 0) return null;
		else if(task == null) return tasks.get(0);
		else if(tasks.indexOf(task) + 1 <= tasks.size()) return tasks.get(tasks.indexOf(task) + 1);
		else return null;
	}
	
	public int getIndexOfTask(Task task) {
		return tasks.indexOf(task);
	}
	
	public void addReward(Reward rwd) {
		rewards.add(rwd);
	}
	
	public List<Reward> getRewards() {
		return rewards;
	}
	
	public void removeReward(Reward rwd) {
		rewards.remove(rwd);
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public List<Requirement> getRequirements() {
		return requirements;
	}
	
	public void addRequirement(Requirement rqm) {
		requirements.add(rqm);
	}
	
	public void removeRequirement(Requirement rqm) {
		requirements.remove(rqm);
	}
	
	public QuestRegistry getQuestRegistry() {
		return quest_registry;
	}
	
	public void setEnabled(boolean bool) {
		this.enabled = bool;
	}
	
	public boolean meetRequirements(Player player) {
		for(Requirement rqm : this.requirements) {
			switch(rqm.getRequirementType()) {
			case ITEM:
				if(player.getInventory().contains((Material) rqm.getRequirement()) == false) return false;
				break;
			case LEVEL:
				if(player.getLevel() < (int) rqm.getRequirement()) return false;
				break;
			default:
				break;
			}
		}
		
		return true;
	}
	
	public boolean isOnQuest(Player player) {
		for(QuestCompanion qc : quest_registry.getQCsForUUID(player.getUniqueId())) {
			if(qc.getQuest() == this) return true;
		}
		
		return false;
	}
	
}