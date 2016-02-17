package com.cracksn0w.rpgquests.companion;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import com.cracksn0w.rpgquests.QuestRegistry;
import com.cracksn0w.rpgquests.companion.listener.CollectTaskListener;
import com.cracksn0w.rpgquests.companion.listener.KillTaskListener;
import com.cracksn0w.rpgquests.companion.listener.TaskListener;
import com.cracksn0w.rpgquests.events.QuestFinishEvent;
import com.cracksn0w.rpgquests.quest.Quest;
import com.cracksn0w.rpgquests.quest.reward.Reward;
import com.cracksn0w.rpgquests.quest.task.CollectTask;
import com.cracksn0w.rpgquests.quest.task.KillTask;
import com.cracksn0w.rpgquests.quest.task.Task;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.item.ItemInfo;
import net.milkbowl.vault.item.Items;

public class QuestCompanion {

	private QuestRegistry quest_registry;
	private UUID uuid;
	private Quest quest;
	private Task current_task;
	private Object progress;
	
	private TaskListener quest_listener;
	
	//Konstruktor wird fuer einen neuen QuestCompanion benutzt
	public QuestCompanion(QuestRegistry quest_registry, Player player, Quest quest) {
		this.quest_registry = quest_registry;
		this.uuid = player.getUniqueId();
		this.quest = quest;
		
		//null --> get the first task
		current_task = quest.getNextTask(null);
		
		this.sendToDoMsg(player);
		
		this.onTaskStart();
	}
	
	//Konstruktor wird benutzt nachdem ein QuestCompanion vom Dateisystem geladen wurde
	public QuestCompanion(QuestRegistry quest_registry, UUID uuid, Quest quest, Task current_task, Object progress) {
		this.quest_registry = quest_registry;
		this.uuid = uuid;
		this.quest = quest;
		this.current_task = current_task;
		this.progress = progress;
	}
	
	public void setTask(Task task) {
		current_task = task;
	}
	
	public Task getCurrentTask() {
		return current_task;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public Quest getQuest() {
		return quest;
	}
	
	public void setProgress(Object obj) {
		progress = obj;
	}
	
	public Object getProgress() {
		return progress;
	}
	
	public QuestRegistry getQuestRegistry() {
		return quest_registry;
	}
	
	public void onTaskFinish() {
		Player player = this.getPlayer();
		
		if(quest.getTasks().indexOf(current_task) == quest.getTasks().size() - 1) {
			this.onQuestFinish();
		}else {
			
			player.sendMessage(ChatColor.GREEN + quest.getQuestNPC().getNPC().getFullName() + ":" + ChatColor.WHITE + " Aufgabe " + (quest.getIndexOfTask(current_task) + 1) + "/" + quest.getTasks().size() + " der Quest " + quest.getName() + " erledigt!");
			current_task = quest.getNextTask(current_task);
			this.setProgress(null);
			
			this.sendToDoMsg(player);
			
			this.onTaskStart();
			
		}
	}
	
	public void onQuestFinish() {
		Player player = this.getPlayer();
		
		player.sendMessage(ChatColor.GREEN + quest.getQuestNPC().getNPC().getFullName() + ":" + ChatColor.WHITE + " Glückwunsch! Du hast die Quest " + quest.getName() + " beendet!");
		this.giveRewards();
		
		//call custom event
		Bukkit.getServer().getPluginManager().callEvent(new QuestFinishEvent(player, quest));
		
		quest_registry.removeQuestCompanion(this);
		
		this.removeSaveFile();
	}
	
	public void onTaskStart() {
		this.registerQCListener();
		
		//this.getPlayer().sendMessage(ChatColor.GREEN + quest.getQuestNPC().getNPC().getFullName() + ":" + ChatColor.WHITE + " Aufgabe " + (quest.getIndexOfTask(current_task) + 1) + "/" + quest.getTasks().size() + " der Quest " + quest.getName() + " angefangen!");
	}
	
	public void registerQCListener() {
		switch(current_task.getTaskType()) {
		case COLLECT:
			quest_listener = new CollectTaskListener(this);
			
			break;
		case KILL:
			quest_listener = new KillTaskListener(this);
			
			break;
		default:
			quest_registry.getPlugin().getLogger().severe("QuestCompanion | UUID: " + uuid + " | Unbekannter TaskType!");
			
			break;
		}
	}
	
	public void giveRewards() {
		Player player = this.getPlayer();
		
		player.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "------ Belohnung/en: ------");
		
		for(Reward reward : quest.getRewards()) {
			switch(reward.getRewardType()) {
			case ITEM:
				ItemStack stack = (ItemStack) reward.getActualReward();
				player.getInventory().addItem(stack);
				player.sendMessage(ChatColor.GOLD + " - Du hast " + stack.getAmount() + " " + stack.getType() + " bekommen!");
				
				break;
			case MONEY:
				double refund = (double) reward.getActualReward();
				
				quest_registry.getPlugin().checkEconomy();
				Economy econ = quest_registry.getPlugin().getEconomy();
				
				if(econ == null) break;
				
				econ.depositPlayer(player, refund);
				player.sendMessage(ChatColor.GOLD + " - Du hast " + refund + " " + econ.currencyNamePlural() + " bekommen!");
				
				break;
			case XP:
				int xp = (int) reward.getActualReward();
				player.giveExp(xp);
				player.sendMessage(ChatColor.GOLD + " - Du hast " + xp + " Erfahrungspunkte erhalten!");
				
				break;
			default:
				quest_registry.getPlugin().getLogger().severe("QuestCompanion | UUID: " + uuid + " | Unbekannter RewardType!");
				
				break;
			}
		}
	}
	
