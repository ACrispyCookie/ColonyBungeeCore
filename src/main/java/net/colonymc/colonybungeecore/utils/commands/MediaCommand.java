package net.colonymc.colonybungeecore.utils.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.plugin.Command;

public class MediaCommand extends Command{

	public MediaCommand() {
		super("media");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		TextComponent msg = new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou can apply for the Media rank &d&lHERE"));
		msg.setClickEvent(new ClickEvent(Action.OPEN_URL, "https://colonymc.net/media"));
		sender.sendMessage(msg);
	}

}
