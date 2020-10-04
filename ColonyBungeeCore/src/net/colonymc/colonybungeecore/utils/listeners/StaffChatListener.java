package net.colonymc.colonybungeecore.utils.listeners;

import com.mysql.jdbc.StringUtils;

import net.colonymc.colonybungeecore.utils.commands.StaffChatToggleCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class StaffChatListener implements Listener{
	
	@EventHandler
	public void onChatToggled(ChatEvent e) {
		if(e.getSender() instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) e.getSender();
			if(StaffChatToggleCommand.toggledplayers.contains(p)) {
				if(!e.getMessage().startsWith("/")) {
					e.setCancelled(true);
					if(!StringUtils.isEmptyOrWhitespaceOnly(e.getMessage())) {
						for(ProxiedPlayer staff: ProxyServer.getInstance().getPlayers()) {
							if(staff.hasPermission("staff.store") || p.hasPermission("builder.store")) {
								staff.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&d&lStaff» &f(&d" + p.getServer().getInfo().getName() + "&f) &d" + p.getName() + "&f: &f" + e.getMessage())));
							}
						}
					}
				}
			}
			else if(e.getMessage().startsWith("#")) {
				if(p.hasPermission("staff.store")) {
					e.setCancelled(true);
					if(!StringUtils.isEmptyOrWhitespaceOnly(e.getMessage().substring(1))) {
						for(ProxiedPlayer staff: ProxyServer.getInstance().getPlayers()) {
							if(staff.hasPermission("staff.store") || p.hasPermission("builder.store")) {
								staff.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&d&lStaff» &f(&d" + p.getServer().getInfo().getName() + "&f) &d" + p.getName() + "&f: &f" + e.getMessage().substring(1))));
							}
						}
					}
					else {
						p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d#<message>")));
					}
				}
			}
		}
	}
	
}
