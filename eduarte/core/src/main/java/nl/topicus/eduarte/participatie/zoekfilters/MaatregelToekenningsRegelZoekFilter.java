package nl.topicus.eduarte.participatie.zoekfilters;

import nl.topicus.eduarte.entities.participatie.AbsentieReden;
import nl.topicus.eduarte.entities.participatie.Maatregel;
import nl.topicus.eduarte.entities.participatie.MaatregelToekenningsRegel;
import nl.topicus.eduarte.entities.participatie.PeriodeIndeling;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class MaatregelToekenningsRegelZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<MaatregelToekenningsRegel>
{
	private static final long serialVersionUID = 1L;

	private IModel<Maatregel> maatregel;

	private IModel<AbsentieReden> absentieReden;

	private IModel<PeriodeIndeling> periodeIndeling;

	public MaatregelToekenningsRegelZoekFilter()
	{
	}

	public Maatregel getMaatregel()
	{
		return getModelObject(maatregel);
	}

	public void setMaatregel(Maatregel maatregel)
	{
		this.maatregel = makeModelFor(maatregel);
	}

	public AbsentieReden getAbsentieReden()
	{
		return getModelObject(absentieReden);
	}

	public void setAbsentieReden(AbsentieReden absentieReden)
	{
		this.absentieReden = makeModelFor(absentieReden);
	}

	public PeriodeIndeling getPeriodeIndeling()
	{
		return getModelObject(periodeIndeling);
	}

	public void setPeriodeIndeling(PeriodeIndeling periodeIndeling)
	{
		this.periodeIndeling = makeModelFor(periodeIndeling);
	}
}
