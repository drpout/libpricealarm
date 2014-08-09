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
		Alarm testAlarm = new PriceHitAlarm(new BitstampExchange(), new Pair("BTC", "USD"), new Timer(), 1000, notify, 600, 580);
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
		Alarm newAlarm = null;
		newAlarm = (Alarm) in.readObject();
		in.close();
		byteArrayInputStream.close();
		Assert.assertEquals(testAlarm.getClass(), newAlarm.getClass());
		Assert.assertEquals(testAlarm.getPair(), newAlarm.getPair());
		Assert.assertEquals(testAlarm.getPeriod(), newAlarm.getPeriod());
		Assert.assertEquals(testAlarm.getExchange().getClass(), newAlarm.getExchange().getClass());
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
		Alarm newAlarm = null;
		newAlarm = (Alarm) in.readObject();
		in.close();
		byteArrayInputStream.close();
		Assert.assertEquals(testAlarm.getClass(), newAlarm.getClass());
		Assert.assertEquals(testAlarm.getPair(), newAlarm.getPair());
		Assert.assertEquals(testAlarm.getPeriod(), newAlarm.getPeriod());
		Assert.assertEquals(testAlarm.getExchange().getClass(), newAlarm.getExchange().getClass());

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
		Alarm newAlarm = null;
		newAlarm = (Alarm) in.readObject();
		in.close();
		byteArrayInputStream.close();
		Assert.assertEquals(testAlarm.getClass(), newAlarm.getClass());
		Assert.assertEquals(testAlarm.getPair(), newAlarm.getPair());
		Assert.assertEquals(testAlarm.getPeriod(), newAlarm.getPeriod());
		Assert.assertEquals(testAlarm.getExchange().getClass(), newAlarm.getExchange().getClass());
	}

	@Test
	public void testPriceVarAlarmSerialization() throws IOException, ClassNotFoundException {
		PriceVarAlarm testAlarm = new PriceVarAlarm(new BitstampExchange(), new Pair("BTC", "USD"), new Timer(), 1000, notify, 0.001);
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
		Alarm newAlarm = null;
		newAlarm = (Alarm) in.readObject();
		in.close();
		byteArrayInputStream.close();
		Assert.assertEquals(testAlarm.getClass(), newAlarm.getClass());
		Assert.assertEquals(testAlarm.getPair(), newAlarm.getPair());
		Assert.assertEquals(testAlarm.getPeriod(), newAlarm.getPeriod());
		Assert.assertEquals(testAlarm.getExchange().getClass(), newAlarm.getExchange().getClass());
	}
}
