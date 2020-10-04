package net.colonymc.colonybungeecore.utils.commands;

import net.colonymc.colonybungeecore.Main;
import net.colonymc.colonybungeecore.Messages;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class MaintenanceCommand extends Command {

	public MaintenanceCommand() {
		super("maintenance");
	}
	
	boolean maintenance = Main.getConfig().getBoolean("maintenance");

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender.hasPermission("*")) {
			if(maintenance) {
				Main.getConfig().set("maintenance", false);
				maintenance = Main.getConfig().getBoolean("maintenance");
				Main.saveConfig();
				sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have &cdisabled &fthe maintenance mode for the network!")));
			}
			else {
				Main.getConfig().set("maintenance", true);
				maintenance = Main.getConfig().getBoolean("maintenance");
				Main.saveConfig();
				sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have &aenabled &fthe maintenance mode for the network!")));
			}
		}
		else {
			sender.sendMessage(new TextComponent(Messages.noPerm));
		}
	}

}
