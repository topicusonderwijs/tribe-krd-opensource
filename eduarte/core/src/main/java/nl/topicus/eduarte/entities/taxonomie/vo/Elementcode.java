package nl.topicus.eduarte.entities.taxonomie.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Elementcode is het niveau waarop leerlingen in het VO worden ingeschreven.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class Elementcode extends Verbintenisgebied
{
	private static final long serialVersionUID = 1L;

	/**
	 * Profiel voor bovenbouw havo/vwo.
	 */
	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private Profiel profiel;

	/**
	 * Sector voor bovenbouw vmbo.
	 */
	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private Sector sector;

	/**
	 * Het taxonomie-element dat overeenkomt met de LWOO-versie van dit taxonomieelement.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "lwooTaxonomieElement")
	@Index(name = "idx_TaxEl_lwooTaxEl")
	private TaxonomieElement lwooTaxonomieElement;

	/**
	 * Is dit een lwoo-taxonomie-element?
	 */
	@Column(nullable = true)
	private Boolean lwoo;

	public Elementcode()
	{
		super();
	}

	public Elementcode(EntiteitContext context)
	{
		super(context);
	}

	/**
	 * @return Returns the profiel.
	 */
	public Profiel getProfiel()
	{
		return profiel;
	}

	/**
	 * @param profiel
	 *            The profiel to set.
	 */
	public void setProfiel(Profiel profiel)
	{
		this.profiel = profiel;
	}

	/**
	 * @return Returns the sector.
	 */
	public Sector getSector()
	{
		return sector;
	}

	/**
	 * @param sector
	 *            The sector to set.
	 */
	public void setSector(Sector sector)
	{
		this.sector = sector;
	}

	/**
	 * @return Returns the lwooTaxonomieElement.
	 */
	public TaxonomieElement getLwooTaxonomieElement()
	{
		return lwooTaxonomieElement;
	}

	/**
	 * @param lwooTaxonomieElement
	 *            The lwooTaxonomieElement to set.
	 */
	public void setLwooTaxonomieElement(TaxonomieElement lwooTaxonomieElement)
	{
		this.lwooTaxonomieElement = lwooTaxonomieElement;
	}

	/**
	 * @return Returns the lwoo.
	 */
	public Boolean getLwoo()
	{
		return lwoo;
	}

	/**
	 * @param lwoo
	 *            The lwoo to set.
	 */
	public void setLwoo(Boolean lwoo)
	{
		this.lwoo = lwoo;
	}

	/**
	 * 
	 * @return true als dit een VAVO-elementcode is
	 */
	public boolean isVAVO()
	{
		return getExterneCode().startsWith("5");
	}

	@Exportable()
	@Override
	public String getProfiel1()
	{
		if (getProfiel() == null)
			return null;
		if (getProfiel().getProfiel1() == null)
			return getProfiel().getNaam();
		return getProfiel().getProfiel1().getNaam();
	}

	@Exportable()
	@Override
	public String getProfiel2()
	{
		if (getProfiel() == null)
			return null;
		if (getProfiel().getProfiel2() == null)
			return StringUtil.repeatString("x", 60);
		return getProfiel().getProfiel2().getNaam();
	}

	@Exportable()
	@Override
	public String getSectornamen()
	{
		if (getSector() == null)
			return null;
		return getSector().toString();
	}
}
