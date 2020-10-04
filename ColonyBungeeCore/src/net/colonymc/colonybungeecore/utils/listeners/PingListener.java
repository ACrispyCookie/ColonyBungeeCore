package net.colonymc.colonybungeecore.utils.listeners;

import net.colonymc.colonybungeecore.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PingListener implements Listener {
	
	@EventHandler
	public void onPing(ProxyPingEvent e) {
		ServerPing response = e.getResponse();
		response.getVersion().setName("Please use 1.8.x");
		if(Main.getConfig().getBoolean("maintenance")) {
			response.setDescriptionComponent(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&5&lColonyMC &f» &dstore.colonymc.net"
					+ "\n&c&lMaintenance &f» Discord @ &dcolonymc.net/discord")));
		}
		else {
			response.setDescriptionComponent(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&5&lColonyMC &f» &dstore.colonymc.net"
					+ "\n&5&lNEWS &f» &fFollow us on twitter - &d@Colony_MC")));
		}
	}

}
