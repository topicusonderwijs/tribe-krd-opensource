package nl.topicus.eduarte.app.resultaat;

import java.io.Serializable;

import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.DeelnemerResultaatVersie;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;

public class ResultaatVersionKey implements Serializable, Comparable<ResultaatVersionKey>
{
	private static final long serialVersionUID = 1L;

	private long structuurId;

	private long deelnemerId;

	public ResultaatVersionKey(Deelnemer deelnemer, Resultaatstructuur structuur)
	{
		this.structuurId = structuur.getId();
		this.deelnemerId = deelnemer.getId();
	}

	public ResultaatVersionKey(DeelnemerResultaatVersie versie)
	{
		structuurId = versie.getResultaatstructuur().getId();
		deelnemerId = versie.getDeelnemer().getId();
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof ResultaatVersionKey)
		{
			ResultaatVersionKey other = (ResultaatVersionKey) o;
			return other.structuurId == structuurId && other.deelnemerId == deelnemerId;
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return Long.valueOf(structuurId).hashCode() ^ Long.valueOf(deelnemerId).hashCode();
	}

	public long getStructuurId()
	{
		return structuurId;
	}

	public long getDeelnemerId()
	{
		return deelnemerId;
	}

	@Override
	public int compareTo(ResultaatVersionKey o)
	{
		int ret = (int) Math.signum(deelnemerId - o.deelnemerId);
		return ret == 0 ? (int) Math.signum(structuurId - o.structuurId) : ret;
	}
}
