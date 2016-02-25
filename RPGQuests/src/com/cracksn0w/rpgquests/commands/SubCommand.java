package com.cracksn0w.rpgquests.commands;

import org.bukkit.entity.Player;

public interface SubCommand {
	
	public void onCommand(Player player, String[] args);
	
}