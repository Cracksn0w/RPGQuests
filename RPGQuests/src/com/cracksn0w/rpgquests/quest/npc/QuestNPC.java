package com.cracksn0w.rpgquests.quest.npc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.event.HandlerList;

import com.cracksn0w.rpgquests.quest.Quest;
import com.cracksn0w.rpgquests.quest.npc.listener.InteractionListener;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class QuestNPC {

	private Quest quest;
	private NPC npc;
	private int id;
	
	private List<String> message;
	
	private InteractionListener interaction_listener;
	
	public QuestNPC(Quest quest, NPC npc) {
		this.quest = quest;
		this.npc = npc;
		id = npc.getId();
		npc.setProtected(true);
		
		this.message = new ArrayList<>();
		
		interaction_listener = new InteractionListener(this);
	}
	
	public void setMessage(List<String> message2) {
		this.message = message2;
	}
	
	public List<String> getMessage() {
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
	
	public void remove() {
		CitizensAPI.getNPCRegistry().deregister(npc);
		HandlerList.unregisterAll(interaction_listener);
	}
	
}