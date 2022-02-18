import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Game {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		Position goal = new Position();
		System.out.println("Goal: " + goal);
		// Position turn = new Position();
		// System.out.println(turn + " " + goal.guess(turn));
		List<Position> possible = Position.AllPositions();
		// System.out.println(possible);
		ArrayList<Guess> guesses = new ArrayList<Guess>();
		int count = 0;

		while (possible.size() > 0) {
			// System.out.println(possible.size() + " possible: \t" + possible);
			System.out.println(possible.size() + " possible: \t");
			// Choose a guess for this turn.
			// Position turn = possible.remove(0);

			Position turn = bestTurn(possible, guesses);
			possible.remove(turn);
			count++; // Count the number of guesses.
			Report hint = goal.guess(turn);
			guesses.add(new Guess(turn, hint));
			System.out.println(turn + " " + hint);

			// Shorten the list of possibilities based on the new results.
			removeImpossible(possible, guesses);
		}

		long endTime = System.currentTimeMillis();
		System.out.println("Solved it in " + count + " guesses. It took " + (endTime - startTime) / 1000.0 + " seconds.");
	}

	/*
	 * Given a list of possible Positions and a list of guesses already made,
	 * return the best next guess. "Best" is the guess that would reduce the
	 * number of possibilities the most.
	 */
	private static Position bestTurn(List<Position> possible, List<Guess> guesses) {
		// The first guess is always the same since there are no guesses yet.
		// if (guesses.size() == 0)
		// return new Position(new int[] { 1, 1, 2, 3 });

		// For each possible next move, see which reduces the possible list the
		// most on average.
		int minTotal = Integer.MAX_VALUE;
//		int count = 0;
//		int found = 0;
		Set<Integer> colors = Position.allColorsGuessed(guesses);
		colors.add(1);
		Position best = possible.get(0);
		Set<Integer> localColors = new HashSet<Integer>();
		for (int col: best.getPos())
			localColors.add(col);
		for (Position pos : possible) {
//			System.out.println("colors: " + localColors);
			boolean red = false;
			for (int i = 0; i < Position.NUMBERCELLS; i++)
				if (best.getPos()[i] != pos.getPos()[i] 
//						&& best.getPos()[(i + 1) % 4] == pos.getPos()[(i + 1) % 4]
//						&& best.getPos()[(i + 2) % 4] == pos.getPos()[(i + 2) % 4]
//						&& best.getPos()[(i + 3) % 4] == pos.getPos()[(i + 3) % 4]
						&& !colors.contains((Integer) best.getPos()[i])
						&& !localColors.contains((Integer) pos.getPos()[i])) {
					red = true;
					break;
				}
			if (!red) {
				int total = 0;
				for (Position trial : possible) {
					total += possibleSize(possible, guesses, new Guess(pos, trial.guess(pos)));
					if (total >= minTotal)
						break;
				}

				if (total < minTotal) {
					// Save the new best.
					minTotal = total;
					best = pos;
//					found = count;
					localColors = new HashSet<Integer>();
					for (int col: best.getPos())
						localColors.add(col);
				}
			}
//			System.out.println(count + ": " + found + " " + minTotal + " " + best + " - " + total + " " + pos);
//			System.out.println(count + ": " + minTotal + " " + best + " - " + total + " " + pos);
			// else
			// Cut this iteration short if we've gone a while without finding a
			// better guess.
//			if (count > 10 * found) break;
//			count++;
		}
		return best;
	}

	/*
	 * Return the size of possible positions given the previous guesses and the
	 * next potential guess.
	 */
	private static int possibleSize(List<Position> possible, List<Guess> guesses, Guess nextGuess) {
		List<Position> thisPossible = new ArrayList<Position>(possible);
		guesses.add(nextGuess);
		removeImpossible(thisPossible, guesses);
		guesses.remove(nextGuess);
		return thisPossible.size();
	}

	/*
	 * Remove Positions from the list if they are impossible given the data in
	 * the guesses.
	 */
	private static void removeImpossible(List<Position> possible, List<Guess> allGuesses) {
		for (int i = 0; i < possible.size(); i++) {
			for (Guess g : allGuesses) {
				Report hint = g.getPos().guess(possible.get(i));
				if (!hint.equals(g.getHint())) {
					possible.remove(i);
					i--;
					break;
				}
			}
		}
	}

}