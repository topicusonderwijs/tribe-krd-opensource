package nl.topicus.eduarte.zoekfilters.bpv;

import nl.topicus.eduarte.entities.bpv.BPVKandidaat;
import nl.topicus.eduarte.entities.bpv.BPVMatch;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class BPVMatchZoekFilter extends AbstractZoekFilter<BPVMatch>
{
	private static final long serialVersionUID = 1L;

	private IModel<BPVKandidaat> bpvKandidaat;

	private String codeLeerbedrijf;

	public void setBpvKandidaat(BPVKandidaat bpvKandidaat)
	{
		this.bpvKandidaat = makeModelFor(bpvKandidaat);
	}

	public BPVKandidaat getBpvKandidaat()
	{
		return getModelObject(bpvKandidaat);
	}

	public void setCodeLeerbedrijf(String codeLeerbedrijf)
	{
		this.codeLeerbedrijf = codeLeerbedrijf;
	}

	public String getCodeLeerbedrijf()
	{
		return codeLeerbedrijf;
	}

}
