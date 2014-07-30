package com.github.andrefbsantos.libpricealarm;

import java.util.Timer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import com.github.andrefbsantos.libdynticker.core.Exchange;

public class PriceHitAlarmTest {

	private PriceHitAlarm testAlarm;
	private Notify notify;

	@Before
	public void setUp() throws Exception {
		notify = mock(Notify.class);
		Exchange exchange = mock(Exchange.class);
		Timer timer = new Timer();
		when(exchange.getLastValue(null)).thenReturn(0.00445625);
		when(notify.trigger()).thenReturn(false);
		testAlarm = new PriceHitAlarm(exchange, null, timer, 1000, notify, 0.0043, 0.0042);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAlarm() {
	}

}
