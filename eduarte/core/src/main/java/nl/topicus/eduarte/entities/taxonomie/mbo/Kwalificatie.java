package nl.topicus.eduarte.entities.taxonomie.mbo;

import javax.persistence.Entity;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Kwalificatie in de oude MBO-structuur
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class Kwalificatie extends AbstractMBOVerbintenisgebied
{
	private static final long serialVersionUID = 1L;

	public Kwalificatie()
	{
		super();
	}

	public Kwalificatie(EntiteitContext context)
	{
		super(context);
	}

	/**
	 * Geeft aan of deze kwalificatie van LNV is of niet. Bij LNV-kwalificaties moeten ook
	 * de deelkwalificaties verstuurd worden.
	 */
	public boolean isLnv()
	{
		// LNV-kwalificaties beginnen allemaal met '11' of '12' OCW-kwalificaties beginnen
		// met
		// '10'.
		if (getExterneCode() != null && getExterneCode().startsWith("11")
			|| getExterneCode().startsWith("12"))
			return true;
		return false;
	}
}
