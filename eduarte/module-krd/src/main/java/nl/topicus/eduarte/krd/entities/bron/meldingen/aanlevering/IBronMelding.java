package nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.entities.BronMeldingOnderdeel;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.onderwijs.duo.bron.IBronBatch;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;

public interface IBronMelding extends IdObject, DeelnemerProvider
{
	public IBronBatch getBatch();

	public Integer getBatchNummer();

	public String getBestandsnaam();

	public Deelnemer getDeelnemer();

	public List<SoortMutatie> getSoortMutaties();

	public String getSoortMutatiesOmsch();

	public String getBronMeldingOnderdelenOmsch();

	public List<BronMeldingOnderdeel> getBronMeldingOnderdelen();

	public BronMeldingStatus getBronMeldingStatus();

	public String getOnderwijssoort();

	public String getReden();

	public Date getIngangsDatum();

	public boolean isBekostigingsRelevant();

	public boolean isGeblokkeerd();

	public void setGeblokkeerd(boolean geblokkeerd);

	public String getRedenGeblokkeerd();

	public void setRedenGeblokkeerd(String redenGeblokkeerd);

	public boolean isVerwijderd();

	public void setVerwijderd(boolean verwijderd);

	public boolean bevatAlleenToevoegingen();

	public boolean bevatSofiOfOnderwijsNummer();

	public Integer getMeldingnummer();

	public Integer getTerugkoppelbestandNummer();

	public Date getCreatedAt();
}