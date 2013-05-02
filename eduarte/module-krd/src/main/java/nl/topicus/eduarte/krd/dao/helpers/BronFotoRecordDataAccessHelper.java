package nl.topicus.eduarte.krd.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.ZoekFilterDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestand;
import nl.topicus.eduarte.krd.entities.bron.foto.bve.BronFotoRecord;
import nl.topicus.eduarte.krd.entities.bron.foto.bve.IBronFotoOnderwijsontvangendeRecord;
import nl.topicus.eduarte.krd.zoekfilters.BronFotoRecordZoekFilter;

public interface BronFotoRecordDataAccessHelper extends
		ZoekFilterDataAccessHelper<BronFotoRecord, BronFotoRecordZoekFilter>
{
	@Override
	public List<Long> listIds(BronFotoRecordZoekFilter filter);

	public IBronFotoOnderwijsontvangendeRecord getOnderwijsontvangendeRecord(
			BronFotobestand fotobestand, Long pgn,
			Class< ? extends IBronFotoOnderwijsontvangendeRecord> clazz);

	public List<BronFotoRecord> getAlleBronFotoRecords(BronFotobestand fotobestand);

}
