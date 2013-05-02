package nl.topicus.eduarte.resultaten.jobs;

import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.web.components.resultaat.ResultaatKey;

import org.apache.wicket.model.IModel;

public class ResultaatPogingKey extends ResultaatKey
{
	private static final long serialVersionUID = 1L;

	private int pogingNr;

	public ResultaatPogingKey(IModel<Toets> toetsModel, IModel<Deelnemer> deelnemerModel,
			int pogingNr)
	{
		super(toetsModel, deelnemerModel);
		this.pogingNr = pogingNr;
	}

	public int getPogingNr()
	{
		return pogingNr;
	}

	@Override
	public int hashCode()
	{
		return super.hashCode() ^ getPogingNr();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof ResultaatPogingKey)
		{
			ResultaatPogingKey other = (ResultaatPogingKey) obj;
			return super.equals(obj) && other.getPogingNr() == getPogingNr();
		}
		return false;
	}
}
