package com.cracksn0w.rpgquests.quest.npc;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.ChatColor;

public class InteractionListener implements Listener {

	private QuestNPC quest_npc;
	
	private String interact_permission = "rpgquests.npc.interact";
	
	private ArrayList<Player> answering_player;
	
	public InteractionListener(QuestNPC quest_npc) {
		this.quest_npc = quest_npc;
		this.answering_player = new ArrayList<>();
		
		Bukkit.getPluginManager().registerEvents(this, quest_npc.getQuest().getPlugin());
	}
	
	@EventHandler
	public void onNPCRightClickEvent(NPCRightClickEvent event) {
		NPC npc = event.getNPC();
		
		if(quest_npc.getNPC() == npc) {
			Player player = event.getClicker();
			
			if(player.hasPermission(interact_permission)) {
				player.sendMessage(ChatColor.GOLD + "Quest: " + quest_npc.getQuest().getName());
				
				for(String msg_line : quest_npc.getMessage()) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg_line));
				}
				
				player.sendMessage(ChatColor.AQUA + "Quest annehmen?");
				answering_player.add(player);
			}
		}
	}
	
	@EventHandler
	public void onPlayerChatEvent(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		
		if(answering_player.contains(player)) {
			String msg = event.getMessage();
			
			if(msg.equalsIgnoreCase("ja") || msg.equalsIgnoreCase("yes")) {
				
				
				answering_player.remove(player);
			}else if(msg.equalsIgnoreCase("nein") || msg.equalsIgnoreCase("no")) {
				
				
				answering_player.remove(player);
			}else {
				player.sendMessage(ChatColor.RED + "Ja/Nein or Yes/No!!!");
			}
			
			event.setCancelled(true);
		}
	}
	
}