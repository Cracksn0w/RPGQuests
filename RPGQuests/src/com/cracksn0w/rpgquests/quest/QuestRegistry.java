package com.cracksn0w.rpgquests.quest;

import java.util.ArrayList;

import com.cracksn0w.rpgquests.utils.IDGen;

public class QuestRegistry {

	private IDGen id_gen;
	
	private ArrayList<Quest> quests;
	
	public QuestRegistry() {
		this.id_gen = new IDGen(this);
		
		this.quests = new ArrayList<>();
	}
	
	//creates empty quest
	public Quest createQuest(String name, String npc_name) {
		Quest quest = new Quest(name, id_gen.generateID(), npc_name);
		quests.add(quest);
		return quest;
	}
	
	public ArrayList<Quest> getQuests() {
		return quests;
	}
	
}