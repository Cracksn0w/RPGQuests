package com.cracksn0w.rpgquests.utils;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardAPI {

	public static Scoreboard createScoreboard(String displayname) {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		
		Objective obj = scoreboard.registerNewObjective("default", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(displayname);
		
		return scoreboard;
	}
	
	public static void addText(Scoreboard scoreboard, String text) {
		Objective obj = scoreboard.getObjective("default");
		
		if(obj != null) {
			Score score = obj.getScore(text);
			score.setScore(0);
		}else {
			Bukkit.getLogger().severe("Scoreboard: No objective found!");
		}
	}
	
}