package net.colonymc.colonybungeecore;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class UUIDGetter {
	
	public static String getUUID(String p) {
		URLConnection connection;
		String UUID = null;
		try {
			connection = new URL("https://api.mojang.com/users/profiles/minecraft/" + p).openConnection();
			try(Scanner scanner = new Scanner(connection.getInputStream());){
				if(scanner.useDelimiter("\\A").hasNext()) {
					String response = scanner.useDelimiter("\\A").next();
					UUID = response.substring(7, 15) + "-" + response.substring(15,19) + "-" + response.substring(19,23) + "-" + response.substring(23,27) + "-" + response.substring(27,39);
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return UUID;
	}

}
