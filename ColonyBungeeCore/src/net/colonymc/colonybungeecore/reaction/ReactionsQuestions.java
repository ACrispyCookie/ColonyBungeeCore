package net.colonymc.colonybungeecore.reaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ReactionsQuestions {
	
	static ArrayList<String[]> math = new ArrayList<>();
	static ArrayList<String> write = new ArrayList<>();
	static ArrayList<String[]> unscramble = new ArrayList<>();
	
	public ReactionsQuestions() {
		setupMath();
		setupWrite();
		setupUnscramble();
	}
	
	public String[] getRandomMath() {
		Random rand = new Random();
		int i = rand.nextInt(math.size());
		return math.get(i);
	}
	
	public String getRandomWrite() {
		Random rand = new Random();
		int i = rand.nextInt(write.size());
		return write.get(i);
	}
	
	public String[] getRandomUnscramble() {
		Random rand = new Random();
		int i = rand.nextInt(unscramble.size());
		return unscramble.get(i);
	}
	
	public void setupMath() {
		Random rand = new Random();
		for(int i = 0; i < 50; i++) {
			int firstNumber;
			int secondNumber;
			String[] entry;
			switch(rand.nextInt(4)) {
			case 0:
				firstNumber = rand.nextInt(200);
				secondNumber = rand.nextInt(200);
				entry = new String[] {firstNumber + "+" + secondNumber, String.valueOf(firstNumber + secondNumber)};
				math.add(entry);
				break;
			case 1:
				firstNumber = rand.nextInt(200);
				secondNumber = rand.nextInt(200);
				while(firstNumber < secondNumber) {
					firstNumber = rand.nextInt(200);
					secondNumber = rand.nextInt(200);
				}
				entry = new String[] {firstNumber + "-" + secondNumber, String.valueOf(firstNumber - secondNumber)};
				math.add(entry);
				break;
			case 2:
				firstNumber = rand.nextInt(20);
				secondNumber = rand.nextInt(20);
				entry = new String[] {firstNumber + "x" + secondNumber, String.valueOf(firstNumber * secondNumber)};
				math.add(entry);
				break;
			case 3:
				firstNumber = rand.nextInt(120);
				secondNumber = rand.nextInt(120) + 2;
				while((secondNumber == 0 || firstNumber == 0) || (double) firstNumber/secondNumber != firstNumber/secondNumber || firstNumber == secondNumber) {
					firstNumber = rand.nextInt(120);
					secondNumber = rand.nextInt(120) + 2;
				}
				entry = new String[] {firstNumber + "/" + secondNumber, String.valueOf(firstNumber / secondNumber)};
				math.add(entry);
				break;
			}
		}
	}
	
	public void setupWrite() {
		write.add("pork");
	}
	
	public void setupUnscramble() {
		unscramble.add(new String[] {getScrambled("pork"), "pork"});
	}
	
	private String getScrambled(String word) {
		ArrayList<Character> chars = new ArrayList<>(word.length());
		for ( char c : word.toCharArray() ) {
		   chars.add(c);
		}
		Collections.shuffle(chars);
		char[] shuffled = new char[chars.size()];
		for ( int i = 0; i < shuffled.length; i++ ) {
		   shuffled[i] = chars.get(i);
		}
		return new String(shuffled);
	}

}
