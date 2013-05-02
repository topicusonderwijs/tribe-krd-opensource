package nl.topicus.eduarte.krd.dao.helpers;

import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.meldingen.AbstractBronBatchVO;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchBVE;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.AbstractBronVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronExamenMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.IBronSignaal;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.IBronTerugkoppeling;
import nl.topicus.eduarte.krd.zoekfilters.BronBatchZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronExamenmeldingZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronSignaalZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronTerugkoppelingZoekFilter;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.IBronBatch;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Sectordeel;

public interface BronDataAccessHelper extends BatchDataAccessHelper<Void>
{
	List<IBronBatch> getBronBatches(BronBatchZoekFilter filter);

	/**
	 * Haalt de lijst van BRON meldingen op die voldoen aan het filter, dit kunnen BVE
	 * *en* VO meldingen zijn.
	 */
	List<IBronMelding> getBronMeldingen(BronMeldingZoekFilter filter);

	/**
	 * Haalt de lijst van BVE BRON meldingen op die voldoen aan het filter (dus zonder de
	 * VO meldingen).
	 */
	List<BronAanleverMelding> getBronBveMeldingen(BronMeldingZoekFilter filter);

	/**
	 * Haalt de lijst van VO BRON meldingen op die voldoen aan het filter (dus zonder de
	 * BVE meldingen).
	 * 
	 * @param class1
	 */
	<T extends AbstractBronVOMelding> List<T> getBronVoMeldingen(BronMeldingZoekFilter filter,
			Class<T> class1);

	List<IBronSignaal> getSignalen(BronSignaalZoekFilter filter);

	List<IBronTerugkoppeling> getTerugkoppelingen(BronTerugkoppelingZoekFilter filter);

	long getAantalMeldingen(BronMeldingZoekFilter filter);

	long getAantalBVEMeldingen(BronMeldingZoekFilter filter);

	long getAantalVOMeldingen(BronMeldingZoekFilter filter);

	BronBatchBVE findBveBatch(Sectordeel sectordeel, BronAanleverpunt aanleverpunt, int batchnummer);

	AbstractBronBatchVO< ? extends AbstractBronVOMelding> findVoBatch(
			BronAanleverpunt aanleverpunt, int batchnummer);

	List<IBronExamenMelding> getBronExamenMeldingen(BronExamenmeldingZoekFilter filter);

	Map<BronOnderwijssoort, Integer> getVerwachteTerugkoppelBatchnummers(
			BronAanleverpunt aanleverpunt);

	long getAantalOngeaccordeerdeSignalen(BronOnderwijssoort onderwijssoort,
			BronAanleverpunt aanleverpunt, Schooljaar schooljaar);

	boolean isTerugkoppelingIngelezenVoorBatch(IBronBatch bronBatchBVE);

	public List<BronBveAanleverRecord> getAanleverRecords(OnderwijsproductAfnameContext context);

}
