import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Position {
	// Constants for the game parameters.
	public static final int NUMBERCOLORS = 6;
	public static final int NUMBERCELLS = 4;
	
	private int[] pos;
	private static Random rand = new Random();

	/*
	 * Main constructor
	 */
	public Position(int[] input) {
		if (input.length > 0 && input.length != NUMBERCELLS)
			throw new IllegalArgumentException();
		for (int member : input) {
			if (member < 1 || member > NUMBERCOLORS)
				throw new IllegalArgumentException();
		}
		pos = input;
	}
	
	/*
	 * Private constructor is a helper in building the list of all possible combinations.
	 */
	private Position(int[] root, int add) {
		int[] nextPos = Arrays.copyOf(root, root.length+1);
		nextPos[root.length] = add;
		pos = nextPos;
	}

	/*
	 * Create a random position.
	 */
	public Position() {
		pos = new int[NUMBERCELLS];
		for (int i = 0; i < NUMBERCELLS; i++) {
			getPos()[i] = roll(NUMBERCOLORS);
		}
	}

	// Report how well the argument matches this position.
	public Report guess(Position input) {
		Report result = new Report();
		int exact = 0;
		int color = 0;
		ArrayList<Integer> inl = new ArrayList<Integer>();
		for (int i : input.getPos())
			inl.add(i);
		ArrayList<Integer> tl = new ArrayList<Integer>();
		for (int i : this.getPos())
			tl.add(i);
		// Check exact matches - same color in same position.
		for (int i = NUMBERCELLS - 1; i >= 0; i--) {
			if (inl.get(i) == tl.get(i)) {
				exact++;
				inl.remove(i);
				tl.remove(i);
			}
		}
		// Check for right color, wrong position.
		for (Integer i: tl) {
			if (inl.contains(i)) {
				color++;
				inl.remove(i);
			}
		}
		result.exact = exact;
		result.rightColor = color;
		return result;
	}
	
	/*
	 * Create a list of all possible positions.
	 */
	public static List<Position> AllPositions() {
		List<Position> result = new ArrayList<Position>();
		result.add(new Position(new int[0]));
		for (int i = 0; i < NUMBERCELLS; i++) {
			ArrayList<Position> newResult = new ArrayList<Position>();
			for (Position elem : result) {
				for (int j = 0; j < NUMBERCOLORS; j++) {
					newResult.add(new Position(elem.getPos(), j+1));
				}
			}
			result = newResult;
		}
		return result;
	}

	public String toString() {
		return Arrays.toString(getPos());
	}

	// Pick a number from 1 to num, inclusive
	private static int roll(int num) {
		return 1 + rand.nextInt(num);
	}
	
	/*
	 * Summarize all colors guessed in all guesses
	 */
	public static Set<Integer> allColorsGuessed(List<Guess> allGuesses) {
		Set<Integer> found = new HashSet<Integer>();
		for (Guess g: allGuesses) {
			for (int i = 0; i < g.getPos().getPos().length; i++)  {
				found.add(g.getPos().getPos()[i]);
			}
		}
		return found;
	}

	public int[] getPos() {
		return pos;
	}

}
