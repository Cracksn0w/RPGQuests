package com.cracksn0w.rpgquests.commands.q;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.cracksn0w.rpgquests.commands.SubCommand;
import com.cracksn0w.rpgquests.quest.Quest;

public class AvailableSubCommand implements SubCommand {

	private QCommand exe;
	
	public AvailableSubCommand(QCommand exe) {
		this.exe = exe;
	}

	public void onCommand(Player player, String[] args) {
		if(player.hasPermission("rpgquests.q.available")) {
			player.sendMessage(ChatColor.GREEN + "------ Verfügbare Quests ------");
			ArrayList<Quest> quests = exe.getPlugin().getQuestRegistry().getQuests();
			
			boolean foundOne = false;
				
			for(Quest quest : quests) {
				if(quest.meetRequirements(player) && quest.isOnQuest(player) == false) {
					Location loc = quest.getQuestNPC().getLocation();
					player.sendMessage(ChatColor.GOLD + quest.getName() + ChatColor.WHITE + " | X:" + ChatColor.RED + loc.getBlockX() + ChatColor.WHITE + " | Y:" + ChatColor.RED + loc.getBlockY() + ChatColor.WHITE + " | Z:" + ChatColor.RED + loc.getBlockZ());
					foundOne = true;
				}	
			}
			
			if(!foundOne) player.sendMessage(ChatColor.WHITE + "Keine Quest gefunden!");
			
		}else player.sendMessage(ChatColor.RED + "Du darfst das nicht!");
	}

}