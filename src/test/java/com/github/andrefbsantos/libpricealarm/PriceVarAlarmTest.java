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
		this.notify = mock(Notify.class);
		this.exchange = mock(Exchange.class);
		this.timer = new Timer();
	}

	@After
	public void tearDown() throws Exception {
		this.testAlarm.turnOff();
	}

	@Test
	public void testVarNoReset() throws IOException {
		when(this.notify.trigger()).thenReturn(false);
		when(this.exchange.getLastValue(null)).thenReturn(0.004);
		this.testAlarm = new PriceVarAlarm(1, this.exchange, null, this.timer, 1000, this.notify, 0.001);
		verify(this.notify, timeout(1500).never()).trigger();
		when(this.exchange.getLastValue(null)).thenReturn(0.005);
		verify(this.notify, timeout(1000).times(1)).trigger();
	}

	@Test
	public void testVarReset() throws IOException {
		when(this.notify.trigger()).thenReturn(true);
		when(this.exchange.getLastValue(null)).thenReturn(0.004);
		this.testAlarm = new PriceVarAlarm(1, this.exchange, null, this.timer, 1000, this.notify, 0.001);
		verify(this.notify, timeout(1500).never()).trigger();
		when(this.exchange.getLastValue(null)).thenReturn(0.005);
		verify(this.notify, timeout(1000).times(1)).trigger();
		verify(this.notify, timeout(1000).times(1)).trigger();
		when(this.exchange.getLastValue(null)).thenReturn(0.006);
		verify(this.notify, timeout(1000).times(2)).trigger();
	}

	@Test
	public void testPercentNoReset() throws IOException {
		when(this.notify.trigger()).thenReturn(false);
		when(this.exchange.getLastValue(null)).thenReturn(0.004);
		this.testAlarm = new PriceVarAlarm(1, this.exchange, null, this.timer, 1000, this.notify, 50);
		verify(this.notify, timeout(1500).never()).trigger();
		when(this.exchange.getLastValue(null)).thenReturn(0.006);
		verify(this.notify, timeout(1000).times(1)).trigger();
	}

	@Test
	public void testPercentReset() throws IOException {
		when(this.notify.trigger()).thenReturn(true);
		when(this.exchange.getLastValue(null)).thenReturn(0.004);
		this.testAlarm = new PriceVarAlarm(1, this.exchange, null, this.timer, 1000, this.notify, 50);
		verify(this.notify, timeout(1500).never()).trigger();
		when(this.exchange.getLastValue(null)).thenReturn(0.006);
		verify(this.notify, timeout(1000).times(1)).trigger();
		when(this.exchange.getLastValue(null)).thenReturn(0.008);
		verify(this.notify, timeout(1000).times(1)).trigger();
		when(this.exchange.getLastValue(null)).thenReturn(0.012);
		verify(this.notify, timeout(1000).times(2)).trigger();
	}
}
