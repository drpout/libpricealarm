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

public class PriceVarAlarmTest {

	private PriceVarAlarm testAlarm;
	private Notify notify;
	private Exchange exchange;
	private Timer timer;

	@Before
	public void setUp() throws Exception {
		notify = mock(Notify.class);
		exchange = mock(Exchange.class);
		timer = new Timer();
	}

	@After
	public void tearDown() throws Exception {
		testAlarm.turnOff();
	}

	@Test
	public void testVarNoReset() throws IOException {
		when(notify.trigger()).thenReturn(false);
		when(exchange.getLastValue(null)).thenReturn(0.004);
		testAlarm = new PriceVarAlarm(exchange, null, timer, 1000, notify, 0.001);
		verify(notify, timeout(1500).never()).trigger();
		when(exchange.getLastValue(null)).thenReturn(0.005);
		verify(notify, timeout(1000).times(1)).trigger();
	}

	@Test
	public void testVarReset() throws IOException {
		when(notify.trigger()).thenReturn(true);
		when(exchange.getLastValue(null)).thenReturn(0.004);
		testAlarm = new PriceVarAlarm(exchange, null, timer, 1000, notify, 0.001);
		verify(notify, timeout(1500).never()).trigger();
		when(exchange.getLastValue(null)).thenReturn(0.005);
		verify(notify, timeout(1000).times(1)).trigger();
		verify(notify, timeout(1000).times(1)).trigger();
		when(exchange.getLastValue(null)).thenReturn(0.006);
		verify(notify, timeout(1000).times(2)).trigger();
	}

	@Test
	public void testPercentNoReset() throws IOException {
		when(notify.trigger()).thenReturn(false);
		when(exchange.getLastValue(null)).thenReturn(0.004);
		testAlarm = new PriceVarAlarm(exchange, null, timer, 1000, notify, 50);
		verify(notify, timeout(1500).never()).trigger();
		when(exchange.getLastValue(null)).thenReturn(0.006);
		verify(notify, timeout(1000).times(1)).trigger();
	}

	@Test
	public void testPercentReset() throws IOException {
		when(notify.trigger()).thenReturn(true);
		when(exchange.getLastValue(null)).thenReturn(0.004);
		testAlarm = new PriceVarAlarm(exchange, null, timer, 1000, notify, 50);
		verify(notify, timeout(1500).never()).trigger();
		when(exchange.getLastValue(null)).thenReturn(0.006);
		verify(notify, timeout(1000).times(1)).trigger();
		when(exchange.getLastValue(null)).thenReturn(0.008);
		verify(notify, timeout(1000).times(1)).trigger();
		when(exchange.getLastValue(null)).thenReturn(0.012);
		verify(notify, timeout(1000).times(2)).trigger();
	}

}
