package nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.Ernst;

public interface IBronSignaal extends IdObject
{
	public Integer getSignaalcode();

	public String getOmschrijvingSignaal();

	public Ernst getErnst();

	public Boolean getGeaccordeerd();

	public String getOpmerking();

	public IBronMelding getAanleverMelding();

	public IBronTerugkMelding getTerugkMelding();
}