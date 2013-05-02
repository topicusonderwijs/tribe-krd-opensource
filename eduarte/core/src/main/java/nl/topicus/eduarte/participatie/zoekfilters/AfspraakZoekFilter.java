package nl.topicus.eduarte.participatie.zoekfilters;

import java.util.Date;
import java.util.Set;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.AfspraakType;
import nl.topicus.eduarte.entities.participatie.Basisrooster;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakTypeCategory;
import nl.topicus.eduarte.entities.participatie.enums.UitnodigingStatus;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class AfspraakZoekFilter extends AbstractOrganisatieEenheidLocatieZoekFilter<Afspraak>
{
	private static final long serialVersionUID = 1L;

	private Date beginDatumTijd;

	private Date eindDatumTijd;

	private Date afsprakenNaDezeDatumBeginTijd;

	private Date afsprakenVoorDezeDatumBeginTijd;

	private Date afsprakenNaDezeDatumEindTijd;

	private Date afsprakenVoorDezeDatumEindTijd;

	private Integer beginLesuur;

	private Integer eindLesuur;

	private Integer aantalMinutenIIVOGroterDan;

	private Boolean presentieRegistratieVerplicht = null;

	private Boolean presentieRegistratieVerwerkt = null;

	private Boolean presentieRegistratieIngevuldVoorDeelnemer = null;

	private Boolean presentiedoordeelnemer = null;

	private IModel<Deelnemer> deelnemerModel;

	private IModel<Medewerker> medewerkerModel;

	private IModel<Groep> groepModel;

	private IModel<Basisrooster> basisrooster;

	private IModel<Onderwijsproduct> onderwijsproduct;

	private String titel;

	private IModel<Opleiding> opleiding;

	private String onderwijsproductCode;

	private IModel<AfspraakType> afspraakType;

	private IModel<Contract> contract;

	private AfspraakTypeZoekFilter afspraakTypeZoekFilter;

	private Set<UitnodigingStatus> uitnodigingStatussen;

	private Set<AfspraakTypeCategory> afspraakTypeCategorie;

	private boolean medewerkerNull = false;

	private boolean ingeplandeAfspraken = true;

	public AfspraakZoekFilter()
	{
	}

	public Date getBeginDatumTijd()
	{
		return beginDatumTijd;
	}

	/**
	 * @param beginDatumTijd
	 *            Alleen te gebruiken voor het opvragen van afspraken binnen een bepaalde
	 *            tijd, bijvoorbeeld een week. Er wordt gecontroleerd of de eindDatumTijd
	 *            van een afspraak na deze beginDatumTijd ligt.
	 */
	public void setBeginDatumTijd(Date beginDatumTijd)
	{
		this.beginDatumTijd = beginDatumTijd;
	}

	public Date getEindDatum()
	{
		return getEindDatumTijd();
	}

	public void setEindDatum(Date eindDatum)
	{
		if (eindDatum == null)
			setEindDatumTijd(null);
		else
			setEindDatumTijd(TimeUtil.getInstance().maakEindeVanDagVanDatum(eindDatum));
	}

	public Date getEindDatumTijd()
	{
		return eindDatumTijd;
	}

	/**
	 * @param eindDatumTijd
	 *            Alleen te gebruiken voor het opvragen van afspraken binnen een bepaalde
	 *            tijd, bijvoorbeeld een week. Er wordt gecontroleerd of de beginDatumTijd
	 *            van een afspraak voor deze eindDatumTijd ligt.
	 */
	public void setEindDatumTijd(Date eindDatumTijd)
	{
		this.eindDatumTijd = eindDatumTijd;
	}

	public Integer getBeginLesuur()
	{
		return beginLesuur;
	}

	/**
	 * @param beginLesuur
	 *            Alleen te gebruiken voor het opvragen van afspraken binnen een bepaalde
	 *            tijd, bijvoorbeeld een week. Er wordt gecontroleerd of de eindLesuur van
	 *            een afspraak na deze beginLesuur ligt.
	 */
	public void setBeginLesuur(Integer beginLesuur)
	{
		this.beginLesuur = beginLesuur;
	}

	public Integer getEindLesuur()
	{
		return eindLesuur;
	}

	/**
	 * @param eindLesuur
	 *            Alleen te gebruiken voor het opvragen van afspraken binnen een bepaalde
	 *            tijd, bijvoorbeeld een week. Er wordt gecontroleerd of de beginLesuur
	 *            van een afspraak voor deze eindLesuur ligt.
	 */
	public void setEindLesuur(Integer eindLesuur)
	{
		this.eindLesuur = eindLesuur;
	}

	public Boolean getPresentieRegistratieVerplicht()
	{
		return presentieRegistratieVerplicht;
	}

	public void setPresentieRegistratieVerplicht(Boolean presentieRegistratieVerplicht)
	{
		this.presentieRegistratieVerplicht = presentieRegistratieVerplicht;
	}

	public Boolean getPresentieRegistratieVerwerkt()
	{
		return presentieRegistratieVerwerkt;
	}

	public void setPresentieRegistratieVerwerkt(Boolean presentieRegistratieVerwerkt)
	{
		this.presentieRegistratieVerwerkt = presentieRegistratieVerwerkt;
	}

	public Deelnemer getDeelnemer()
	{
		return getModelObject(deelnemerModel);
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		deelnemerModel = makeModelFor(deelnemer);
	}

	public Medewerker getMedewerker()
	{
		return getModelObject(medewerkerModel);
	}

	public void setMedewerker(Medewerker medewerker)
	{
		if (medewerker != null)
			setMedewerkerNull(false);
		medewerkerModel = makeModelFor(medewerker);
	}

	public IdObject getParticipantEntiteit()
	{
		if (getMedewerker() != null)
			return getMedewerker();
		return getDeelnemer();
	}

	public void setParticipantEntiteit(IdObject participant)
	{
		if (participant instanceof Medewerker)
			setMedewerker((Medewerker) participant);
		else
			setDeelnemer((Deelnemer) participant);
	}

	public Basisrooster getBasisrooster()
	{
		return getModelObject(basisrooster);
	}

	public void setBasisrooster(Basisrooster basisrooster)
	{
		this.basisrooster = makeModelFor(basisrooster);
	}

	public String getTitel()
	{
		return titel;
	}

	public void setTitel(String titel)
	{
		this.titel = titel;
	}

	public Opleiding getOpleiding()
	{
		return getModelObject(opleiding);
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = makeModelFor(opleiding);
	}

	public AfspraakTypeZoekFilter getAfspraakTypeZoekFilter()
	{
		return afspraakTypeZoekFilter;
	}

	public void setAfspraakTypeZoekFilter(AfspraakTypeZoekFilter afspraakTypeZoekFilter)
	{
		this.afspraakTypeZoekFilter = afspraakTypeZoekFilter;
	}

	public AfspraakType getAfspraakType()
	{
		return getModelObject(afspraakType);
	}

	public void setAfspraakType(AfspraakType afspraakType)
	{
		this.afspraakType = makeModelFor(afspraakType);
	}

	public Set<UitnodigingStatus> getUitnodigingStatussen()
	{
		return uitnodigingStatussen;
	}

	public void setUitnodigingStatussen(Set<UitnodigingStatus> uitnodigingStatussen)
	{
		this.uitnodigingStatussen = uitnodigingStatussen;
	}

	public void setAfspraakTypeCategorie(Set<AfspraakTypeCategory> afspraakTypeCategorie)
	{
		this.afspraakTypeCategorie = afspraakTypeCategorie;
	}

	public Set<AfspraakTypeCategory> getAfspraakTypeCategorie()
	{
		return afspraakTypeCategorie;
	}

	public void setPresentiedoordeelnemer(Boolean presentiedoordeelnemer)
	{
		this.presentiedoordeelnemer = presentiedoordeelnemer;
	}

	public Boolean getPresentiedoordeelnemer()
	{
		return presentiedoordeelnemer;
	}

	public Date getAfsprakenNaDezeDatumBeginTijd()
	{
		return afsprakenNaDezeDatumBeginTijd;
	}

	public void setAfsprakenNaDezeDatumBeginTijd(Date afsprakenNaDezeDatumBeginTijd)
	{
		this.afsprakenNaDezeDatumBeginTijd = afsprakenNaDezeDatumBeginTijd;
	}

	public Date getAfsprakenVoorDezeDatumEindTijd()
	{
		return afsprakenVoorDezeDatumEindTijd;
	}

	public void setAfsprakenVoorDezeDatumEindTijd(Date afsprakenVoorDezeDatumEindTijd)
	{
		this.afsprakenVoorDezeDatumEindTijd = afsprakenVoorDezeDatumEindTijd;
	}

	public Integer getAantalMinutenIIVOGroterDan()
	{
		return aantalMinutenIIVOGroterDan;
	}

	public void setAantalMinutenIIVOGroterDan(Integer aantalMinutenIIVOGroterDan)
	{
		this.aantalMinutenIIVOGroterDan = aantalMinutenIIVOGroterDan;
	}

	public void setOnderwijsproductCode(String onderwijsproductCode)
	{
		this.onderwijsproductCode = onderwijsproductCode;
	}

	public String getOnderwijsproductCode()
	{
		return onderwijsproductCode;
	}

	public void setPresentieRegistratieIngevuldVoorDeelnemer(
			Boolean presentieRegistratieIngevuldVoorDeelnemer)
	{
		this.presentieRegistratieIngevuldVoorDeelnemer = presentieRegistratieIngevuldVoorDeelnemer;
	}

	public Boolean getPresentieRegistratieIngevuldVoorDeelnemer()
	{
		return presentieRegistratieIngevuldVoorDeelnemer;
	}

	public Groep getGroep()
	{
		return getModelObject(groepModel);
	}

	public void setGroep(Groep groep)
	{
		this.groepModel = makeModelFor(groep);
	}

	public boolean isMedewerkerNull()
	{
		return medewerkerNull;
	}

	public void setMedewerkerNull(boolean medewerkerNull)
	{
		this.medewerkerNull = medewerkerNull;
		if (medewerkerNull)
			setMedewerker(null);
	}

	public Date getAfsprakenVoorDezeDatumBeginTijd()
	{
		return afsprakenVoorDezeDatumBeginTijd;
	}

	public void setAfsprakenVoorDezeDatumBeginTijd(Date afsprakenVoorDezeDatumBeginTijd)
	{
		this.afsprakenVoorDezeDatumBeginTijd = afsprakenVoorDezeDatumBeginTijd;
	}

	public Date getAfsprakenNaDezeDatumEindTijd()
	{
		return afsprakenNaDezeDatumEindTijd;
	}

	public void setAfsprakenNaDezeDatumEindTijd(Date afsprakenNaDezeDatumEindTijd)
	{
		this.afsprakenNaDezeDatumEindTijd = afsprakenNaDezeDatumEindTijd;
	}

	public Contract getContract()
	{
		return getModelObject(contract);
	}

	public void setContract(Contract contract)
	{
		this.contract = makeModelFor(contract);
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = makeModelFor(onderwijsproduct);
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return getModelObject(onderwijsproduct);
	}

	public void setIngeplandeAfspraken(boolean ingeplandeAfspraken)
	{
		this.ingeplandeAfspraken = ingeplandeAfspraken;
	}

	public boolean isIngeplandeAfspraken()
	{
		return ingeplandeAfspraken;
	}
}
