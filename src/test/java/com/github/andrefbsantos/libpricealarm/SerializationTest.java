package com.github.andrefbsantos.libpricealarm;

import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Timer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.andrefbsantos.libdynticker.bitstamp.BitstampExchange;
import com.github.andrefbsantos.libdynticker.core.Pair;

public class SerializationTest {

	private Notify notify;
	private static long exchangeExpiredPeriod = 604800000L; // One week
	private Alarm testAlarm;
	private Alarm newAlarm;

	@Before
	public void setUp() throws Exception {
		notify = mock(Notify.class);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPriceHitAlarmSerialization() throws IOException, ClassNotFoundException,
	UpperBoundSmallerThanLowerBoundException {

		testAlarm = new PriceHitAlarm(1, new BitstampExchange(exchangeExpiredPeriod), new Pair("BTC", "USD"), 1000, notify, 600, 580);
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
		newAlarm = (PriceHitAlarm) in.readObject();
		in.close();
		byteArrayInputStream.close();
		Assert.assertTrue(newAlarm instanceof PriceHitAlarm);
		Assert.assertEquals(((PriceHitAlarm) testAlarm).getLowerBound(), ((PriceHitAlarm) newAlarm).getLowerBound(), 0.1);
		Assert.assertEquals(((PriceHitAlarm) testAlarm).getUpperBound(), ((PriceHitAlarm) newAlarm).getUpperBound(), 0.1);
		Assert.assertNotNull(newAlarm);
		Assert.assertEquals(testAlarm.getClass(), newAlarm.getClass());
		Assert.assertEquals(testAlarm.getPair(), newAlarm.getPair());
		Assert.assertEquals(testAlarm.getPeriod(), newAlarm.getPeriod());
		Assert.assertEquals(testAlarm.getExchangeCode(), newAlarm.getExchangeCode());
		Assert.assertNull(newAlarm.getNotify());
		Assert.assertNull(newAlarm.getExchange());

	}

	@Test
	public void testPriceHitLowerBoundAlarmSerialization() throws IOException,
	ClassNotFoundException, UpperBoundSmallerThanLowerBoundException {
		testAlarm = new PriceHitLowerBoundAlarm(1, new BitstampExchange(exchangeExpiredPeriod), new Pair("BTC", "USD"), 1000, notify, 0.001);
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
		newAlarm = (PriceHitLowerBoundAlarm) in.readObject();
		in.close();
		byteArrayInputStream.close();
		Assert.assertTrue(newAlarm instanceof PriceHitLowerBoundAlarm);
		Assert.assertEquals(((PriceHitAlarm) testAlarm).getLowerBound(), ((PriceHitAlarm) newAlarm).getLowerBound(), 0.1);
		Assert.assertEquals(((PriceHitAlarm) testAlarm).getUpperBound(), ((PriceHitAlarm) newAlarm).getUpperBound(), 0.1);

		Assert.assertEquals(testAlarm.getClass(), newAlarm.getClass());
		Assert.assertEquals(testAlarm.getPair(), newAlarm.getPair());
		Assert.assertEquals(testAlarm.getPeriod(), newAlarm.getPeriod());
		Assert.assertEquals(testAlarm.getExchangeCode(), newAlarm.getExchangeCode());
		Assert.assertNull(newAlarm.getNotify());
		Assert.assertNull(newAlarm.getExchange());
	}

	@Test
	public void testPriceHitUpperBoundAlarmSerialization() throws IOException,
	ClassNotFoundException, UpperBoundSmallerThanLowerBoundException {
		testAlarm = new PriceHitUpperBoundAlarm(1, new BitstampExchange(exchangeExpiredPeriod), new Pair("BTC", "USD"), 1000, notify, 0.001);
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
		newAlarm = (PriceHitUpperBoundAlarm) in.readObject();
		in.close();
		byteArrayInputStream.close();

		Assert.assertNotNull(newAlarm);
		Assert.assertTrue(newAlarm instanceof PriceHitUpperBoundAlarm);
		Assert.assertEquals(((PriceHitAlarm) testAlarm).getLowerBound(), ((PriceHitAlarm) newAlarm).getLowerBound(), 0.1);
		Assert.assertEquals(((PriceHitAlarm) testAlarm).getUpperBound(), ((PriceHitAlarm) newAlarm).getUpperBound(), 0.1);

		Assert.assertEquals(testAlarm.getClass(), newAlarm.getClass());
		Assert.assertEquals(testAlarm.getPair(), newAlarm.getPair());
		Assert.assertEquals(testAlarm.getPeriod(), newAlarm.getPeriod());
		Assert.assertEquals(testAlarm.getExchangeCode(), newAlarm.getExchangeCode());
		Assert.assertNull(newAlarm.getNotify());
		Assert.assertNull(newAlarm.getExchange());
	}

	@Test
	public void testPriceVarAlarmSerialization() throws IOException, ClassNotFoundException {
		testAlarm = new PriceVarAlarm(1, new BitstampExchange(exchangeExpiredPeriod), new Pair("BTC", "USD"), 1000, notify, 50);
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
		newAlarm = (PriceVarAlarm) in.readObject();
		in.close();
		byteArrayInputStream.close();
		Assert.assertNotNull(newAlarm);
		Assert.assertTrue(newAlarm instanceof PriceVarAlarm);
		Assert.assertEquals(((PriceVarAlarm) testAlarm).getPercent(), ((PriceVarAlarm) newAlarm).getPercent(), 0);
		Assert.assertEquals(((PriceVarAlarm) testAlarm).getVariation(), ((PriceVarAlarm) newAlarm).getVariation(), 0);

		Assert.assertEquals(testAlarm.getClass(), newAlarm.getClass());
		Assert.assertEquals(testAlarm.getPair(), newAlarm.getPair());
		Assert.assertEquals(testAlarm.getPeriod(), newAlarm.getPeriod());
		Assert.assertEquals(testAlarm.getExchangeCode(), newAlarm.getExchangeCode());
		Assert.assertNull(newAlarm.getNotify());
		Assert.assertNull(newAlarm.getExchange());
	}

	@Test
	public void testTimerTaskAlarmWrapperSerialization() throws IOException, ClassNotFoundException {
		PriceVarAlarm testAlarm = new PriceVarAlarm(1, new BitstampExchange(exchangeExpiredPeriod), new Pair("BTC", "USD"), 1000, notify, 50);
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
