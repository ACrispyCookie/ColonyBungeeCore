package net.colonymc.colonybungeecore;

import net.md_5.bungee.api.ChatColor;

public class CenteredMessage {
	
	private final static int CENTER_PX = 154;
	private final static int MAX_PX = 250;
	
	public static String getCenteredMessage(String message){
		message = ChatColor.translateAlternateColorCodes('&', message);
		int messagePxSize = 0;
		boolean previousCode = false;
		boolean isBold = false;
		int charIndex = 0;
		int lastSpaceIndex = 0;
		for(char c : message.toCharArray()){
			if(c == '§'){
				previousCode = true;
				continue;
			}else if(previousCode == true){
				previousCode = false;
				if(c == 'l' || c == 'L'){
					isBold = true;
					continue;
				}else isBold = false;
			}else if(c == ' ') lastSpaceIndex = charIndex;
			else{
				DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
				messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
				messagePxSize++;
			}
			if(messagePxSize >= MAX_PX){
				message = message.substring(0, lastSpaceIndex + 1);
				break;
			}
			charIndex++;
		}
		int halvedMessageSize = messagePxSize / 2;
		int toCompensate = CENTER_PX - halvedMessageSize;
		int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
		int compensated = 0;
		StringBuilder sb = new StringBuilder();
		while(compensated < toCompensate){
			sb.append(" ");
			compensated += spaceLength;
		}
		return sb.toString() + message;
	}

}
