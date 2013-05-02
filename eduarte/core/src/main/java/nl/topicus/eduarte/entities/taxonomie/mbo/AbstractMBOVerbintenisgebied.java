package nl.topicus.eduarte.entities.taxonomie.mbo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.StringUtil.StringConverter;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.taxonomie.MBONiveau;
import nl.topicus.eduarte.entities.taxonomie.MBOSoortOpleiding;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElementMBOLeerweg;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Abstracte superclass voor verbintenisgebieden in het MBO.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public abstract class AbstractMBOVerbintenisgebied extends Verbintenisgebied
{
	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "taxonomieElement")
	private List<TaxonomieElementMBOLeerweg> leerwegen;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private MBOSoortOpleiding soortOpleiding;

	@Column(nullable = true)
	@Index(name = "idx_TaxEl_niveau")
	@Enumerated(EnumType.STRING)
	private MBONiveau niveau;

	@Column(nullable = true, scale = 10, precision = 20)
	private BigDecimal prijsfactor;

	@Column(nullable = true)
	private Integer studiebelastingsuren;

	@Column(nullable = true)
	private Boolean wettelijkeEisen;

	@Column(nullable = true, length = 20)
	private String bronWettelijkeEisen;

	@Column(nullable = true, length = 4)
	private String brinKenniscentrum;

	@Column(nullable = true, length = 100)
	private String naamKenniscentrum;

	@Column(nullable = true, length = 20)
	private String codeCoordinatiepunt;

	/**
	 * Constructor
	 */
	public AbstractMBOVerbintenisgebied()
	{
	}

	public AbstractMBOVerbintenisgebied(EntiteitContext context)
	{
		super(context);
	}

	/**
	 * @return Returns the leerwegen.
	 */
	public List<TaxonomieElementMBOLeerweg> getLeerwegen()
	{
		if (leerwegen == null)
			leerwegen = new ArrayList<TaxonomieElementMBOLeerweg>();
		return leerwegen;
	}

	/**
	 * @param leerwegen
	 *            The leerwegen to set.
	 */
	public void setLeerwegen(List<TaxonomieElementMBOLeerweg> leerwegen)
	{
		this.leerwegen = leerwegen;
	}

	@Override
	public String getLeerwegCodes()
	{
		return StringUtil.toString(getLeerwegen(), "",
			new StringConverter<TaxonomieElementMBOLeerweg>()
			{

				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(TaxonomieElementMBOLeerweg object, int listIndex)
				{
					return object.getMboLeerweg().name();
				}

			});
	}

	/**
	 * @return Returns the soortOpleiding.
	 */
	public MBOSoortOpleiding getSoortOpleiding()
	{
		return soortOpleiding;
	}

	/**
	 * @param soortOpleiding
	 *            The soortOpleiding to set.
	 */
	public void setSoortOpleiding(MBOSoortOpleiding soortOpleiding)
	{
		this.soortOpleiding = soortOpleiding;
	}

	@Override
	public String getSoortOpleidingNaam()
	{
		return getSoortOpleiding() == null ? null : getSoortOpleiding().toString();
	}

	/**
	 * @return Returns the niveau.
	 */
	public MBONiveau getNiveau()
	{
		return niveau;
	}

	/**
	 * @param niveau
	 *            The niveau to set.
	 */
	public void setNiveau(MBONiveau niveau)
	{
		this.niveau = niveau;
	}

	@Override
	public String getNiveauNaam()
	{
		return getNiveau() == null ? null : getNiveau().toString();
	}

	@Override
	public String getNiveauNaamLC()
	{
		return getNiveau() == null ? null : getNiveau().toString().toLowerCase();
	}

	/**
	 * @return Returns the prijsfactor.
	 */
	@Override
	public BigDecimal getPrijsfactor()
	{
		return prijsfactor;
	}

	/**
	 * @param prijsfactor
	 *            The prijsfactor to set.
	 */
	public void setPrijsfactor(BigDecimal prijsfactor)
	{
		this.prijsfactor = prijsfactor;
	}

	/**
	 * @return Returns the studiebelastingsuren.
	 */
	@Override
	public Integer getStudiebelastingsuren()
	{
		return studiebelastingsuren;
	}

	/**
	 * @param studiebelastingsuren
	 *            The studiebelastingsuren to set.
	 */
	public void setStudiebelastingsuren(Integer studiebelastingsuren)
	{
		this.studiebelastingsuren = studiebelastingsuren;
	}

	/**
	 * @return Returns the wettelijkeEisen.
	 */
	public Boolean getWettelijkeEisen()
	{
		return wettelijkeEisen;
	}

	/**
	 * @param wettelijkeEisen
	 *            The wettelijkeEisen to set.
	 */
	public void setWettelijkeEisen(Boolean wettelijkeEisen)
	{
		this.wettelijkeEisen = wettelijkeEisen;
	}

	@Override
	public String getWettelijkeEisenOmschrijving()
	{
		return getWettelijkeEisen() == null ? null : getWettelijkeEisen().booleanValue() ? "Ja"
			: "Nee";
	}

	/**
	 * @return Returns the bronWettelijkeEisen.
	 */
	@Override
	public String getBronWettelijkeEisen()
	{
		return bronWettelijkeEisen;
	}

	/**
	 * @param bronWettelijkeEisen
	 *            The bronWettelijkeEisen to set.
	 */
	public void setBronWettelijkeEisen(String bronWettelijkeEisen)
	{
		this.bronWettelijkeEisen = bronWettelijkeEisen;
	}

	/**
	 * @return Returns the brinKenniscentrum.
	 */
	@Override
	public String getBrinKenniscentrum()
	{
		return brinKenniscentrum;
	}

	/**
	 * @param brinKenniscentrum
	 *            The brinKenniscentrum to set.
	 */
	public void setBrinKenniscentrum(String brinKenniscentrum)
	{
		this.brinKenniscentrum = brinKenniscentrum;
	}

	/**
	 * @return Returns the codeCoordinatiepunt.
	 */
	@Override
	public String getCodeCoordinatiepunt()
	{
		return codeCoordinatiepunt;
	}

	/**
	 * @param codeCoordinatiepunt
	 *            The codeCoordinatiepunt to set.
	 */
	public void setCodeCoordinatiepunt(String codeCoordinatiepunt)
	{
		this.codeCoordinatiepunt = codeCoordinatiepunt;
	}

	@Override
	@Exportable
	public String getNaamKenniscentrum()
	{
		return naamKenniscentrum;
	}

	public void setNaamKenniscentrum(String naamKenniscentrum)
	{
		this.naamKenniscentrum = naamKenniscentrum;
	}

}
