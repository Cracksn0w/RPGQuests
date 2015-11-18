package com.cracksn0w.rpgquests.quest.npc;

import org.bukkit.Location;

import net.citizensnpcs.api.npc.NPC;

public class QuestNPC {

	NPC npc;
	int id;
	
	public QuestNPC(NPC npc) {
		this.npc = npc;
		id = npc.getId();
	}
	
	public NPC getNPC() {
		return npc;
	}
	
	public int getid() {
		return id;
	}
	
	public Location getLocation() {
		return npc.getStoredLocation();
	}
	
}