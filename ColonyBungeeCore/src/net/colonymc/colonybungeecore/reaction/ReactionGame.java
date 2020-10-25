package net.colonymc.colonybungeecore.reaction;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import net.colonymc.colonybungeecore.CenteredMessage;
import net.colonymc.colonybungeecore.Main;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class ReactionGame implements Runnable {

	int i = 0;
	public boolean isStarted = false;
	type currentType = null;
	String question = null;
	String answer = null;
	ServerInfo server;
	ScheduledTask task = null;
	ReactionsQuestions questions;
	public enum type {
		MATH,
		WORD,
		UNSCRAMBLE,
		RANDOM
	}

	public ReactionGame(ServerInfo server, type selectedType) {
		if(selectedType == type.RANDOM) {
			Random rand = new Random();
			int random = rand.nextInt(3);
			switch(random) {
			case 0:
				currentType = type.MATH;
				break;
			case 1:
				currentType = type.UNSCRAMBLE;
				break;
			case 2:
				currentType = type.WORD;
				break;
			}
			this.server = server;
		}
		else {
			this.server = server;
			currentType = selectedType;
		}
	}
	
	@Override
	public void run() {
		if(i == 0) {
			isStarted = true;
			questions = new ReactionsQuestions();
			broadcastMessage(currentType);
		}
		else if(i == 30000) {
			endGame(null);
		}
		i++;
	}
	
	public void startGame() {
		if (ReactionCommand.reactions.containsKey(server)) {
			ReactionCommand.reactions.get(server).endGame(null);
		}
		task = ProxyServer.getInstance().getScheduler().schedule(Main.getInstance(), this, 0, 1, TimeUnit.MILLISECONDS);
	}
	
	public void endGame(ProxiedPlayer winner) {
		ReactionCommand.reactions.remove(server);
		task.cancel();
		if(winner == null) {
			sendMessageToServer(server, new String[] {" "});
			sendMessageToServer(server, new String[] {CenteredMessage.getCenteredMessage("&d&lREACTION GAME")});
			sendMessageToServer(server, new String[] {CenteredMessage.getCenteredMessage("&cNobody answered the question!")});
			sendMessageToServer(server, new String[] {CenteredMessage.getCenteredMessage("&fAnswer: &d" + answer)});
			sendMessageToServer(server, new String[] {" "});
		}
		else {
			sendMessageToServer(server, new String[] {" "});
			sendMessageToServer(server, new String[] {CenteredMessage.getCenteredMessage("&d&lREACTION GAME")});
			sendMessageToServer(server, new String[] {CenteredMessage.getCenteredMessage("&d" + winner.getName() + " &fanswered correctly and won &d$500&f!")});
			sendMessageToServer(server, new String[] {CenteredMessage.getCenteredMessage("&fAnswer: &d" + answer)});
			sendMessageToServer(server, new String[] {" "});
		}
	}
	
	public void broadcastMessage(type actualType) {
		switch(actualType) {
		case MATH:
			sendMessageToServer(server, new String[] {" "});
			sendMessageToServer(server, new String[] {CenteredMessage.getCenteredMessage("&d&lREACTION GAME")});
			sendMessageToServer(server, new String[] {CenteredMessage.getCenteredMessage("&fBe the first player to solve this and win &d$500")});
			sendMessageToServer(server, new String[] {CenteredMessage.getCenteredMessage("&fQuestion: &d" + getQuestion(type.MATH))});
			sendMessageToServer(server, new String[] {" "});
			break;
		case UNSCRAMBLE:
			sendMessageToServer(server, new String[] {" "});
			sendMessageToServer(server, new String[] {CenteredMessage.getCenteredMessage("&d&lREACTION GAME")});
			sendMessageToServer(server, new String[] {CenteredMessage.getCenteredMessage("&fBe the first player to unscramble the word below and win &d$500")});
			sendMessageToServer(server, new String[] {CenteredMessage.getCenteredMessage("&fWord: &d" + getQuestion(type.UNSCRAMBLE))});
			sendMessageToServer(server, new String[] {" "});
			break;
		case WORD:
			sendMessageToServer(server, new String[] {" "});
			sendMessageToServer(server, new String[] {CenteredMessage.getCenteredMessage("&d&lREACTION GAME")});
			sendMessageToServer(server, new String[] {CenteredMessage.getCenteredMessage("&fBe the first player to write this word and win &d$500")});
			sendMessageToServer(server, new String[] {CenteredMessage.getCenteredMessage("&fWord: &d" + getQuestion(type.WORD))});
			sendMessageToServer(server, new String[] {" "});
			break;
		default:
			break;
		}
	}
	
	public String getQuestion(type actualType) {
		switch(actualType) {
		case MATH:
			String[] qna = questions.getRandomMath();
			question = qna[0];
			answer = qna[1];
			return question;
		case UNSCRAMBLE:
			String[] qna2 = questions.getRandomUnscramble();
			question = qna2[0];
			answer = qna2[1];
			return question;
		case WORD:
			String qna1 = questions.getRandomWrite();
			question = qna1;
			answer = qna1;
			return question;
		default:
			break;
		}
		return "";
	}
	
	public void sendMessageToServer(ServerInfo server, String[] s) {
		for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
			if(p.getServer().getInfo().equals(server)) {
				for(String str : s) {
					p.sendMessage(new TextComponent(str));
				}
			}
		}
	}
	
	public String getAnswer() {
		return answer;
	}
	

}
