package com.cracksn0w.rpgquests.companion.listener;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.cracksn0w.rpgquests.companion.QuestCompanion;
import com.cracksn0w.rpgquests.quest.task.CollectTask;

public class CollectTaskListener implements TaskListener {

	private QuestCompanion quest_companion;
	
	private int progress;
	
	private ArrayList<Item> dropped_items;
	private ArrayList<Block> placed_blocks;
	
	public CollectTaskListener(QuestCompanion quest_companion) {
		this.quest_companion = quest_companion;
		
		progress = 0;
		if(quest_companion.getProgress() != null) progress = (int) quest_companion.getProgress();
		
		this.dropped_items = new ArrayList<>();
		this.placed_blocks = new ArrayList<>();
		
		Bukkit.getServer().getPluginManager().registerEvents(this, quest_companion.getQuestRegistry().getPlugin());
	}
	
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		if(quest_companion.getPlayer() == event.getPlayer()) {
			ItemStack stack = event.getItem().getItemStack();
			CollectTask ct = (CollectTask) quest_companion.getCurrentTask();
			
			if(dropped_items.contains(event.getItem())) return;
			
			this.validation(event.getPlayer(), stack, ct);
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if((Player) event.getWhoClicked() == quest_companion.getPlayer()) {
			Player player = (Player) event.getWhoClicked();
			
			if(event.getInventory().getType() == InventoryType.WORKBENCH) {
				if(event.getSlotType() == SlotType.RESULT) {
					ItemStack stack = event.getCurrentItem();
					CollectTask ct = (CollectTask) quest_companion.getCurrentTask();
					
					this.validation(player, stack, ct);
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if(placed_blocks.contains(event.getBlock())) {
			Block b = event.getBlock();
			
			for(ItemStack stack : b.getDrops()) {
				dropped_items.add(b.getWorld().dropItemNaturally(b.getLocation(), stack));
			}
			
			b.setType(Material.AIR, true);
			
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onItemDropped(PlayerDropItemEvent event) {
		if(event.getPlayer() == quest_companion.getPlayer()) {
			dropped_items.add(event.getItemDrop());
		}else {
			if(event.getPlayer().getNearbyEntities(50, 50, 50).contains(quest_companion.getPlayer())) {
				dropped_items.add(event.getItemDrop());
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if(event.getPlayer() == quest_companion.getPlayer()) {
			placed_blocks.add(event.getBlock());
		}else {
			if(event.getPlayer().getNearbyEntities(50, 50, 50).contains(quest_companion.getPlayer())) {
				placed_blocks.add(event.getBlock());
			}
		}
	}
	
	private void validation(Player player, ItemStack stack, CollectTask ct) {
		if(stack.getType() == ct.getMaterial()) {
			if(progress < ct.getAmount()) {
				progress += stack.getAmount();
				
				if(progress > ct.getAmount()) progress = ct.getAmount();
				
				quest_companion.setProgress(progress);
				
				player.sendMessage(ChatColor.GREEN + quest_companion.getQuest().getName() + ": " + ChatColor.GRAY + "Du hast " + progress + "/" + ct.getAmount() + " " + ct.getMaterial() + " gesammelt.");
			}
			
			if(progress >= ct.getAmount()) {
				quest_companion.onTaskFinish();
				dropped_items.clear();
				HandlerList.unregisterAll(this);
			}
		}
	}
	
}