package mobi.boilr.libpricealarm;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public class PriceHitLowerLimitAlarm extends PriceHitAlarm {

	private static final long serialVersionUID = -7414735506840590645L;

	public PriceHitLowerLimitAlarm(int id, Exchange exchange, Pair pair, long period,
			Notify notify, double lowerLimit) throws UpperLimitSmallerOrEqualLowerLimitException {
		super(id, exchange, pair, period, notify, Double.POSITIVE_INFINITY, lowerLimit);
	}

}
