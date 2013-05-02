package nl.topicus.eduarte.participatie.zoekfilters;

import java.util.Date;

import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.participatie.AbsentieMelding;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingSoort;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class WaarnemingZoekFilter extends AbstractOrganisatieEenheidLocatieZoekFilter<Waarneming>
{
	private static final long serialVersionUID = 1L;

	private Date beginDatumTijd;

	private Date eindDatumTijd;

	private Integer beginLesuur;

	private Integer eindLesuur;

	private IModel<Groep> groep;

	private IModel<Afspraak> afspraak;

	private IModel<Deelnemer> deelnemer;

	private WaarnemingSoort waarnemingSoort;

	private Boolean afgehandeld;

	private IModel<AbsentieMelding> absentieMelding;

	private Boolean zonderAbsentieMelding;

	private Boolean datumTijdExactGelijk;

	private Boolean zonderNvtWaarnemingen;

	private AfspraakZoekFilter afspraakZoekFilter;

	private IModel<Contract> contract;

	private boolean alleenOngeoorloofd = false;

	private IModel<Afspraak> afspraakOngelijkAan;

	private IModel<Opleiding> opleiding;

	private String achternaam;

	public WaarnemingZoekFilter()
	{
	}

	public AbsentieMelding getAbsentieMelding()
	{
		return getModelObject(absentieMelding);
	}

	public void setAbsentieMelding(AbsentieMelding absentieMelding)
	{
		this.absentieMelding = makeModelFor(absentieMelding);
	}

	public Groep getGroep()
	{
		return getModelObject(groep);
	}

	public void setGroep(Groep groep)
	{
		this.groep = makeModelFor(groep);
	}

	public Deelnemer getDeelnemer()
	{
		return getModelObject(deelnemer);
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = makeModelFor(deelnemer);
	}

	public Integer getBeginLesuur()
	{
		return beginLesuur;
	}

	public void setBeginLesuur(Integer beginLesuur)
	{
		this.beginLesuur = beginLesuur;
	}

	public Integer getEindLesuur()
	{
		return eindLesuur;
	}

	public void setEindLesuur(Integer eindLesuur)
	{
		this.eindLesuur = eindLesuur;
	}

	public Date getBeginDatumTijd()
	{
		return beginDatumTijd;
	}

	public void setBeginDatumTijd(Date beginDatumTijd)
	{
		this.beginDatumTijd = beginDatumTijd;
	}

	public Date getEindDatumTijd()
	{
		return eindDatumTijd;
	}

	public void setEindDatumTijd(Date eindDatumTijd)
	{
		this.eindDatumTijd = eindDatumTijd;
	}

	public WaarnemingSoort getWaarnemingSoort()
	{
		return waarnemingSoort;
	}

	public void setWaarnemingSoort(WaarnemingSoort waarnemingSoort)
	{
		this.waarnemingSoort = waarnemingSoort;
	}

	public Boolean getAfgehandeld()
	{
		return afgehandeld;
	}

	public void setAfgehandeld(Boolean afgehandeld)
	{
		this.afgehandeld = afgehandeld;
	}

	public Afspraak getAfspraak()
	{
		return getModelObject(afspraak);
	}

	public void setAfspraak(Afspraak afspraak)
	{
		this.afspraak = makeModelFor(afspraak);
	}

	public Afspraak getAfspraakOngelijkAan()
	{
		return getModelObject(afspraakOngelijkAan);
	}

	public void setAfspraakOngelijkAan(Afspraak afspraak)
	{
		this.afspraakOngelijkAan = makeModelFor(afspraak);
	}

	public Boolean getZonderAbsentieMelding()
	{
		return zonderAbsentieMelding;
	}

	public void setZonderAbsentieMelding(Boolean zonderAbsentieMelding)
	{
		this.zonderAbsentieMelding = zonderAbsentieMelding;
	}

	public Boolean getDatumTijdExactGelijk()
	{
		return datumTijdExactGelijk;
	}

	public void setDatumTijdExactGelijk(Boolean datumTijdExactGelijk)
	{
		this.datumTijdExactGelijk = datumTijdExactGelijk;
	}

	public Boolean getZonderNvtWaarnemingen()
	{
		return zonderNvtWaarnemingen;
	}

	public void setZonderNvtWaarnemingen(Boolean zonderNvtWaarnemingen)
	{
		this.zonderNvtWaarnemingen = zonderNvtWaarnemingen;
	}

	public AfspraakZoekFilter getAfspraakZoekFilter()
	{
		return afspraakZoekFilter;
	}

	public void setAfspraakZoekFilter(AfspraakZoekFilter afspraakZoekFilter)
	{
		this.afspraakZoekFilter = afspraakZoekFilter;
	}

	public Contract getContract()
	{
		return getModelObject(contract);
	}

	public void setContract(Contract contract)
	{
		this.contract = makeModelFor(contract);
	}

	public boolean isAlleenOngeoorloofd()
	{
		return alleenOngeoorloofd;
	}

	public void setAlleenOngeoorloofd(boolean alleenOngeoorloofd)
	{
		this.alleenOngeoorloofd = alleenOngeoorloofd;
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = makeModelFor(opleiding);
	}

	public Opleiding getOpleiding()
	{
		return getModelObject(opleiding);
	}

	public void setAchternaam(String achternaam)
	{
		this.achternaam = achternaam;
	}

	public String getAchternaam()
	{
		return achternaam;
	}
}
