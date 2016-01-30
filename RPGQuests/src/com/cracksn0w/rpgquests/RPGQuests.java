package com.cracksn0w.rpgquests;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.cracksn0w.rpgquests.quest.Quest;
import com.cracksn0w.rpgquests.quest.QuestRegistry;
import com.cracksn0w.rpgquests.quest.reward.MoneyReward;
import com.cracksn0w.rpgquests.quest.task.CollectTask;
import com.sun.org.apache.bcel.internal.generic.AALOAD;

import net.milkbowl.vault.economy.Economy;

public class RPGQuests extends JavaPlugin {

	private Economy econ;
	
	private QuestRegistry quest_registry;
	
	/**
	 * Einstieg in das Programm. Erste Methode die aufgerufen wird.
	 */
	@Override
	public void onEnable() {
		if(setupEconomy()) this.getLogger().info("Vault found!");
		else this.getLogger().severe("Vault not found!");
		
		if(setupCitizens()) this.getLogger().info("Citizens found!");
		else {
			this.getLogger().severe("Citizens not found! Shutting down...");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		quest_registry = new QuestRegistry(this);
		
		test();
	}
	
	@Override
	public void onDisable() {
		for(Quest quest : quest_registry.getQuests()) {
			quest.getQuestNPC().getNPC().despawn();
		}
	}
	
	/**
	 * @return Wurde das Wirtschaftssystem "Vault" gefunden oder nicht und ob der Variable "econ" ein Wert zugewiesen wurde.
	 */
	private boolean setupEconomy() {
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
	
	/**
	 * Test methode.
	 */
	private void test() {
		
		Quest quest = quest_registry.createQuest("Test1", "Questman 1");
		
		MoneyReward mr = new MoneyReward(20.0);
		quest.addReward(mr);
		
		CollectTask ct = new CollectTask(new ItemStack(Material.DIAMOND, 5));
		quest.addTask(ct);
		
		ArrayList<String> list = new ArrayList<>();
		list.add("&4Hello my friend how are you?!");
		list.add("&1Das ist bestimmt deine erste Quest oder?");
		
		quest.getQuestNPC().setMessage(list);
		
		quest.spawnQuestNPC(new Location(this.getServer().getWorld("world"), 475, 67, -263));
		
	}
	
}