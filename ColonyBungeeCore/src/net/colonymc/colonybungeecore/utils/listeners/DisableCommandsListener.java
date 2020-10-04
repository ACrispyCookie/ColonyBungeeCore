package net.colonymc.colonybungeecore.utils.listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class DisableCommandsListener implements Listener{
	
	@EventHandler
	public void onDisabledCommand(ChatEvent e) {
		if(e.getSender() instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) e.getSender();
			if(!p.hasPermission("*")) {
				if(e.getMessage().startsWith("/bungee") || e.getMessage().startsWith("/glist") || e.getMessage().startsWith("/server")) {
					e.setCancelled(true);
					p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou cannot execute this command.")));
				}
			}
		}
	}

}
