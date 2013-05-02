package nl.topicus.eduarte.resultaten.web.components.resultaat;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.ToetsCodePathMode;
import nl.topicus.eduarte.web.components.resultaat.AbstractResultatenmatrixTable;
import nl.topicus.eduarte.web.components.resultaat.DeelnemerToetsResolver;
import nl.topicus.eduarte.web.components.resultaat.ResultaatColumnCreator;
import nl.topicus.eduarte.web.components.resultaat.ResultatenModel;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class GroepResultatenmatrixTable<M extends ResultatenModel> extends
		AbstractResultatenmatrixTable<Deelnemer, M>
{
	private static class ResultatenmatrixResolver implements DeelnemerToetsResolver<Deelnemer>
	{
		private static final long serialVersionUID = 1L;

		private IModel<List<Resultaatstructuur>> resultaatstructurenModel;

		private String toetsCodePath;

		private String toetsCode;

		private int toetsDepth;

		private ToetsZoekFilter toetsFilter;

		private int pogingNr;

		public ResultatenmatrixResolver(IModel<List<Resultaatstructuur>> resultaatstructurenModel,
				String toetsCodePath, String toetsCode, int toetsDepth, int pogingNr,
				ToetsZoekFilter toetsFilter)
		{
			this.resultaatstructurenModel = resultaatstructurenModel;
			this.toetsCodePath = toetsCodePath;
			this.toetsCode = toetsCode;
			this.toetsDepth = toetsDepth;
			this.pogingNr = pogingNr;
			this.toetsFilter = toetsFilter;
		}

		@Override
		public IModel<Deelnemer> getDeelnemerModel(IModel<Deelnemer> rowModel)
		{
			return rowModel;
		}

		@Override
		public IModel<Toets> getToetsModel(IModel<Deelnemer> rowModel)
		{
			List<Resultaatstructuur> structuren = resultaatstructurenModel.getObject();
			if (structuren == null)
				return new Model<Toets>(null);
			for (Resultaatstructuur structuur : structuren)
			{
				if (rowModel == null
					|| DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class)
						.isStructuurAfgenomen(rowModel.getObject(), structuur))
				{
					Toets toets =
						structuur.findToets(toetsCodePath, ToetsCodePathMode.STRUCTUUR_LOKAAL);
					if (toets != null)
						return ModelFactory.getModel(toets);
				}
			}
			return new Model<Toets>(null);
		}

		@Override
		public int getToetsDepth()
		{
			return toetsDepth;
		}

		@Override
		public boolean isColumnVisible()
		{
			return resultaatstructurenModel.getObject() != null
				&& (toetsFilter.getToetsCodeFilter() == null || toetsFilter.getToetsCodeFilter()
					.getToetsCodesAsSet().contains(toetsCode));
		}

		@Override
		public boolean isColumnVisibleInExport()
		{
			return pogingNr == ResultatenModel.ALTERNATIEF_NR
				|| pogingNr == ResultatenModel.RESULTAAT_NR;
		}

		@Override
		public void detach()
		{
			resultaatstructurenModel.detach();
			toetsFilter.detach();
		}

		@Override
		public String toString()
		{
			return "RR " + toetsCodePath;
		}
	}

	private static final long serialVersionUID = 1L;

	private IModel<List<Resultaatstructuur>> resultaatstructurenModel;

	public GroepResultatenmatrixTable(M resultatenModel,
			IModel<List<Resultaatstructuur>> resultaatstructurenModel,
			ResultaatColumnCreator<Deelnemer, M> columnCreator, ToetsZoekFilter toetsFilter)
	{
		super(resultatenModel, columnCreator);
		this.resultaatstructurenModel = resultaatstructurenModel;

		addColumn(new CustomPropertyColumn<Deelnemer>("Deelnemernummer.", "Nr.", "deelnemernummer",
			"deelnemernummer"));
		addColumn(new CustomPropertyColumn<Deelnemer>("Naam", "Naam", "persoon.volledigeNaam",
			"persoon.volledigeNaam"));
		createCijferColumns(resultatenModel.getToetsen(), toetsFilter,
			ToetsCodePathMode.STRUCTUUR_LOKAAL);
	}

	@Override
	protected DeelnemerToetsResolver<Deelnemer> createResolver(String toetsPath, String toetsCode,
			int toetsDepth, int pogingNr, ToetsZoekFilter toetsFilter)
	{
		return new ResultatenmatrixResolver(resultaatstructurenModel, toetsPath, toetsCode,
			toetsDepth, pogingNr, toetsFilter);
	}
}
