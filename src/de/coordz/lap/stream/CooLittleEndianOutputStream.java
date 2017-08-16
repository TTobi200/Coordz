package de.coordz.lap.stream;

import static de.coordz.lap.stream.CooByteSwapper.toLE;

import java.io.*;

/**
 * Class that implements an little endian output stream. It uses the
 * basic functionality of an {@link FilterOutputStream}, stores
 * the {@link #written} byte count and provides method to simply
 * write little endian data stream.
 * 
 * @author tobias.ohm
 * @version 1.0
 */
public final class CooLittleEndianOutputStream extends FilterOutputStream
{
	/** Written byte count */
	protected long written;

	/**
	 * Creates a new little endian output stream and chains it to the
	 * {@link OutputStream} specified by the out argument.
	 *
	 * @param out = the underlining {@link OutputStream}
	 * @see java.io.FilterOutputStream#out
	 */
	public CooLittleEndianOutputStream(OutputStream out)
	{
		super(out);
	}

	@Override
	public void write(int b) throws IOException
	{
		super.write(b);
		written++;
	}

	@Override
	public void write(byte[] data, int offset, int length)
		throws IOException
	{
		super.write(data, offset, length);
		written += length;
	}

	@Override
	public void write(byte[] b) throws IOException
	{
		super.write(b);
	}

	/**
	 * Method to write an boolean to this stream.
	 * @param b = the boolean to write
	 * @throws IOException when writing failed
	 */
	public void writeBoolean(boolean b) throws IOException
	{
		this.write(b ? 1 : 0);
	}

	/**
	 * Method to write an byte to this stream.
	 * @param b = the byte to write
	 * @throws IOException when writing failed
	 */
	public void writeByte(int b) throws IOException
	{
		super.write(b);
		written++;
	}

	/**
	 * Method to write an short to this stream.
	 * NOTE: Short converted to little endian.
	 * @param s = the short to write
	 * @throws IOException when writing failed
	 */
	public void writeShort(short s) throws IOException
	{
		super.write(toLE(s));
	}

	/**
	 * Method to write an char to this stream.
	 * NOTE: Char converted to little endian.
	 * @param c = the char to write
	 * @throws IOException when writing failed
	 */
	public void writeChar(char c) throws IOException
	{
		super.write(toLE(c));
	}

	/**
	 * Method to write an integer to this stream.
	 * NOTE: Integer converted to little endian.
	 * @param i = the integer to write
	 * @throws IOException when writing failed
	 */
	public void writeInt(int i) throws IOException
	{
		super.write(toLE(i));
	}

	/**
	 * Method to write an long to this stream.
	 * NOTE: Long converted to little endian.
	 * @param l = the long to write
	 * @throws IOException when writing failed
	 */
	public void writeLong(long l) throws IOException
	{
		super.write(toLE(l));
	}

	/**
	 * Method to write an float to this stream.
	 * NOTE: Float converted to little endian.
	 * @param f = the float to write
	 * @throws IOException when writing failed
	 */
	public final void writeFloat(float f) throws IOException
	{
		super.write(toLE(f));
	}

	/**
	 * Method to write an double to this stream.
	 * NOTE: Double converted to little endian.
	 * @param d = the double to write
	 * @throws IOException when writing failed
	 */
	public final void writeDouble(double d) throws IOException
	{
		super.write(toLE(d));
	}

	/**
	 * Method to write an byte to this stream.
	 * NOTE: Byte converted to little endian.
	 * @param s = the byte to write
	 * @throws IOException when writing failed
	 */
	@Deprecated
	public void writeBytes(String s) throws IOException
	{
		super.write(toLE(s));
	}

	/**
	 * Method to write an {@link String} to this stream.
	 * NOTE: {@link String} converted to little endian null
	 * terminated byte array.
	 * @param s = the {@link String} to write
	 * @throws IOException when writing failed
	 */
	public void writeString(String s) throws IOException
	{
		writeBytes(s);
		write((byte)0);
	}

	/**
	 * Method to get {@link #written} byte count.
	 * @return the {@link #written} byte count
	 */
	public long getWrittenCount()
	{
		return written;
	}

	/**
	 * Method to set the {@link #written} byte count to 
	 * provide to enable "reseting" to a previous state.
	 * @param count = the new written byte count
	 */
	public void setWrittenCount(long count)
	{
		this.written = count;
	}
}