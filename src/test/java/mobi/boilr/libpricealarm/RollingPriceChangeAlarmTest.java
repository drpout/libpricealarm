package mobi.boilr.libpricealarm;

import static org.mockito.Mockito.after;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Timer;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class RollingPriceChangeAlarmTest {

	private RollingPriceChangeAlarm testAlarm;
	private TimerTaskAlarmWrapper wrapper;
	private Notifier notifier;
	private Exchange exchange;
	private Timer timer;
	private Pair pair;
	private static final int alarmID = 1;

	@Before
	public void setUp() throws Exception {
		notifier = mock(Notifier.class, Mockito.CALLS_REAL_METHODS);
		when(notifier.notify(alarmID)).thenReturn(true);
		exchange = mock(Exchange.class);
		pair = new Pair("XXX", "YYY");
		timer = new Timer();
	}

	@Test(expected = TimeFrameSmallerOrEqualUpdateIntervalException.class)
	public void testCreateEqualPeriods() throws TimeFrameSmallerOrEqualUpdateIntervalException, NumberFormatException, IOException {
		testAlarm = new RollingPriceChangeAlarm(alarmID, exchange, pair, 500, notifier, 0.0043, 500);
	}

	@Test(expected = TimeFrameSmallerOrEqualUpdateIntervalException.class)
	public void testCreateInvertedPeriods() throws TimeFrameSmallerOrEqualUpdateIntervalException, NumberFormatException, IOException {
		testAlarm = new RollingPriceChangeAlarm(alarmID, exchange, pair, 2000, notifier, 0.0042, 500);
	}

	@Test(expected = TimeFrameSmallerOrEqualUpdateIntervalException.class)
	public void testSetEqualPeriods() throws TimeFrameSmallerOrEqualUpdateIntervalException, NumberFormatException, IOException {
		testAlarm = new RollingPriceChangeAlarm(alarmID, exchange, pair, 500, notifier, 0.002d, 2000);
		testAlarm.setPeriod(2000);
	}

	@Test(expected = TimeFrameSmallerOrEqualUpdateIntervalException.class)
	public void testSetInvertedPeriods() throws TimeFrameSmallerOrEqualUpdateIntervalException, NumberFormatException, IOException {
		testAlarm = new RollingPriceChangeAlarm(alarmID, exchange, pair, 500, notifier, 0.002d, 2000);
		testAlarm.setTimeFrame(250);
	}

	@Test
	public void testRollingChange() throws NumberFormatException, IOException, TimeFrameSmallerOrEqualUpdateIntervalException {
		when(exchange.getLastValue(pair)).thenReturn(0.004);
		testAlarm = new RollingPriceChangeAlarm(alarmID, exchange, pair, 500, notifier, 0.002d, 2000);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		verify(notifier, after(250).never()).notify(alarmID);
		when(exchange.getLastValue(pair)).thenReturn(0.005);
		verify(notifier, after(500).never()).notify(alarmID);
		Assert.assertEquals(0.001, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.006);
		verify(notifier, after(500).times(1)).notify(alarmID);
		Assert.assertEquals(0.002, testAlarm.getLastChange(), 0);
		verify(notifier, after(500).times(2)).notify(alarmID);
		Assert.assertEquals(0.002, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.003);
		verify(notifier, after(500).times(2)).notify(alarmID);
		Assert.assertEquals(0.001, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.005);
		verify(notifier, after(500).times(2)).notify(alarmID);
		Assert.assertEquals(0.0, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.006);
		verify(notifier, after(500).times(2)).notify(alarmID);
		Assert.assertEquals(0.0, testAlarm.getLastChange(), 0);
		verify(notifier, after(500).times(2)).notify(alarmID);
		Assert.assertEquals(0.0, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.003);
		verify(notifier, after(500).times(2)).notify(alarmID);
		Assert.assertEquals(0.002, testAlarm.getChange(), 0);
		Assert.assertEquals(0.0, testAlarm.getLastChange(), 0);
		Assert.assertEquals(2000, testAlarm.getElapsedMilis(), 100);
		Assert.assertEquals(0.003, testAlarm.getBaseValue(), 0);
		Assert.assertEquals(0.003, testAlarm.getLastValue(), 0);
		wrapper.turnOff();
	}

	@Test
	public void testRollingChangeNoNet() throws NumberFormatException, IOException, TimeFrameSmallerOrEqualUpdateIntervalException {
		when(exchange.getLastValue(pair)).thenReturn(0.001);
		testAlarm = new RollingPriceChangeAlarm(alarmID, exchange, pair, 500, notifier, 0.002d, 2000);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		verify(notifier, after(250).never()).notify(alarmID);
		when(exchange.getLastValue(pair)).thenReturn(0.002);
		verify(notifier, after(500).never()).notify(alarmID);
		Assert.assertEquals(0.001, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenThrow(new IOException("No net simulation"));
		verify(notifier, after(2500).never()).notify(alarmID);
		Mockito.reset(exchange);
		when(exchange.getLastValue(pair)).thenReturn(0.003);
		verify(notifier, after(500).never()).notify(alarmID);
		Assert.assertEquals(0.002, testAlarm.getChange(), 0);
		Assert.assertEquals(0.001, testAlarm.getLastChange(), 0);
		Assert.assertEquals(3000, testAlarm.getElapsedMilis(), 100);
		Assert.assertEquals(0.002, testAlarm.getBaseValue(), 0);
		Assert.assertEquals(0.003, testAlarm.getLastValue(), 0);
		wrapper.turnOff();
	}

	@Test
	public void testRollingPercent() throws NumberFormatException, IOException, TimeFrameSmallerOrEqualUpdateIntervalException {
		when(exchange.getLastValue(pair)).thenReturn(0.004);
		testAlarm = new RollingPriceChangeAlarm(alarmID, exchange, pair, 500, notifier, 50f, 2000);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		verify(notifier, after(250).never()).notify(alarmID);
		when(exchange.getLastValue(pair)).thenReturn(0.005);
		verify(notifier, after(500).never()).notify(alarmID);
		Assert.assertEquals(25, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.006);
		verify(notifier, after(500).times(1)).notify(alarmID);
		Assert.assertEquals(50, testAlarm.getLastChange(), 0);
		verify(notifier, after(500).times(2)).notify(alarmID);
		Assert.assertEquals(50, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.003);
		verify(notifier, after(500).times(2)).notify(alarmID);
		Assert.assertEquals(25, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.010);
		verify(notifier, after(500).times(3)).notify(alarmID);
		Assert.assertEquals(0.0025, testAlarm.getChange(), 0);
		Assert.assertEquals(100, testAlarm.getLastChange(), 0);
		Assert.assertEquals(0.005, testAlarm.getBaseValue(), 0);
		Assert.assertEquals(0.010, testAlarm.getLastValue(), 0);
		wrapper.turnOff();
	}

	@Test
	public void testGetLimits() throws NumberFormatException, IOException, InterruptedException, TimeFrameSmallerOrEqualUpdateIntervalException {
		when(exchange.getLastValue(pair)).thenReturn(0.004);
		testAlarm = new RollingPriceChangeAlarm(alarmID, exchange, pair, 500, notifier, 50f, 2000);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		Thread.sleep(250);
		Assert.assertEquals(0.006, testAlarm.getUpperLimit(), 0);
		Assert.assertEquals(0.002, testAlarm.getLowerLimit(), 0);
		wrapper.turnOff();
	}

	@Test
	public void testClearBuffer() throws NumberFormatException, IOException, TimeFrameSmallerOrEqualUpdateIntervalException {
		when(notifier.notify(alarmID)).thenReturn(true);
		when(exchange.getLastValue(pair)).thenReturn(0.004);
		testAlarm = new RollingPriceChangeAlarm(alarmID, exchange, pair, 500, notifier, 50f, 2000);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		verify(notifier, after(750).never()).notify(alarmID);
		Assert.assertEquals(0.004, testAlarm.getLastValue(), 0);
		testAlarm.setPair(new Pair("FOO", "BAR"));
		Assert.assertTrue(Double.isNaN(testAlarm.getLastValue()));
		Assert.assertTrue(Double.isNaN(testAlarm.getBaseValue()));
	}

	@Test
	public void testToString() throws IOException, NumberFormatException, TimeFrameSmallerOrEqualUpdateIntervalException {
		when(exchange.getName()).thenReturn("DummyExchange");
		testAlarm = new RollingPriceChangeAlarm(alarmID, exchange, pair, 1000, notifier, 50f, 60000);
		Assert.assertEquals("RollingPriceChangeAlarm XXX YYY DummyExchange", testAlarm.toString());
	}
}
