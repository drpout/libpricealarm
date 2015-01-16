package mobi.boilr.libpricealarm;

import java.io.IOException;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public class PriceSpikeAlarm extends PriceChangeAlarm {

	private static final long serialVersionUID = 3011908896235313698L;
	private long timeFrame;
	private long startOfFrameTimestamp;

	public PriceSpikeAlarm(int id, Exchange exchange, Pair pair, long updateInterval, Notify notify, double change, long timeFrame)
			throws NumberFormatException, IOException {
		super(id, exchange, pair, updateInterval, notify, change);
		this.timeFrame = timeFrame;
		startOfFrameTimestamp = getLastUpdateTimestamp().getTime();
	}

	public PriceSpikeAlarm(int id, Exchange exchange, Pair pair, long updateInterval, Notify notify, float percent, long timeFrame)
			throws NumberFormatException, IOException {
		super(id, exchange, pair, updateInterval, notify, percent);
		this.timeFrame = timeFrame;
		startOfFrameTimestamp = getLastUpdateTimestamp().getTime();
	}

	@Override
	public boolean run() throws NumberFormatException, IOException {
		boolean ret = true;
		double newValue = getExchangeLastValue();
		elapsedMilis = getLastUpdateTimestamp().getTime() - startOfFrameTimestamp;
		lastChange = Math.abs(lastValue - newValue);
		if(lastChange >= change) {
			ret = notify.trigger(getId());
		}
		if(percent > 0) {
			lastChange = (lastChange / lastValue) * 100;
		}
		if(elapsedMilis >= timeFrame) {
			startOfFrameTimestamp = getLastUpdateTimestamp().getTime();
			lastValue = newValue;
			if(percent > 0) {
				change = newValue * (percent * 0.01);
			}
		}
		return ret;
	}

	public long getTimeFrame() {
		return timeFrame;
	}

	public void setTimeFrame(long timeFrame) {
		this.timeFrame = timeFrame;
	}

}
