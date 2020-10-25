package net.colonymc.colonybungeecore.utils.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class RulesCommand extends Command{

	public RulesCommand() {
		super("rules", "" , "rule");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		TextComponent rules = new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou can read our full rules here: &drules.colonymc.net"));
		rules.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://rules.colonymc.net"));
		sender.sendMessage(rules);
	}

}
