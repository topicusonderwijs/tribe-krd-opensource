package nl.topicus.eduarte.krd.entities.bron.foto.bve;

import java.util.Date;

import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestand;
import nl.topicus.onderwijs.duo.bron.bve.foto.pve_9_9.BronFotoRecordType;

public interface IBronFotoRecord
{

	public BronFotobestand getBestand();

	public BronFotoRecordType getRecordtype();

	public Date getTeldatum();

}
