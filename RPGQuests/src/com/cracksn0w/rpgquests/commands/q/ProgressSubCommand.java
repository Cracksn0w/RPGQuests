package com.cracksn0w.rpgquests.commands.q;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.cracksn0w.rpgquests.commands.SubCommand;
import com.cracksn0w.rpgquests.companion.QuestCompanion;
import com.cracksn0w.rpgquests.quest.Quest;

public class ProgressSubCommand implements SubCommand {

	private QCommand exe;
	
	public ProgressSubCommand(QCommand exe) {
		this.exe = exe;
	}

	public void onCommand(Player player, String[] args) {
		if(player.hasPermission("rpgquests.q.progress")) {
			
			int id;
			
			try {
				id = Integer.parseInt(args[0]);
			}catch (Exception e) {
				player.sendMessage(ChatColor.RED + "Bitte gebe die Id der Quest ein!");
				return;
			}
			
			Quest quest = exe.getPlugin().getQuestRegistry().getById(id);
			
			if(quest.isOnQuest(player)) {
				
				QuestCompanion qc = exe.getPlugin().getQuestRegistry().getQCForQuest(player.getUniqueId(), quest);
				player.sendMessage(qc.getProgressMsg());
				
			}else player.sendMessage(ChatColor.RED + "Du arbeitest gar nicht an dieser Quest!");
			
		}else player.sendMessage(ChatColor.RED + "Du hast nicht das Recht dazu!");
	}

}