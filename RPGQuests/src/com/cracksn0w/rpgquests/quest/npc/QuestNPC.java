package com.cracksn0w.rpgquests.quest.npc;

import org.bukkit.Location;

import net.citizensnpcs.api.npc.NPC;

public class QuestNPC {

	private NPC npc;
	private int id;
	
	public QuestNPC(NPC npc) {
		this.npc = npc;
		id = npc.getId();
		npc.setProtected(true);
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