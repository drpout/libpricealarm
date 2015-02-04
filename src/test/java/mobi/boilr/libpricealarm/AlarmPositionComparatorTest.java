package mobi.boilr.libpricealarm;

import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AlarmPositionComparatorTest {

	private Notifier notifier;
	private Exchange exchange;
	private Pair pair;
	private AlarmPositionComparator apc;
	private List<Alarm> alarms;

	@Before
	public void setUp() throws UpperLimitSmallerOrEqualLowerLimitException {
		notifier = mock(Notifier.class);
		exchange = mock(Exchange.class);
		pair = new Pair("XXX", "YYY");
		alarms = new ArrayList<Alarm>();
		for(int i = 9; i >= 0; i--) {
			Alarm alarm = new PriceHitAlarm(1, exchange, pair, 600000, notifier, 10000000, 10);
			alarm.setPosition(i);
			alarms.add(alarm);
		}
		apc = new AlarmPositionComparator();
		Collections.sort(alarms, apc);
	}

	public void tearDown() {

	}

	@Test
	public void testOder() {
		for(int i = 0; i < alarms.size(); i++) {
			Assert.assertEquals(i, alarms.get(i).getPosition());
		}
	}
}
