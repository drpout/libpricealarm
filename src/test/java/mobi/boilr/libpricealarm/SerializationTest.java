package mobi.boilr.libpricealarm;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Timer;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SerializationTest {

	private Notify notify;
	private Alarm testAlarm;
	private Alarm newAlarm;
	private Exchange exchange;
	private Pair pair = new Pair("BTC", "USD");

	@Before
	public void setUp() throws Exception {
		notify = mock(Notify.class);
		exchange = mock(Exchange.class);
		when(exchange.getLastValue(pair)).thenReturn(200.0);
	}

	@After
	public void tearDown() throws Exception {
	}

	private void serializeAndDeserialize() throws IOException, ClassNotFoundException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = null;
		objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		objectOutputStream.writeObject(testAlarm);
		byte[] bytes = byteArrayOutputStream.toByteArray();
		objectOutputStream.close();
		byteArrayOutputStream.close();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		ObjectInputStream in = null;
		in = new ObjectInputStream(byteArrayInputStream);
		newAlarm = (Alarm) in.readObject();
		in.close();
		byteArrayInputStream.close();
	}

	private void checkAlarmIntegrity() {
		Assert.assertNotNull(newAlarm);
		Assert.assertEquals(testAlarm.getClass(), newAlarm.getClass());
		Assert.assertEquals(testAlarm.getPair(), newAlarm.getPair());
		Assert.assertEquals(testAlarm.getPeriod(), newAlarm.getPeriod());
		Assert.assertEquals(testAlarm.getExchangeCode(), newAlarm.getExchangeCode());
		Assert.assertNotNull(testAlarm.getNotify());
		Assert.assertNull(newAlarm.getExchange());
	}

	private void checkPriceHitAlarmIntegrity() {
		PriceHitAlarm testHitAlarm = (PriceHitAlarm) testAlarm;
		PriceHitAlarm newHitAlarm = (PriceHitAlarm) newAlarm;
		Assert.assertEquals(testHitAlarm.getLowerLimit(), newHitAlarm.getLowerLimit(), 0.1);
		Assert.assertEquals(testHitAlarm.getUpperLimit(), newHitAlarm.getUpperLimit(), 0.1);
	}

	@Test
	public void testPriceHitAlarmSerialization() throws IOException, ClassNotFoundException, UpperLimitSmallerOrEqualLowerLimitException {
		testAlarm = new PriceHitAlarm(1, exchange, pair, 1000, notify, 600, 580);
		serializeAndDeserialize();
		checkAlarmIntegrity();
		checkPriceHitAlarmIntegrity();
	}

	@Test
	public void testPriceHitLowerLimitAlarmSerialization() throws IOException,
			ClassNotFoundException, UpperLimitSmallerOrEqualLowerLimitException {
		testAlarm = new PriceHitLowerLimitAlarm(1, exchange, pair, 1000, notify, 0.001);
		serializeAndDeserialize();
		checkAlarmIntegrity();
		checkPriceHitAlarmIntegrity();
	}

	@Test
	public void testPriceHitUpperLimitAlarmSerialization() throws IOException,
			ClassNotFoundException, UpperLimitSmallerOrEqualLowerLimitException {
		testAlarm = new PriceHitUpperLimitAlarm(1, exchange, pair, 1000, notify, 0.001);
		serializeAndDeserialize();
		checkAlarmIntegrity();
		checkPriceHitAlarmIntegrity();
	}

	private void checkPriceChangeAlarmIntegrity() {
		PriceChangeAlarm testChangeAlarm = (PriceChangeAlarm) testAlarm;
		PriceChangeAlarm newChangeAlarm = (PriceChangeAlarm) newAlarm;
		Assert.assertEquals(testChangeAlarm.getPercent(), newChangeAlarm.getPercent(), 0);
		Assert.assertEquals(testChangeAlarm.getChange(), newChangeAlarm.getChange(), 0);
		Assert.assertEquals(testChangeAlarm.getLastChange(), newChangeAlarm.getLastChange(), 0.1);
		Assert.assertEquals(testChangeAlarm.getElapsedMilis(), newChangeAlarm.getElapsedMilis(), 0);
	}

	@Test
	public void testPriceChangeAlarmSerialization() throws IOException, ClassNotFoundException {
		testAlarm = new PriceChangeAlarm(1, exchange, pair, 1000, notify, 50);
		serializeAndDeserialize();
		checkAlarmIntegrity();
		checkPriceChangeAlarmIntegrity();
	}

	@Test
	public void testPriceSpikeAlarmSerialization() throws IOException, ClassNotFoundException {
		testAlarm = new PriceSpikeAlarm(1, exchange, pair, 1000, notify, 50, 600000);
		serializeAndDeserialize();
		checkPriceChangeAlarmIntegrity();
		Assert.assertEquals(((PriceSpikeAlarm) testAlarm).getTimeFrame(), ((PriceSpikeAlarm) newAlarm).getTimeFrame(), 0);
	}

	@Test
	public void testTimerTaskAlarmWrapperSerialization() throws IOException, ClassNotFoundException {
		PriceChangeAlarm testAlarm = new PriceChangeAlarm(1, exchange, pair, 1000, notify, 50);
		TimerTaskAlarmWrapper wrapper = new TimerTaskAlarmWrapper(testAlarm, new Timer());
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = null;
		objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		objectOutputStream.writeObject(wrapper);
		byte[] bytes = byteArrayOutputStream.toByteArray();
		objectOutputStream.close();
		byteArrayOutputStream.close();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		ObjectInputStream in = null;
		in = new ObjectInputStream(byteArrayInputStream);
		TimerTaskAlarmWrapper newWrapper = null;
		newWrapper = (TimerTaskAlarmWrapper) in.readObject();
		in.close();
		byteArrayInputStream.close();
		Assert.assertEquals(wrapper.getClass(), newWrapper.getClass());
		Assert.assertEquals(wrapper.getAlarm().getClass(), testAlarm.getClass());
		Assert.assertNull(newWrapper.getTimer());
	}
}