	public void onDisable() {
		if(quest_listener != null) HandlerList.unregisterAll(quest_listener);
	}
	
	public void onQuestRemoved() {
		Player player = this.getPlayer();
		
		if(player.isOnline()) {
			player.sendMessage(ChatColor.RED + "Die Quest " + quest.getName() + " wurde entfernt...");
		}
		
		this.onDisable();
	}
	
	public void onCurrentTaskRemoved() {
		this.onTaskFinish();
	}
	
	public Player getPlayer() {
		return Bukkit.getServer().getPlayer(uuid);
	}
	
	public void sendToDoMsg(Player player) {
		switch(current_task.getTaskType()) {
		case COLLECT:
			CollectTask ct = (CollectTask) current_task;
			ItemInfo iteminfo = Items.itemByType(ct.getMaterial(), (short) ct.getMaterialData());
			player.sendMessage(ChatColor.GREEN + quest.getQuestNPC().getNPC().getFullName() + ":" + ChatColor.WHITE + " Sammel " + ct.getAmount() + " " + iteminfo.getName() + ".");
			
			break;
		case KILL:
			KillTask kt = (KillTask) current_task;
			player.sendMessage(ChatColor.GREEN + quest.getQuestNPC().getNPC().getFullName() + ":" + ChatColor.WHITE + " Erlege " + kt.getAmount() + " " + kt.getEntityType() + ".");
			
			break;
		default:
			quest_registry.getPlugin().getLogger().severe("QuestCompanion | UUID: " + uuid + " | Unbekannter TaskType!");
			
			break;
		}
	}
	
	public void onPlayerConnect() {
		this.registerQCListener();
	}
	
	public void onPlayerDisconnect() {
		this.onDisable();
	}
	
	private void removeSaveFile() {
		Bukkit.getScheduler().runTaskAsynchronously(quest_registry.getPlugin(), new Runnable() {
			
			public void run() {
				File file = new File(quest_registry.getPlugin().getDataFolder() + "/companions/" + uuid + "_" + quest.getId() + ".yml");
				file.delete();
			}
			
		});
	}
	
}