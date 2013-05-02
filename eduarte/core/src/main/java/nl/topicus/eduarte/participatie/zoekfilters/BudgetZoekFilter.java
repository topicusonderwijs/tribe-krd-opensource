package nl.topicus.eduarte.participatie.zoekfilters;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.participatie.Budget;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class BudgetZoekFilter extends AbstractOrganisatieEenheidLocatieZoekFilter<Budget>
{
	private static final long serialVersionUID = 1L;

	private IModel<Onderwijsproduct> onderwijsproduct;

	private IModel<Verbintenis> verbintenis;

	private Integer aantalUur;

	public BudgetZoekFilter()
	{
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return getModelObject(onderwijsproduct);
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = makeModelFor(onderwijsproduct);
	}

	public Verbintenis getVerbintenis()
	{
		return getModelObject(verbintenis);
	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = makeModelFor(verbintenis);
	}

	public void setInschrijvingModel(IModel<Verbintenis> inschrijvingModel)
	{
		this.verbintenis = inschrijvingModel;
	}

	public Integer getAantalUur()
	{
		return aantalUur;
	}

	public void setAantalUur(Integer aantalUur)
	{
		this.aantalUur = aantalUur;
	}
}
