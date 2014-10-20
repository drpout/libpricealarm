package mobi.boilr.libpricealarm;

import java.io.IOException;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public class PriceHitAlarm extends Alarm {

	private static final long serialVersionUID = 7243560546256303827L;
	private double upperLimit;
	private double lowerLimit;
	private boolean wasUpperLimitHit = false;

	public PriceHitAlarm(int id, Exchange exchange, Pair pair, long period, Notify notify,
			double upperLimit, double lowerLimit)
					throws UpperLimitSmallerOrEqualLowerLimitException {
		super(id, exchange, pair, period, notify);
		if(upperLimit <= lowerLimit) {
			throw new UpperLimitSmallerOrEqualLowerLimitException();
		} else {
			this.upperLimit = upperLimit;
			this.lowerLimit = lowerLimit;
		}
	}

	@Override
	public boolean run() throws NumberFormatException, IOException {
		lastValue = getExchangeLastValue();
		if(lastValue <= lowerLimit) {
			wasUpperLimitHit = true;
		} else if(lastValue >= upperLimit) {
			wasUpperLimitHit = false;
		} else {
			return true;
		}
		return notify.trigger(getId());
	}

	public double getLowerLimit() {
		return this.lowerLimit;
	}

	public void setLowerLimit(double lowerLimit) throws UpperLimitSmallerOrEqualLowerLimitException {
		if(lowerLimit >= upperLimit)
			throw new UpperLimitSmallerOrEqualLowerLimitException();
		this.lowerLimit = lowerLimit;
	}

	public double getUpperLimit() {
		return this.upperLimit;
	}

	public void setUpperLimit(double upperLimit) throws UpperLimitSmallerOrEqualLowerLimitException {
		if(upperLimit <= lowerLimit)
			throw new UpperLimitSmallerOrEqualLowerLimitException();
		this.upperLimit = upperLimit;
	}

	public boolean wasUpperLimitHit() {
		return wasUpperLimitHit;
	}
}
