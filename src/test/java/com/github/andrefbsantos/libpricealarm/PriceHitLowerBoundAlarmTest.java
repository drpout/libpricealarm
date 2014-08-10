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
		this.notify = mock(Notify.class);
		this.exchange = mock(Exchange.class);
		Timer timer = new Timer();
		this.testAlarm = new PriceHitLowerBoundAlarm(1, this.exchange, null, timer, 1000, this.notify, 0.0042);
	}

	@After
	public void tearDown() throws Exception {
		this.testAlarm.turnOff();
	}

	@Test
	public void testLowerBoundNoReset() throws IOException {
		when(this.notify.trigger()).thenReturn(false);
		when(this.exchange.getLastValue(null)).thenReturn(0.004123);
		verify(this.notify, timeout(1500).times(1)).trigger();
	}

	@Test
	public void testLowerBoundAndReset() throws IOException {
		when(this.notify.trigger()).thenReturn(true);
		when(this.exchange.getLastValue(null)).thenReturn(0.004123);
		verify(this.notify, timeout(2500).times(2)).trigger();
	}

	@Test
	public void testNoBoundHit() throws IOException {
		when(this.notify.trigger()).thenReturn(false);
		when(this.exchange.getLastValue(null)).thenReturn(0.0042523);
		verify(this.notify, timeout(1500).never()).trigger();
	}

}
