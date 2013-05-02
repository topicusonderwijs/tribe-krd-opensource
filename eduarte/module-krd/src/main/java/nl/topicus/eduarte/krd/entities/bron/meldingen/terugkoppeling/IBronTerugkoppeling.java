package nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling;

import java.util.Date;

import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;

public interface IBronTerugkoppeling
{
	public int getAantalGeleverdeRecords();

	public BestandSoort getBestandSoort();

	public String getBrinNummer();

	public int getBRONBatchNummer();

	public Date getDatumTerugkoppeling();

	public BronOnderwijssoort getBronOnderwijssoort();

	public int getAanleverpuntNummer();

	public String getBatchesInBestand();

	public int getAantalSignalen();

	public int getAantalAfkeurSignalen();

	public int getAantalMeldingen();

	public int getAantalGoedgekeurdeMeldingen();

	public int getAantalAfgekeurdeMeldingen();
}