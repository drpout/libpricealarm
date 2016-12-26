package mobi.boilr.libpricealarm;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public class PriceHitUpperLimitAlarm extends PriceHitAlarm {

	private static final long serialVersionUID = -7107219115788419337L;

	public PriceHitUpperLimitAlarm(int id, Exchange exchange, Pair pair, long period, Notifier notifier,
			boolean defusable, double upperLimit) throws UpperLimitSmallerOrEqualLowerLimitException {
		super(id, exchange, pair, period, notifier, defusable, upperLimit, Double.NEGATIVE_INFINITY);
	}

}