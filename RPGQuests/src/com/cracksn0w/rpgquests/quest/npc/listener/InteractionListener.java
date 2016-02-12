package com.cracksn0w.rpgquests.quest.npc.listener;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.cracksn0w.rpgquests.quest.npc.QuestNPC;

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
		
		Bukkit.getPluginManager().registerEvents(this, quest_npc.getQuest().getQuestRegistry().getPlugin());
	}
	
	
    /**
     * Wird aufgerufen, wenn ein Spieler einen QuestNPC anklickt.
     * 
     * @param event Das Event, welches ausgelöst wird.
     */
	@EventHandler
	public void onNPCRightClickEvent(NPCRightClickEvent event) {
		NPC npc = event.getNPC();
		
		if(quest_npc.getNPC() == npc) {
			Player player = event.getClicker();
			
			if(player.hasPermission(interact_permission) && quest_npc.getQuest().isEnabled()) {
				player.sendMessage(ChatColor.GOLD + " ~ Quest: " + quest_npc.getQuest().getName() + " ~");
				player.sendMessage(ChatColor.GREEN + quest_npc.getNPC().getFullName() + ":");
				
				for(String msg_line : quest_npc.getMessage()) {
					player.sendMessage("  " + msg_line);
				}
				
				player.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + "Quest annehmen?");
				answering_player.add(player);
			}else {
				player.sendMessage(ChatColor.RED + "Entweder darfst du keine Quests erledigen oder die Quest ist deaktiviert!");
			}
		}
	}
	
	/**
	 * Diese Methode wird zur Überprüfung der Konversation zwischen NPC und Spieler benutzt.
	 * 
	 * @param event Das Event, welches ausgelöst wird.
	 */
	@EventHandler
	public void onPlayerChatEvent(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		
		if(answering_player.contains(player)) {
			String msg = event.getMessage();
			
			if(msg.equalsIgnoreCase("ja") || msg.equalsIgnoreCase("yes")) {
				quest_npc.getQuest().getQuestRegistry().createQuestCompanion(player, quest_npc.getQuest());
				
				player.sendMessage(ChatColor.GREEN + quest_npc.getNPC().getFullName() + ": " + ChatColor.WHITE + "Viel Spaß!");
				
				answering_player.remove(player);
			}else if(msg.equalsIgnoreCase("nein") || msg.equalsIgnoreCase("no")) {
				player.sendMessage(ChatColor.GREEN + quest_npc.getNPC().getFullName() + ": " + ChatColor.WHITE + "Schade! Komm später nochmal vorbei!");
				
				answering_player.remove(player);
			}else {
				player.sendMessage(ChatColor.RED + "Ja/Nein oder Yes/No!!!");
			}
			
			event.setCancelled(true);
		}
	}
	
}