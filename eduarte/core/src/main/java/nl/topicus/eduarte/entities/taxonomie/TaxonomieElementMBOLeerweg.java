package nl.topicus.eduarte.entities.taxonomie;

import javax.persistence.*;

import nl.topicus.eduarte.entities.LandelijkEntiteit;
import nl.topicus.eduarte.entities.taxonomie.mbo.AbstractMBOVerbintenisgebied;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppeltabel tussen taxonomie-element en MBO Leerweg.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"taxonomieElement",
	"mboLeerweg"})})
public class TaxonomieElementMBOLeerweg extends LandelijkEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "taxonomieElement")
	@Index(name = "idx_TaxElMBOLW_taxEl")
	private AbstractMBOVerbintenisgebied taxonomieElement;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private MBOLeerweg mboLeerweg;

	/**
	 * Default constructor voor Hibernate.
	 */
	public TaxonomieElementMBOLeerweg()
	{
	}

	/**
	 * @return Returns the taxonomieElement.
	 */
	public AbstractMBOVerbintenisgebied getTaxonomieElement()
	{
		return taxonomieElement;
	}

	/**
	 * @param taxonomieElement
	 *            The taxonomieElement to set.
	 */
	public void setTaxonomieElement(AbstractMBOVerbintenisgebied taxonomieElement)
	{
		this.taxonomieElement = taxonomieElement;
	}

	/**
	 * @return Returns the mboLeerweg.
	 */
	public MBOLeerweg getMboLeerweg()
	{
		return mboLeerweg;
	}

	/**
	 * @param mboLeerweg
	 *            The mboLeerweg to set.
	 */
	public void setMboLeerweg(MBOLeerweg mboLeerweg)
	{
		this.mboLeerweg = mboLeerweg;
	}

}
