package net.colonymc.colonybungeecore.utils.listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class RedirectListener implements Listener{

	@EventHandler
	public void onKick(ServerKickEvent e) {
		e.setCancelled(true);
		ServerInfo server = e.getKickedFrom();
		ServerInfo hubServer = ProxyServer.getInstance().getServerInfo("lobby");
		if(!(server == hubServer)) {
			ProxiedPlayer p = e.getPlayer();
			if(p.getServer().getInfo() != hubServer) {
				p.connect(hubServer);
				TextComponent kickMessage = new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou have been sent back to the lobby! Reason:"));
				p.sendMessage(kickMessage);
			}
			else {
				TextComponent kickMessage = new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &cUnable to join this server! Reason:"));
				p.sendMessage(kickMessage);
			}
		}
	}
	
}
