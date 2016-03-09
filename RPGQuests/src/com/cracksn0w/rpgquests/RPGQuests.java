package com.cracksn0w.rpgquests;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.cracksn0w.rpgquests.commands.q.QCommand;

//Vault API --> http://dev.bukkit.org/bukkit-plugins/vault/

//Citizens2 API --> http://wiki.citizensnpcs.co/Downloads

import net.milkbowl.vault.economy.Economy;

public class RPGQuests extends JavaPlugin {
	
	private Economy econ;
	
	private QuestRegistry quest_registry;
	
	/**
	 * Diese Methode wird vom PluginManager aufgerufen, wenn das Plugin geladen wurde. Sie ist der Einstieg in den Programm ablauf.
	 */
	@Override
	public void onEnable() {
		if(checkEconomy()) this.getLogger().info("Vault gefunden!");
		else this.getLogger().severe("Vault wurde nicht gefunden!");
		
		this.getConfig().options().copyHeader(true);
		this.saveDefaultConfig();
		
		if(isCitizensInstalled()) this.getLogger().info("Citizens found!");
		else {
			this.getLogger().severe("Citizens not found! Shutting down...");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		quest_registry = new QuestRegistry(this);
		
		this.registerCommands();
	}
	
	/**
	 * Diese Methode wird vom PluginManager aufgerufen, wenn der Server beendet wird.
	 */
	@Override
	public void onDisable() {
		quest_registry.saveQuests();
		
		for(Player player : Bukkit.getOnlinePlayers()) quest_registry.saveQuestCompanions(player.getUniqueId());
	}
	
	/**
	 * Überprüft, ob das Plugin Vault gefunden wurde. Das Plugin Vault bietet ein Wirtschaftssystem.
	 * 
	 * @return Wahr, wenn das Plugin Vault gefunden wurde.
	 */
	public boolean checkEconomy() {
		if(this.getServer().getPluginManager().getPlugin("Vault") == null) return false;
		
		RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
		if(rsp == null) return false;
		
		econ = rsp.getProvider();
		if(econ != null) return true;
		
		return false;
	}
	
	private boolean isCitizensInstalled() {
		return this.getServer().getPluginManager().isPluginEnabled("Citizens");
	}
	
	/**
	 * Gibt eine Instanz der aktuellen QuestRegistry zurück.
	 * 
	 * @return Aktuelle QuestRegistry
	 */
	public QuestRegistry getQuestRegistry() {
		return quest_registry;
	}
	
	/**
	 * Gibt eine Instanz des Wirtschaftssystem zurück.
	 * 
	 * @return Das aktuelle Wirtschaftssystem.
	 */
	public Economy getEconomy() {
		return econ;
	}
	
	private void registerCommands() {
		this.getCommand("q").setExecutor(new QCommand(this));
	}
	
}