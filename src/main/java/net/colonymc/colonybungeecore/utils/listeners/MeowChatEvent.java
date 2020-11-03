package net.colonymc.colonybungeecore.utils.listeners;

import net.colonymc.colonybungeecore.utils.commands.MeowCommand;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class MeowChatEvent implements Listener{

	@EventHandler
	public void onChat(ChatEvent e) {
		if(e.getSender() instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) e.getSender();
			if(!e.getMessage().startsWith("/")) {
				if(MeowCommand.meows.contains(p.getServer().getInfo().getName()) || MeowCommand.meows.contains("all")) {
						String[] words = e.getMessage().split(" ");
						StringBuilder msg = new StringBuilder();
						for(String s : words) {
							s = "meow ";
							msg.append(s);
						}
						e.setMessage(msg.toString());
				}
			}
		}
	}
	
}
