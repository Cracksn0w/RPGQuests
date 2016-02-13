package com.cracksn0w.rpgquests.quest.npc.listener;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.cracksn0w.rpgquests.quest.Quest;
import com.cracksn0w.rpgquests.quest.npc.QuestNPC;
import com.cracksn0w.rpgquests.quest.requirement.Requirement;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.ChatColor;

public class InteractionListener implements Listener {

	private QuestNPC quest_npc;
	private Quest quest;
	
	private String interact_permission = "rpgquests.npc.interact";
	
	private ArrayList<Player> answering_player;
	
	public InteractionListener(QuestNPC quest_npc) {
		this.quest_npc = quest_npc;
		this.quest = quest_npc.getQuest();
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
			
			if(answering_player.contains(player)) return;
			
			if(player.hasPermission(interact_permission) && quest.isEnabled()) {
				if(quest.isOnQuest(player)) {
					player.sendMessage(ChatColor.GREEN + npc.getFullName() + ": " + ChatColor.WHITE + "Du arbeitest bereits an dieser Quest!");
					return;
				}
				
				if(quest.meetRequirements(player) == false) {
					this.sendRequirementMsg(player);
					return;
				}
				
				player.sendMessage(ChatColor.GOLD + " ~ Quest: " + quest.getName() + " ~");
				player.sendMessage(ChatColor.GREEN + npc.getFullName() + ":");
				
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
				quest.getQuestRegistry().createQuestCompanion(player, quest);
				
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
	
	private void sendRequirementMsg(Player player) {
		player.sendMessage(ChatColor.GREEN + quest_npc.getNPC().getFullName() + ": " + ChatColor.WHITE + "Anscheinend erfüllst du nicht alle Vorraussetzungen für diese Aufgabe!");
		player.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "------ Vorraussetzung/en: ------");
		
		for(Requirement rqm : quest.getRequirements()) {
			switch(rqm.getRequirementType()) {
			case ITEM:
				player.sendMessage(ChatColor.GOLD + " - Du must " + rqm.getAddition() + " " + rqm.getRequirement() + " haben!");
				
				break;
			case LEVEL:
				player.sendMessage(ChatColor.GOLD + " - Du must Level " + rqm.getRequirement() + " sein!");
				
				break;
			default:
				quest.getQuestRegistry().getPlugin().getLogger().severe("InteractionListener | Quest: " + quest.getName() + " | Unbekannter RequirementType!");
				
				break;
			}
		}
	}
	
}