package mobi.boilr.libpricealarm;

import static org.mockito.Mockito.after;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Timer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public class NotifierTest {

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
		testAlarm = new PriceHitAlarm(alarmID, exchange, pair, 500, notifier, true, 20.0, 10.0);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
	}

	@After
	public void tearDown() throws Exception {
		wrapper.turnOff();
	}

	@Test
	public void testOffNoNotification() throws NumberFormatException, IOException {
		verify(notifier, after(250).never()).notify(alarmID);
		when(exchange.getLastValue(pair)).thenReturn(21.0);
		verify(notifier, after(500).times(1)).notify(alarmID);
		testAlarm.turnOff();
		verify(notifier, after(500).times(1)).notify(alarmID);
	}

	@Test
	public void testNotDefusableNoSuppress() throws NumberFormatException, IOException {
		verify(notifier, after(250).times(1)).suppress(alarmID);
		testAlarm.setDefusable(false);
		verify(notifier, after(500).times(1)).suppress(alarmID);
		testAlarm.setDefusable(true);
		verify(notifier, after(500).times(2)).suppress(alarmID);
		when(exchange.getLastValue(pair)).thenReturn(21.0);
		verify(notifier, after(500).times(2)).suppress(alarmID);
	}
}
