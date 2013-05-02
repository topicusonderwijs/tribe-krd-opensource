package nl.topicus.eduarte.web.components.resultaat;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.topicus.cobra.web.components.datapanel.CustomColumn;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.ToetsCodePathMode;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

public abstract class AbstractResultatenmatrixTable<T, M extends ResultatenModel> extends
		AbstractResultatenTable<T, M>
{
	private static class ToetsData
	{
		private String code;

		private String path;

		private int depth;

		private int maxPogingen;

		private boolean alternatief;

		public ToetsData(Toets toets, ToetsCodePathMode mode)
		{
			code = toets.getCode();
			path = toets.getToetscodePath(mode);
			depth = toets.getDepth();
			maxPogingen = toets.getAantalPogingen();
			alternatief = toets.isAlternatiefResultaatMogelijk();
		}

		public void merge(Toets toets)
		{
			maxPogingen = Math.max(toets.getAantalPogingen(), maxPogingen);
			depth = Math.min(toets.getDepth(), depth);
			alternatief |= toets.isAlternatiefResultaatMogelijk();
		}

		public int getMaxPogingen()
		{
			return maxPogingen;
		}

		public boolean isAlternatief()
		{
			return alternatief;
		}

		public String getPath()
		{
			return path;
		}

		public String getCode()
		{
			return code;
		}

		public int getDepth()
		{
			return depth;
		}
	}

	private static final long serialVersionUID = 1L;

	public AbstractResultatenmatrixTable(M resultatenModel,
			ResultaatColumnCreator<T, M> columnCreator)
	{
		super(resultatenModel, columnCreator);
	}

	protected void createCijferColumns(List<Toets> toetsen, ToetsZoekFilter toetsFilter,
			ToetsCodePathMode mode)
	{
		SortedMap<Toets, ToetsData> codeSet =
			new TreeMap<Toets, ToetsData>(new MatrixToetsComparator(toetsFilter
				.getResultaatstructuurFilter(), mode));
		for (Toets curToets : toetsen)
		{
			ToetsData curData = codeSet.get(curToets);
			if (curData == null)
				codeSet.put(curToets, new ToetsData(curToets, mode));
			else
				curData.merge(curToets);
		}

		for (ToetsData curToets : codeSet.values())
		{
			createColumn(createResolver(curToets.getPath(), curToets.getCode(),
				curToets.getDepth(), 0, toetsFilter), curToets, 0);
			if (curToets.isAlternatief())
				createColumn(createResolver(curToets.getPath(), curToets.getCode(), curToets
					.getDepth(), -1, toetsFilter), curToets, -1);
			for (int curCijfer = 1; curCijfer <= curToets.getMaxPogingen(); curCijfer++)
			{
				createColumn(createResolver(curToets.getPath(), curToets.getCode(), curToets
					.getDepth(), curCijfer, toetsFilter), curToets, curCijfer);
			}
		}
	}

	abstract protected DeelnemerToetsResolver<T> createResolver(String toetsPath, String toetsCode,
			int toetsDepth, int pogingNr, ToetsZoekFilter toetsFilter);

	private void createColumn(DeelnemerToetsResolver<T> resolver, ToetsData toetsdata, int pogingNr)
	{
		createColumn(toetsdata.getPath() + pogingNr, toetsdata.getCode(), pogingNr, resolver);
	}

	@Override
	public int calculateWidth(CustomDataPanel<T> datapanel)
	{
		int toetsCount = 0;
		for (CustomColumn<T> curColumn : getColumns())
		{
			if (curColumn instanceof ResultaatColumn< ? >)
			{
				ResultaatColumn< ? > curResColumn = (ResultaatColumn< ? >) curColumn;
				if (curResColumn.getPoging() == 0 && curResColumn.isColumnVisible())
					toetsCount++;
			}
		}
		return toetsCount * 51 + 300;
	}
}
