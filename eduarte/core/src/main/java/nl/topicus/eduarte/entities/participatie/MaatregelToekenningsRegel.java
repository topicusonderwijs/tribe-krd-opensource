package nl.topicus.eduarte.entities.participatie;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.enums.MaatregelRegelSoort;
import nl.topicus.eduarte.entities.participatie.enums.MaatregelToekennenOp;
import nl.topicus.eduarte.entities.participatie.enums.PeriodeType;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class MaatregelToekenningsRegel extends InstellingEntiteit implements IActiefEntiteit,
		OrganisatieEenheidLocatieProvider
{
	private static final long serialVersionUID = 1L;

	/**
	 * Het aantal meldingen waarbij deze toekenningsregel aanslaat.
	 */
	@Column(nullable = false)
	private int aantalMeldingen;

	/**
	 * Het aantal 'vrije' meldingen, dwz het aantal meldingen een leerling mag hebben
	 * voordat deze regel in werking treedt.
	 */
	@Column(nullable = false)
	private int aantalVrijeMeldingen;

	@Column(nullable = false)
	private boolean actief;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "absentieReden", nullable = false)
	@Index(name = "idx_MaatregelTR_absentReden")
	private AbsentieReden absentieReden;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "maatregel", nullable = false)
	@Index(name = "idx_MaatregelTR_Maatregel")
	private Maatregel maatregel;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MaatregelRegelSoort regelsoort;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MaatregelToekennenOp maatregelToekennenOp;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PeriodeType periodeType;

	/*
	 * Moet alleen gezet worden als periodeType Laatste_x_weken is.
	 */
	@Column(nullable = true)
	private int aantalWeken;

	/*
	 * Moet alleen gezet worden als periodeType Periode is.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "periode", nullable = true)
	@Index(name = "idx_Maatregel_periode")
	private PeriodeIndeling periode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatieEenheid", nullable = false)
	@Index(name = "idx_MaatregelTR_organEenheid")
	private OrganisatieEenheid organisatieEenheid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "locatie", nullable = true)
	@Index(name = "idx_MaatregelTR_locatie")
	private Locatie locatie;

	public MaatregelToekenningsRegel()
	{
	}

	public int getAantalMeldingen()
	{
		return aantalMeldingen;
	}

	public void setAantalMeldingen(int aantalMeldingen)
	{
		this.aantalMeldingen = aantalMeldingen;
	}

	public int getAantalVrijeMeldingen()
	{
		return aantalVrijeMeldingen;
	}

	public void setAantalVrijeMeldingen(int aantalVrijeMeldingen)
	{
		this.aantalVrijeMeldingen = aantalVrijeMeldingen;
	}

	public boolean isActief()
	{
		return actief;
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}

	public AbsentieReden getAbsentieReden()
	{
		return absentieReden;
	}

	public void setAbsentieReden(AbsentieReden absentieReden)
	{
		this.absentieReden = absentieReden;
	}

	public Maatregel getMaatregel()
	{
		return maatregel;
	}

	public void setMaatregel(Maatregel maatregel)
	{
		this.maatregel = maatregel;
	}

	public MaatregelRegelSoort getRegelsoort()
	{
		return regelsoort;
	}

	public void setRegelsoort(MaatregelRegelSoort regelsoort)
	{
		this.regelsoort = regelsoort;
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;
	}

	public PeriodeType getPeriodeType()
	{
		return periodeType;
	}

	public void setPeriodeType(PeriodeType periodeType)
	{
		this.periodeType = periodeType;
	}

	public int getAantalWeken()
	{
		return aantalWeken;
	}

	public void setAantalWeken(int aantalWeken)
	{
		this.aantalWeken = aantalWeken;
	}

	public PeriodeIndeling getPeriode()
	{
		return periode;
	}

	public void setPeriode(PeriodeIndeling periode)
	{
		this.periode = periode;
	}

	public MaatregelToekennenOp getMaatregelToekennenOp()
	{
		return maatregelToekennenOp;
	}

	public void setMaatregelToekennenOp(MaatregelToekennenOp maatregelToekennenOp)
	{
		this.maatregelToekennenOp = maatregelToekennenOp;
	}

	/**
	 * @return true als de organisatie-eenheid is toegestaan, anders false
	 */
	public boolean isOrganisatieEenheidToegestaan()
	{
		return getMaatregel().getOrganisatieEenheid().isParentOf(getOrganisatieEenheid());
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
