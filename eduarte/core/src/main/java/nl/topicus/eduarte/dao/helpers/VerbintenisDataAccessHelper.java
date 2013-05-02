package nl.topicus.eduarte.dao.helpers;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Aanmelding;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;
import nl.topicus.onderwijs.duo.bron.bve.foto.pve_9_9.BronFotoType;

import org.hibernate.criterion.Conjunction;

/**
 * @author loite
 */
public interface VerbintenisDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Verbintenis, VerbintenisZoekFilter>
{
	public static class DeelnemerVerbintenisCount implements Serializable
	{
		private static final long serialVersionUID = 1L;

		private int deelnemerCount;

		private int verbintenisCount;

		public DeelnemerVerbintenisCount(int deelnemerCount, int verbintenisCount)
		{
			this.deelnemerCount = deelnemerCount;
			this.verbintenisCount = verbintenisCount;
		}

		public int getDeelnemerCount()
		{
			return deelnemerCount;
		}

		public int getVerbintenisCount()
		{
			return verbintenisCount;
		}
	}

	public List<Verbintenis> getVerbintenissenByDeelnemer(Deelnemer deelnemer);

	public Conjunction addQuickSearchCriteria(String multiZoek, boolean checkOnderwijs,
			Boolean gearchiveerd);

	public Verbintenis getVerbintenisById(Long id);

	/**
	 * Geeft een lijst van de id's van alle verbintenissen die in een BRON-foto zouden
	 * moeten zitten.
	 */
	public List<Long> getBRONFotoVerbintenissen(BronFotoType fototype, Date teldatum);

	public Long getNumberOfBronCommuniceerbareVerbintenissen(Vooropleiding vooropleiding);

	public long getDeelnemerCount(VerbintenisZoekFilter zoekFilter);

	public DeelnemerVerbintenisCount getDeelnemerVerbintenisCount(VerbintenisZoekFilter zoekFilter);

	public List<Long> getIds(VerbintenisZoekFilter zoekfilter);

	public List<Long> getDeelnemerIds(VerbintenisZoekFilter verbintenisZoekFilter);

	public Aanmelding getAanmelding(Verbintenis verbintenis);

	public Verbintenis getVerbintenisByIdInOudPakket(Long id);
}
