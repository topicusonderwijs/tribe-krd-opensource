package nl.topicus.eduarte.krd.bron.parser;

import nl.topicus.onderwijs.duo.bron.terugkoppeling.Recording;
import nl.topicus.onderwijs.duo.bron.terugkoppeling.RecordingFactory;

public class BronEntiteitRecordingFactory implements RecordingFactory
{
	@SuppressWarnings("unchecked")
	@Override
	public <T> Recording<T> create(Class<T> clazz)
	{
		return (Recording<T>) new BronEntiteitRecording(clazz);
	}
}
