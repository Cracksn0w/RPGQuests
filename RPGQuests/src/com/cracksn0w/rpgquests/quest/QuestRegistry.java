package com.cracksn0w.rpgquests.quest;

import java.util.ArrayList;

import com.cracksn0w.rpgquests.RPGQuests;
import com.cracksn0w.rpgquests.utils.IDGen;

public class QuestRegistry {

	private IDGen id_gen;
	
	private RPGQuests plugin;
	
	private ArrayList<Quest> quests;
	
	public QuestRegistry(RPGQuests plugin) {
		this.plugin = plugin;
		this.id_gen = new IDGen(this);
		
		this.quests = new ArrayList<>();
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
	
}