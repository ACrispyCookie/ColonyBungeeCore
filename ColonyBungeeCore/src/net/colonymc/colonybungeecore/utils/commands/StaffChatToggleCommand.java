package net.colonymc.colonybungeecore.utils.commands;

import java.util.ArrayList;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class StaffChatToggleCommand extends Command{
	
	public static ArrayList<ProxiedPlayer> toggledplayers = new ArrayList<ProxiedPlayer>();

	public StaffChatToggleCommand() {
		super("togglestaffchat", "", new String[] {"togglesc", "tsc", "togglestaffc"});
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			if(sender.hasPermission("staff.store")) {
				ProxiedPlayer staff = (ProxiedPlayer) sender;
				if(args.length == 0) {
					if(toggledplayers.contains(staff)) {
						toggledplayers.remove(toggledplayers.indexOf(staff));
						staff.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou disabled your &dstaff chat&f!")));
					}
					else {
						toggledplayers.add(staff);
						staff.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou enabled your &dstaff chat&f!")));
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
