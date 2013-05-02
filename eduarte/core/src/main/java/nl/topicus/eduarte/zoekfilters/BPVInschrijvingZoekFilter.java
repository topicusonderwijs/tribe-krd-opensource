package nl.topicus.eduarte.zoekfilters;

import java.util.Date;
import java.util.List;

import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.PraktijkbiedendeOrganisatie;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.vrijevelden.BPVInschrijvingVrijVeld;

import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class BPVInschrijvingZoekFilter
		extends
		AbstractOrganisatieEenheidLocatieVrijVeldableZoekFilter<BPVInschrijvingVrijVeld, BPVInschrijving>
		implements IMentorDocentZoekFilter<BPVInschrijving>,
		IVerantwoordelijkeUitvoerendeZoekFilter<BPVInschrijving>
{
	private static final long serialVersionUID = 1L;

	private IModel<Verbintenis> verbintenis;

	private IModel<ExterneOrganisatie> bpvBedrijf;

	// Zoek voor dit BPV Bedrijf op alle mogelijke relaties: BPV Bedrijf, Contractpartner
	// of Ondertekening door
	private IModel<ExterneOrganisatie> bpvBedrijfAlleRelaties;

	private IModel<PraktijkbiedendeOrganisatie> bpvRelatieSoort;

	private IModel<RedenUitschrijving> redenUitschrijving;

	private BPVStatus status;

	private IModel<Medewerker> docent;

	private IModel<Deelnemer> deelnemer;

	private IModel<Medewerker> mentor;

	private IModel<Medewerker> mentorOfDocent;

	private IModel<Medewerker> verantwoordelijke;

	private IModel<Medewerker> uitvoerende;

	private IModel<Medewerker> verantwoordelijkeOfUitvoerende;

	private List<BPVStatus> bpvStatusOngelijkAanList;

	private String roepnaam;

	private String achternaam;

	private IModel<Opleiding> opleiding;

	private IModel<Groep> groep;

	public BPVInschrijvingZoekFilter()
	{
		super(BPVInschrijvingVrijVeld.class);
	}

	public Verbintenis getVerbintenis()
	{
		return getModelObject(verbintenis);
	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = makeModelFor(verbintenis);
	}

	public void setVerbintenis(IModel<Verbintenis> verbintenisModel)
	{
		this.verbintenis = verbintenisModel;
	}

	public void setBpvBedrijf(ExterneOrganisatie bpvBedrijf)
	{
		this.bpvBedrijf = makeModelFor(bpvBedrijf);
	}

	public ExterneOrganisatie getBpvBedrijf()
	{
		return getModelObject(bpvBedrijf);
	}

	public void setBpvBedrijfAlleRelaties(ExterneOrganisatie bpvBedrijfAlleRelaties)
	{
		this.bpvBedrijfAlleRelaties = makeModelFor(bpvBedrijfAlleRelaties);
	}

	public ExterneOrganisatie getBpvBedrijfAlleRelaties()
	{
		return getModelObject(bpvBedrijfAlleRelaties);
	}

	public PraktijkbiedendeOrganisatie getBpvRelatieSoort()
	{
		return getModelObject(bpvRelatieSoort);
	}

	public void setBpvRelatieSoort(PraktijkbiedendeOrganisatie bpvRelatieSoort)
	{
		this.bpvRelatieSoort = makeModelFor(bpvRelatieSoort);
	}

	public RedenUitschrijving getRedenUitschrijving()
	{
		return getModelObject(redenUitschrijving);
	}

	public void setRedenUitschrijving(RedenUitschrijving redenUitschrijving)
	{
		this.redenUitschrijving = makeModelFor(redenUitschrijving);
	}

	public BPVStatus getStatus()
	{
		return status;
	}

	public void setStatus(BPVStatus status)
	{
		this.status = status;
	}

	@Override
	public Date getPeilEindDatum()
	{
		return getPeildatum();
	}

	@Override
	public void setMentorOfDocent(Medewerker medewerker)
	{
		this.mentorOfDocent = makeModelFor(medewerker);
	}

	@Override
	public Medewerker getMentorOfDocent()
	{
		return getModelObject(mentorOfDocent);
	}

	@Override
	public Medewerker getDocent()
	{
		return getModelObject(docent);
	}

	@Override
	public void setDocent(Medewerker docent)
	{
		this.docent = makeModelFor(docent);
	}

	@Override
	public Medewerker getMentor()
	{
		return getModelObject(mentor);
	}

	@Override
	public void setMentor(Medewerker mentor)
	{
		this.mentor = makeModelFor(mentor);
	}

	@Override
	public Medewerker getVerantwoordelijke()
	{
		return getModelObject(verantwoordelijke);
	}

	@Override
	public void setVerantwoordelijke(Medewerker verantwoordelijke)
	{
		this.verantwoordelijke = makeModelFor(verantwoordelijke);
	}

	@Override
	public Medewerker getUitvoerende()
	{
		return getModelObject(uitvoerende);
	}

	@Override
	public void setUitvoerende(Medewerker uitvoerende)
	{
		this.uitvoerende = makeModelFor(uitvoerende);
	}

	public Deelnemer getDeelnemer()
	{
		return getModelObject(deelnemer);
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = makeModelFor(deelnemer);
	}

	@Override
	public Medewerker getVerantwoordelijkeOfUitvoerende()
	{
		return getModelObject(verantwoordelijkeOfUitvoerende);
	}

	@Override
	public void setVerantwoordelijkeOfUitvoerende(Medewerker verantwoordelijkeOfUitvoerende)
	{
		this.verantwoordelijkeOfUitvoerende = makeModelFor(verantwoordelijkeOfUitvoerende);
	}

	public void setBpvStatusOngelijkAanList(List<BPVStatus> bpvStatusOngelijkAanList)
	{
		this.bpvStatusOngelijkAanList = bpvStatusOngelijkAanList;
	}

	public List<BPVStatus> getBpvStatusOngelijkAanList()
	{
		return bpvStatusOngelijkAanList;
	}

	public String getRoepnaam()
	{
		return roepnaam;
	}

	public void setRoepnaam(String roepnaam)
	{
		this.roepnaam = roepnaam;
	}

	public String getAchternaam()
	{
		return achternaam;
	}

	public void setAchternaam(String achternaam)
	{
		this.achternaam = achternaam;
	}

	public Opleiding getOpleiding()
	{
		return getModelObject(opleiding);
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = makeModelFor(opleiding);
	}

	public Groep getGroep()
	{
		return getModelObject(groep);
	}

	public void setGroep(Groep groep)
	{
		this.groep = makeModelFor(groep);
	}
}
