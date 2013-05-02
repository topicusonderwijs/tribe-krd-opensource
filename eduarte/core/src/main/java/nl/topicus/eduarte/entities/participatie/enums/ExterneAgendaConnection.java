package nl.topicus.eduarte.entities.participatie.enums;

import java.util.List;

import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.CacheRegion;
import nl.topicus.eduarte.entities.participatie.ExterneAgendaException;

public interface ExterneAgendaConnection
{
	public List<Afspraak> fetch(CacheRegion region) throws ExterneAgendaException;
}
