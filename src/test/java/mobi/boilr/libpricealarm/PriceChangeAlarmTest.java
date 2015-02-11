package mobi.boilr.libpricealarm;

import static org.mockito.Mockito.after;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Timer;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PriceChangeAlarmTest {

	private PriceChangeAlarm testAlarm;
	private TimerTaskAlarmWrapper wrapper;
	private Notifier notifier;
	private Exchange exchange;
	private Timer timer;
	private Pair pair;
	private static final int alarmID = 1;

	@Before
	public void setUp() throws Exception {
		notifier = mock(Notifier.class, Mockito.CALLS_REAL_METHODS);
		exchange = mock(Exchange.class);
		pair = new Pair("XXX", "YYY");
		timer = new Timer();
	}

	@After
	public void tearDown() throws Exception {
		wrapper.turnOff();
	}

	@Test
	public void testChangeNoReset() throws IOException {
		when(notifier.notify(alarmID)).thenReturn(false);
		when(exchange.getLastValue(pair)).thenReturn(0.004);
		testAlarm = new PriceChangeAlarm(alarmID, exchange, pair, 500, notifier, 0.001d);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		verify(notifier, after(750).never()).notify(alarmID);
		Assert.assertEquals(0, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.005);
		verify(notifier, after(500).times(1)).notify(alarmID);
		Assert.assertEquals(0.001, testAlarm.getChange(), 0);
		Assert.assertEquals(0.001, testAlarm.getLastChange(), 0);
		Assert.assertEquals(500, testAlarm.getElapsedMilis(), 100);
		Assert.assertEquals(0.005, testAlarm.getLastValue(), 0);
	}

	@Test
	public void testChangeReset() throws IOException, InterruptedException {
		when(notifier.notify(alarmID)).thenReturn(true);
		when(exchange.getLastValue(pair)).thenReturn(0.004);
		testAlarm = new PriceChangeAlarm(alarmID, exchange, pair, 500, notifier, 0.001d);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		verify(notifier, after(750).never()).notify(alarmID);
		Assert.assertEquals(0, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.005);
		verify(notifier, after(500).times(1)).notify(alarmID);
		Assert.assertEquals(0.001, testAlarm.getLastChange(), 0);
		verify(notifier, after(500).times(1)).notify(alarmID);
		Assert.assertEquals(0, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.006);
		verify(notifier, after(500).times(2)).notify(alarmID);
		Assert.assertEquals(0.001, testAlarm.getChange(), 0);
		Assert.assertEquals(0.001, testAlarm.getLastChange(), 0);
		Assert.assertEquals(500, testAlarm.getElapsedMilis(), 100);
		Assert.assertEquals(0.006, testAlarm.getLastValue(), 0);
	}

	@Test
	public void testPercentNoReset() throws IOException {
		when(notifier.notify(alarmID)).thenReturn(false);
		when(exchange.getLastValue(pair)).thenReturn(0.004);
		testAlarm = new PriceChangeAlarm(alarmID, exchange, pair, 500, notifier, 50f);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		verify(notifier, after(750).never()).notify(alarmID);
		Assert.assertEquals(0, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.006);
		verify(notifier, after(500).times(1)).notify(alarmID);
		Assert.assertEquals(0.003, testAlarm.getChange(), 0);
		Assert.assertEquals(50, testAlarm.getLastChange(), 0);
		Assert.assertEquals(500, testAlarm.getElapsedMilis(), 100);
		Assert.assertEquals(0.006, testAlarm.getLastValue(), 0);
	}

	@Test
	public void testPercentReset() throws IOException {
		when(notifier.notify(alarmID)).thenReturn(true);
		when(exchange.getLastValue(pair)).thenReturn(0.004);
		testAlarm = new PriceChangeAlarm(alarmID, exchange, pair, 500, notifier, 50f);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		verify(notifier, after(750).never()).notify(alarmID);
		Assert.assertEquals(0, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.006);
		verify(notifier, after(500).times(1)).notify(alarmID);
		Assert.assertEquals(50, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.008);
		verify(notifier, after(500).times(1)).notify(alarmID);
		Assert.assertEquals(33, testAlarm.getLastChange(), 1);
		when(exchange.getLastValue(pair)).thenReturn(0.012);
		verify(notifier, after(500).times(2)).notify(alarmID);
		Assert.assertEquals(0.006, testAlarm.getChange(), 0);
		Assert.assertEquals(50, testAlarm.getLastChange(), 0);
		Assert.assertEquals(500, testAlarm.getElapsedMilis(), 100);
		Assert.assertEquals(0.012, testAlarm.getLastValue(), 0);
	}

	@Test
	public void testGetLimits() throws NumberFormatException, IOException, InterruptedException {
		when(exchange.getLastValue(pair)).thenReturn(0.004);
		testAlarm = new PriceChangeAlarm(alarmID, exchange, pair, 500, notifier, 50f);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		Thread.sleep(250);
		Assert.assertEquals(0.006, testAlarm.getUpperLimit(), 0);
		Assert.assertEquals(0.002, testAlarm.getLowerLimit(), 0);
	}

	@Test
	public void testClearBuffer() throws NumberFormatException, IOException {
		when(notifier.notify(alarmID)).thenReturn(true);
		when(exchange.getLastValue(pair)).thenReturn(0.004);
		testAlarm = new PriceChangeAlarm(alarmID, exchange, pair, 500, notifier, 50f);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		verify(notifier, after(750).never()).notify(alarmID);
		Assert.assertEquals(0.004, testAlarm.getLastValue(), 0);
		testAlarm.setPair(new Pair("FOO", "BAR"));
		Assert.assertTrue(Double.isNaN(testAlarm.getLastValue()));
	}

	@Test
	public void testToString() throws IOException {
		when(exchange.getName()).thenReturn("DummyExchange");
		when(exchange.getLastValue(pair)).thenReturn(0.004);
		testAlarm = new PriceChangeAlarm(alarmID, exchange, pair, 1000, notifier, 50f);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		Assert.assertEquals("PriceChangeAlarm XXX YYY DummyExchange", testAlarm.toString());
	}
}
