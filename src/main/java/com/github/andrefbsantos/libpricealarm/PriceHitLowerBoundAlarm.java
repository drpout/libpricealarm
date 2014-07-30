package com.github.andrefbsantos.libpricealarm;

import java.util.Timer;

import com.github.andrefbsantos.libdynticker.core.Exchange;
import com.github.andrefbsantos.libdynticker.core.Pair;

public class PriceHitLowerBoundAlarm extends PriceHitAlarm {

	public PriceHitLowerBoundAlarm(Exchange exchange, Pair pair,
	Timer timer, long period, Notify notify, double lowerBound)
	throws UpperBoundSmallerThanLowerBoundException {
		super(exchange, pair, timer, period, notify, Double.POSITIVE_INFINITY, lowerBound);
	}

}
