package nl.topicus.eduarte.participatie.zoekfilters;

import java.util.Date;

import nl.topicus.eduarte.entities.participatie.AbsentieMelding;
import nl.topicus.eduarte.entities.participatie.Maatregel;
import nl.topicus.eduarte.entities.participatie.MaatregelToekenning;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class MaatregelToekenningZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<MaatregelToekenning>
{
	private static final long serialVersionUID = 1L;

	private IModel<Deelnemer> deelnemer;

	private IModel<Maatregel> maatregel;

	private IModel<AbsentieMelding> veroorzaaktDoor;

	private Date beginDatum;

	private Date eindDatum;

	private Date maatregelDatum;

	private Boolean nagekomen = null;

	public MaatregelToekenningZoekFilter()
	{
	}

	public Deelnemer getDeelnemer()
	{
		return getModelObject(deelnemer);
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = makeModelFor(deelnemer);
	}

	public Maatregel getMaatregel()
	{
		return getModelObject(maatregel);
	}

	public void setMaatregel(Maatregel maatregel)
	{
		this.maatregel = makeModelFor(maatregel);
	}

	public Boolean getNagekomen()
	{
		return nagekomen;
	}

	public void setNagekomen(Boolean nagekomen)
	{
		this.nagekomen = nagekomen;
	}

	public Date getBeginDatum()
	{
		return beginDatum;
	}

	public void setBeginDatum(Date beginDatum)
	{
		this.beginDatum = beginDatum;
	}

	public Date getEindDatum()
	{
		return eindDatum;
	}

	public void setEindDatum(Date eindDatum)
	{
		this.eindDatum = eindDatum;
	}

	public AbsentieMelding getVeroorzaaktDoor()
	{
		return getModelObject(veroorzaaktDoor);
	}

	public void setVeroorzaaktDoor(AbsentieMelding absentieMelding)
	{
		this.veroorzaaktDoor = makeModelFor(absentieMelding);
	}

	public Date getMaatregelDatum()
	{
		return maatregelDatum;
	}

	public void setMaatregelDatum(Date maatregelDatum)
	{
		this.maatregelDatum = maatregelDatum;
	}
}
