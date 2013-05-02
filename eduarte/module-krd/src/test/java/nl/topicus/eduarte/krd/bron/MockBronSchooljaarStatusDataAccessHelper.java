package nl.topicus.eduarte.krd.bron;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.krd.dao.helpers.BronSchooljaarStatusDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.BronSchooljaarStatus;
import nl.topicus.eduarte.krd.zoekfilters.BronSchooljaarStatusZoekFilter;
import nl.topicus.eduarte.tester.EduArteTester;

public class MockBronSchooljaarStatusDataAccessHelper implements
		BronSchooljaarStatusDataAccessHelper
{
	private EduArteTester tester;

	public MockBronSchooljaarStatusDataAccessHelper(EduArteTester tester)
	{
		this.tester = tester;
	}

	@Override
	public BronSchooljaarStatus getSchooljaarStatus(BronAanleverpunt aanleverpunt,
			Schooljaar schooljaar)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int getIndex(BronSchooljaarStatusZoekFilter filter, Object object)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public List<BronSchooljaarStatus> list(BronSchooljaarStatusZoekFilter filter)
	{
		ArrayList<BronSchooljaarStatus> resultaat = new ArrayList<BronSchooljaarStatus>();

		BronSchooljaarStatusZoekFilter zoekfilter = filter;
		List<IdObject> objectsFromTransaction = tester.getObjectsFromTransaction();
		for (IdObject object : objectsFromTransaction)
		{
			if (object instanceof BronSchooljaarStatus)
			{
				BronSchooljaarStatus status = (BronSchooljaarStatus) object;
				if (zoekfilter.getAanleverpunt() != null
					&& !zoekfilter.getAanleverpunt().equals(status.getAanleverpunt()))
				{
					continue;
				}
				if (zoekfilter.getSchooljaren() != null && !zoekfilter.getSchooljaren().isEmpty()
					&& !zoekfilter.getSchooljaren().contains(status.getSchooljaar()))
					continue;
				resultaat.add(status);
			}
		}
		return resultaat;
	}

	@Override
	public List<BronSchooljaarStatus> list(BronSchooljaarStatusZoekFilter filter, int firstResult,
			int maxResults)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <R> List<R> list(DatabaseSelection<R, BronSchooljaarStatus> selection,
			List<String> orderBy, boolean ascending)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int listCount(BronSchooljaarStatusZoekFilter filter)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Serializable> listIds(BronSchooljaarStatusZoekFilter filter)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Serializable> listIds(BronSchooljaarStatusZoekFilter filter, int firstResult,
			int maxResults)
	{
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public void deleteAndCommit(BronSchooljaarStatus dataObject)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void evict(BronSchooljaarStatus dataObject)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <R extends BronSchooljaarStatus> R get(Class<R> class1, Serializable id)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <R extends BronSchooljaarStatus> List<R> list(Class<R> class1, String... orderBy)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <R extends BronSchooljaarStatus> R load(Class<R> class1, Serializable id)
	{
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public Serializable saveAndCommit(BronSchooljaarStatus dataObject)
	{
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public void saveOrUpdateAndCommit(BronSchooljaarStatus dataObject)
	{
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public void updateAndCommit(BronSchooljaarStatus dataObject)
	{
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public void batchDelete(BronSchooljaarStatus dataObject)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void batchExecute()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <R extends BronSchooljaarStatus> List<R> list(Class<R> class1,
			Collection< ? extends Serializable> ids)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void batchRollback()
	{
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public Serializable batchSave(BronSchooljaarStatus dataObject)
	{
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public void batchSaveOrUpdate(BronSchooljaarStatus dataObject)
	{
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public void batchUpdate(BronSchooljaarStatus dataObject)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void flush()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <Y> int touch(java.lang.Class< ? > clz, String property, Y van, Y totEnMet)
	{
		return 0;
	}

	@Override
	public void delete(BronSchooljaarStatus dataObject)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Serializable save(BronSchooljaarStatus dataObject)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void saveOrUpdate(BronSchooljaarStatus dataObject)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(BronSchooljaarStatus dataObject)
	{
		throw new UnsupportedOperationException();
	}
}
