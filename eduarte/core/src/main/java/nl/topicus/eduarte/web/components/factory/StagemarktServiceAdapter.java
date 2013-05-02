package nl.topicus.eduarte.web.components.factory;

import java.util.Date;
import java.util.List;

public interface StagemarktServiceAdapter
{
	public List<StagemarktBedrijfsgegevens> getBedrijfgegevens(String bedrijfsnaam,
			String postcode, int huisnummer, String brinKenniscentrum);

	public BPVAccreditatieAntwoord verifieerAccreditatie(String codeleerbedrijf, int crebonummer,
			Date peildatum);
}
