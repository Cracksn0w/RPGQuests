package com.cracksn0w.rpgquests.utils;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class GUIFactory {

	public Inventory getCreationGUI() {
		Inventory inv = Bukkit.createInventory(null, InventoryType.PLAYER, "Creating Quest...");
		
		return inv;
	}
	
}