package net.colonymc.colonybungeecore.utils.listeners;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.colonymc.colonybungeecore.reaction.ReactionCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatCooldownListener implements Listener{
	
	final HashMap<ProxiedPlayer, Long> cooldowns = new HashMap<>();
	final HashMap<ProxiedPlayer, String> lastmessage = new HashMap<>();
	
	@EventHandler
	public void onChat(ChatEvent e) {
		if(e.getSender() instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) e.getSender();
			if(!e.getMessage().startsWith("/")) {
				if(ReactionCommand.reactions.containsKey(p.getServer().getInfo()) && e.getMessage().equalsIgnoreCase(ReactionCommand.reactions.get(p.getServer().getInfo()).getAnswer())) {
					ReactionCommand.reactions.get(p.getServer().getInfo()).endGame(p);
					e.setCancelled(true);
				}
				else {
					if(!p.hasPermission("prince.store")) {
						if(cooldowns.containsKey(p)) {
							long cooldownInMilliseconds = cooldowns.get(p);
							if(cooldownInMilliseconds >= System.currentTimeMillis()) {
								int cooldownLeft = (int) (cooldownInMilliseconds - System.currentTimeMillis())/1000 + 1;
								e.setCancelled(true);
								p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fPlease wait another &d" + cooldownLeft + " seconds &fin order to talk! If you want to bypass this restriction, you can buy a rank at &dhttp://store.colonymc.net &f!")));
							}
							else {
								cooldowns.put(p, System.currentTimeMillis() + 3000);
								if(!p.hasPermission("staff.store") && !p.hasPermission("famous.store")) {
									if(isUrlOrIp(e.getMessage())) {
										e.setCancelled(true);
										p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fPlease do not post URLs/IPs in the chat!")));
										lastmessage.put(p, e.getMessage());
									}
									if(lastmessage.containsKey(p)) {
										String msg = lastmessage.get(p);
										if(msg.equals(e.getMessage())) {
											e.setCancelled(true);
											p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fPlease do not repeat the same message!")));
										}
									}
									lastmessage.put(p, e.getMessage());
								}
							}
						}
						else {
							cooldowns.put(p, System.currentTimeMillis() + 3000);
						}
					}
					else {
						if(!p.hasPermission("staff.store") || !p.hasPermission("famous.store")) {
							if(isUrlOrIp(e.getMessage())) {
								e.setCancelled(true);
								p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fPlease do not post URLs/IPs in the chat!")));
								lastmessage.put(p, e.getMessage());
							}
							else if(lastmessage.containsKey(p)) {
								String msg = lastmessage.get(p);
								if(msg.equals(e.getMessage())) {
									e.setCancelled(true);
									p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fPlease do not repeat the same message!")));
								}
								lastmessage.put(p, e.getMessage());
							}
							else {
								lastmessage.put(p, e.getMessage());
							}
						}
					}
				}
			}
		}
	}

	private boolean isUrlOrIp(String message) {
		String URL_REGEX = "[a-zA-Z0-9\\-\\.]+\\s?(\\.|dot|\\(dot\\)|-|;|:|,)\\s?(com|org|net|cz|co|uk|sk|site|gov|biz|mobi|xxx|eu|me|io|gr|be|gl|ly)";
		Pattern p = Pattern.compile(URL_REGEX);
		Matcher m = p.matcher(message); //replace with string to compare
		if(m.find()) {
			return !m.group().equals("colonymc.net") && !m.group().equals("youtube.net") && !m.group().equals("store.colonymc.net");
		}
		return false;
	}

}