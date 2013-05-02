package nl.topicus.eduarte.krd.zoekfilters;

import nl.topicus.eduarte.krd.entities.bron.cfi.BronCfiRegelType;
import nl.topicus.eduarte.krd.entities.bron.cfi.BronCfiTerugmelding;
import nl.topicus.eduarte.krd.entities.bron.cfi.BronCfiTerugmeldingRegel;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class BronCfiTerugmeldingRegelZoekFilter extends
		AbstractZoekFilter<BronCfiTerugmeldingRegel>
{
	private static final long serialVersionUID = 1L;

	private IModel<BronCfiTerugmelding> cfiTerugmelding;

	private BronCfiRegelType regelType;

	public BronCfiTerugmeldingRegelZoekFilter(BronCfiTerugmelding cfiTerugmelding,
			BronCfiRegelType regelType)
	{
		setCfiTerugmelding(cfiTerugmelding);
		setRegelType(regelType);
	}

	public void setCfiTerugmelding(BronCfiTerugmelding cfiTerugmelding)
	{
		this.cfiTerugmelding = makeModelFor(cfiTerugmelding);
	}

	public BronCfiTerugmelding getCfiTerugmelding()
	{
		return getModelObject(cfiTerugmelding);
	}

	public void setRegelType(BronCfiRegelType regelType)
	{
		this.regelType = regelType;
	}

	public BronCfiRegelType getRegelType()
	{
		return regelType;
	}
}
