/*  Copyright 2013 Florian Bornkessel

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package org.lk35.api;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Florian Bornkessel
 * 
 */
public class LK35ColorHandlerImpl implements LK35ColorHandler {

	private final byte WHITE = (byte) 0x21;
	private final byte BLUE = (byte) 0x20;
	private final byte GREEN = (byte) 0x19;
	private final byte RED = (byte) 0x18;
	private final byte BRIGHTNESS = (byte) 0x23;

	private final int MAX_RGB_VALUE = 140;
	/**
	 * sleep between two commands in a series
	 */
	final int SLEEP = 8;

	/**
	 * sleep at the end of a command series
	 */
	final int SLEEP_AT_END = 7;

	private final byte CATEGORY_RGB = (byte) 0x8;
	private final byte CATEGORY_GLOBAL = (byte) 0x02;

	private OutputStream os = null;


	/**
	 * Default constructor. Uses the outputstream which is generated by the
	 * {@link LK35DeviceHandler} connect() method.
	 * 
	 * @see LK35DeviceHandler
	 * @param os
	 */
	public LK35ColorHandlerImpl(OutputStream os) {
		this.os = os;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lk35.LK35ColorHandler#setRGB(int[], int, int, int)
	 */
	@Override
	public void setRGB(List<Integer> zones, int r, int g, int b) throws IOException, InterruptedException {
		byte[] sendToLK;

		sendToLK = getMessage(zones, CATEGORY_RGB, RED, getLK35ColorValueFor8BitInput(r));
		os.write(sendToLK);
		os.flush();
		Thread.sleep(SLEEP);
		sendToLK = getMessage(zones, CATEGORY_RGB, GREEN, getLK35ColorValueFor8BitInput(g));
		os.write(sendToLK);
		os.flush();
		Thread.sleep(SLEEP);
		sendToLK = getMessage(zones, CATEGORY_RGB, BLUE, getLK35ColorValueFor8BitInput(b));
		os.write(sendToLK);
		os.flush();
		Thread.sleep(SLEEP_AT_END);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lk35.LK35ColorHandler#setHSV(int[], int, int, int)
	 */
	@Override
	public void setHSV(List<Integer> zones, int h, int s, int v) throws IOException, InterruptedException {
		Color color = Color.getHSBColor(h, s, v);
		this.setRGB(zones, color.getRed(), color.getGreen(), color.getBlue());
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lk35.LK35ColorHandler#setRGBwithWhiteChannel(int[], int, int,
	 * int, boolean)
	 */
	@Override
	public void setRGBWithWhiteChannel(List<Integer> zones, int r, int g, int b, boolean maxBrightness) throws IOException,
	InterruptedException {
		int commonValue = r;
		commonValue = g < commonValue ? g : commonValue;
		commonValue = b < commonValue ? b : commonValue;
		if (maxBrightness) {
			double correctionFactor = Math.pow((double) commonValue / 255, 3);
			commonValue = (int) (commonValue * correctionFactor);
			this.setRGB(zones, r, g, b);
		} else {
			double correctionFactor = Math.pow((double) commonValue / 255, 2);
			commonValue = (int) (commonValue * correctionFactor);
			// lowest value will be used for white channel. the difference will
			// be send to the rgb channels
			r = r - commonValue;
			g = g - commonValue;
			b = b - commonValue;
			this.setRGB(zones, r, g, b);

			// correct by this factor to preserve saturation
			commonValue = (int) (commonValue * 0.6);
		}

		// the common white will be set to the white channel
		this.setW(zones, commonValue);
	}


	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.lk35.LK35ColorHandler#setHSVwithWihiteChannel(int[], int, int,
	 * int, boolean)
	 */
	@Override
	public void setHSVwithWihiteChannel(List<Integer> zones, int h, int s, int v, boolean maxBrightness) throws IOException,
	InterruptedException {
		Color color = Color.getHSBColor(h, s, v);
		this.setRGBWithWhiteChannel(zones, color.getRed(), color.getGreen(), color.getBlue(), maxBrightness);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lk35.LK35ColorHandler#setR(int[], int)
	 */
	@Override
	public void setR(List<Integer> zones, int value) throws IOException, InterruptedException {
		os.write(this.getMessage(zones, CATEGORY_RGB, RED, getLK35ColorValueFor8BitInput(value)));
		Thread.sleep(SLEEP_AT_END);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lk35.LK35ColorHandler#setG(int[], int)
	 */
	@Override
	public void setG(List<Integer> zones, int value) throws InterruptedException, IOException {
		os.write(this.getMessage(zones, CATEGORY_RGB, GREEN, getLK35ColorValueFor8BitInput(value)));
		Thread.sleep(SLEEP_AT_END);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lk35.LK35ColorHandler#setB(int[], int)
	 */
	@Override
	public void setB(List<Integer> zones, int value) throws InterruptedException, IOException {
		os.write(this.getMessage(zones, CATEGORY_RGB, BLUE, getLK35ColorValueFor8BitInput(value)));
		Thread.sleep(SLEEP_AT_END);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lk35.LK35ColorHandler#setW(int[], int)
	 */
	@Override
	public void setW(List<Integer> zones, int value) throws InterruptedException, IOException {
		os.write(this.getMessage(zones, CATEGORY_RGB, WHITE, getLK35ColorValueFor8BitInput(value)));
		Thread.sleep(SLEEP_AT_END);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lk35.LK35ColorHandler#resetColor(int[])
	 */
	@Override
	public void resetColor(List<Integer> zones) throws IOException, InterruptedException {
		this.setRGB(zones, 0, 0, 0);
		this.setW(zones, 0);
		this.setBrightness(zones, 7);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lk35.LK35ColorHandler#togglePower(boolean)
	 */
	@Override
	public void togglePower(boolean powerState) throws IOException {
		// TODO which zone is to use for global power switch?
		final byte CHANNEL_POWER = 0x12;

		final byte ALL_OFF = (byte) 0xa9;
		final byte ALL_ON = (byte) 0xab;

		byte powerStateTo = powerState ? ALL_ON : ALL_OFF;
		os.write(this.getMessage(new ArrayList<Integer>(), CATEGORY_GLOBAL, CHANNEL_POWER, powerStateTo));
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lk35.LK35ColorHandler#togglePower(int[], boolean)
	 */
	@Override
	public void togglePower(List<Integer> zones, boolean powerState) throws IOException {
		for (int zone : zones) {
			byte zoneKey = (byte) (0x09 + zone);
			if (powerState) {
				os.write(this.getMessage(zone, CATEGORY_GLOBAL, zoneKey, 0x1c));
			} else {
				os.write(this.getMessage(zone, CATEGORY_GLOBAL, zoneKey, 0x19));
			}
		}
		// TODO this code does not match :-(
		// 55 70 c6 4f 02 01 02 0a 19 a1 aa aa 00 00 00 (Zone 1 aus)
		//
		// 55 70 c6 4f 02 01 02 0a 1c a2 aa aa 00 00 00 (Zone 1 ein)

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lk35.LK35ColorHandler#setBrightness(int)
	 */
	@Override
	public void setBrightness(List<Integer> zones, int value) throws InterruptedException, IOException {
		if (value > 7) {
			value = 7;
		}

		os.write(this.getMessage(zones, CATEGORY_RGB, BRIGHTNESS, value));
		Thread.sleep(SLEEP_AT_END);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lk35.LK35ColorHandler#toggleColorFader(int[])
	 */
	@Override
	public void toggleColorFader(List<Integer> zones) throws IOException, InterruptedException {
		final byte CHANNEL_COLOR_FADER = (byte) 0x13;
		final byte VALUE_COLOR_FADER = (byte) 0xad;

		os.write(this.getMessage(zones, CATEGORY_GLOBAL, CHANNEL_COLOR_FADER, VALUE_COLOR_FADER));
		Thread.sleep(SLEEP_AT_END);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lk35.LK35ColorHandler#speedUpColorFader(int[])
	 */
	@Override
	public void speedUpColorFader(List<Integer> zones) throws IOException, InterruptedException {
		final byte CHANNEL_SPEED_UP = (byte) 0x07;
		final byte VALUE_SPEED_UP = (byte) 0x8e;

		os.write(this.getMessage(zones, CATEGORY_GLOBAL, CHANNEL_SPEED_UP, VALUE_SPEED_UP));
		Thread.sleep(SLEEP_AT_END);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lk35.LK35ColorHandler#speedDownColorFader(int[])
	 */
	@Override
	public void speedDownColorFader(List<Integer> zones) throws IOException, InterruptedException {
		final byte CHANNEL_SPEED_DOWN = (byte) 0x06;
		final byte VALUE_SPEED_DOWN = (byte) 0x8d;

		os.write(this.getMessage(zones, CATEGORY_GLOBAL, CHANNEL_SPEED_DOWN, VALUE_SPEED_DOWN));
		Thread.sleep(SLEEP_AT_END);
	}


	/**
	 * set bit for the corresponding zonenumber. If array is empty no bit will
	 * be set.
	 * 
	 * @param zones
	 * @return
	 */
	private byte generateZoneByte(List<Integer> zones) {
		if (zones.size() == 0)
			return 0;

		byte result = 0;
		for (int currentZone : zones) {
			if (currentZone <= 0 || currentZone > 8) {
				continue;
			}
			result = (byte) (result | (1 << currentZone - 1));
		}
		return result;
	}


	/**
	 * see {@link #getMessage(List, byte, byte, int)}
	 * 
	 * @param zone
	 * @param category
	 * @param channel
	 * @param value
	 * @return
	 */
	private byte[] getMessage(int zone, byte category, byte channel, int value) {
		ArrayList<Integer> zoneArray = new ArrayList<Integer>();
		zoneArray.add(zone);
		return this.getMessage(zoneArray, category, channel, value);
	}


	/**
	 * create message for LK35.
	 * 
	 * @param zones
	 *            zones will be set in zonebit
	 * @param category
	 *            see category constants (different remote layouts are grouped
	 *            in categories)
	 * @param channel
	 *            channel or button name
	 * @param value
	 *            constant or ar range (depends on function of that channel)
	 * @return generated message, ready to send
	 */
	private byte[] getMessage(List<Integer> zones, byte category, byte channel, int value) {
		byte[] result = new byte[12];

		// remote identifier
		result[0] = 0x55;
		result[1] = 0x33;
		result[2] = 0x61;
		result[3] = 0x39;
		result[4] = 0x02;
		// zone
		result[5] = this.generateZoneByte(zones);
		// category - rgb vaules
		result[6] = category;
		// color channel
		result[7] = channel;
		// value
		result[8] = (byte) value;
		// checksum
		result[9] = (byte) (result[8] + result[7] + result[6] + result[5] + result[4]);
		// marker bytes
		result[10] = (byte) 0xaa;
		result[11] = (byte) 0xaa;

		return result;
	}


	/**
	 * converts 8 bit color value to a value between 0 and
	 * {@link #MAX_RGB_VALUE}. do not try to set higher values by yourself. the
	 * led will not get brighter and the controller will result into an
	 * inconsistent state.
	 * 
	 * @param input
	 * @return
	 */
	private int getLK35ColorValueFor8BitInput(int input) {
		if (input > 255) {
			input = 255;
		}
		if (input < 0) {
			input = 0;
		}
		return (int) (input * ((float) MAX_RGB_VALUE / 255));
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lk35.api.LK35ColorHandler#saveCurrentColor(java.util.List, int)
	 */
	@Override
	public void saveCurrentColor(List<Integer> zones, int slot) throws IOException, InterruptedException {
		// TODO Auto-generated method stub

	}
}
