package mobi.boilr.libpricealarm;

public class UpperLimitSmallerOrEqualLowerLimitException extends Exception {

	private static final long serialVersionUID = 3260364362230403962L;

	public UpperLimitSmallerOrEqualLowerLimitException() {
		super("Upper limit is smaller or equal to the lower limit.");
	}

}
