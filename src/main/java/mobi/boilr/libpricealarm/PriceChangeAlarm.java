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
	protected double change = Double.NaN;
	protected float percent;
	protected double lastChange = 0;
	protected long elapsedMilis = 0;

	public PriceChangeAlarm(int id, Exchange exchange, Pair pair, long timeFrame, Notifier notifier, double change) {
		super(id, exchange, pair, timeFrame, notifier);
		this.change = change;
		percent = 0;
	}

	public PriceChangeAlarm(int id, Exchange exchange, Pair pair, long timeFrame, Notifier notifier, float percent) {
		super(id, exchange, pair, timeFrame, notifier);
		this.percent = percent;
	}

	@Override
	public boolean run() throws NumberFormatException, IOException {
		boolean ret = true;
		double newValue;
		if(Double.isNaN(lastValue)) { // Initialize.
			newValue = getExchangeLastValue();
		} else {
			long prevMilis = getLastUpdateTimestamp().getTime();
			newValue = getExchangeLastValue();
			computeDirection(lastValue, newValue);
			elapsedMilis = getLastUpdateTimestamp().getTime() - prevMilis;
			lastChange = Math.abs(lastValue - newValue);
			if(lastChange >= change) {
				ret = notifier.trigger(this);
			}
			if(isPercent()) {
				calcLastChangeInPercent(lastValue);
			}
		}
		if(isPercent()) {
			calcChangeFromPercent(newValue);
		}
		lastValue = newValue;
		return ret;
	}

	protected void calcLastChangeInPercent(double baseValue) {
		lastChange = (lastChange / baseValue) * 100;
	}

	protected void calcChangeFromPercent(double baseValue) {
		change = baseValue * (percent * 0.01);
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
		calcChangeFromPercent(lastValue);
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

	@Override
	public double getLowerLimit() {
		return lastValue - change;
	}

	@Override
	public double getUpperLimit() {
		return lastValue + change;
	}
}