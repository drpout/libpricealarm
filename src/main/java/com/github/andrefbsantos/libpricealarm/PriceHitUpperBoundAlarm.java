package com.github.andrefbsantos.libpricealarm;

import java.util.Timer;

import com.github.andrefbsantos.libdynticker.core.Exchange;
import com.github.andrefbsantos.libdynticker.core.Pair;

public class PriceHitUpperBoundAlarm extends PriceHitAlarm {

	public PriceHitUpperBoundAlarm(Exchange exchange, Pair pair,
	Timer timer, long period, Notify notify, double upperBound)
	throws UpperBoundSmallerThanLowerBoundException {
		super(exchange, pair, timer, period, notify, upperBound, Double.NEGATIVE_INFINITY);
	}

}