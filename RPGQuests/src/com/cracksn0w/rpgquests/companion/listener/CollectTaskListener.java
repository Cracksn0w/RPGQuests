package com.cracksn0w.rpgquests.companion.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.cracksn0w.rpgquests.companion.QuestCompanion;
import com.cracksn0w.rpgquests.quest.task.CollectTask;

import net.milkbowl.vault.item.ItemInfo;
import net.milkbowl.vault.item.Items;

public class CollectTaskListener implements TaskListener {

	private QuestCompanion quest_companion;
	private PlayerListener player_listener;
	
	private int progress;
	
	public CollectTaskListener(QuestCompanion quest_companion) {
		this.quest_companion = quest_companion;
		this.player_listener = quest_companion.getQuestRegistry().getPlayerListener();
		
		progress = 0;
		if(quest_companion.getProgress() != null) progress = (int) quest_companion.getProgress();
		
		Bukkit.getServer().getPluginManager().registerEvents(this, quest_companion.getQuestRegistry().getPlugin());
	}
	
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		if(quest_companion.getUUID().equals(event.getPlayer().getUniqueId())) {
			ItemStack stack = event.getItem().getItemStack();
			CollectTask ct = (CollectTask) quest_companion.getCurrentTask();
			
			if(player_listener.isDroppedByPlayer(event.getItem())) {
				player_listener.removeItem(event.getItem());
				return;
			}
			
			this.validate(event.getPlayer(), stack, ct);
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(quest_companion.getUUID().equals(event.getWhoClicked())) {
			Player player = (Player) event.getWhoClicked();
			ItemStack stack = event.getCurrentItem();
			CollectTask ct = (CollectTask) quest_companion.getCurrentTask();
			InventoryType type = event.getInventory().getType();
			
			if(event.getSlotType() == SlotType.RESULT) {
				
				if(type == InventoryType.WORKBENCH || type == InventoryType.CRAFTING || type == InventoryType.BREWING || type == InventoryType.FURNACE) {
					validate(player, stack, ct);
				}
				
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void validate(Player player, ItemStack stack, CollectTask ct) {
		if(stack.getType() == ct.getMaterial()) {
			if(stack.getData() != null && ct.getMaterialData() != 0) {
				if(stack.getData().getData() != (byte) ct.getMaterialData()) return;
			}
			
			if(progress < ct.getAmount()) {
				progress += stack.getAmount();
				
				if(progress > ct.getAmount()) progress = ct.getAmount();
				
				quest_companion.setProgress(progress);
				
				ItemInfo iteminfo = Items.itemByType(ct.getMaterial(), (short) ct.getMaterialData());
				
				player.sendMessage(ChatColor.GREEN + quest_companion.getQuest().getName() + ": " + ChatColor.GRAY + "Du hast " + progress + "/" + ct.getAmount() + " " + iteminfo.getName() + " gesammelt.");
			}
			
			if(progress >= ct.getAmount()) {
				quest_companion.onTaskFinish();
				HandlerList.unregisterAll(this);
			}
		}
	}
	
}