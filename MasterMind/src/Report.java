
public class Report {
	public int exact;
	public int rightColor;
	
	public String toString() {
		return "exact: " + exact + ", color: " + rightColor;
	}
	
	public boolean equals(Report r) {
		return r.exact == this.exact && r.rightColor == this.rightColor;
	}

}
