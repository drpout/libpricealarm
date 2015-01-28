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

public class PriceSpikeAlarmTest {

	private PriceSpikeAlarm testAlarm;
	private TimerTaskAlarmWrapper wrapper;
	private Notifier notify;
	private Exchange exchange;
	private Timer timer;
	private Pair pair;
	private static final int alarmID = 1;

	@Before
	public void setUp() throws Exception {
		notify = mock(Notifier.class, Mockito.CALLS_REAL_METHODS);
		when(notify.notify(alarmID)).thenReturn(true);
		exchange = mock(Exchange.class);
		pair = new Pair("XXX", "YYY");
		timer = new Timer();
	}

	@Test(expected = TimeFrameSmallerOrEqualUpdateIntervalException.class)
	public void testCreateEqualPeriods() throws TimeFrameSmallerOrEqualUpdateIntervalException, NumberFormatException, IOException {
		testAlarm = new PriceSpikeAlarm(alarmID, exchange, pair, 500, notify, 0.0043, 500);
	}

	@Test(expected = TimeFrameSmallerOrEqualUpdateIntervalException.class)
	public void testCreateInvertedPeriods() throws TimeFrameSmallerOrEqualUpdateIntervalException, NumberFormatException, IOException {
		testAlarm = new PriceSpikeAlarm(alarmID, exchange, pair, 2000, notify, 0.0042, 500);
	}

	@Test(expected = TimeFrameSmallerOrEqualUpdateIntervalException.class)
	public void testSetEqualPeriods() throws TimeFrameSmallerOrEqualUpdateIntervalException, NumberFormatException, IOException {
		testAlarm = new PriceSpikeAlarm(alarmID, exchange, pair, 500, notify, 0.002d, 2000);
		testAlarm.setPeriod(2000);
	}

	@Test(expected = TimeFrameSmallerOrEqualUpdateIntervalException.class)
	public void testSetInvertedPeriods() throws TimeFrameSmallerOrEqualUpdateIntervalException, NumberFormatException, IOException {
		testAlarm = new PriceSpikeAlarm(alarmID, exchange, pair, 500, notify, 0.002d, 2000);
		testAlarm.setTimeFrame(250);
	}

	@Test
	public void testSpikeChange() throws NumberFormatException, IOException, TimeFrameSmallerOrEqualUpdateIntervalException {
		when(exchange.getLastValue(pair)).thenReturn(0.004);
		testAlarm = new PriceSpikeAlarm(alarmID, exchange, pair, 500, notify, 0.002d, 2000);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		verify(notify, after(250).never()).notify(alarmID);
		when(exchange.getLastValue(pair)).thenReturn(0.005);
		verify(notify, after(500).never()).notify(alarmID);
		Assert.assertEquals(0.001, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.006);
		verify(notify, after(500).times(1)).notify(alarmID);
		Assert.assertEquals(0.002, testAlarm.getLastChange(), 0);
		verify(notify, after(500).times(2)).notify(alarmID);
		Assert.assertEquals(0.002, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.003);
		verify(notify, after(500).times(2)).notify(alarmID);
		Assert.assertEquals(0.001, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.005);
		verify(notify, after(500).times(2)).notify(alarmID);
		Assert.assertEquals(0.0, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.006);
		verify(notify, after(500).times(2)).notify(alarmID);
		Assert.assertEquals(0.0, testAlarm.getLastChange(), 0);
		verify(notify, after(500).times(2)).notify(alarmID);
		Assert.assertEquals(0.0, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.003);
		verify(notify, after(500).times(2)).notify(alarmID);
		Assert.assertEquals(0.002, testAlarm.getChange(), 0);
		Assert.assertEquals(0.0, testAlarm.getLastChange(), 0);
		Assert.assertEquals(2000, testAlarm.getElapsedMilis(), 100);
		Assert.assertEquals(0.003, testAlarm.getBaseValue(), 0);
		Assert.assertEquals(0.003, testAlarm.getLastValue(), 0);
		wrapper.turnOff();
	}

	@Test
	public void testSpikeChangeNoNet() throws NumberFormatException, IOException, TimeFrameSmallerOrEqualUpdateIntervalException {
		when(exchange.getLastValue(pair)).thenReturn(0.001);
		testAlarm = new PriceSpikeAlarm(alarmID, exchange, pair, 500, notify, 0.002d, 2000);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		verify(notify, after(250).never()).notify(alarmID);
		when(exchange.getLastValue(pair)).thenReturn(0.002);
		verify(notify, after(500).never()).notify(alarmID);
		Assert.assertEquals(0.001, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenThrow(new IOException("No net simulation"));
		verify(notify, after(2500).never()).notify(alarmID);
		Mockito.reset(exchange);
		when(exchange.getLastValue(pair)).thenReturn(0.003);
		verify(notify, after(500).never()).notify(alarmID);
		Assert.assertEquals(0.002, testAlarm.getChange(), 0);
		Assert.assertEquals(0.001, testAlarm.getLastChange(), 0);
		Assert.assertEquals(3000, testAlarm.getElapsedMilis(), 100);
		Assert.assertEquals(0.002, testAlarm.getBaseValue(), 0);
		Assert.assertEquals(0.003, testAlarm.getLastValue(), 0);
		wrapper.turnOff();
	}

	@Test
	public void testSpikePercent() throws NumberFormatException, IOException, TimeFrameSmallerOrEqualUpdateIntervalException {
		when(exchange.getLastValue(pair)).thenReturn(0.004);
		testAlarm = new PriceSpikeAlarm(alarmID, exchange, pair, 500, notify, 50f, 2000);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		verify(notify, after(250).never()).notify(alarmID);
		when(exchange.getLastValue(pair)).thenReturn(0.005);
		verify(notify, after(500).never()).notify(alarmID);
		Assert.assertEquals(25, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.006);
		verify(notify, after(500).times(1)).notify(alarmID);
		Assert.assertEquals(50, testAlarm.getLastChange(), 0);
		verify(notify, after(500).times(2)).notify(alarmID);
		Assert.assertEquals(50, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.003);
		verify(notify, after(500).times(2)).notify(alarmID);
		Assert.assertEquals(25, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.010);
		verify(notify, after(500).times(3)).notify(alarmID);
		Assert.assertEquals(0.0025, testAlarm.getChange(), 0);
		Assert.assertEquals(100, testAlarm.getLastChange(), 0);
		Assert.assertEquals(0.005, testAlarm.getBaseValue(), 0);
		Assert.assertEquals(0.010, testAlarm.getLastValue(), 0);
		wrapper.turnOff();
	}

	@Test
	public void testGetLimits() throws NumberFormatException, IOException, InterruptedException, TimeFrameSmallerOrEqualUpdateIntervalException {
		when(exchange.getLastValue(pair)).thenReturn(0.004);
		testAlarm = new PriceSpikeAlarm(alarmID, exchange, pair, 500, notify, 50f, 2000);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		Thread.sleep(250);
		Assert.assertEquals(0.006, testAlarm.getUpperLimit(), 0);
		Assert.assertEquals(0.002, testAlarm.getLowerLimit(), 0);
		wrapper.turnOff();
	}

	@Test
	public void testToString() throws IOException, NumberFormatException, TimeFrameSmallerOrEqualUpdateIntervalException {
		when(exchange.getName()).thenReturn("DummyExchange");
		testAlarm = new PriceSpikeAlarm(alarmID, exchange, pair, 1000, notify, 50f, 60000);
		Assert.assertEquals("PriceSpikeAlarm XXX YYY DummyExchange", testAlarm.toString());
	}
}
