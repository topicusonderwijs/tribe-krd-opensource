package nl.topicus.eduarte.entities.productregel;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppeltabel tussen productregel en onderwijsproduct waarmee aangegeven wordt welke
 * onderwijsproducten toegestaan zijn voor een bepaalde productregel.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class ToegestaanOnderwijsproduct extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productregel", nullable = false)
	@Index(name = "idx_ToegProd_regel")
	@IgnoreInGebruik
	private Productregel productregel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "onderwijsproduct", nullable = false)
	@Index(name = "idx_ToegProd_product")
	private Onderwijsproduct onderwijsproduct;

	public ToegestaanOnderwijsproduct()
	{
	}

	public Productregel getProductregel()
	{
		return productregel;
	}

	public void setProductregel(Productregel productregel)
	{
		this.productregel = productregel;
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return onderwijsproduct;
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = onderwijsproduct;
	}
}
