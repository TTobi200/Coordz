/*
 * (C) 2004 - Geotechnical Software Services
 *
 * This code is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
 * MA 02111-1307, USA.
 */
package de.coordz.lap.stream;

import java.util.stream.IntStream;

/**
 * Utility class for doing byte swapping (i.e. conversion between
 * little-endian and big-endian representations) of different data types.
 * Byte swapping is typically used when data is read from a stream
 * delivered by a system of different endian type as the present one.
 * 
 * @author tobias.ohm
 * @version 1.0
 */
public class CooByteSwapper
{
	/**
	 * Static method to convert short to little endian byte array.
	 * @param value = the short to convert
	 * @return the short as little endian
	 */
	public static byte[] toLE(short value)
	{
		return new byte[] {
				(byte)(value & 0xFF),
				(byte)((value >>> 8) & 0xFF)
		};
	}

	/**
	 * Static method to convert int to little endian byte array.
	 * @param value = the int to convert
	 * @return the int as little endian
	 */
	public static byte[] toLE(int value)
	{
		return new byte[] {
				(byte)(value & 0xFF),
				(byte)((value >>> 8) & 0xFF),
				(byte)((value >>> 16) & 0xFF),
				(byte)((value >>> 24) & 0xFF)
		};
	}

	/**
	 * Static method to convert long to little endian byte array.
	 * @param value = the long to convert
	 * @return the long as little endian
	 */
	public static byte[] toLE(long value)
	{
		return new byte[] {
				(byte)(value & 0xFF),
				(byte)((value >>> 8) & 0xFF),
				(byte)((value >>> 16) & 0xFF),
				(byte)((value >>> 24) & 0xFF),
				(byte)((value >>> 32) & 0xFF),
				(byte)((value >>> 40) & 0xFF),
				(byte)((value >>> 48) & 0xFF),
				(byte)((value >>> 56) & 0xFF)
		};
	}

	/**
	 * Static method to convert float to little endian byte array.
	 * @param value = the float to convert
	 * @return the float as little endian
	 */
	public static byte[] toLE(float value)
	{
		return toLE(Float.floatToIntBits(value));
	}

	/**
	 * Static method to convert double to little endian byte array.
	 * @param value = the double to convert
	 * @return the double as little endian
	 */
	public static byte[] toLE(double value)
	{
		return toLE(Double.doubleToLongBits(value));
	}

	/**
	 * Static method to convert char to little endian byte array.
	 * @param value = the char to convert
	 * @return the char as little endian
	 */
	public static byte[] toLE(char value)
	{
		return new byte[] {
				(byte)value
		};
	}

	/**
	 * Static method to convert {@link String} to little endian byte array.
	 * @param value = the String to convert
	 * @return the byte array as little endian
	 */
	public static byte[] toLE(String value)
	{
		char[] chars = value.toCharArray();
		byte[] b = new byte[chars.length];
		IntStream.range(0, chars.length)
//			.parallel()
			.forEach(i -> b[i] = (byte)chars[i]);

		return b;
	}

	/**
	 * Static method to byte swap a single short value.
	 * @param value Value to byte swap.
	 * @return Byte swapped representation.
	 */
	public static short swap(short value)
	{
		int b1 = value & 0xff;
		int b2 = (value >> 8) & 0xff;

		return (short)(b1 << 8 | b2 << 0);
	}

	/**
	 * Static method to byte swap a single int value.
	 * @param value Value to byte swap.
	 * @return Byte swapped representation.
	 */
	public static int swap(int value)
	{
		int b1 = (value >> 0) & 0xff;
		int b2 = (value >> 8) & 0xff;
		int b3 = (value >> 16) & 0xff;
		int b4 = (value >> 24) & 0xff;

		return b1 << 24 | b2 << 16 | b3 << 8 | b4 << 0;
	}

	/**
	 * Static method to byte swap a single long value.
	 * @param value Value to byte swap.
	 * @return Byte swapped representation.
	 */
	public static long swap(long value)
	{
		long b1 = (value >> 0) & 0xff;
		long b2 = (value >> 8) & 0xff;
		long b3 = (value >> 16) & 0xff;
		long b4 = (value >> 24) & 0xff;
		long b5 = (value >> 32) & 0xff;
		long b6 = (value >> 40) & 0xff;
		long b7 = (value >> 48) & 0xff;
		long b8 = (value >> 56) & 0xff;

		return b1 << 56 | b2 << 48 | b3 << 40 | b4 << 32 |
				b5 << 24 | b6 << 16 | b7 << 8 | b8 << 0;
	}

	/**
	 * Static method to byte swap a single float value.
	 * @param value Value to byte swap.
	 * @return Byte swapped representation.
	 */
	public static float swap(float value)
	{
		int intValue = Float.floatToIntBits(value);
		intValue = swap(intValue);
		return Float.intBitsToFloat(intValue);
	}

	/**
	 * Static method to byte swap a single double value.
	 * @param value Value to byte swap.
	 * @return Byte swapped representation.
	 */
	public static double swap(double value)
	{
		long longValue = Double.doubleToLongBits(value);
		longValue = swap(longValue);
		return Double.longBitsToDouble(longValue);
	}

	/**
	 * Static method to byte swap an array of shorts. The result 
	 * of the swapping is put back into the specified array.
	 * @param array Array of values to swap
	 */
	public static void swap(short[] array)
	{
		for(int i = 0; i < array.length; i++)
		{
			array[i] = swap(array[i]);
		}
	}

	/**
	 * Static method to byte swap an array of ints. The result 
	 * of the swapping is put back into the specified array.
	 * @param array Array of values to swap
	 */
	public static void swap(int[] array)
	{
		for(int i = 0; i < array.length; i++)
		{
			array[i] = swap(array[i]);
		}
	}

	/**
	 * Static method to byte swap an array of longs. The result 
	 * of the swapping is put back into the specified array.
	 * @param array Array of values to swap
	 */
	public static void swap(long[] array)
	{
		for(int i = 0; i < array.length; i++)
		{
			array[i] = swap(array[i]);
		}
	}

	/**
	 * Static method to byte swap an array of floats. The result 
	 * of the swapping is put back into the specified array.
	 * @param array Array of values to swap
	 */
	public static void swap(float[] array)
	{
		for(int i = 0; i < array.length; i++)
		{
			array[i] = swap(array[i]);
		}
	}

	/**
	 * Static method to byte swap an array of doubles. The result
	 * of the swapping is put back into the specified array.
	 * @param array Array of values to swap
	 */
	public static void swap(double[] array)
	{
		for(int i = 0; i < array.length; i++)
		{
			array[i] = swap(array[i]);
		}
	}
}