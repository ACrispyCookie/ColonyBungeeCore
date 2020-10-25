package net.colonymc.colonybungeecore.utils.commands;

import com.mysql.jdbc.StringUtils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class StaffChatCommand extends Command{

	public StaffChatCommand() {
		super("staffchat", "", "sc", "schat", "adminchat");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			if(sender.hasPermission("staff.store")) {
				ProxiedPlayer staff = (ProxiedPlayer) sender;
				if(args.length > 0) {
					String server = staff.getServer().getInfo().getName();
					String message = "";
					for(String arg: args) {
						message = message.concat(arg + " ");
					}
					if(!StringUtils.isEmptyOrWhitespaceOnly(message)) {
						for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
							if(p.hasPermission("staff.store") || p.hasPermission("builder.store")) {
								p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&d&lStaff» &f(&d" + server + "&f) &d" + staff.getName() + "&f: &f" + message)));
							}
						}
					}
					else {
						staff.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/staffchat <message>")));
					}
				}
				else {
					staff.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/staffchat <message>")));
				}
			}
			else {
				sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou cannot execute this command.")));
			}
		}
		else {
			sender.sendMessage(new TextComponent(" » Only players can use this command!"));
		}
	}

}
