package com.cracksn0w.rpgquests.commands.q;

import org.bukkit.entity.Player;

import com.cracksn0w.rpgquests.commands.SubCommand;

import net.md_5.bungee.api.ChatColor;

public class HelpSubCommand implements SubCommand {

	@Override
	public void onCommand(Player player, String[] args) {
		if(player.hasPermission("rpgquests.q.help")) {
			
			player.sendMessage(ChatColor.GREEN + "------ RPG Quests Hilfe ------");
			player.sendMessage(ChatColor.GOLD + "/q" + ChatColor.WHITE + " | Zeigt dir deine aktiven Quests!");
			player.sendMessage(ChatColor.GOLD + "/q progress <id>" + ChatColor.WHITE + " | Zeigt dir deine deinen fortschritt bei einer Quest!");
			player.sendMessage(ChatColor.GOLD + "/q available" + ChatColor.WHITE + " | Zeigt für dich verfügbare quests!");
			
		}else player.sendMessage(ChatColor.RED + "Du draft das nicht!");
	}

}
