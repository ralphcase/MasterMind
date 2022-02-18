
public class Guess {
	private Position p;
	private Report score;

	public Guess(Position p, Report s) {
		this.p = p;
		this.score = s;
	}

	public Position getPos() {
		return p;
	}

	public Report getHint() {
		return score;
	}

}
