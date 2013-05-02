package nl.topicus.eduarte.web.components.resultaat;

import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class ResultaatKey implements IDetachable
{
	private static final long serialVersionUID = 1L;

	private IModel<Toets> toetsModel;

	private IModel<Deelnemer> deelnemerModel;

	/**
	 * Maakt een wegwerp key. Deze key nooit in de sessie laten komen!
	 */
	public ResultaatKey(Resultaat resultaat)
	{
		toetsModel = new Model<Toets>(resultaat.getToets());
		deelnemerModel = new Model<Deelnemer>(resultaat.getDeelnemer());
	}

	/**
	 * Maakt een wegwerp key. Deze key nooit in de sessie laten komen!
	 */
	public ResultaatKey(Toets toets, Deelnemer deelnemer)
	{
		toetsModel = new Model<Toets>(toets);
		deelnemerModel = new Model<Deelnemer>(deelnemer);
	}

	public ResultaatKey(IModel<Toets> toetsModel, IModel<Deelnemer> deelnemerModel)
	{
		this.toetsModel = toetsModel;
		this.deelnemerModel = deelnemerModel;
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemerModel.getObject();
	}

	public IModel<Deelnemer> getDeelnemerModel()
	{
		return deelnemerModel;
	}

	public Toets getToets()
	{
		return toetsModel.getObject();
	}

	public IModel<Toets> getToetsModel()
	{
		return toetsModel;
	}

	@Override
	public int hashCode()
	{
		Toets toets = getToets();
		return (toets == null ? 0 : toets.hashCode()) ^ getDeelnemer().hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof ResultaatKey)
		{
			ResultaatKey other = (ResultaatKey) obj;
			return getToets().equals(other.getToets())
				&& getDeelnemer().equals(other.getDeelnemer());
		}
		return false;
	}

	@Override
	public void detach()
	{
		toetsModel.detach();
		deelnemerModel.detach();
	}

	@Override
	public String toString()
	{
		return "(" + getToets().getResultaatstructuur().getOnderwijsproduct().getCode() + ", "
			+ getToets().getCode() + ", " + getDeelnemer().getDeelnemernummer() + ")";
	}
}
