package nl.topicus.eduarte.web.components.resultaat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.dao.helpers.ResultaatDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.ToetsCodePathMode;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

@Exportable
public class RapportageResultaten
{
	@Exportable
	public class ResultatenPerStructuur
	{
		private Resultaatstructuur resultaatstructuur;

		public ResultatenPerStructuur(Resultaatstructuur resultaatstructuur)
		{
			this.resultaatstructuur = resultaatstructuur;
		}

		@Exportable
		public Resultaatstructuur getResultaatstructuur()
		{
			return resultaatstructuur;
		}

		@Exportable
		public List<Resultaat> getResultaten()
		{
			ResultaatDataAccessHelper resultaatHelper =
				DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class);
			List<Resultaat> ret = new ArrayList<Resultaat>();
			for (String curToetsPath : getToetsCodePaden())
			{
				Toets toets = resultaatstructuur.findToets(curToetsPath);
				if (toets == null)
					ret.add(null);
				else
					ret.add(resultaatHelper.getGeldendeResultaat(toets, context.getDeelnemer()));
			}
			return ret;
		}
	}

	private ResultaatRapportageConfiguratieFactory parent;

	private Verbintenis context;

	public RapportageResultaten()
	{
	}

	public RapportageResultaten(ResultaatRapportageConfiguratieFactory parent, Verbintenis context)
	{
		this.parent = parent;
		this.context = context;
	}

	private ResultaatstructuurZoekFilter getStructuurFilterVoorDeelnemer()
	{
		ResultaatstructuurZoekFilter filter =
			new ZoekFilterCopyManager().copyObject(parent.getStructuurFilter());
		filter.setDeelnemers(Arrays.asList(context.getDeelnemer()));
		return filter;
	}

	private ToetsZoekFilter getToetsFilterVoorDeelnemer()
	{
		ToetsZoekFilter filter = new ZoekFilterCopyManager().copyObject(parent.getToetsFilter());
		filter.getResultaatstructuurFilter().setDeelnemers(Arrays.asList(context.getDeelnemer()));
		return filter;
	}

	@Exportable
	public List<Resultaatstructuur> getResultaatstructuren()
	{
		return DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class).list(
			getStructuurFilterVoorDeelnemer());
	}

	@Exportable
	public List<Resultaat> getEindResultaten()
	{
		ResultaatDataAccessHelper resultaatHelper =
			DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class);
		List<Resultaat> ret = new ArrayList<Resultaat>();
		for (Resultaatstructuur curStructuur : parent.getResultaatstructuren())
		{
			ret.add(resultaatHelper.getGeldendeResultaat(curStructuur.getEindresultaat(), context
				.getDeelnemer()));
		}
		return ret;
	}

	@Exportable
	public List<String> getToetsCodes()
	{
		SortedSet<Toets> toetsen = getToetsen();
		List<String> ret = new ArrayList<String>();
		for (Toets curToets : toetsen)
			ret.add(curToets.getCode());
		return ret;
	}

	@Exportable
	public List<String> getToetsCodePaden()
	{
		SortedSet<Toets> toetsen = getToetsen();
		List<String> ret = new ArrayList<String>();
		for (Toets curToets : toetsen)
			ret.add(curToets.getToetscodePath());
		return ret;
	}

	private SortedSet<Toets> getToetsen()
	{
		SortedSet<Toets> toetsen =
			new TreeSet<Toets>(new MatrixToetsComparator(getStructuurFilterVoorDeelnemer(),
				ToetsCodePathMode.STANDAARD));
		toetsen.addAll(DataAccessRegistry.getHelper(ToetsDataAccessHelper.class).list(
			getToetsFilterVoorDeelnemer()));
		return toetsen;
	}

	@Exportable
	public List<ResultatenPerStructuur> getResultatenPerStructuur()
	{
		List<ResultatenPerStructuur> ret = new ArrayList<ResultatenPerStructuur>();
		for (Resultaatstructuur curStructuur : getResultaatstructuren())
		{
			ret.add(new ResultatenPerStructuur(curStructuur));
		}
		return ret;
	}
}
