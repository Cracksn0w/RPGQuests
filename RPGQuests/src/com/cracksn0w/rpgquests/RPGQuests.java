package com.cracksn0w.rpgquests;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class RPGQuests extends JavaPlugin {

	private Economy econ;
	
	@Override
	public void onEnable() {
		if(setupEconomy()) this.getLogger().info("Vault loaded!");
		else this.getLogger().severe("Vault not found!");
		
		
	}
	
	private boolean setupEconomy() {
		if(this.getServer().getPluginManager().getPlugin("Vault") == null) return false;
		
		RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
		if(rsp == null) return false;
		
		econ = rsp.getProvider();
		if(econ != null) return true;
		
		return false;
	}
	
}