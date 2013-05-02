package nl.topicus.eduarte.entities.security.authentication.vasco;

/**
 * De status van een authenticatie token.
 * <p>
 * Met behulp van beheerfunctionaliteit op landelijk niveau kan worden aangegeven welke
 * tokens uitgegeven zijn aan welke instelling. Naast het uitgeven van tokens moet het ook
 * mogelijk zijn om deze weer in te trekken. Een token moet ook kunnen worden geblokkeerd
 * als een gebruiker te vaak verkeerde passwords heeft ingevoerd en tenslotte kan een
 * token ook defect raken.
 * <p>
 * In totaal zijn er landelijk vier statussen mogelijk met betrekking tot een token:
 * <ul>
 * <li>beschikbaar</li>
 * <li>uitgegeven</li>
 * <li>geblokkeerd of</li>
 * <li>defect</li>
 * </ul>
 * 
 * @author dashorst
 */
public enum TokenStatus
{
	Beschikbaar,
	Uitgegeven,
	Geblokkeerd,
	Defect;

	@Override
	public String toString()
	{
		return name().toLowerCase();
	}
}
