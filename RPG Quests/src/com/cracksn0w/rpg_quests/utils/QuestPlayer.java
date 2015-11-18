package com.cracksn0w.rpg_quests.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class QuestPlayer {
	
	private UUID uuid;
	
	public QuestPlayer(UUID uuid) {
		this.uuid = uuid;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public Player getBukkitPlayer() {
		return Bukkit.getServer().getPlayer(uuid);
	}
	
}