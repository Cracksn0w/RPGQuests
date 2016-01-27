package com.cracksn0w.rpgquests.quest.npc;

import java.util.ArrayList;

import org.bukkit.Location;

import com.cracksn0w.rpgquests.quest.Quest;

import net.citizensnpcs.api.npc.NPC;

public class QuestNPC {

	private Quest quest;
	private NPC npc;
	private int id;
	
	private ArrayList<String> message;
	
	private InteractionListener interaction_listener;
	
	public QuestNPC(Quest quest, NPC npc) {
		this.quest = quest;
		this.npc = npc;
		id = npc.getId();
		npc.setProtected(true);
		
		this.message = new ArrayList<>();
		
		interaction_listener = new InteractionListener(this);
	}
	
	public void setMessage(ArrayList<String> message) {
		this.message = message;
	}
	
	public ArrayList<String> getMessage() {
		return message;
	}
	
	public NPC getNPC() {
		return npc;
	}
	
	public Quest getQuest() {
		return quest;
	}
	
	public int getid() {
		return id;
	}
	
	public Location getLocation() {
		return npc.getStoredLocation();
	}
	
}