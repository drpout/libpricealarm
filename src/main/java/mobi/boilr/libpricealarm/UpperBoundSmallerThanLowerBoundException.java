package mobi.boilr.libpricealarm;

public class UpperBoundSmallerThanLowerBoundException extends Exception {

	private static final long serialVersionUID = 3260364362230403962L;

	public UpperBoundSmallerThanLowerBoundException() {
		super("Upper bound is smaller than lower bound.");
	}

}
