package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.InstellingsLogo;

/**
 * @author vandekamp
 */
public interface InstellingsLogoDataAccessHelper extends BatchDataAccessHelper<InstellingsLogo>
{

	public InstellingsLogo getInstellingsLogo();
}
