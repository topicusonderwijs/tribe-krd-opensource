package nl.topicus.eduarte.krd.bron.jobs;

import java.io.ByteArrayInputStream;

import nl.topicus.eduarte.krd.bron.parser.BronEntiteitRecordingFactory;
import nl.topicus.onderwijs.duo.bron.BronException;
import nl.topicus.onderwijs.duo.bron.terugkoppeling.TerugkoppelingParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class BronTerugkoppelbestandProcessor
{
	private static final Logger log =
		LoggerFactory.getLogger(BronTerugkoppelbestandProcessor.class);

	public static class BronParseResult<T>
	{
		private final T result;

		private final Exception exception;

		public BronParseResult(T result)
		{
			this.result = result;
			this.exception = null;
		}

		public BronParseResult(Exception exception)
		{
			this.result = null;
			this.exception = exception;
		}

		public T getResult()
		{
			return result;
		}

		public boolean hasError()
		{
			return exception != null;
		}

		public Exception getException()
		{
			return exception;
		}
	}

	/**
	 * Parses the contents based on the rootRecordType.
	 * 
	 * @see TerugkoppelingParser
	 */
	protected <T> BronParseResult<T> parseTerugkoppelbestand(byte[] contents,
			Class<T> rootRecordType)
	{
		try
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(contents);
			TerugkoppelingParser parser = new TerugkoppelingParser();
			parser.setCheckEmpty(false);
			parser.setCheckRequired(false);
			parser.setRecordingFactory(new BronEntiteitRecordingFactory());

			T voorlooprecord = parser.parse(bais, rootRecordType);
			return new BronParseResult<T>(voorlooprecord);
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			return new BronParseResult<T>(e);
		}
	}

	/**
	 * @return <code>true</code> als het verwerken succesvol is geweest.
	 */
	public abstract boolean processBestand(byte[] bytes) throws InterruptedException, BronException;
}