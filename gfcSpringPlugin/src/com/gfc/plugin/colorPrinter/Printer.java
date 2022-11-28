package com.gfc.plugin.colorPrinter;

public class Printer {
//	End tag
	private static final String ANSI_RESET = "\u001B[0m";
//	Text color
	private static final String ANSI_BLACK = "\u001B[30m";
	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_BLUE = "\u001B[34m";
	private static final String ANSI_PURPLE = "\u001B[35m";
	private static final String ANSI_CYAN = "\u001B[36m";
	private static final String ANSI_WHITE = "\u001B[37m";
//	Background color
	private static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	private static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	private static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	private static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	private static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

	public Printer() {

	}

	private static String getAnsiReset() {
		return ANSI_RESET;
	}

	private static String getAnsiBlack() {
		return ANSI_BLACK;
	}

	private static String getAnsiRed() {
		return ANSI_RED;
	}

	private static String getAnsiGreen() {
		return ANSI_GREEN;
	}

	private static String getAnsiYellow() {
		return ANSI_YELLOW;
	}

	private static String getAnsiBlue() {
		return ANSI_BLUE;
	}

	private static String getAnsiPurple() {
		return ANSI_PURPLE;
	}

	private static String getAnsiCyan() {
		return ANSI_CYAN;
	}

	private static String getAnsiWhite() {
		return ANSI_WHITE;
	}

	private static String getAnsiBlackBackground() {
		return ANSI_BLACK_BACKGROUND;
	}

	private static String getAnsiRedBackground() {
		return ANSI_RED_BACKGROUND;
	}

	private static String getAnsiGreenBackground() {
		return ANSI_GREEN_BACKGROUND;
	}

	private static String getAnsiYellowBackground() {
		return ANSI_YELLOW_BACKGROUND;
	}

	private static String getAnsiBlueBackground() {
		return ANSI_BLUE_BACKGROUND;
	}

	private static String getAnsiPurpleBackground() {
		return ANSI_PURPLE_BACKGROUND;
	}

	private static String getAnsiCyanBackground() {
		return ANSI_CYAN_BACKGROUND;
	}

	private static String getAnsiWhiteBackground() {
		return ANSI_WHITE_BACKGROUND;
	}

	private static String println(String s) {
		return s + getAnsiReset();
	}

	/**
	 * 印出紅色字
	 * 
	 * @param content
	 * @return
	 */
	public String redText(String content) {
		return println(getAnsiRed() + content);
	}

	/**
	 * 印出紅色背景
	 * 
	 * @param content
	 * @return
	 */
	public String redBackground(String content) {
		return println(getAnsiRedBackground() + content);
	}

	/**
	 * 印出藍色字
	 * 
	 * @param content
	 * @return
	 */
	public String blueText(String content) {
		return println(getAnsiBlue() + content);
	}

	/**
	 * 印出藍色背景
	 * 
	 * @param content
	 * @return
	 */
	public String blueBackground(String content) {
		return println(getAnsiBlueBackground() + content);
	}

	public String greenText(String content) {
		return println(getAnsiGreen() + content);
	}

	public String greenBackground(String content) {
		return println(getAnsiGreenBackground() + content);
	}

	public String blackText(String content) {
		return println(getAnsiBlack() + content);
	}

	public String blackBackground(String content) {
		return println(getAnsiBlackBackground() + content);
	}

	public String whiteText(String content) {
		return println(getAnsiWhite() + content);
	}

	public String whiteBackground(String content) {
		return println(getAnsiWhiteBackground() + content);
	}

	public String cyanText(String content) {
		return println(getAnsiCyan() + content);
	}

	public String cyanBackground(String content) {
		return println(getAnsiCyanBackground() + content);
	}

	public String purpleText(String content) {
		return println(getAnsiPurple() + content);
	}

	public String purpleBackground(String content) {
		return println(getAnsiPurpleBackground() + content);
	}

	public String yellowText(String content) {
		return println(getAnsiYellow() + content);
	}

	public String yellowBackground(String content) {
		return println(getAnsiYellowBackground() + content);
	}

}
