package nl.topicus.eduarte.zoekfilters.bpv;

import java.util.Set;

import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.bpv.BPVKandidaat;
import nl.topicus.eduarte.entities.bpv.BPVKandidaat.MatchingStatus;
import nl.topicus.eduarte.entities.bpv.BPVKandidaat.MatchingType;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class BPVKandidaatZoekFilter extends AbstractZoekFilter<BPVKandidaat>
{
	private static final long serialVersionUID = 1L;

	private MatchingType matchingType;

	private Set<MatchingStatus> matchingStatussen;

	private MatchingStatus matchingStatus;

	private String leerbedrijfNaam;

	private IModel<Verbintenis> verbintenis;

	@AutoFormEmbedded
	private VerbintenisZoekFilter verbintenisFilter;

	private IModel<BPVInschrijving> bpvInschrijving;

	private IModel<Onderwijsproduct> onderwijsproduct;

	public BPVKandidaatZoekFilter()
	{
		setVerbintenisFilter(new VerbintenisZoekFilter());
	}

	public MatchingType getMatchingType()
	{
		return matchingType;
	}

	public void setMatchingType(MatchingType matchingType)
	{
		this.matchingType = matchingType;
	}

	public Verbintenis getVerbintenis()
	{
		return getModelObject(verbintenis);
	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = makeModelFor(verbintenis);
	}

	public BPVInschrijving getBpvInschrijving()
	{
		return getModelObject(bpvInschrijving);
	}

	public void setBpvInschrijving(BPVInschrijving bpvInschrijving)
	{
		this.bpvInschrijving = makeModelFor(bpvInschrijving);
	}

	public void setVerbintenisFilter(VerbintenisZoekFilter verbintenisFilter)
	{
		this.verbintenisFilter = verbintenisFilter;
	}

	public VerbintenisZoekFilter getVerbintenisFilter()
	{
		return verbintenisFilter;
	}

	public void setLeerbedrijfNaam(String leerbedrijfNaam)
	{
		this.leerbedrijfNaam = leerbedrijfNaam;
	}

	public String getLeerbedrijfNaam()
	{
		return leerbedrijfNaam;
	}

	public void setMatchingStatussen(Set<MatchingStatus> matchingStatussen)
	{
		this.matchingStatussen = matchingStatussen;
	}

	public Set<MatchingStatus> getMatchingStatussen()
	{
		return matchingStatussen;
	}

	public void setMatchingStatus(MatchingStatus matchingStatus)
	{
		this.matchingStatus = matchingStatus;
	}

	public MatchingStatus getMatchingStatus()
	{
		return matchingStatus;
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = makeModelFor(onderwijsproduct);
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return getModelObject(onderwijsproduct);
	}

}
