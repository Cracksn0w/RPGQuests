package com.cracksn0w.rpgquests.quest;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.cracksn0w.rpgquests.RPGQuests;
import com.cracksn0w.rpgquests.quest.companion.QuestCompanion;
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
	
	//creates empty quest
	public Quest createQuest(String name, String npc_name) {
		Quest quest = new Quest(plugin, name, id_gen.generateID(), npc_name);
		quests.add(quest);
		return quest;
	}
	
	public ArrayList<Quest> getQuests() {
		return quests;
	}
	
	public ArrayList<QuestCompanion> getQuestCompanions() {
		return quest_companions;
	}
	
	public void createQuestCompanion(Player player, Quest quest) {
		quest_companions.add(new QuestCompanion(player, quest));
	}
	
	public void addQuestCompanion(QuestCompanion qc) {
		quest_companions.add(qc);
	}
	
}