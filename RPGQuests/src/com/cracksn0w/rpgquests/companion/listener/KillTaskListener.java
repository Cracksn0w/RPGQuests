package com.cracksn0w.rpgquests.companion.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;

import com.cracksn0w.rpgquests.companion.QuestCompanion;
import com.cracksn0w.rpgquests.quest.task.KillTask;

public class KillTaskListener implements TaskListener {

private QuestCompanion quest_companion;
	
	private int progress;
	
	public KillTaskListener(QuestCompanion quest_companion) {
		this.quest_companion = quest_companion;
		
		progress = 0;
		if(quest_companion.getProgress() != null) progress = (int) quest_companion.getProgress();
		
		Bukkit.getServer().getPluginManager().registerEvents(this, quest_companion.getQuestRegistry().getPlugin());
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		LivingEntity entity = event.getEntity();
		
		if(entity.getKiller().getUniqueId().equals(quest_companion.getUUID())) {
			KillTask task = (KillTask) quest_companion.getCurrentTask();
			
			if(entity.getType() == task.getEntityType()) {
				if(progress < task.getAmount()) {
					progress ++;
					quest_companion.setProgress(progress);
					quest_companion.getPlayer().sendMessage(quest_companion.getProgressMsg());
				}
				
				if(progress == task.getAmount()) {
					quest_companion.onTaskFinish();
					HandlerList.unregisterAll(this);
				}
			}
		}
	}
	
}