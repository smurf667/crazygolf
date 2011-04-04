package de.engehausen.mobile.crazygolf.model;

/**
 * The golf player.
 */
public class Player {

	/**
	 * The names of players.
	 */
	private static final String[] PLAYERS = {
		"player 1",
		"player 2",
		"player 3",
		"player 4"
	};
	
	private final int[] strokes;
	private final int num;

	/**
	 * Creates a player with the given player number.
	 * @param playerNum the player number
	 */
	public Player(final int playerNum) {
		strokes = new int[18];
		num = playerNum;
	}

	/**
	 * Increment the number of strokes the user performed for the given hole
	 * @param holeIndex the index of the hole (0..17)
	 * @return the number of strokes on the given hole
	 */
	public int nextStroke(final int holeIndex) {
		return ++strokes[holeIndex];		
	}
	
	/**
	 * Returns the number of strokes the user performed for the given hole
	 * @param holeIndex the index of the hole (0..17)
	 * @return the number of strokes on the given hole
	 */
	public int getStrokeCount(final int holeIndex) {
		return strokes[holeIndex];
	}
	
	/**
	 * Returns the player name.
	 * @return the player name.
	 */
	public String getName() {
		return PLAYERS[num];
	}

}
