package mobi.boilr.libpricealarm;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

/**
 * A RollingPriceChangeAlarm is a PriceChangeAlarm with a rolling time frame.
 * Besides the time frame it also takes into account an update interval. The
 * update interval denotes how often price is fetched. The time frame defines
 * the base price, i.e., which price to compare to. When a new price is fetched
 * it marks the end of a time frame and is compared to the value previously
 * fetched at the beginning of such time frame. To do this we need to store
 * values in a buffer for later comparison.
 * 
 * With this approach, a RollingPriceChangeAlarm is triggered when price
 * drops/rises quickly all of a sudden.
 * 
 * Check: https://github.com/drpout/boilr/issues/47
 */
public class RollingPriceChangeAlarm extends PriceChangeAlarm {

	private class TimestampedLastValue implements Serializable {
		private static final long serialVersionUID = 3449660866846381443L;
		private double lastValue;
		private long timestamp;

		public TimestampedLastValue(double lastValue, long timestamp) {
			this.lastValue = lastValue;
			this.timestamp = timestamp;
		}

		public double getLastValue() {
			return lastValue;
		}

		public void setLastValue(double lastValue) {
			this.lastValue = lastValue;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
	}

	private static final long serialVersionUID = 3011908896235313698L;
	private long timeFrame;
	private double baseValue = Double.NaN;
	private List<TimestampedLastValue> lastValueBuffer = new ArrayList<TimestampedLastValue>();

	public RollingPriceChangeAlarm(int id, Exchange exchange, Pair pair, long updateInterval, Notifier notify,
			boolean defusable, double change, long timeFrame) throws TimeFrameSmallerOrEqualUpdateIntervalException {
		super(id, exchange, pair, updateInterval, notify, defusable, change);
		setTimeFrame(timeFrame);
	}

	public RollingPriceChangeAlarm(int id, Exchange exchange, Pair pair, long updateInterval, Notifier notify,
			boolean defusable, float percent, long timeFrame) throws TimeFrameSmallerOrEqualUpdateIntervalException {
		super(id, exchange, pair, updateInterval, notify, defusable, percent);
		setTimeFrame(timeFrame);
	}

	@Override
	public boolean run() throws NumberFormatException, IOException {
		boolean ret = true;
		double newValue = getExchangeLastValue();
		long newValueTimestamp = getLastUpdateTimestamp().getTime();
		if(Double.isNaN(lastValue)) { // Initialize.
			lastValueBuffer.add(new TimestampedLastValue(newValue, newValueTimestamp));
			// Define the base value.
			baseValue = newValue;
			// Compute change.
			if(isPercent()) {
				calcChangeFromPercent(baseValue);
			}
		} else {
			/*
			 * Get the base value for comparison.
			 */
			TimestampedLastValue tmlv = lastValueBuffer.get(0);
			/*
			 * In the event the alarm could not update during a period longer
			 * than one update interval, we remove the outdated values from the
			 * buffer and do the change comparison with the most up-to-date
			 * value available.
			 */
			while(!lastValueBuffer.isEmpty()) {
				tmlv = lastValueBuffer.get(0);
				elapsedMilis = newValueTimestamp - tmlv.getTimestamp();
				if(elapsedMilis < timeFrame + getPeriod()) { // overTimeFrame
					break;
				} else {
					lastValueBuffer.remove(0);
				}
			}

			baseValue = tmlv.getLastValue();
			computeDirection(baseValue, newValue);

			/*
			 * Compute change and check if we should trigger.
			 */
			lastChange = Math.abs(baseValue - newValue);
			if(isPercent()) {
				calcChangeFromPercent(baseValue);
			}
			if(lastChange >= change)
				ret = notifier.trigger(this);
			else
				notifier.defuse(this);
			if(isPercent()) {
				calcLastChangeInPercent(baseValue);
			}

			/*
			 * If the buffer does not contain enough elements to represent one
			 * time frame we keep filling it. Otherwise we remove the head
			 * element (the oldest one) and add the new value at the tail.
			 */
			if(elapsedMilis < timeFrame - (getPeriod() / 2)) {
				// underTimeFrame, fill buffer
				lastValueBuffer.add(new TimestampedLastValue(newValue, newValueTimestamp));
			} else {
				lastValueBuffer.remove(tmlv);
				tmlv.setLastValue(newValue);
				tmlv.setTimestamp(newValueTimestamp);
				lastValueBuffer.add(tmlv);
			}
		}
		lastValue = newValue;
		return ret;
	}

	public long getTimeFrame() {
		return timeFrame;
	}

	public void setTimeFrame(long timeFrame) throws TimeFrameSmallerOrEqualUpdateIntervalException {
		if(timeFrame <= getPeriod())
			throw new TimeFrameSmallerOrEqualUpdateIntervalException();
		this.timeFrame = timeFrame;
	}

	@Override
	public void setPeriod(long period) throws TimeFrameSmallerOrEqualUpdateIntervalException {
		if(timeFrame <= period)
			throw new TimeFrameSmallerOrEqualUpdateIntervalException();
		super.setPeriod(period);
	}

	public double getBaseValue() {
		return baseValue;
	}

	@Override
	public double getLowerLimit() {
		return baseValue - change;
	}

	@Override
	public double getUpperLimit() {
		return baseValue + change;
	}

	@Override
	protected void clearBuffer() {
		lastValueBuffer.clear();
		lastValue = baseValue = Double.NaN;
	}
}
