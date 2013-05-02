package nl.topicus.eduarte.web.components.resultaat;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.ToetsCodePathMode;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class ResultatenmatrixTable<M extends ResultatenModel> extends
		AbstractResultatenmatrixTable<Resultaatstructuur, M>
{
	private static class ResultatenmatrixResolver implements
			DeelnemerToetsResolver<Resultaatstructuur>
	{
		private static final long serialVersionUID = 1L;

		private IModel<Deelnemer> deelnemerModel;

		private String toetsCodePath;

		private String toetsCode;

		private int pogingNr;

		private ToetsZoekFilter toetsFilter;

		private int toetsDepth;

		public ResultatenmatrixResolver(IModel<Deelnemer> deelnemerModel, String toetsCodePath,
				String toetsCode, int toetsDepth, int pogingNr, ToetsZoekFilter toetsFilter)
		{
			this.deelnemerModel = deelnemerModel;
			this.toetsCodePath = toetsCodePath;
			this.toetsCode = toetsCode;
			this.toetsDepth = toetsDepth;
			this.pogingNr = pogingNr;
			this.toetsFilter = toetsFilter;
		}

		@Override
		public IModel<Deelnemer> getDeelnemerModel(IModel<Resultaatstructuur> rowModel)
		{
			return deelnemerModel;
		}

		@Override
		public IModel<Toets> getToetsModel(IModel<Resultaatstructuur> rowModel)
		{
			if (rowModel == null)
				return new Model<Toets>(null);
			Resultaatstructuur structuur = rowModel.getObject();
			Toets toets = structuur.findToets(toetsCodePath);
			return ModelFactory.getModel(toets);
		}

		@Override
		public boolean isColumnVisible()
		{
			return toetsFilter.getToetsCodeFilter() == null
				|| toetsFilter.getToetsCodeFilter().getToetsCodesAsSet().contains(toetsCode);
		}

		@Override
		public boolean isColumnVisibleInExport()
		{
			return pogingNr == ResultatenModel.ALTERNATIEF_NR
				|| pogingNr == ResultatenModel.RESULTAAT_NR;
		}

		@Override
		public int getToetsDepth()
		{
			return toetsDepth;
		}

		@Override
		public void detach()
		{
			deelnemerModel.detach();
			toetsFilter.detach();
		}

		@Override
		public String toString()
		{
			return "RR " + toetsCodePath;
		}
	}

	private static final long serialVersionUID = 1L;

	private IModel<Deelnemer> deelnemerModel;

	public ResultatenmatrixTable(M resultatenModel, IModel<Deelnemer> deelnemerModel,
			ResultaatColumnCreator<Resultaatstructuur, M> columnCreator, ToetsZoekFilter toetsFilter)
	{
		super(resultatenModel, columnCreator);

		this.deelnemerModel = deelnemerModel;
		if (resultatenModel.isEditable())
		{
			addColumn(new CustomPropertyColumn<Resultaatstructuur>("Code", "Code",
				"onderwijsproduct.code", "onderwijsproduct.code"));
		}
		else
		{
			addColumn(new StructuurLinkColumn<Resultaatstructuur>("Code", "Code",
				"onderwijsproduct.code", "onderwijsproduct.code")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected Resultaatstructuur getResultaatstructuur(
						IModel<Resultaatstructuur> rowModel)
				{
					return rowModel.getObject();
				}
			});
		}
		addColumn(new CustomPropertyColumn<Resultaatstructuur>("Onderwijsproduct",
			"Onderwijsproduct", "onderwijsproduct.titel", "onderwijsproduct.titel"));
		createCijferColumns(resultatenModel.getToetsen(), toetsFilter, ToetsCodePathMode.STANDAARD);
	}

	@Override
	protected DeelnemerToetsResolver<Resultaatstructuur> createResolver(String toetsPath,
			String toetsCode, int toetsDepth, int pogingNr, ToetsZoekFilter toetsFilter)
	{
		return new ResultatenmatrixResolver(deelnemerModel, toetsPath, toetsCode, toetsDepth,
			pogingNr, toetsFilter);
	}
}
