package com.cracksn0w.rpgquests;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

//import java.util.ArrayList;
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.entity.EntityType;
//import org.bukkit.inventory.ItemStack;
//import com.cracksn0w.rpgquests.quest.Quest;
//import com.cracksn0w.rpgquests.quest.requirement.Requirement;
//import com.cracksn0w.rpgquests.quest.requirement.RequirementType;
//import com.cracksn0w.rpgquests.quest.reward.Reward;
//import com.cracksn0w.rpgquests.quest.reward.RewardType;
//import com.cracksn0w.rpgquests.quest.task.CollectTask;
//import com.cracksn0w.rpgquests.quest.task.KillTask;

import net.milkbowl.vault.economy.Economy;

public class RPGQuests extends JavaPlugin {

	private Economy econ;
	
	private QuestRegistry quest_registry;
	
	/**
	 * Einstieg in das Programm. Erste Methode die aufgerufen wird.
	 */
	@Override
	public void onEnable() {
		if(checkEconomy()) this.getLogger().info("Vault gefunden!");
		else this.getLogger().severe("Vault wurde nicht gefunden!");
		
		this.getConfig().options().copyHeader(true);
		this.saveDefaultConfig();
		
		if(setupCitizens()) this.getLogger().info("Citizens found!");
		else {
			this.getLogger().severe("Citizens not found! Shutting down...");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		quest_registry = new QuestRegistry(this);
		
//		test();
	}
	
	@Override
	public void onDisable() {
		quest_registry.saveQuests();
		
		for(Player player : Bukkit.getOnlinePlayers()) quest_registry.saveQuestCompanions(player.getUniqueId());
	}
	
	/**
	 * @return Wurde das Wirtschaftssystem "Vault" gefunden oder nicht und ob der Variable "econ" ein Wert zugewiesen wurde.
	 */
	public boolean checkEconomy() {
		if(this.getServer().getPluginManager().getPlugin("Vault") == null) return false;
		
		RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
		if(rsp == null) return false;
		
		econ = rsp.getProvider();
		if(econ != null) return true;
		
		return false;
	}
	
	/**
	 * @return Wurde das Plugin "Citizens" gefunden oder nicht.
	 */
	private boolean setupCitizens() {
		return this.getServer().getPluginManager().isPluginEnabled("Citizens");
	}
	
	public QuestRegistry getQuestRegistry() {
		return quest_registry;
	}
	
//	/**
//	 * Test methode.
//	 */
//	private void test() {
//		
//		Quest quest = quest_registry.createQuest("Quest 1", "Thomas Müller");
//		quest.setEnabled(true);
//		
//		Requirement rq = new Requirement(RequirementType.LEVEL, 1);
//		quest.addRequirement(rq);
//		
//		Reward mr = new Reward(RewardType.MONEY ,20.0);
//		quest.addReward(mr);
//		
//		Reward ir = new Reward(RewardType.ITEM, new ItemStack(Material.GRASS, 2));
//		quest.addReward(ir);
//		
//		CollectTask ct = new CollectTask(Material.GRASS, 3, 0);
//		quest.addTask(ct);
//		
//		KillTask kt = new KillTask(EntityType.CHICKEN, 1);
//		quest.addTask(kt);
//		
//		ArrayList<String> list = new ArrayList<>();
//		list.add("Das ist eine Quest zum testen der Funktionsweise!");
//		list.add("Funktioniert doch super oder?!");
//		list.add("Man kann machen was man will ist das nicht toll!?");
//		
//		quest.getQuestNPC().setMessage(list);
//		
//		quest.spawnQuestNPC(new Location(this.getServer().getWorld("world"), 475, 67, -263));
//		
//	}
	
	public Economy getEconomy() {
		return econ;
	}
	
}