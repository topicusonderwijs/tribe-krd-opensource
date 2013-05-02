package nl.topicus.eduarte.tester;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.inschrijving.Aanmelding;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;
import nl.topicus.onderwijs.duo.bron.bve.foto.pve_9_9.BronFotoType;

import org.hibernate.criterion.Conjunction;

public class MockVerbintenisDataAccessHelper implements VerbintenisDataAccessHelper
{
	private EduArteTester database;

	public MockVerbintenisDataAccessHelper(EduArteTester database)
	{
		this.database = database;
	}

	@Override
	public Conjunction addQuickSearchCriteria(String multiZoek, boolean checkOnderwijs,
			Boolean gearchiveerd)
	{

		return null;
	}

	@Override
	public Aanmelding getAanmelding(Verbintenis verbintenis)
	{

		return null;
	}

	@Override
	public List<Long> getBRONFotoVerbintenissen(BronFotoType fototype, Date teldatum)
	{

		return null;
	}

	@Override
	public long getDeelnemerCount(VerbintenisZoekFilter zoekFilter)
	{
		return 0;
	}

	@Override
	public List<Long> getDeelnemerIds(VerbintenisZoekFilter verbintenisZoekFilter)
	{
		return null;
	}

	@Override
	public DeelnemerVerbintenisCount getDeelnemerVerbintenisCount(VerbintenisZoekFilter zoekFilter)
	{
		return new DeelnemerVerbintenisCount(0, 0);
	}

	@Override
	public List<Long> getIds(VerbintenisZoekFilter zoekfilter)
	{
		return null;
	}

	@Override
	public Long getNumberOfBronCommuniceerbareVerbintenissen(Vooropleiding vooropleiding)
	{
		return null;
	}

	@Override
	public Verbintenis getVerbintenisById(Long id)
	{
		return null;
	}

	@Override
	public Verbintenis getVerbintenisByIdInOudPakket(Long id)
	{
		return null;
	}

	@Override
	public List<Verbintenis> getVerbintenissenByDeelnemer(Deelnemer deelnemer)
	{
		return null;
	}

	@Override
	public int getIndex(VerbintenisZoekFilter filter, Object object)
	{
		return 0;
	}

	@Override
	public List<Verbintenis> list(VerbintenisZoekFilter filter)
	{
		List<Verbintenis> resultaat = new ArrayList<Verbintenis>();
		if (filter.heeftBPVCriteria())
		{
			List<IdObject> objectsFromTransaction = database.getObjectsFromTransaction();
			for (IdObject object : objectsFromTransaction)
			{
				if (object instanceof Verbintenis)
				{
					Verbintenis verbintenis = (Verbintenis) object;
					List<BPVInschrijving> bpvInschrijvingen = verbintenis.getBpvInschrijvingen();
					for (BPVInschrijving bpvInschrijving : bpvInschrijvingen)
					{
						if (bpvInschrijving.getBedrijfsgegeven().getExterneOrganisatie().equals(
							filter.getBpvBedrijf()))
							resultaat.add(verbintenis);
					}
				}
			}
		}
		return resultaat;
	}

	@Override
	public List<Verbintenis> list(VerbintenisZoekFilter filter, int firstResult, int maxResults)
	{
		return Collections.emptyList();
	}

	@Override
	public <R> List<R> list(DatabaseSelection<R, Verbintenis> selection, List<String> orderBy,
			boolean ascending)
	{
		return null;
	}

	@Override
	public int listCount(VerbintenisZoekFilter filter)
	{
		return 0;
	}

	@Override
	public List<Serializable> listIds(VerbintenisZoekFilter filter)
	{
		return null;
	}

	@Override
	public List<Serializable> listIds(VerbintenisZoekFilter filter, int firstResult, int maxResults)
	{
		return null;
	}

	@Deprecated
	@Override
	public void deleteAndCommit(Verbintenis dataObject)
	{
	}

	@Override
	public void evict(Verbintenis dataObject)
	{
	}

	@Override
	public <R extends Verbintenis> R get(Class<R> class1, Serializable id)
	{
		return null;
	}

	@Override
	public <R extends Verbintenis> List<R> list(Class<R> class1, String... orderBy)
	{
		return null;
	}

	@Override
	public <R extends Verbintenis> R load(Class<R> class1, Serializable id)
	{
		return null;
	}

	@Deprecated
	@Override
	public Serializable saveAndCommit(Verbintenis dataObject)
	{
		return null;
	}

	@Deprecated
	@Override
	public void saveOrUpdateAndCommit(Verbintenis dataObject)
	{
	}

	@Deprecated
	@Override
	public void updateAndCommit(Verbintenis dataObject)
	{
	}

	@Deprecated
	@Override
	public void batchDelete(Verbintenis dataObject)
	{
	}

	@Override
	public void batchExecute()
	{
	}

	@Override
	public <R extends Verbintenis> List<R> list(Class<R> class1,
			Collection< ? extends Serializable> ids)
	{
		return null;
	}

	@Override
	public void batchRollback()
	{
	}

	@Deprecated
	@Override
	public Serializable batchSave(Verbintenis dataObject)
	{
		return null;
	}

	@Deprecated
	@Override
	public void batchSaveOrUpdate(Verbintenis dataObject)
	{
	}

	@Deprecated
	@Override
	public void batchUpdate(Verbintenis dataObject)
	{
	}

	@Override
	public void flush()
	{
	}

	@Override
	public <Y> int touch(java.lang.Class< ? > clz, String property, Y van, Y totEnMet)
	{
		return 0;
	}

	@Override
	public void delete(Verbintenis dataObject)
	{
	}

	@Override
	public Serializable save(Verbintenis dataObject)
	{
		return null;
	}

	@Override
	public void saveOrUpdate(Verbintenis dataObject)
	{
	}

	@Override
	public void update(Verbintenis dataObject)
	{
	}
}
