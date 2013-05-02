package nl.topicus.eduarte.krd.bron;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.meldingen.AbstractBronBatchVO;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchBVE;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.AbstractBronVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronExamenMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.IBronSignaal;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.IBronTerugkoppeling;
import nl.topicus.eduarte.krd.zoekfilters.BronBatchZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronExamenmeldingZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronSignaalZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronTerugkoppelingZoekFilter;
import nl.topicus.eduarte.tester.EduArteTester;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.IBronBatch;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Sectordeel;

public class MockBronHibernateDataAccessHelper implements BronDataAccessHelper
{
	private EduArteTester database;

	public MockBronHibernateDataAccessHelper(EduArteTester database)
	{
		this.database = database;
	}

	@Override
	public BronBatchBVE findBveBatch(Sectordeel sectordeel, BronAanleverpunt aanleverpunt,
			int batchnummer)
	{
		return null;
	}

	@Override
	public AbstractBronBatchVO< ? extends AbstractBronVOMelding> findVoBatch(
			BronAanleverpunt aanleverpunt, int batchnummer)
	{
		return null;
	}

	@Override
	public long getAantalMeldingen(BronMeldingZoekFilter filter)
	{
		return 0;
	}

	@Override
	public long getAantalBVEMeldingen(BronMeldingZoekFilter filter)
	{
		return 0;
	}

	@Override
	public long getAantalVOMeldingen(BronMeldingZoekFilter filter)
	{
		return 0;
	}

	@Override
	public long getAantalOngeaccordeerdeSignalen(BronOnderwijssoort onderwijssoort,
			BronAanleverpunt aanleverpunt, Schooljaar schooljaar)
	{
		return 0;
	}

	@Override
	public List<IBronBatch> getBronBatches(BronBatchZoekFilter filter)
	{
		return null;
	}

	@Override
	public List<IBronExamenMelding> getBronExamenMeldingen(BronExamenmeldingZoekFilter filter)
	{
		return null;
	}

	@Override
	public List<IBronMelding> getBronMeldingen(BronMeldingZoekFilter filter)
	{
		List<IBronMelding> meldingen = new ArrayList<IBronMelding>();
		List<BronAanleverMelding> bve = getBronBveMeldingen(filter);
		meldingen.addAll(bve);
		return meldingen;
	}

	@Override
	public List<BronAanleverMelding> getBronBveMeldingen(BronMeldingZoekFilter filter)
	{
		List<BronAanleverMelding> meldingen = new ArrayList<BronAanleverMelding>();
		List<IdObject> objectsFromTransaction = database.getObjectsFromTransaction();
		for (IdObject object : objectsFromTransaction)
		{
			if (object instanceof BronAanleverMelding)
			{
				BronAanleverMelding melding = (BronAanleverMelding) object;
				if (meldingen.contains(melding))
					continue;
				if (filter.getVerbintenis() != null
					&& !filter.getVerbintenis().equals(melding.getVerbintenis()))
				{
					continue;
				}
				if (filter.getMeldingStatus() != null
					&& filter.getMeldingStatus() != melding.getBronMeldingStatus())
				{
					continue;
				}
				if (filter.getBronMeldingOnderdeel() != null)
				{
					boolean bevatMeldingOnderdeel = false;
					List<Integer> recordTypes = filter.getBronMeldingOnderdeel().getRecordTypes();
					for (BronBveAanleverRecord record : melding.getMeldingen())
					{
						bevatMeldingOnderdeel =
							bevatMeldingOnderdeel || recordTypes.contains(record.getRecordType());
					}
					if (!bevatMeldingOnderdeel)
						continue;
				}
				if (filter.getBronMeldingOnderdeelNot() != null)
				{
					boolean bevatMeldingOnderdeel = false;
					List<Integer> recordTypes =
						filter.getBronMeldingOnderdeelNot().getRecordTypes();
					for (BronBveAanleverRecord record : melding.getMeldingen())
					{
						bevatMeldingOnderdeel =
							bevatMeldingOnderdeel || recordTypes.contains(record.getRecordType());
					}
					if (bevatMeldingOnderdeel)
						continue;
				}
				meldingen.add(melding);
			}
		}
		return meldingen;
	}

