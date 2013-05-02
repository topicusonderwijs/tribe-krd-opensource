package nl.topicus.eduarte.entities.inschrijving;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.onderwijs.duo.bron.Bron;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Afname van een onderwijsproduct door een deelnemer.
 * 
 * @author loite
 */
@Exportable
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"deelnemer", "onderwijsproduct",
	"cohort"})})
@org.hibernate.annotations.Table(appliesTo = "OnderwijsproductAfname", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
public class OnderwijsproductAfname extends BeginEinddatumInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	/**
	 * Het onderwijsproduct dat is afgenomen.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "onderwijsproduct")
	@Index(name = "idx_Afname_product")
	@Bron
	private Onderwijsproduct onderwijsproduct;

	/**
	 * Is het onderwijsproduct echt afgenomen (VrijstellingType.Geen), behaald met een EVC
	 * of vervallen. Als een onderwijsproduct als EVC is afgenomen of is vervallen, is het
	 * onderwijsproduct per definitie behaald. Het kan ook zijn dat het onderwijsproduct
	 * dan niet op de onderwijsovereenkomst opgenomen moet worden. Is het onderwijsproduct
	 * vervallen, dan tellen de bijbehorende credits niet mee en verschijnt het
	 * onderwijsproduct niet op de cijferlijst.
	 */
	@Enumerated(EnumType.STRING)
	@Column(length = 15, nullable = false)
	private VrijstellingType vrijstellingType = VrijstellingType.Geen;

	/**
	 * Titel van het werkstuk dat een deelnemer heeft gemaakt. Kan alleen ingevuld worden
	 * indien bij het onderwijsproduct dat afgenomen wordt het property heeftWerkstuktitel
	 * op true is gezet.
	 */
	@Column(nullable = true, length = 200)
	@Bron
	private String werkstuktitel;

	/**
	 * De deelnemer die het onderwijsproduct afneemt.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "deelnemer")
	@Index(name = "idx_Afname_deelnemer")
	private Deelnemer deelnemer;

	/**
	 * Een eventuele externe organisatie waar de deelnemer dit onderwijsproduct heeft
	 * afgenomen
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "externeOrganisatie")
	@Index(name = "idx_Afname_organisatie")
	private ExterneOrganisatie externeOrganisatie;

	/**
	 * Het cohort waarin de deelnemer het onderwijsproduct afneemt. Dit komt normaal
	 * gesproken overeen met het cohort waarop de deelnemer is ingeschreven, maar zou ook
	 * kunnen verschillen. Hiermee wordt bepaald welke resultatenstructuur voor de
	 * deelnemer geldt.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "cohort")
	@Index(name = "idx_Afname_cohort")
	private Cohort cohort;

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "onderwijsproductAfname")
	// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<OnderwijsproductAfnameContext> afnameContexten =
		new ArrayList<OnderwijsproductAfnameContext>();

	/**
	 * Het aantal credits dat de student verdient met het behalen van het
	 * onderwijsproduct. Indien null, dan geldt het aantal credits van het
	 * onderwijsproduct (default).
	 */
	@Column(nullable = true)
	private Integer credits;

	/**
	 * De onderwijsproduct afname kan gevuld worden met een stage, de stageinschrijving
	 * die als vulling fungeerd is gekoppeld
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "bpvInschrijving")
	@Index(name = "idx_Afname_bpvI")
	private BPVInschrijving bpvInschrijving;

	public OnderwijsproductAfname()
	{
	}

	@Exportable
	public Onderwijsproduct getOnderwijsproduct()
	{
		return onderwijsproduct;
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = onderwijsproduct;
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	public Cohort getCohort()
	{
		return cohort;
	}

	public void setCohort(Cohort cohort)
	{
		this.cohort = cohort;
	}

	public List<OnderwijsproductAfnameContext> getAfnameContexten()
	{
		return afnameContexten;
	}

	public void setAfnameContexten(List<OnderwijsproductAfnameContext> afnameContexten)
	{
		this.afnameContexten = afnameContexten;
	}

	public String getWerkstuktitel()
	{
		return werkstuktitel;
	}

	public void setWerkstuktitel(String werkstuktitel)
	{
		this.werkstuktitel = werkstuktitel;
	}

	public String getCodeTitelCohort()
	{
		return getOnderwijsproduct().getCode() + " - " + getOnderwijsproduct().getTitel() + " - "
			+ getCohort().getNaam();
	}

	/**
	 * @return de resultaatstructuur van het onderwijsproduct van deze afname in het
	 *         cohort van de afname, oftewel de resultaatstructuur die van toepassing is
	 *         voor deze onderwijsproductafname.
	 */
	public Resultaatstructuur getResultaatstructuur()
	{
		for (Resultaatstructuur structuur : getOnderwijsproduct().getResultaatstructuren())
		{
			if (structuur.getCohort().equals(getCohort()))
			{
				return structuur;
			}
		}
		return null;
	}

	public void setExterneOrganisatie(ExterneOrganisatie externeOrganisatie)
	{
		this.externeOrganisatie = externeOrganisatie;
	}

	@Exportable
	public ExterneOrganisatie getExterneOrganisatie()
	{
		return externeOrganisatie;
	}

	public void setCredits(Integer credits)
	{
		if (credits != null
			&& (getOnderwijsproduct() != null && credits.equals(getOnderwijsproduct().getCredits())))
			this.credits = null;

		this.credits = credits;

	}

	public Integer getCredits()
	{
		if (credits != null)
			return credits;

		// Onderwijsproduct is niet nullable, maar bij aanmaken van nieuwe afname niet
		// altijd direct gevuld.
		if (getOnderwijsproduct() != null)
			return getOnderwijsproduct().getCredits();

		return null;
	}

	public void setVrijstellingType(VrijstellingType vrijstellingType)
	{
		this.vrijstellingType = vrijstellingType;
	}

	public VrijstellingType getVrijstellingType()
	{
		return vrijstellingType;
	}

	public void setBpvInschrijving(BPVInschrijving bpvInschrijving)
	{
		this.bpvInschrijving = bpvInschrijving;
	}

	public BPVInschrijving getBpvInschrijving()
	{
		return bpvInschrijving;
	}

}
