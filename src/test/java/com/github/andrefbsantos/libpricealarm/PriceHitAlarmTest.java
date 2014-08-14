package com.github.andrefbsantos.libpricealarm;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Timer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.andrefbsantos.libdynticker.core.Exchange;

public class PriceHitAlarmTest {

	private PriceHitAlarm testAlarm;
	private TimerTaskAlarmWrapper wrapper;
	private Notify notify;
	private Exchange exchange;

	@Before
	public void setUp() throws Exception {
		notify = mock(Notify.class);
		exchange = mock(Exchange.class);
		Timer timer = new Timer();
		testAlarm = new PriceHitAlarm(1, exchange, null, 1000, notify, 0.0043, 0.0042);
		wrapper = new TimerTaskAlarmWrapper(testAlarm, timer);
	}

	@After
	public void tearDown() throws Exception {
		wrapper.turnOff();
	}

	@Test
	public void testUpperBoundNoReset() throws IOException {
		when(notify.trigger()).thenReturn(false);
		when(exchange.getLastValue(null)).thenReturn(0.00445625);
		verify(notify, timeout(1500).times(1)).trigger();
	}

	@Test
	public void testUpperBoundAndReset() throws IOException {
		when(notify.trigger()).thenReturn(true);
		when(exchange.getLastValue(null)).thenReturn(0.00445625);
		verify(notify, timeout(2500).times(2)).trigger();
	}

	@Test
	public void testLowerBoundNoReset() throws IOException {
		when(notify.trigger()).thenReturn(false);
		when(exchange.getLastValue(null)).thenReturn(0.004123);
		verify(notify, timeout(1500).times(1)).trigger();
	}

	@Test
	public void testNoBoundHit() throws IOException {
		when(notify.trigger()).thenReturn(false);
		when(exchange.getLastValue(null)).thenReturn(0.0042523);
		verify(notify, timeout(1500).never()).trigger();
	}

}
