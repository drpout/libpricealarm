package mobi.boilr.libpricealarm;

import static org.mockito.Mockito.after;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Timer;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;
import mobi.boilr.libpricealarm.Alarm.Direction;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AlarmTest {

	private PriceHitAlarm testAlarm;
	private TimerTaskAlarmWrapper wrapper;
	private Notifier notifier;
	private Exchange exchange;
	private Pair pair;
	private static final int alarmID = 1;

	@Before
	public void setUp() throws Exception {
		notifier = mock(Notifier.class, Mockito.CALLS_REAL_METHODS);
		when(notifier.notify(alarmID)).thenReturn(true);
		exchange = mock(Exchange.class);
		pair = new Pair("XXX", "YYY");
		when(exchange.getLastValue(pair)).thenReturn(15.0);
		Timer timer = new Timer();
		testAlarm = new PriceHitAlarm(alarmID, exchange, pair, 500, notifier, 20.0, 10.0);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
	}

	@After
	public void tearDown() throws Exception {
		wrapper.turnOff();
	}

	@Test
	public void testDirection() throws IOException, InterruptedException {
		Thread.sleep(250);
		Assert.assertEquals(testAlarm.getDirection(), Direction.NONE);
		Thread.sleep(500);
		Assert.assertEquals(testAlarm.getDirection(), Direction.NONE);
		when(exchange.getLastValue(pair)).thenReturn(16.0);
		Thread.sleep(500);
		Assert.assertEquals(testAlarm.getDirection(), Direction.UP);
		Thread.sleep(500);
		Assert.assertEquals(testAlarm.getDirection(), Direction.UP);
		when(exchange.getLastValue(pair)).thenReturn(15.0);
		Thread.sleep(500);
		Assert.assertEquals(testAlarm.getDirection(), Direction.DOWN);
		Thread.sleep(500);
		Assert.assertEquals(testAlarm.getDirection(), Direction.DOWN);
	}

	@Test
	public void testOffNoNotification() throws NumberFormatException, IOException {
		verify(notifier, after(250).never()).notify(alarmID);
		when(exchange.getLastValue(pair)).thenReturn(21.0);
		verify(notifier, after(500).times(1)).notify(alarmID);
		testAlarm.turnOff();
		verify(notifier, after(500).times(1)).notify(alarmID);
	}
}
