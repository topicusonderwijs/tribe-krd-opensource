package nl.topicus.eduarte.rapportage.leerplicht;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.entities.personen.Deelnemer;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

public class LeerplichtRapportage implements IDetachable
{
	private static final long serialVersionUID = 1L;

	private IModel<Deelnemer> deelnemer;

	private int aantalUrenAbsent;

	private int aantalWekenAbsent;

	public LeerplichtRapportage(Deelnemer deelnemer, int aantalUrenAbsent, int aantalWekenAbsent)
	{
		this.deelnemer = ModelFactory.getModel(deelnemer);
		this.aantalUrenAbsent = aantalUrenAbsent;
		this.setAantalWekenAbsent(aantalWekenAbsent);
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer.getObject();
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = ModelFactory.getModel(deelnemer);
	}

	public void setAantalUrenAbsent(int aantalUrenAbsent)
	{
		this.aantalUrenAbsent = aantalUrenAbsent;
	}

	public int getAantalUrenAbsent()
	{
		return aantalUrenAbsent;
	}

	public void setAantalWekenAbsent(int aantalWekenAbsent)
	{
		this.aantalWekenAbsent = aantalWekenAbsent;
	}

	public int getAantalWekenAbsent()
	{
		return aantalWekenAbsent;
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(deelnemer);
	}

}
