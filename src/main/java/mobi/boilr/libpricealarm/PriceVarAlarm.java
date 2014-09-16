package mobi.boilr.libpricealarm;

import java.io.IOException;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public class PriceVarAlarm extends Alarm {

	private static final long serialVersionUID = -5424769817492896869L;
	private double variation;
	private float percent;

	public PriceVarAlarm(int id, Exchange exchange, Pair pair, long period, Notify notify,
			double variation) throws NumberFormatException, IOException {
		super(id, exchange, pair, period, notify);
		this.variation = variation;
		percent = 0;
		lastValue = getExchangeLastValue();
	}

	public PriceVarAlarm(int id, Exchange exchange, Pair pair, long period, Notify notify,
			float percent) throws NumberFormatException, IOException {
		super(id, exchange, pair, period, notify);
		this.percent = percent;
		lastValue = getExchangeLastValue();
		variation = lastValue * (percent * 0.01);
	}

	@Override
	public boolean run() throws NumberFormatException, IOException {
		boolean ret = true;
		double newValue = getExchangeLastValue();
		if(Math.abs(lastValue - newValue) >= variation) {
			ret = notify.trigger(getId());
		}
		lastValue = newValue;
		if(percent > 0) {
			variation = lastValue * (percent * 0.01);
		}
		return ret;
	}

	public double getVariation() {
		return variation;
	}

	public void setVariation(double variation) {
		this.variation = variation;
	}

	public float getPercent() {
		return percent;
	}

	public void setPercent(float percent) {
		this.percent = percent;
	}

	public boolean isPercent() {
		return percent != 0;
	}
}