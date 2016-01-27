package com.cracksn0w.rpgquests.quest;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.TraitFactory;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import com.cracksn0w.rpgquests.RPGQuests;
import com.cracksn0w.rpgquests.quest.npc.QuestNPC;
import com.cracksn0w.rpgquests.quest.requirement.Requirement;
import com.cracksn0w.rpgquests.quest.reward.Reward;
import com.cracksn0w.rpgquests.quest.task.Task;

public class Quest {

	private RPGQuests plugin;
	
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
	public Quest(RPGQuests plugin, String name, int id, String npc_name) {
		this.plugin = plugin;
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
	public Quest(RPGQuests plugin, String name, int id, String questnpcname, List<Task> tasks, List<Reward> rewards, List<Requirement> requirements, boolean enabled) {
		this.plugin = plugin;
		this.name = name;
		this.id = id;
		this.questnpc = this.createQuestNPC(questnpcname);
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
	
	public void spawnQuestNPC(Location loc) {
		questnpc.getNPC().spawn(loc);
	}
	
	public void addTask(Task task) {
		tasks.add(task);
	}
	
	public List<Task> getTasks() {
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
	
	public List<Requirement> getRequirements() {
		return requirements;
	}
	
	public RPGQuests getPlugin() {
		return plugin;
	}
	
}