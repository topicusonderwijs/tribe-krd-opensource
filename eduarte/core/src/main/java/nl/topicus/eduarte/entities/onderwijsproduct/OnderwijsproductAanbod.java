package nl.topicus.eduarte.entities.onderwijsproduct;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppeltabel tussen onderwijsproduct en organisatie-eenheid die aangeeft op welke
 * organisatie-eenheden een onderwijsproduct aangeboden wordt.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {
	"organisatieEenheid", "locatie", "onderwijsproduct"})})
public class OnderwijsproductAanbod extends InstellingEntiteit implements
		IOrganisatieEenheidLocatieKoppelEntiteit<OnderwijsproductAanbod>
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "organisatieEenheid")
	@Index(name = "idx_ProdAanbod_orgEhd")
	private OrganisatieEenheid organisatieEenheid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "locatie")
	@Index(name = "idx_ProdAanbod_locatie")
	private Locatie locatie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "onderwijsproduct")
	@Index(name = "idx_ProdAanbod_product")
	private Onderwijsproduct onderwijsproduct;

	public OnderwijsproductAanbod()
	{
	}

	public OnderwijsproductAanbod(OrganisatieEenheid organisatieEenheid,
			Onderwijsproduct onderwijsproduct)
	{
		setOrganisatieEenheid(organisatieEenheid);
		setOnderwijsproduct(onderwijsproduct);
	}

	public OnderwijsproductAanbod(Onderwijsproduct onderwijsproduct)
	{
		setOnderwijsproduct(onderwijsproduct);
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return onderwijsproduct;
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = onderwijsproduct;
	}

	public Locatie getLocatie()
	{
		return locatie;
	}

	public void setLocatie(Locatie locatie)
	{
		this.locatie = locatie;
	}

	@Override
	public Onderwijsproduct getEntiteit()
	{
		return getOnderwijsproduct();
	}

	@Override
	public boolean isActief(Date peildatum)
	{
		return true;
	}
}
