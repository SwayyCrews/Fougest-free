package ru.fougest.client.util.font.common;

public enum Lang {
	
	ENG(new int[] {31, 127, 0, 0}),
	ENG_RU(new int[] {31, 127, 1024, 1106}),
	RU(new int[] {1024, 1106, 0, 0});
	
	private int[] charCodes;
	
	Lang(int[] charCodes) {
		this.charCodes = charCodes;
	}
	
	public int[] getCharCodes() {
		return charCodes;
	}
	
}