	@Override
	public <T extends AbstractBronVOMelding> List<T> getBronVoMeldingen(
			BronMeldingZoekFilter filter, Class<T> clz)
	{
		ArrayList<T> meldingen = new ArrayList<T>();
		List<IdObject> objectsFromTransaction = database.getObjectsFromTransaction();
		for (IdObject object : objectsFromTransaction)
		{
			if (clz.isAssignableFrom(object.getClass()))
			{
				T melding = clz.cast(object);
				if (meldingen.contains(melding))
					continue;
				if (filter.getVerbintenis() != null
					&& !filter.getVerbintenis().equals(melding.getVerbintenis()))
				{
					continue;
				}
				if (filter.getMeldingStatus() != null
					&& filter.getMeldingStatus() != melding.getBronMeldingStatus())
				{
					continue;
				}
				if (filter.getVoMeldingSoort() != null
					&& melding instanceof BronInschrijvingsgegevensVOMelding
					&& ((BronInschrijvingsgegevensVOMelding) melding).getSoort() != filter
						.getVoMeldingSoort())
				{
					continue;
				}
				meldingen.add(melding);
			}
		}
		return meldingen;
	}

	@Override
	public List<IBronSignaal> getSignalen(BronSignaalZoekFilter filter)
	{
		return null;
	}

	@Override
	public List<IBronTerugkoppeling> getTerugkoppelingen(BronTerugkoppelingZoekFilter filter)
	{
		return null;
	}

	@Override
	public Map<BronOnderwijssoort, Integer> getVerwachteTerugkoppelBatchnummers(
			BronAanleverpunt aanleverpunt)
	{
		return null;
	}

	@Deprecated
	@Override
	public void batchDelete(Void dataObject)
	{
	}

	@Override
	public void batchExecute()
	{
	}

	@Override
	public void batchRollback()
	{
	}

	@Deprecated
	@Override
	public Serializable batchSave(Void dataObject)
	{
		return null;
	}

	@Deprecated
	@Override
	public void batchSaveOrUpdate(Void dataObject)
	{
	}

	@Deprecated
	@Override
	public void batchUpdate(Void dataObject)
	{
	}

	@Deprecated
	@Override
	public void deleteAndCommit(Void dataObject)
	{
	}

	@Override
	public void flush()
	{
	}

	@Deprecated
	@Override
	public Serializable saveAndCommit(Void dataObject)
	{
		return null;
	}

	@Deprecated
	@Override
	public void saveOrUpdateAndCommit(Void dataObject)
	{
	}

	@Deprecated
	@Override
	public void updateAndCommit(Void dataObject)
	{
	}

	@Override
	public void evict(Void dataObject)
	{
	}

	@SuppressWarnings("unchecked")
	@Override
	public Void get(Class class1, Serializable id)
	{
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Void> list(Class class1, String... orderBy)
	{
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Void load(Class class1, Serializable id)
	{
		return null;
	}

	@Override
	public boolean isTerugkoppelingIngelezenVoorBatch(IBronBatch bronBatchBVE)
	{
		return false;
	}

	@Override
	public List<BronBveAanleverRecord> getAanleverRecords(OnderwijsproductAfnameContext context)
	{
		return null;
	}

	@Override
	public <Y> int touch(java.lang.Class< ? > clz, String property, Y van, Y totEnMet)
	{
		return 0;
	}

	@Override
	public void delete(Void dataObject)
	{
	}

	@Override
	public Serializable save(Void dataObject)
	{
		return null;
	}

	@Override
	public void saveOrUpdate(Void dataObject)
	{
	}

	@Override
	public void update(Void dataObject)
	{
	}

	@Override
	@SuppressWarnings("unchecked")
	public List list(Class class1, Collection ids)
	{
		return null;
	}
}
