package mobi.boilr.libpricealarm;

import java.io.IOException;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

/**
 * A PriceChangeAlarm is triggered when price changes more than X amount (in
 * currency or percentage) in a specified time frame (e.g. 1 day). It updates
 * the last value from the exchange only at the end of the time frame (e.g. once
 * a day). Therefore it does not track sudden price changes in the middle of the
 * time frame.
 */
public class PriceChangeAlarm extends Alarm {

	private static final long serialVersionUID = -5424769817492896869L;
	protected double change;
	protected float percent;
	protected double lastChange = 0;
	protected long elapsedMilis = 0;

	public PriceChangeAlarm(int id, Exchange exchange, Pair pair, long timeFrame, Notify notify,
			double change) throws NumberFormatException, IOException {
		super(id, exchange, pair, timeFrame, notify);
		this.change = change;
		percent = 0;
		lastValue = getExchangeLastValue();
	}

	public PriceChangeAlarm(int id, Exchange exchange, Pair pair, long timeFrame, Notify notify,
			float percent) throws NumberFormatException, IOException {
		super(id, exchange, pair, timeFrame, notify);
		this.percent = percent;
		lastValue = getExchangeLastValue();
		change = lastValue * (percent * 0.01);
	}

	@Override
	public boolean run() throws NumberFormatException, IOException {
		boolean ret = true;
		long prevMilis = getLastUpdateTimestamp().getTime();
		double newValue = getExchangeLastValue();
		computeDirection(newValue);
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