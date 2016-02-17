package com.cracksn0w.rpgquests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.cracksn0w.rpgquests.companion.QuestCompanion;
import com.cracksn0w.rpgquests.companion.listener.PlayerListener;
import com.cracksn0w.rpgquests.quest.Quest;
import com.cracksn0w.rpgquests.quest.requirement.Requirement;
import com.cracksn0w.rpgquests.quest.requirement.RequirementType;
import com.cracksn0w.rpgquests.quest.reward.Reward;
import com.cracksn0w.rpgquests.quest.reward.RewardType;
import com.cracksn0w.rpgquests.quest.task.CollectTask;
import com.cracksn0w.rpgquests.quest.task.KillTask;
import com.cracksn0w.rpgquests.quest.task.Task;
import com.cracksn0w.rpgquests.quest.task.TaskType;
import com.cracksn0w.rpgquests.utils.IDGen;

public class QuestRegistry {

	private IDGen id_gen;
	
	private RPGQuests plugin;
	private QuestRegistry instance;
	
	private ArrayList<Quest> quests;
	private ArrayList<QuestCompanion> quest_companions;
	
	private PlayerListener player_listener;
	
	public QuestRegistry(RPGQuests plugin) {
		this.plugin = plugin;
		this.instance = this;
		this.id_gen = new IDGen(this);
		
		this.quests = new ArrayList<>();
		this.quest_companions = new ArrayList<>();
		
		this.loadQuests();
		
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
	
	public ArrayList<QuestCompanion> getQCsForUUID(UUID uuid) {
		ArrayList<QuestCompanion> result = new ArrayList<>();
		
		for(QuestCompanion qc : quest_companions) {
			if(qc.getUUID().equals(uuid)) {
				result.add(qc);
			}
		}
		
		return result;
	}
	
	public Quest getById(int id) {
		for(Quest quest : quests) {
			if(quest.getId() == id) return quest;
		}
		
		return null;
	}
	
	private void registerPlayerListener() {
		player_listener = new PlayerListener(this);
	}
	
	public PlayerListener getPlayerListener() {
		return player_listener;
	}
	
	private void loadQuests() {
		final QuestRegistry reg = this;
		
		Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
			
			public void run() {
				File folder = new File(plugin.getDataFolder() + "/quests");
				
				if(folder.isDirectory() == false) return;
				
				for(File file : folder.listFiles()) {
					FileConfiguration config = YamlConfiguration.loadConfiguration(file);
					
					String name = config.getString("name");
					int id = config.getInt("id");
					boolean enabled = config.getBoolean("enabled");
					ArrayList<Task> tasks = new ArrayList<>();
					ArrayList<Reward> rewards = new ArrayList<>();
					ArrayList<Requirement> requirements = new ArrayList<>();
					
					ConfigurationSection tasks_section = config.getConfigurationSection("tasks");
					
					for(String key : tasks_section.getKeys(false)) {
						ConfigurationSection task_section = tasks_section.getConfigurationSection(key);
						
						TaskType type = TaskType.valueOf(task_section.getString("type"));
						
						switch(type) {
						case COLLECT:
							Material mat = Material.getMaterial(task_section.getString("material"));
							int amount_ct = task_section.getInt("amount");
							int mat_data = task_section.getInt("material-data");
							
							tasks.add(new CollectTask(mat, amount_ct, mat_data));
							
							break;
						case KILL:
							EntityType ent = EntityType.valueOf(task_section.getString("entity"));
							int amount_kt = task_section.getInt("amount");
							
							tasks.add(new KillTask(ent, amount_kt));
							
							break;
						default:
							continue;
						}
					}
					
					ConfigurationSection rewards_section = config.getConfigurationSection("rewards");
					
					for(String key : rewards_section.getKeys(false)) {
						ConfigurationSection reward_section = rewards_section.getConfigurationSection(key);
						
						rewards.add(new Reward(RewardType.valueOf(reward_section.getString("type")), reward_section.get("reward")));
					}
					
					ConfigurationSection requirements_section = config.getConfigurationSection("requirements");
					
					for(String key: requirements_section.getKeys(false)) {
						ConfigurationSection requirement_section = requirements_section.getConfigurationSection(key);
						
						RequirementType type = RequirementType.valueOf(requirement_section.getString("type"));
						Object rqm = requirement_section.get("rqm");
						
						switch(type) {
						case ITEM:
							rqm = Material.getMaterial((String) rqm);
							
							break;
						case LEVEL:
							rqm = Integer.parseInt((String) rqm);
							
							break;
						default:
							continue;
						}
						
						requirements.add(new Requirement(type, rqm));
					}
					
					ConfigurationSection npc_section = config.getConfigurationSection("npc");
					
					int npc_id = npc_section.getInt("id");
					Location npc_loc = (Location) npc_section.get("location");
					List<String> npc_msg = npc_section.getStringList("message");
					
					quests.add(new Quest(reg, name, id, npc_id, npc_loc, npc_msg, tasks, rewards, requirements, enabled));
				}
			}
			
		}, 40);
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
					ts.set("material-data", ct.getMaterialData());
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
	
	public void loadQuestCompanions(final UUID uuid) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			
			public void run() {
				File folder = new File(plugin.getDataFolder() + "/companions");
				
				if(folder.isDirectory() == false) return;
				
				for(File file : folder.listFiles()) {
					if(file.getName().contains(uuid.toString()) == false) continue;
					
					FileConfiguration config = YamlConfiguration.loadConfiguration(file);
					
					UUID uuid_qc = UUID.fromString(config.getString("uuid"));
					Quest quest = instance.getById(config.getInt("quest"));
					
					if(quest == null) continue;
					
					Task current_task = quest.getTasks().get(config.getInt("current-task"));
					
					//skip loading if quest has no tasks anymore (maybe deleted by user)
					if(current_task == null) {
						if(quest.getTasks().size() == 0) continue;
						current_task = quest.getTasks().get(0);
					}
					
					Object progress = config.get("progress");
					
					QuestCompanion qc = new QuestCompanion(instance, uuid_qc, quest, current_task, progress);
					qc.onPlayerConnect();
					
					instance.addQuestCompanion(qc);
				}
			}
			
		});
	}
	
	public void saveQuestCompanions(UUID uuid) {
		for(QuestCompanion qc : this.getQCsForUUID(uuid)) {
			File file = new File(plugin.getDataFolder() + "/companions/" + uuid + "_" + qc.getQuest().getId() + ".yml");
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			config.set("uuid", qc.getUUID().toString());
			config.set("quest", qc.getQuest().getId());
			config.set("current-task", qc.getQuest().getIndexOfTask(qc.getCurrentTask()));
			config.set("progress", qc.getProgress());
			
			qc.onDisable();
			quest_companions.remove(qc);
			
			try {
				config.save(file);
			} catch (IOException e) {
				plugin.getLogger().info("Der QuestCompanion von " + qc.getUUID() + " konnte nicht gespeichert werden.");
			}
		}
	}
	
}