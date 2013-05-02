package nl.topicus.cobra.io;

import java.io.OutputStream;

/**
 * OutputStream die absoluut niks doet.
 * 
 * @author hoeve
 */
public class NoopOutputStream extends OutputStream
{
	@Override
	public void close()
	{
	}

	@Override
	public void flush()
	{
	}

	@Override
	public void write(byte[] b)
	{
	}

	@Override
	public void write(byte[] b, int i, int l)
	{
	}

	@Override
	public void write(int b)
	{
	}
}
