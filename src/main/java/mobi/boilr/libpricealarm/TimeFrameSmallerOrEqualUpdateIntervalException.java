package mobi.boilr.libpricealarm;

public class TimeFrameSmallerOrEqualUpdateIntervalException extends Exception {

	private static final long serialVersionUID = -6533462004845580107L;

	public TimeFrameSmallerOrEqualUpdateIntervalException() {
		super("Time frame is smaller or equal to the update interval.");
	}

}
