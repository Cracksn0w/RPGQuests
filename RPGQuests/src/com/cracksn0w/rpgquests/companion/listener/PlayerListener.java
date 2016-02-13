package com.cracksn0w.rpgquests.companion.listener;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.cracksn0w.rpgquests.QuestRegistry;
import com.cracksn0w.rpgquests.companion.QuestCompanion;

public class PlayerListener implements Listener {

	private QuestRegistry quest_registry;
	
	private ArrayList<Item> dropped_items;
	private ArrayList<Block> placed_blocks;
	
	public PlayerListener(QuestRegistry quest_registry) {
		this.quest_registry = quest_registry;
		
		dropped_items = new ArrayList<>();
		placed_blocks = new ArrayList<>();
		
		this.registerCleanupRepeatingTask();
		
		Bukkit.getServer().getPluginManager().registerEvents(this, quest_registry.getPlugin());
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		for(QuestCompanion qc : quest_registry.getQCsForPlayer(player)) {
			qc.onPlayerConnect();
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		for(QuestCompanion qc : quest_registry.getQCsForPlayer(player)) {
			qc.onPlayerDisconnect();
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if(this.isBuildByPlayer(event.getBlock())) {
			Block b = event.getBlock();
			
			for(ItemStack stack : b.getDrops()) {
				this.removeBlock(b);
				this.addItem(b.getWorld().dropItemNaturally(b.getLocation(), stack));
			}
			
			b.setType(Material.AIR, true);
			
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onItemDropped(PlayerDropItemEvent event) {
		this.addItem(event.getItemDrop());
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		this.addBlock(event.getBlock());
	}
	
	@EventHandler
	public void onItemMerge(ItemMergeEvent event) {
		Item merged_item = event.getEntity();
		
		if(this.isDroppedByPlayer(merged_item)) {
			this.removeItem(merged_item);
			this.addItem(event.getTarget());
		}
	}
	
	@EventHandler
	public void onItemDespanw(ItemDespawnEvent event) {
		if(this.isDroppedByPlayer(event.getEntity())) {
			this.removeItem(event.getEntity());
		}
	}
	
	@EventHandler
	public void onItemCombustByBlock(EntityCombustByBlockEvent event) {
		this.removeCombustedEntity(event.getEntity());
	}
	
	@EventHandler
	public void onItemCombustByEntity(EntityCombustByEntityEvent event) {
		this.removeCombustedEntity(event.getEntity());
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onItemPickup(PlayerPickupItemEvent event) {
		if(this.isDroppedByPlayer(event.getItem())) {
			this.removeItem(event.getItem());
		}
	}
	
	public boolean isDroppedByPlayer(Item item) {
		if(dropped_items.contains(item)) return true;
		else return false;
	}
	
	private boolean isBuildByPlayer(Block block) {
		if(placed_blocks.contains(block)) return true;
		else return false;
	}
	
	public void removeItem(Item item) {
		dropped_items.remove(item);
	}
	
	private void addItem(Item item) {
		dropped_items.add(item);
	}
	
	private void addBlock(Block block) {
		placed_blocks.add(block);
	}
	
	private void removeBlock(Block block) {
		placed_blocks.remove(block);
	}
	
	private void removeCombustedEntity(Entity entity) {
		if(entity instanceof Item) {
			Item combusted = (Item) entity;
			
			if(this.isDroppedByPlayer(combusted)) {
				this.removeItem(combusted);
			}
		}
	}

	private void registerCleanupRepeatingTask() {
		Bukkit.getScheduler().runTaskTimer(quest_registry.getPlugin(), new Runnable() {
			
			public void run() {
				dropped_items.clear();
				placed_blocks.clear();
				
				quest_registry.getPlugin().getLogger().info("Speicher gesäubert!");
			}
			
		}, 1200, quest_registry.getPlugin().getConfig().getInt("cleanup-intervall") * 1200);
	}
	
}