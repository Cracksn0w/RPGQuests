package com.cracksn0w.rpgquests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.cracksn0w.rpgquests.companion.QuestCompanion;
import com.cracksn0w.rpgquests.companion.listener.PlayerListener;
import com.cracksn0w.rpgquests.quest.Quest;
import com.cracksn0w.rpgquests.quest.requirement.Requirement;
import com.cracksn0w.rpgquests.quest.reward.Reward;
import com.cracksn0w.rpgquests.quest.task.CollectTask;
import com.cracksn0w.rpgquests.quest.task.KillTask;
import com.cracksn0w.rpgquests.quest.task.Task;
import com.cracksn0w.rpgquests.utils.IDGen;

public class QuestRegistry {

	private IDGen id_gen;
	
	private RPGQuests plugin;
	
	private ArrayList<Quest> quests;
	private ArrayList<QuestCompanion> quest_companions;
	
	private PlayerListener player_listener;
	
	public QuestRegistry(RPGQuests plugin) {
		this.plugin = plugin;
		this.id_gen = new IDGen(this);
		
		this.quests = new ArrayList<>();
		this.quest_companions = new ArrayList<>();
		
		this.registerPlayerListener();
	}
	
	public RPGQuests getPlugin() {
		return plugin;
	}
	
	public Quest createQuest(String name, String npc_name) {
		Quest quest = new Quest(this, name, id_gen.generateID(), npc_name);
		quests.add(quest);
		return quest;
	}
	
	public void removeQuest(Quest quest) {
		for(QuestCompanion qc : this.getQCsForQuest(quest)) {
			qc.onQuestRemoved();
		}
		
		quest.getQuestNPC().remove();
		quests.remove(quest);
		
		plugin.getLogger().info("Quest " + quest.getName() + " removed!");
	}
	
	public ArrayList<Quest> getQuests() {
		return quests;
	}
	
	public ArrayList<QuestCompanion> getQuestCompanions() {
		return quest_companions;
	}
	
	public void createQuestCompanion(Player player, Quest quest) {
		quest_companions.add(new QuestCompanion(this, player, quest));
	}
	
	public void addQuestCompanion(QuestCompanion qc) {
		quest_companions.add(qc);
	}
	
	public void removeQuestCompanion(QuestCompanion qc) {
		quest_companions.remove(qc);
	}
	
	public ArrayList<QuestCompanion> getQCsForCurrentTask(Task task) {
		ArrayList<QuestCompanion> result = new ArrayList<>();
		
		for(QuestCompanion qc : quest_companions) {
			if(qc.getCurrentTask() == task) {
				result.add(qc);
			}
		}
		
		return result;
	}
	
	private ArrayList<QuestCompanion> getQCsForQuest(Quest quest) {
		ArrayList<QuestCompanion> result = new ArrayList<>();
		
		for(QuestCompanion qc : quest_companions) {
			if(qc.getQuest() == quest) {
				result.add(qc);
			}
		}
		
		return result;
	}
	
	public ArrayList<QuestCompanion> getQCsForPlayer(Player player) {
		ArrayList<QuestCompanion> result = new ArrayList<>();
		
		for(QuestCompanion qc : quest_companions) {
			if(qc.getUUIDofPlayer() == player.getUniqueId()) {
				result.add(qc);
			}
		}
		
		return result;
	}
	
	private void registerPlayerListener() {
		player_listener = new PlayerListener(this);
	}
	
	public PlayerListener getPlayerListener() {
		return player_listener;
	}
	
	//Serealisiert alle Quests
	public void saveQuests() {
		for(Quest quest : quests) {
			File file = new File(plugin.getDataFolder() + "/quests/" + quest.getName() + ".yml");
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			config.set("name", quest.getName());
			config.set("id", quest.getId());
			config.set("enabled", quest.isEnabled());
			
			ConfigurationSection tasks = config.createSection("tasks");
			
			for(Task task : quest.getTasks()) {
				ConfigurationSection ts = tasks.createSection("" + quest.getTasks().indexOf(task));
				
				ts.set("type", task.getTaskType().toString());
				
				switch(task.getTaskType()) {
				case COLLECT:
					CollectTask ct = (CollectTask) task;
					
					ts.set("material", ct.getMaterial().toString());
					ts.set("amount", ct.getAmount());
					continue;
				case KILL:
					KillTask kt = (KillTask) task;
					
					ts.set("entity", kt.getEntityType().toString());
					ts.set("amount", kt.getAmount());
					continue;
				default:
					continue;
				}
			}
			
			ConfigurationSection rewards = config.createSection("rewards");
			
			for(Reward reward : quest.getRewards()) {
				ConfigurationSection rs = rewards.createSection("" + quest.getRewards().indexOf(reward));
				
				rs.set("type", reward.getRewardType().toString());
				rs.set("reward", reward.getActualReward());
			}
			
			ConfigurationSection requirements = config.createSection("requirements");
			
			for(Requirement requirement : quest.getRequirements()) {
				ConfigurationSection rqs = requirements.createSection("" + quest.getRequirements().indexOf(requirement));
				
				rqs.set("type", requirement.getRequirementType().toString());
				rqs.set("rqm", requirement.getRequirement().toString());
				
				switch(requirement.getRequirementType()) {
				case ITEM:
					rqs.set("add", requirement.getAddition());
					
					continue;
				default:
					continue;
				}
			}
			
			ConfigurationSection npc = config.createSection("npc");
			
			npc.set("id", quest.getQuestNPC().getid());
			npc.set("location", quest.getQuestNPC().getLocation());
			npc.set("message", quest.getQuestNPC().getMessage());
			
			try {
				config.save(file);
			} catch (IOException e) {
				plugin.getLogger().severe("Die Quest " + quest.getName() + " konnte nicht gespeichert werden!");
			}
		}
	}
	
}