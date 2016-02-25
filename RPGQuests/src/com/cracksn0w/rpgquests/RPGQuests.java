package com.cracksn0w.rpgquests;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.cracksn0w.rpgquests.commands.q.QCommand;

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
		
		this.registerCommands();
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
	
	public Economy getEconomy() {
		return econ;
	}
	
	private void registerCommands() {
		this.getCommand("q").setExecutor(new QCommand(this));
	}
	
}