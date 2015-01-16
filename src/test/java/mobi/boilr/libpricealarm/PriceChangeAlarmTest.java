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

public class PriceChangeAlarmTest {

	private PriceChangeAlarm testAlarm;
	private TimerTaskAlarmWrapper wrapper;
	private Notify notify;
	private Exchange exchange;
	private Timer timer;
	private Pair pair;
	private static final int alarmID = 1;

	@Before
	public void setUp() throws Exception {
		notify = mock(Notify.class);
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
		when(notify.trigger(alarmID)).thenReturn(false);
		when(exchange.getLastValue(pair)).thenReturn(0.004);
		testAlarm = new PriceChangeAlarm(alarmID, exchange, pair, 500, notify, 0.001d);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		verify(notify, after(750).never()).trigger(alarmID);
		Assert.assertEquals(0, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.005);
		verify(notify, after(500).times(1)).trigger(alarmID);
		Assert.assertEquals(0.001, testAlarm.getLastChange(), 0);
		Assert.assertEquals(500, testAlarm.getElapsedMilis(), 100);
	}

	@Test
	public void testChangeReset() throws IOException, InterruptedException {
		when(notify.trigger(alarmID)).thenReturn(true);
		when(exchange.getLastValue(pair)).thenReturn(0.004);
		testAlarm = new PriceChangeAlarm(alarmID, exchange, pair, 500, notify, 0.001d);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		verify(notify, after(750).never()).trigger(alarmID);
		Assert.assertEquals(0, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.005);
		verify(notify, after(500).times(1)).trigger(alarmID);
		Assert.assertEquals(0.001, testAlarm.getLastChange(), 0);
		verify(notify, after(500).times(1)).trigger(alarmID);
		Assert.assertEquals(0, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.006);
		verify(notify, after(500).times(2)).trigger(alarmID);
		Assert.assertEquals(0.001, testAlarm.getLastChange(), 0);
		Assert.assertEquals(500, testAlarm.getElapsedMilis(), 100);
	}

	@Test
	public void testPercentNoReset() throws IOException {
		when(notify.trigger(alarmID)).thenReturn(false);
		when(exchange.getLastValue(pair)).thenReturn(0.004);
		testAlarm = new PriceChangeAlarm(alarmID, exchange, pair, 500, notify, 50f);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		verify(notify, after(750).never()).trigger(alarmID);
		Assert.assertEquals(0, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.006);
		verify(notify, after(500).times(1)).trigger(alarmID);
		Assert.assertEquals(50, testAlarm.getLastChange(), 0);
		Assert.assertEquals(500, testAlarm.getElapsedMilis(), 100);
	}

	@Test
	public void testPercentReset() throws IOException {
		when(notify.trigger(alarmID)).thenReturn(true);
		when(exchange.getLastValue(pair)).thenReturn(0.004);
		testAlarm = new PriceChangeAlarm(alarmID, exchange, pair, 500, notify, 50f);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		verify(notify, after(750).never()).trigger(alarmID);
		Assert.assertEquals(0, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.006);
		verify(notify, after(500).times(1)).trigger(alarmID);
		Assert.assertEquals(50, testAlarm.getLastChange(), 0);
		when(exchange.getLastValue(pair)).thenReturn(0.008);
		verify(notify, after(500).times(1)).trigger(alarmID);
		Assert.assertEquals(33, testAlarm.getLastChange(), 1);
		when(exchange.getLastValue(pair)).thenReturn(0.012);
		verify(notify, after(500).times(2)).trigger(alarmID);
		Assert.assertEquals(50, testAlarm.getLastChange(), 0);
		Assert.assertEquals(500, testAlarm.getElapsedMilis(), 100);
	}

	@Test
	public void testToString() throws IOException {
		when(exchange.getName()).thenReturn("DummyExchange");
		when(exchange.getLastValue(pair)).thenReturn(0.004);
		testAlarm = new PriceChangeAlarm(alarmID, exchange, pair, 1000, notify, 50f);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
		Assert.assertEquals("PriceChangeAlarm XXX YYY DummyExchange", testAlarm.toString());
	}
}
