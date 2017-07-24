package mobi.boilr.libpricealarm;

import static org.mockito.Mockito.after;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Timer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public class PriceHitAlarmTest {

	private PriceHitAlarm testAlarm;
	private TimerTaskAlarmWrapper wrapper;
	private Notifier notifier;
	private Exchange exchange;
	private Pair pair;
	private Timer timer;
	private static final int alarmID = 1;

	@Before
	public void setUp() throws Exception {
		notifier = mock(Notifier.class, Mockito.CALLS_REAL_METHODS);
		exchange = mock(Exchange.class);
		pair = new Pair("XXX", "YYY");
		timer = new Timer();
		testAlarm = new PriceHitAlarm(alarmID, exchange, pair, 500, notifier, true, 0.0043, 0.0042);
	}

	@Test(expected = UpperLimitSmallerOrEqualLowerLimitException.class)
	public void testCreateEqualLimits() throws UpperLimitSmallerOrEqualLowerLimitException {
		testAlarm = new PriceHitAlarm(alarmID, exchange, pair, 500, notifier, true, 0.0043, 0.0043);
	}

	@Test(expected = UpperLimitSmallerOrEqualLowerLimitException.class)
	public void testCreateInvertedLimits() throws UpperLimitSmallerOrEqualLowerLimitException {
		testAlarm = new PriceHitAlarm(alarmID, exchange, pair, 500, notifier, true, 0.0042, 0.0043);
	}

	@Test(expected = UpperLimitSmallerOrEqualLowerLimitException.class)
	public void testSetEqualLimits() throws UpperLimitSmallerOrEqualLowerLimitException {
		testAlarm.setLowerLimit(0.0043);
	}

	@Test(expected = UpperLimitSmallerOrEqualLowerLimitException.class)
	public void testSetInvertedLimits() throws UpperLimitSmallerOrEqualLowerLimitException {
		testAlarm.setUpperLimit(0.0041);
	}

	@Test
	public void testUpperLimitNoReset() throws IOException {
		when(notifier.notify(alarmID)).thenReturn(false);
		when(exchange.getLastValue(pair)).thenReturn(0.00445625);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		verify(notifier, after(750).times(1)).notify(alarmID);
		verify(notifier, after(0).never()).suppress(alarmID);
		Assert.assertEquals(0.00445625, testAlarm.getLastValue(), 0);
		wrapper.turnOff();
	}

	@Test
	public void testUpperLimitAndReset() throws IOException {
		when(notifier.notify(alarmID)).thenReturn(true);
		when(exchange.getLastValue(pair)).thenReturn(0.00445625);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		verify(notifier, after(1250).times(1)).notify(alarmID);
		verify(notifier, after(0).times(2)).suppress(alarmID);
		Assert.assertEquals(0.00445625, testAlarm.getLastValue(), 0);
		wrapper.turnOff();
	}

	@Test
	public void testLowerLimitNoReset() throws IOException {
		when(notifier.notify(alarmID)).thenReturn(false);
		when(exchange.getLastValue(pair)).thenReturn(0.004123);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		verify(notifier, after(750).times(1)).notify(alarmID);
		verify(notifier, after(0).never()).suppress(alarmID);
		Assert.assertEquals(0.004123, testAlarm.getLastValue(), 0);
		wrapper.turnOff();
	}

	@Test
	public void testNoLimitHit() throws IOException {
		when(notifier.notify(alarmID)).thenReturn(false);
		when(exchange.getLastValue(pair)).thenReturn(0.0042523);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		verify(notifier, after(750).never()).notify(alarmID);
		verify(notifier, after(0).times(2)).suppress(alarmID);
		Assert.assertEquals(0.0042523, testAlarm.getLastValue(), 0);
		wrapper.turnOff();
	}

	@Test
	public void testToString() {
		when(exchange.getName()).thenReturn("DummyExchange");
		Assert.assertEquals("PriceHitAlarm XXX YYY DummyExchange", testAlarm.toString());
	}
}
