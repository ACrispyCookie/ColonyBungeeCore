package net.colonymc.colonybungeecore.utils.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class DiscordCommand extends Command {

	public DiscordCommand() {
		super("discord");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		TextComponent msg = new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou can join our discord server &d&lHERE"));
		msg.setClickEvent(new ClickEvent(Action.OPEN_URL, "https://colonymc.net/discord"));
		sender.sendMessage(msg);
	}

}
