package net.colonymc.colonybungeecore.utils.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Command;

public class HubCommand extends Command{

	public HubCommand() {
		super("hub", "", "lobby", "l");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) sender;
			Server server = p.getServer();
			ServerInfo hub = ProxyServer.getInstance().getServerInfo("lobby");
			if(!server.getInfo().getName().equals("lobby")) {
				p.connect(hub);
			}
			else {
				p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou are already connected to the hub!")));
			}
		}
		else {
			sender.sendMessage(new TextComponent(" » Only players can use this command!"));
		}
	}

}
