package nl.topicus.cobra.util;

import java.io.Flushable;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class ResourceUtilTest
{
	@Test(expected = RuntimeException.class)
	public void flushWithException()
	{
		ResourceUtil.flush(new Flushable()
		{
			@Override
			public void flush() throws IOException
			{
				throw new IOException("Een verwachte exceptie");
			}
		});
	}

	@Test
	public void flushWithNull()
	{
		ResourceUtil.flush(null);
	}

	private boolean flushed = false;

	@Test
	public void flush()
	{
		ResourceUtil.flush(new Flushable()
		{
			@Override
			public void flush()
			{
				flushed = true;
			}
		});
		Assert.assertTrue(flushed);
	}
}
