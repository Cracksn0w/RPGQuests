package com.cracksn0w.rpgquests.commands.q;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cracksn0w.rpgquests.RPGQuests;
import com.cracksn0w.rpgquests.companion.QuestCompanion;

public class QCommand implements CommandExecutor {

	private RPGQuests plugin;
	
	private ProgressSubCommand progress;
	private AvailableSubCommand available;
	private HelpSubCommand help;
	
	public QCommand(RPGQuests plugin) {
		this.plugin = plugin;
		
		progress = new ProgressSubCommand(this);
		available = new AvailableSubCommand(this);
		help = new HelpSubCommand();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			
			if(args.length == 0) {
				
				if(player.hasPermission("rpgquests.q")) {
					showActiveQuests(player);
				}else player.sendMessage(ChatColor.RED + "Du hast nicht das Recht dazu!");
				
			}else {
				
				switch(args[0]) {
				case "progress":
					
					progress.onCommand(player, Arrays.copyOfRange(args, 1, args.length));
					break;
				case "available":
					
					available.onCommand(player, null);
					break;
				case "help":
					
					help.onCommand(player, null);
					break;
				default:
					player.sendMessage(ChatColor.RED + "Kommando nicht gefunden!");
				}
				
			}
			
		}else sender.sendMessage(ChatColor.RED + "Dieses Kommando können nur Spieler benutzen!");
		
		return true;
	}

	private void showActiveQuests(Player player) {
		player.sendMessage(ChatColor.GREEN + "------ Aktive Quests ------");
		
		ArrayList<QuestCompanion> qcs = plugin.getQuestRegistry().getQCsForUUID(player.getUniqueId());
		
		if(qcs.size() == 0) player.sendMessage(ChatColor.WHITE + "Keine Quest gefunden!");
		else {
			
			int i = 1;
			for(QuestCompanion qc : qcs) {
				player.sendMessage(i + ". " + ChatColor.AQUA + qc.getQuest().getName() + ChatColor.WHITE + " | " + ChatColor.RED + qc.getQuest().getId());
				i++;
			}
			
		}
	}
	
	public RPGQuests getPlugin() {
		return plugin;
	}
	
}