package com.cracksn0w.rpgquests.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.cracksn0w.rpgquests.quest.Quest;

public class QuestFinishEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	private Player player;
	private Quest quest;
	
	public QuestFinishEvent(Player player, Quest quest) {
		this.player = player;
		this.quest = quest;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Quest getQuest() {
		return quest;
	}
	
}
