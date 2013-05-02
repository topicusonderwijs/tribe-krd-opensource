package nl.topicus.eduarte.krd.entities.bron;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import nl.topicus.cobra.dao.DataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;

import org.hibernate.annotations.Index;

/**
 * Entiteit voor het aanmaken van een tussentabel zodat de instelling ook in die
 * tussentabel terechtkomt, en daarmee indexeerbaar wordt.
 * 
 * @author dashorst
 */
@Entity
@Table(name = "BRON_AANLEVERPUNT_LOCATIE")
public class BronAanleverpuntLocatie extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "aanleverpunt", nullable = false)
	@Index(name = "idx_BronAanlLoc_aanleverpunt")
	private BronAanleverpunt aanleverpunt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "locatie", nullable = false)
	@Index(name = "idx_BronAanlLoc_locatie")
	private Locatie locatie;

	public BronAanleverpuntLocatie()
	{

	}

	public BronAanleverpuntLocatie(BronAanleverpunt aanleverpunt, Locatie locatie)
	{
		this.aanleverpunt = aanleverpunt;
		this.locatie = locatie;
	}

	public Locatie getLocatie()
	{
		return locatie;
	}

	public BronAanleverpunt getAanleverpunt()
	{
		return aanleverpunt;
	}

	public void setLocatie(Locatie locatie)
	{
		this.locatie = locatie;
	}

	public void setAanleverpunt(BronAanleverpunt bronAanleverpunt)
	{
		this.aanleverpunt = bronAanleverpunt;
	}

	@SuppressWarnings("unchecked")
	public boolean exist()
	{
		DataAccessHelper<BronAanleverpuntLocatie> helper =
			DataAccessRegistry.getHelper(DataAccessHelper.class);
		List<BronAanleverpuntLocatie> aanleverpuntLocaties =
			helper.list(BronAanleverpuntLocatie.class);
		for (BronAanleverpuntLocatie aanleverpuntLocatie : aanleverpuntLocaties)
		{
			if (aanleverpuntLocatie.getAanleverpunt().equals(getAanleverpunt())
				&& aanleverpuntLocatie.getLocatie().equals(getLocatie()))
				return true;
		}
		return false;
	}
}
