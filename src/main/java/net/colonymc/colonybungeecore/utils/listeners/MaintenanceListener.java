package net.colonymc.colonybungeecore.utils.listeners;

import net.colonymc.colonybungeecore.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class MaintenanceListener implements Listener {
	
	boolean maintenance = Main.getConfig().getBoolean("maintenance");
	
	@EventHandler
	public void onJoin(ServerConnectEvent e) {
		maintenance = Main.getConfig().getBoolean("maintenance");
		if(maintenance) {
			if(!e.getPlayer().hasPermission("staff.store") && !e.getPlayer().hasPermission("maintenance.join")) {
				e.setCancelled(true);
				e.getPlayer().disconnect(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&5&l» &cOur network is currently on maintenance mode!"
						+ "\n&5&l» &cJoin our discord server @ https://colonymc.net/discord!")));
				for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
					p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &d" + e.getPlayer().getName()
							+ " &ftried to join the network, but it is on maintenance mode!")));
				}
			}
		}
	}

}
