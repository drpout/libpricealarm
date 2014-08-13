package com.github.andrefbsantos.libpricealarm;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

import com.github.andrefbsantos.libdynticker.core.Exchange;
import com.github.andrefbsantos.libdynticker.core.Pair;

public abstract class Alarm extends TimerTask implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 438506410563236110L;

	private long id;
	private boolean on;
	private long period;
	private transient Timer timer;
	private transient Exchange exchange;
	private String exchangeCode;
	private Pair pair;
	protected double lastValue;
	private Timestamp lastUpdateTimestamp;
	protected Notify notify;

	public Alarm(long id, Exchange exchange, Pair pair, Timer timer, long period, Notify notify) {
		this.id = id;
		this.exchange = exchange;
		exchangeCode = exchange.getClass().getCanonicalName();
		this.pair = pair;
		on = true;
		this.timer = timer;
		this.period = period;
		this.notify = notify;
		timer.schedule(this, period, period);
	}

	public void doReset(boolean reset) {
		if (!reset) {
			cancel();
			on = false;
		}
	}

	public Exchange getExchange() {
		return exchange;
	}

	protected double getExchangeLastValue() {
		double lastValue = this.lastValue;
		try {
			lastValue = exchange.getLastValue(pair);
			if (lastUpdateTimestamp == null) {
				lastUpdateTimestamp = new Timestamp(System.currentTimeMillis());
			} else {
				lastUpdateTimestamp.setTime(System.currentTimeMillis());
			}
		} catch (IOException e) {
		}
		return lastValue;
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

	public void setPeriod(long period) {
		this.period = period;
		cancel();
		timer.schedule(this, period, period);
	}

	public void toggle() {
		if (on) {
			cancel();
			on = false;
		} else {
			timer.schedule(this, period, period);
			on = true;
		}
	}

	public void turnOff() {
		if (on) {
			cancel();
			on = false;
		}
	}

	public void turnOn() {
		if (!on) {
			timer.schedule(this, period, period);
			on = true;
		}
	}

	// private void writeObject(ObjectOutputStream os) throws IOException, ClassNotFoundException {
	// try {
	// os.defaultWriteObject();
	// // os.writeChars(exchange.getName());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// private void readObject(ObjectInputStream is)
	// throws IOException, ClassNotFoundException {
	// try {
	// is.defaultReadObject();
	// // System.out.println(is.readUTF());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public void resume() {
		if (on) {
			timer.schedule(this, period, period);
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the exchangeCode
	 */
	public String getExchangeCode() {
		return exchangeCode;
	}
}
