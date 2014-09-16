package mobi.boilr.libpricealarm;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public class PriceHitLowerBoundAlarm extends PriceHitAlarm {

	private static final long serialVersionUID = -7414735506840590645L;

	public PriceHitLowerBoundAlarm(int id, Exchange exchange, Pair pair, long period,
			Notify notify, double lowerBound) throws UpperBoundSmallerThanLowerBoundException {
		super(id, exchange, pair, period, notify, Double.POSITIVE_INFINITY, lowerBound);
	}

}
