package mobi.boilr.libpricealarm;

import java.io.IOException;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public class PriceChangeAlarm extends Alarm {

	private static final long serialVersionUID = -5424769817492896869L;
	protected double change;
	protected float percent;
	protected double lastChange = 0;
	protected long elapsedMilis = 0;

	public PriceChangeAlarm(int id, Exchange exchange, Pair pair, long period, Notify notify,
			double change) throws NumberFormatException, IOException {
		super(id, exchange, pair, period, notify);
		this.change = change;
		percent = 0;
		lastValue = getExchangeLastValue();
	}

	public PriceChangeAlarm(int id, Exchange exchange, Pair pair, long period, Notify notify,
			float percent) throws NumberFormatException, IOException {
		super(id, exchange, pair, period, notify);
		this.percent = percent;
		lastValue = getExchangeLastValue();
		change = lastValue * (percent * 0.01);
	}

	@Override
	public boolean run() throws NumberFormatException, IOException {
		boolean ret = true;
		long prevMilis = getLastUpdateTimestamp().getTime();
		double newValue = getExchangeLastValue();
		elapsedMilis = getLastUpdateTimestamp().getTime() - prevMilis;
		lastChange = Math.abs(lastValue - newValue);
		if(lastChange >= change) {
			ret = notify.trigger(getId());
		}
		if(percent > 0) {
			change = newValue * (percent * 0.01);
			lastChange = (lastChange / lastValue) * 100;
		}
		lastValue = newValue;
		return ret;
	}

	public double getChange() {
		return change;
	}

	public void setChange(double change) {
		this.change = change;
		percent = 0;
	}

	public float getPercent() {
		return percent;
	}

	public void setPercent(float percent) {
		this.percent = percent;
		change = lastValue * (percent * 0.01);
	}

	public boolean isPercent() {
		return percent != 0;
	}

	public double getLastChange() {
		return lastChange;
	}

	public long getElapsedMilis() {
		return elapsedMilis;
	}
}