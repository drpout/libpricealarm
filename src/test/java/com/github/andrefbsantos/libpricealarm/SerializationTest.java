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

	private Alarm testAlarm;
	private Alarm newAlarm;
	private Notify notify;

	@Before
	public void setUp() throws Exception {
		notify = mock(Notify.class);
	}

	@After
	public void tearDown() throws Exception {
		Assert.assertNotNull(newAlarm);
		Assert.assertEquals(testAlarm.getClass(), newAlarm.getClass());
		Assert.assertEquals(testAlarm.getPair(), newAlarm.getPair());
		Assert.assertEquals(testAlarm.getPeriod(), newAlarm.getPeriod());
		Assert.assertNotNull(newAlarm.getExchangeCode());
		Assert.assertEquals(newAlarm.getExchangeCode(), testAlarm.getExchangeCode());
		Assert.assertNull(newAlarm.getExchange());
		Assert.assertNull(newAlarm.getTimer());
	}

	@Test
	public void testPriceHitAlarmSerialization() throws IOException, ClassNotFoundException,
			UpperBoundSmallerThanLowerBoundException {

		testAlarm = new PriceHitAlarm(1, new BitstampExchange(10000), new Pair("BTC", "USD"), new Timer(), 1000, notify, 600, 580);
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
	}

	@Test
	public void testPriceHitLowerBoundAlarmSerialization() throws IOException,
	ClassNotFoundException, UpperBoundSmallerThanLowerBoundException {

		testAlarm = new PriceHitLowerBoundAlarm(1, new BitstampExchange(10000), new Pair("BTC", "USD"), new Timer(), 1000, notify, 0.001);
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
	}

	@Test
	public void testPriceHitUpperBoundAlarmSerialization() throws IOException,
	ClassNotFoundException, UpperBoundSmallerThanLowerBoundException {
		testAlarm = new PriceHitUpperBoundAlarm(1, new BitstampExchange(10000), new Pair("BTC", "USD"), new Timer(), 1000, notify, 0.001);
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

		Assert.assertTrue(newAlarm instanceof PriceHitUpperBoundAlarm);
		Assert.assertEquals(((PriceHitAlarm) testAlarm).getLowerBound(), ((PriceHitAlarm) newAlarm).getLowerBound(), 0.1);
		Assert.assertEquals(((PriceHitAlarm) testAlarm).getUpperBound(), ((PriceHitAlarm) newAlarm).getUpperBound(), 0.1);
	}

	@Test
	public void testPriceVarAlarmSerialization() throws IOException, ClassNotFoundException {
		testAlarm = new PriceVarAlarm(1, new BitstampExchange(10000), new Pair("BTC", "USD"), new Timer(), 1000, notify, 50);
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

		Assert.assertTrue(newAlarm instanceof PriceVarAlarm);
		Assert.assertEquals(((PriceVarAlarm) testAlarm).getPercent(), ((PriceVarAlarm) newAlarm).getPercent());
		Assert.assertEquals(((PriceVarAlarm) testAlarm).getVariation(), ((PriceVarAlarm) newAlarm).getVariation(), 0.001);
	}
}
