package com.cracksn0w.rpgquests;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.cracksn0w.rpgquests.companion.QuestCompanion;
import com.cracksn0w.rpgquests.quest.Quest;
import com.cracksn0w.rpgquests.quest.task.Task;
import com.cracksn0w.rpgquests.utils.IDGen;

public class QuestRegistry {

	private IDGen id_gen;
	
	private RPGQuests plugin;
	
	private ArrayList<Quest> quests;
	private ArrayList<QuestCompanion> quest_companions;
	
	public QuestRegistry(RPGQuests plugin) {
		this.plugin = plugin;
		this.id_gen = new IDGen(this);
		
		this.quests = new ArrayList<>();
		this.quest_companions = new ArrayList<>();
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
	
}