package de.coordz.lap.stream;

import static de.coordz.lap.stream.CooByteSwapper.toLE;

import java.io.*;

public final class CooLittleEndianOutputStream extends FilterOutputStream
{
	protected long written;

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

	public void writeBoolean(boolean b) throws IOException
	{
		if(b)
		{
			this.write(1);
		}
		else
		{
			this.write(0);
		}
	}

	public void writeByte(int b) throws IOException
	{
		super.write(b);
		written++;
	}

	public void writeShort(short s) throws IOException
	{
		super.write(toLE(s));
	}

	public void writeChar(char c) throws IOException
	{
		super.write(toLE(c));
	}

	public void writeInt(int i) throws IOException
	{
		super.write(toLE(i));
	}

	public void writeLong(long l) throws IOException
	{
		super.write(toLE(l));
	}

	public final void writeFloat(float f) throws IOException
	{
		super.write(toLE(f));
	}

	public final void writeDouble(double d) throws IOException
	{
		super.write(toLE(d));
	}

	@Deprecated
	public void writeBytes(String s) throws IOException
	{
		super.write(toLE(s));
	}

	/**
	 * Srite a string as a null terminated byte array.
	 *
	 * @param s
	 * @throws IOException
	 */
	public void writeString(String s) throws IOException
	{
		writeBytes(s);
		write((byte)0);
	}

	public long getWrittenCount()
	{
		return written;
	}

	// Method provide to enable "reseting" to a previous state.
	public void setWrittenCount(long count)
	{
		this.written = count;
	}
}
