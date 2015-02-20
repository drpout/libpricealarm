package mobi.boilr.libpricealarm;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public abstract class Alarm implements Serializable {

	private static final long serialVersionUID = 438506410563236110L;

	private int id;
	private boolean on = true;
	private long period;
	private transient Exchange exchange;
	private String exchangeCode;
	private Pair pair;
	protected double lastValue = Double.NaN;
	private Timestamp lastUpdateTimestamp;
	protected Notifier notifier;
	private int position;
	public enum Direction {
		UP, DOWN, NONE
	};
	private Direction direction = Direction.NONE;

	public Alarm(int id, Exchange exchange, Pair pair, long period, Notifier notifier) {
		this.id = this.position = id;
		this.exchange = exchange;
		exchangeCode = exchange.getClass().getCanonicalName();
		this.pair = pair;
		this.period = period;
		this.notifier = notifier;
	}

	/**
	 * Runs the logic behind the alarm and triggers a notification if the
	 * alarm conditions are met.
	 *
	 * @return true if alarm should be reset, false if it should be turned off
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public abstract boolean run() throws NumberFormatException, IOException;

	public Exchange getExchange() {
		return exchange;
	}

	public double getExchangeLastValue() throws NumberFormatException, IOException {
		double newValue = this.lastValue;
		newValue = exchange.getLastValue(pair);
		if(lastUpdateTimestamp == null) {
			lastUpdateTimestamp = new Timestamp(System.currentTimeMillis());
		} else {
			lastUpdateTimestamp.setTime(System.currentTimeMillis());
		}
		return newValue;
	}

	/**
	 * Checks whether price has gone up or down.
	 * @param baseValue The value to compare to.
	 * @param newValue The last price fetched from the exchange.
	 */
	protected void computeDirection(double baseValue, double newValue) {
		if(newValue > baseValue) {
			direction = Direction.UP;
		} else if(newValue < baseValue) {
			direction = Direction.DOWN;
		}
	}

	public double getLastValue() {
		return lastValue;
	}

	public Pair getPair() {
		return pair;
	}

	public long getPeriod() {
		return period;
	}

	public boolean isOn() {
		return on;
	}

	public void setPeriod(long period) throws TimeFrameSmallerOrEqualUpdateIntervalException {
		this.period = period;
	}

	public void toggle() {
		on = !on;
	}

	public void turnOff() {
		on = false;
	}

	public void turnOn() {
		on = true;
	}

	public int getId() {
		return id;
	}

	public String getExchangeCode() {
		return exchangeCode;
	}

	public Notifier getNotifier() {
		return notifier;
	}

	public void setNotifier(Notifier notifier) {
		this.notifier = notifier;
	}

	public void setExchange(Exchange exchange) {
		this.exchange = exchange;
		this.exchangeCode = exchange.getClass().getCanonicalName();
	}

	public void setPair(Pair pair) {
		this.pair = pair;
	}

	public Timestamp getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}

	public Direction getDirection() {
		return direction;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + pair.getCoin() +
				" " + pair.getExchange() + " " + exchange.getName();
	}

	public abstract double getLowerLimit();

	public abstract double getUpperLimit();

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
