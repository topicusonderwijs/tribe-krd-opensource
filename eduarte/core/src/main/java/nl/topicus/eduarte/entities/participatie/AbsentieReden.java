package nl.topicus.eduarte.entities.participatie;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.dao.participatie.helpers.AbsentieMeldingDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.AbsentieRedenDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.MaatregelToekenningsRegelDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.enums.AbsentieSoort;
import nl.topicus.eduarte.participatie.zoekfilters.AbsentieMeldingZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.MaatregelToekenningsRegelZoekFilter;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Reden voor absentiemeldingen.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class AbsentieReden extends InstellingEntiteit implements OrganisatieEenheidLocatieProvider,
		IActiefEntiteit
{
	private static final long serialVersionUID = 1L;

	/**
	 * De afkorting van deze absentiereden. Kan gebruikt worden in overzichten.
	 */
	@Column(length = 2, nullable = false)
	@AutoForm(htmlClasses = "unit_20")
	private String afkorting;

	/**
	 * Omschrijving van de absentiereden.
	 */
	@Column(length = 30, nullable = false)
	@AutoForm(htmlClasses = "unit_max")
	private String omschrijving;

	/**
	 * @see AbsentieSoort
	 */
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@AutoForm(label = "Soort")
	private AbsentieSoort absentieSoort;

	/**
	 * Is deze absentiereden wel/niet geoorloofd.
	 */
	@Column(nullable = false)
	private boolean geoorloofd;

	/**
	 * Deze boolean geeft aan of een nieuwe absentiemelding op basis van deze
	 * absentiereden wel/niet standaard op afgehandeld moet staan. De gebruiker kan altijd
	 * handmatig de afgehandeldstatus van de nieuwe melding aanpassen.
	 */
	@Column(nullable = false)
	private boolean standaardAfgehandeld;

	/**
	 * Deze boolean geeft aan of een absentiemelding wel/niet automatisch op afgehandeld
	 * gezet moet worden als er een eindatum door het systeem wordt ingevoerd
	 */
	@AutoForm(label = "Automatisch afgehandeld")
	private boolean automatichAfgehandeld;

	/**
	 * Een absentiemelding kan zonder einddatum zijn. Deze staat dan open totdat de
	 * deelnemer zich beter meldt. Een deelnemer kan maximaal 1 absentiemelding zonder
	 * einddatum hebben. Dit property geeft aan of een nieuwe absentiemelding op basis van
	 * deze reden wel/niet standaard zonder een einddatum aangemaakt moet worden.
	 */
	@Column(nullable = false)
	private boolean standaardZonderEinddatum;

	/**
	 * Moet deze reden ook ingevuld kunnen worden als waarneming
	 */
	@Column(nullable = false)
	private boolean tonenBijWaarnemingen;

	@Column(nullable = false)
	private boolean toegestaanVoorDeelnemers;

	@Column
	private boolean actief;

	/**
	 * De absentie kan per organisatie-eenheid ingericht worden, maar dit is niet
	 * verplicht. De absentieredenen die voor een deelnemer van toepassing zijn, zijn de
	 * absentieredenen die niet gekoppeld zijn aan een organisatie-eenheid, of die aan
	 * dezelfde organisatie-eenheid zijn gekoppeld als de inschrijving van de deelnemer.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatieEenheid", nullable = false)
	@Index(name = "idx_AbsentieReden_organEenh")
	@AutoForm(label = "Organisatie-eenheid", htmlClasses = "unit_max")
	private OrganisatieEenheid organisatieEenheid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "locatie", nullable = true)
	@Index(name = "idx_AbsentieReden_locatie")
	@AutoForm(htmlClasses = "unit_max")
	private Locatie locatie;

	public AbsentieReden()
	{
	}

	public String getAfkorting()
	{
		return afkorting;
	}

	public void setAfkorting(String afkorting)
	{
		this.afkorting = afkorting;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public boolean isStandaardAfgehandeld()
	{
		return standaardAfgehandeld;
	}

	public void setStandaardAfgehandeld(boolean standaardAfgehandeld)
	{
		this.standaardAfgehandeld = standaardAfgehandeld;
	}

	public boolean isStandaardZonderEinddatum()
	{
		return standaardZonderEinddatum;
	}

	public void setStandaardZonderEinddatum(boolean standaardZonderEinddatum)
	{
		this.standaardZonderEinddatum = standaardZonderEinddatum;
	}

	public boolean isGeoorloofd()
	{
		return geoorloofd;
	}

	public void setGeoorloofd(boolean geoorloofd)
	{
		this.geoorloofd = geoorloofd;
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;
	}

	public AbsentieSoort getAbsentieSoort()
	{
		return absentieSoort;
	}

	public void setAbsentieSoort(AbsentieSoort absentieSoort)
	{
		this.absentieSoort = absentieSoort;
	}

	public boolean isTonenBijWaarnemingen()
	{
		return tonenBijWaarnemingen;
	}

	public void setTonenBijWaarnemingen(boolean tonenBijWaarnemingen)
	{
		this.tonenBijWaarnemingen = tonenBijWaarnemingen;
	}

	public void setAutomatichAfgehandeld(boolean automatichAfgehandeld)
	{
		this.automatichAfgehandeld = automatichAfgehandeld;
	}

	public boolean isAutomatichAfgehandeld()
	{
		return automatichAfgehandeld;
	}

	/**
	 * @return string bijvoobeeld Z - Ziek
	 */
	public String getAfkortingOmschrijving()
	{
		String afkortingOmschrijving = getAfkorting();
		afkortingOmschrijving += " - ";
		afkortingOmschrijving += getOmschrijving();
		return afkortingOmschrijving;
	}

	/**
	 * @return true als de omschrijving al bestaat en actief is bij de meegegeven of
	 *         hogere of lagere of organisatieeenheid. anders false
	 * 
	 */
	public boolean bestaatEnActief()
	{
		AbsentieRedenDataAccessHelper helper =
			DataAccessRegistry.getHelper(AbsentieRedenDataAccessHelper.class);
		return helper.bestaatEnActief(this);
	}

	public boolean isActief()
	{
		return actief;
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}

	/**
	 * @return true als deze absentiereden gebruikt wordt bij een absentiemelding
	 */
	public boolean inGebruik()
	{
		List<AbsentieMelding> absentieMeldingList = new ArrayList<AbsentieMelding>();
		AbsentieMeldingZoekFilter meldingFilter = new AbsentieMeldingZoekFilter();
		meldingFilter.setAbsentieReden(this);
		AbsentieMeldingDataAccessHelper meldingHelper =
			DataAccessRegistry.getHelper(AbsentieMeldingDataAccessHelper.class);
		absentieMeldingList = meldingHelper.list(meldingFilter);

		List<MaatregelToekenningsRegel> maatregelToekenningsRegelList =
			new ArrayList<MaatregelToekenningsRegel>();
		MaatregelToekenningsRegelZoekFilter maatregelFilter =
			new MaatregelToekenningsRegelZoekFilter();
		maatregelFilter.setAbsentieReden(this);
		MaatregelToekenningsRegelDataAccessHelper maatregelHelper =
			DataAccessRegistry.getHelper(MaatregelToekenningsRegelDataAccessHelper.class);
		maatregelToekenningsRegelList = maatregelHelper.list(maatregelFilter);

		if (absentieMeldingList.isEmpty() && maatregelToekenningsRegelList.isEmpty())
			return false;
		return true;
	}

	public boolean isToegestaanVoorDeelnemers()
	{
		return toegestaanVoorDeelnemers;
	}

	public void setToegestaanVoorDeelnemers(boolean toegestaanVoorDeelnemers)
	{
		this.toegestaanVoorDeelnemers = toegestaanVoorDeelnemers;
	}

	public void setLocatie(Locatie locatie)
	{
		this.locatie = locatie;
	}

	public Locatie getLocatie()
	{
		return locatie;
	}
}
