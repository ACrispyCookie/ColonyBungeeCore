package net.colonymc.colonybungeecore.utils.commands;

import java.util.ArrayList;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MeowCommand extends Command{

	public MeowCommand() {
		super("meow");
	}
	
	public static ArrayList<String> meows = new ArrayList<String>();

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender.hasPermission("*")) {
			if(args.length == 1) {
				if(args[0].isEmpty()) {
					if(!meows.contains("all")) {
						if(sender instanceof ProxiedPlayer) {
							ProxiedPlayer p = (ProxiedPlayer) sender;
							if(meows.contains(p.getServer().getInfo().getName())) {
								meows.remove(meows.indexOf(p.getServer().getInfo().getName()));
								p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fMeow mode has been &cdisabled &ffor the server: &d" + p.getServer().getInfo().getName() + "&f!")));
							}
							else {
								meows.add(p.getServer().getInfo().getName());
								p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fMeow mode has been &aenabled &ffor the server: &d" + p.getServer().getInfo().getName() + "&f!")));
							}
						}
						else {
							ArrayList<String> servers = new ArrayList<String>();
							for(ServerInfo s : ProxyServer.getInstance().getServers().values()) {
								servers.add(s.getName());
							}
							servers.add("all");
							sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fPlease enter a valid server: &d" + servers.toString())));
						}
					}
					else {
						sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &cMeow mode is already enabled for the whole network! To disable it do /meow all!")));
					}
				}
				else if(ProxyServer.getInstance().getServerInfo(args[0]) != null) {
					if(!meows.contains("all")) {
						if(meows.contains(ProxyServer.getInstance().getServerInfo(args[0]).getName())) {
							meows.remove(meows.indexOf(ProxyServer.getInstance().getServerInfo(args[0]).getName()));
							sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fMeow mode has been &cdisabled &ffor the server: &d" + args[0] + "&f!")));
						}
						else {
							meows.add(ProxyServer.getInstance().getServerInfo(args[0]).getName());
							sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fMeow mode has been &aenabled &ffor the server: &d" + args[0] + "&f!")));
						}
					}
					else {
						sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &cMeow mode is already enabled for the whole network! To disable it do /meow all!")));
					}
				}
				else if(args[0].equals("all")) {
					if(meows.contains("all")) {
						meows.remove(meows.indexOf("all"));
						sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fMeow mode has been &cdisabled &ffor the whole network!")));
					}
					else {
						meows.clear();
						meows.add("all");
						sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fMeow mode has been &aenabled &ffor the whole network!")));
					}
				}
				else {
					ArrayList<String> servers = new ArrayList<String>();
					for(ServerInfo s : ProxyServer.getInstance().getServers().values()) {
						servers.add(s.getName());
					}
					servers.add("all");
					sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fPlease enter a valid server: &d" + servers.toString())));
				}
			}
			else if(args.length == 0) {
				if(sender instanceof ProxiedPlayer) {
					if(!meows.contains("all")) {
						ProxiedPlayer p = (ProxiedPlayer) sender;
						if(meows.contains(p.getServer().getInfo().getName())) {
							meows.remove(meows.indexOf(p.getServer().getInfo().getName()));
							p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fMeow mode has been &cdisabled &ffor the server: &d" + p.getServer().getInfo().getName() + "&f!")));
						}
						else {
							meows.add(p.getServer().getInfo().getName());
							p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fMeow mode has been &aenabled &ffor the server: &d" + p.getServer().getInfo().getName() + "&f!")));
						}
					}
					else {
						sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &cMeow mode is already enabled for the whole network! To disable it do /meow all!")));
					}
				}
				else {
					ArrayList<String> servers = new ArrayList<String>();
					for(ServerInfo s : ProxyServer.getInstance().getServers().values()) {
						servers.add(s.getName());
					}
					servers.add("all");
					sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fPlease enter a valid server: &d" + servers.toString())));
				}
			}
			else {
				sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/meow <server>")));
			}
		}
	}

}
