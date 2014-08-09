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
		PriceHitAlarm testAlarm = new PriceHitAlarm(new BitstampExchange(), new Pair("BTC", "USD"), new Timer(), 1000, notify, 600, 580);
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
		PriceHitAlarm newAlarm = null;
		newAlarm = (PriceHitAlarm) in.readObject();
		in.close();
		byteArrayInputStream.close();
		Assert.assertNotNull(newAlarm.getPair());
		Assert.assertNotNull(testAlarm.getPair());
		Assert.assertEquals(testAlarm.getClass(), newAlarm.getClass());
		Assert.assertEquals(testAlarm.getPair(), newAlarm.getPair());
		Assert.assertEquals(testAlarm.getPeriod(), newAlarm.getPeriod());
		Assert.assertEquals(testAlarm.getExchange().getClass(), newAlarm.getExchange().getClass());
		Assert.assertEquals(testAlarm.getLowerBound(), newAlarm.getLowerBound(), 0.1);
		Assert.assertEquals(testAlarm.getUpperBound(), newAlarm.getUpperBound(), 0.1);
		Assert.assertNull(newAlarm.getTimer());
	}

	@Test
	public void testPriceHitLowerBoundAlarmSerialization() throws IOException,
			ClassNotFoundException, UpperBoundSmallerThanLowerBoundException {
		PriceHitLowerBoundAlarm testAlarm = new PriceHitLowerBoundAlarm(new BitstampExchange(), new Pair("BTC", "USD"), new Timer(), 1000, notify, 0.001);
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
		PriceHitLowerBoundAlarm newAlarm = null;
		newAlarm = (PriceHitLowerBoundAlarm) in.readObject();
		in.close();
		byteArrayInputStream.close();
		Assert.assertNotNull(newAlarm.getPair());
		Assert.assertNotNull(testAlarm.getPair());
		Assert.assertEquals(testAlarm.getClass(), newAlarm.getClass());
		Assert.assertEquals(testAlarm.getPair(), newAlarm.getPair());
		Assert.assertEquals(testAlarm.getPeriod(), newAlarm.getPeriod());
		Assert.assertEquals(testAlarm.getExchange().getClass(), newAlarm.getExchange().getClass());
		Assert.assertEquals(testAlarm.getExchange().getClass(), newAlarm.getExchange().getClass());
		Assert.assertEquals(testAlarm.getLowerBound(), newAlarm.getLowerBound(), 0.1);
		Assert.assertEquals(testAlarm.getUpperBound(), newAlarm.getUpperBound(), 0.1);
		Assert.assertNull(newAlarm.getTimer());
	}

	@Test
	public void testPriceHitUpperBoundAlarmSerialization() throws IOException,
			ClassNotFoundException, UpperBoundSmallerThanLowerBoundException {
		PriceHitUpperBoundAlarm testAlarm = new PriceHitUpperBoundAlarm(new BitstampExchange(), new Pair("BTC", "USD"), new Timer(), 1000, notify, 0.001);
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
		PriceHitUpperBoundAlarm newAlarm = null;
		newAlarm = (PriceHitUpperBoundAlarm) in.readObject();
		in.close();
		byteArrayInputStream.close();
		Assert.assertNotNull(newAlarm.getPair());
		Assert.assertNotNull(testAlarm.getPair());
		Assert.assertEquals(testAlarm.getClass(), newAlarm.getClass());
		Assert.assertEquals(testAlarm.getPair(), newAlarm.getPair());
		Assert.assertEquals(testAlarm.getPeriod(), newAlarm.getPeriod());
		Assert.assertEquals(testAlarm.getExchange().getClass(), newAlarm.getExchange().getClass());
		Assert.assertEquals(testAlarm.getLowerBound(), newAlarm.getLowerBound(), 0.1);
		Assert.assertEquals(testAlarm.getUpperBound(), newAlarm.getUpperBound(), 0.1);
		Assert.assertNull(newAlarm.getTimer());
	}

	@Test
	public void testPriceVarAlarmSerialization() throws IOException, ClassNotFoundException {
		PriceVarAlarm testAlarm = new PriceVarAlarm(new BitstampExchange(), new Pair("BTC", "USD"), new Timer(), 1000, notify, 50);
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
		PriceVarAlarm newAlarm = null;
		newAlarm = (PriceVarAlarm) in.readObject();
		in.close();
		byteArrayInputStream.close();
		Assert.assertNotNull(newAlarm.getPair());
		Assert.assertNotNull(testAlarm.getPair());
		Assert.assertEquals(testAlarm.getClass(), newAlarm.getClass());
		Assert.assertEquals(testAlarm.getPair(), newAlarm.getPair());
		Assert.assertEquals(testAlarm.getPeriod(), newAlarm.getPeriod());
		Assert.assertEquals(testAlarm.getExchange().getClass(), newAlarm.getExchange().getClass());
		Assert.assertEquals(testAlarm.getPercent(), newAlarm.getPercent());
		Assert.assertEquals(testAlarm.getVariation(), newAlarm.getVariation(), 0.001);
		Assert.assertNull(newAlarm.getTimer());
	}
}
