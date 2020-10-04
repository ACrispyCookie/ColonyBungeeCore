package net.colonymc.colonybungeecore.utils.commands;

import java.util.HashSet;
import java.util.Set;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class RankCommand extends Command implements TabExecutor{

	String capitalizedName = "";
	String name = "";
	
	public RankCommand(String name) {
		super(name);
		this.name = name;
		this.capitalizedName = name.substring(0,1).toUpperCase() + name.substring(1);
	}

	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
		Set<String> matches = new HashSet<>();
		String search = args[0].toLowerCase();
		for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            if(p.getName().toLowerCase().startsWith(search)) {
        		matches.add(p.getName());
            }
		}
		return matches;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) sender;
			if(sender.hasPermission("colonymc.staffmanager") || p.getUniqueId().toString().equals("37c3bfb6-6fa9-4602-a9bd-a1e95baea85f")) {
				if(args.length == 1) {
					if(!args[0].isEmpty()) {
						if(ProxyServer.getInstance().getPlayer(args[0]) != null) {
							ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
							switch(name) {
							case "admin":
								if(p.getUniqueId().toString().equals("37c3bfb6-6fa9-4602-a9bd-a1e95baea85f")) {
									p.chat("/lp user " + args[0] + " parent set admin");
									p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + target.getName() + "&f's rank to &d&lAdmin")));
									target.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYour rank is now &d&lAdmin")));
								}
								else {
									p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fOnly &dACrispyCookie &fcan execute this command!")));
								}
								break;
							case "developer":
								if(p.getUniqueId().toString().equals("37c3bfb6-6fa9-4602-a9bd-a1e95baea85f")) {
									p.chat("/lp user " + args[0] + " parent set developer");
									p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + target.getName() + "&f's rank to &d&lDeveloper")));
									target.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYour rank is now &d&lDeveloper")));
								}
								else {
									p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fOnly &dACrispyCookie &fcan execute this command!")));
								}
								break;
							case "mediarank":
								if(p.getUniqueId().toString().equals("37c3bfb6-6fa9-4602-a9bd-a1e95baea85f")) {
									p.chat("/lp user " + args[0] + " parent set media");
									p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + target.getName() + "&f's rank to &d&lMedia")));
									target.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYour rank is now &d&lMedia")));
								}
								else {
									p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fOnly &dACrispyCookie &fcan execute this command!")));
								}
								break;
							case "owner":
								if(p.getUniqueId().toString().equals("37c3bfb6-6fa9-4602-a9bd-a1e95baea85f")) {
									p.chat("/lp user " + args[0] + " parent set owner");
									p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + target.getName() + "&f's rank to &d&lOwner")));
									target.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYour rank is now &d&lOwner")));
								}
								else {
									p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fOnly &dACrispyCookie &fcan execute this command!")));
								}
								break;
							default:
								p.chat("/lp user " + args[0] + " parent set " + name);
								p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + target.getName() + "&f's rank to &d&l" + capitalizedName)));
								target.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYour rank is now &d&l" + capitalizedName)));
								break;
							}
								
						}
						else {
							switch(name) {
							case "admin":
								if(p.getUniqueId().toString().equals("37c3bfb6-6fa9-4602-a9bd-a1e95baea85f")) {
									p.chat("/lp user " + args[0] + " parent set admin");
									p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + args[0] + "&f's rank to &d&lAdmin")));
								}
								else {
									p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fOnly &dACrispyCookie &fcan execute this command!")));
								}
								break;
							case "developer":
								if(p.getUniqueId().toString().equals("37c3bfb6-6fa9-4602-a9bd-a1e95baea85f")) {
									p.chat("/lp user " + args[0] + " parent set developer");
									p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + args[0] + "&f's rank to &d&lDeveloper")));
								}
								else {
									p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fOnly &dACrispyCookie &fcan execute this command!")));
								}
								break;
							case "mediarank":
								if(p.getUniqueId().toString().equals("37c3bfb6-6fa9-4602-a9bd-a1e95baea85f")) {
									p.chat("/lp user " + args[0] + " parent set media");
									p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + args[0] + "&f's rank to &d&lMedia")));
								}
								else {
									p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fOnly &dACrispyCookie &fcan execute this command!")));
								}
								break;
							case "owner":
								if(p.getUniqueId().toString().equals("37c3bfb6-6fa9-4602-a9bd-a1e95baea85f")) {
									p.chat("/lp user " + args[0] + " parent set owner");
									p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + args[0] + "&f's rank to &d&lOwner")));
								}
								else {
									p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fOnly &dACrispyCookie &fcan execute this command!")));
								}
								break;
							default:
								p.chat("/lp user " + args[0] + " parent set " + name);
								p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + args[0] + "&f's rank to &d&l" + capitalizedName)));
								break;
							}
						}
					}
					else {
						sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/" + name + " <player>")));
					}
				}
				else {
					sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/" + name + " <player>")));
				}
			}
			else {
				sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou cannot execute this command.")));
			}
		}
		else {
			if(!args[0].isEmpty()) {
				if(ProxyServer.getInstance().getPlayer(args[0]) != null) {
					ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
					switch(name) {
					default:
						ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), "lpb user " + args[0] + " parent set " + name);
						System.out.println(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + target.getName() + "&f's rank to &d&l" + capitalizedName));
						target.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYour rank is now &d&l" + capitalizedName)));
						break;
					}
						
				}
				else {
					switch(name) {
					default:
						ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), "lpb user " + args[0] + " parent set " + name);
						System.out.println(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + args[0] + "&f's rank to &d&l" + capitalizedName));
						break;
					}
				}
			}
			else {
				sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/" + name + " <player>")));
			}
		}
	}

}
