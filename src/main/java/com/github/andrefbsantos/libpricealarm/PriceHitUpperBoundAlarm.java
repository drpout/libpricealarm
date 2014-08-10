package com.github.andrefbsantos.libpricealarm;

import java.util.Timer;

import com.github.andrefbsantos.libdynticker.core.Exchange;
import com.github.andrefbsantos.libdynticker.core.Pair;

public class PriceHitUpperBoundAlarm extends PriceHitAlarm {

	/**
	 *
	 */
	private static final long serialVersionUID = -7107219115788419337L;

	public PriceHitUpperBoundAlarm(long id, Exchange exchange, Pair pair, Timer timer, long period,
			Notify notify, double upperBound) throws UpperBoundSmallerThanLowerBoundException {
		super(id, exchange, pair, timer, period, notify, upperBound, Double.NEGATIVE_INFINITY);
	}

}