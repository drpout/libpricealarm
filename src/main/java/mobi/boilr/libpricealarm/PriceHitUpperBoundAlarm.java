package mobi.boilr.libpricealarm;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public class PriceHitUpperBoundAlarm extends PriceHitAlarm {

	private static final long serialVersionUID = -7107219115788419337L;

	public PriceHitUpperBoundAlarm(int id, Exchange exchange, Pair pair, long period,
			Notify notify, double upperBound) throws UpperBoundSmallerThanLowerBoundException {
		super(id, exchange, pair, period, notify, upperBound, Double.NEGATIVE_INFINITY);
	}

}