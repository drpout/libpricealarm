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

public class PriceHitLowerBoundAlarmTest {
	
	private PriceHitLowerBoundAlarm testAlarm;
	private Notify notify;
	private Exchange exchange;

	@Before
	public void setUp() throws Exception {
		notify = mock(Notify.class);
		exchange = mock(Exchange.class);
		Timer timer = new Timer();
		testAlarm = new PriceHitLowerBoundAlarm(exchange, null, timer, 1000, notify, 0.0042);
	}

	@After
	public void tearDown() throws Exception {
		testAlarm.turnOff();
	}

	@Test
	public void testLowerBoundNoReset() throws IOException {
		when(notify.trigger()).thenReturn(false);
		when(exchange.getLastValue(null)).thenReturn(0.004123);
		verify(notify, timeout(1500).times(1)).trigger();
	}
	
	@Test
	public void testLowerBoundAndReset() throws IOException {
		when(notify.trigger()).thenReturn(true);
		when(exchange.getLastValue(null)).thenReturn(0.004123);
		verify(notify, timeout(2500).times(2)).trigger();
	}
	
	@Test
	public void testNoBoundHit() throws IOException {
		when(notify.trigger()).thenReturn(false);
		when(exchange.getLastValue(null)).thenReturn(0.0042523);
		verify(notify, timeout(1500).never()).trigger();
	}

}
