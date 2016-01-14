package com.cracksn0w.rpgquests.utils;

import java.util.Random;

import com.cracksn0w.rpgquests.quest.Quest;
import com.cracksn0w.rpgquests.quest.QuestRegistry;

public class IDGen {

	private QuestRegistry q_reg;
	
	public IDGen(QuestRegistry q_reg) {
		this.q_reg = q_reg;
	}
	
	public int generateID() {
		Random rnd = new Random();
		boolean found = true;
		
		generate:
		while(found) {
			int i = rnd.nextInt(10000);
			
			for(Quest q : q_reg.getQuests()) {
				if(q.getId() == i) continue generate;
			}
			
			return i;
		}
		
		return -1;
	}
	
}