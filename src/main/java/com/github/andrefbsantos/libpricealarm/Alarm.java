package com.github.andrefbsantos.libpricealarm;

import com.github.andrefbsantos.libdynticker.core.Exchange;
import com.github.andrefbsantos.libdynticker.core.Pair;

public abstract class Alarm {
	
	Exchange exchange;
	Pair pair;
	boolean on;

	public Alarm(Exchange exchange, Pair pair) {
		this.exchange = exchange;
		this.pair = pair;
		this.on = false;
	}
	
}
