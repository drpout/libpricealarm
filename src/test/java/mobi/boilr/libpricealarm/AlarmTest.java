package mobi.boilr.libpricealarm;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Timer;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libpricealarm.Alarm.Direction;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AlarmTest {

	private PriceHitAlarm testAlarm;
	private TimerTaskAlarmWrapper wrapper;
	private Notify notify;
	private Exchange exchange;
	private static final int alarmID = 1;

	@Before
	public void setUp() throws Exception {
		notify = mock(Notify.class);
		exchange = mock(Exchange.class);
		Timer timer = new Timer();
		testAlarm = new PriceHitAlarm(alarmID, exchange, null, 500, notify, 20.0, 10.0);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
	}

	@After
	public void tearDown() throws Exception {
		wrapper.turnOff();
	}

	@Test
	public void testDirection() throws IOException, InterruptedException {
		when(notify.trigger(alarmID)).thenReturn(true);
		when(exchange.getLastValue(null)).thenReturn(15.0);
		Thread.sleep(750);
		Assert.assertEquals(testAlarm.getDirection(), Direction.SAME);
		when(exchange.getLastValue(null)).thenReturn(16.0);
		Thread.sleep(500);
		Assert.assertEquals(testAlarm.getDirection(), Direction.UP);
		when(exchange.getLastValue(null)).thenReturn(15.0);
		Thread.sleep(500);
		Assert.assertEquals(testAlarm.getDirection(), Direction.DOWN);
		Thread.sleep(500);
		Assert.assertEquals(testAlarm.getDirection(), Direction.SAME);
	}

}
