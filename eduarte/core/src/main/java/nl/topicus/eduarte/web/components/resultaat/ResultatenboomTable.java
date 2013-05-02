package nl.topicus.eduarte.web.components.resultaat;

import nl.topicus.cobra.web.components.datapanel.CollapsableRowFactoryDecorator;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.ToetsTreeColumn;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class ResultatenboomTable<M extends ResultatenModel> extends
		AbstractResultatenTable<Toets, M>
{
	private class ResultatenboomResolver implements DeelnemerToetsResolver<Toets>
	{
		private static final long serialVersionUID = 1L;

		private IModel<Deelnemer> deelnemerModel;

		private int pogingNr;

		public ResultatenboomResolver(IModel<Deelnemer> deelnemerModel, int pogingNr)
		{
			this.deelnemerModel = deelnemerModel;
			this.pogingNr = pogingNr;
		}

		@Override
		public IModel<Deelnemer> getDeelnemerModel(IModel<Toets> rowModel)
		{
			return deelnemerModel;
		}

		@Override
		public IModel<Toets> getToetsModel(IModel<Toets> rowModel)
		{
			return rowModel == null ? new Model<Toets>(null) : rowModel;
		}

		@Override
		public boolean isColumnVisible()
		{
			if (pogingNr == ResultatenModel.ALTERNATIEF_NR)
				return getResultatenModel().isToetsMetAlternatiefAanwezig();
			return getResultatenModel().getHuidigMaximumAantalPogingen() >= pogingNr;
		}

		@Override
		public boolean isColumnVisibleInExport()
		{
			return false;
		}

		@Override
		public void detach()
		{
			deelnemerModel.detach();
		}

		@Override
		public int getToetsDepth()
		{
			return 0;
		}
	}

	public static String getHeader(int pogingNummer)
	{
		switch (pogingNummer)
		{
			case ResultatenModel.ALTERNATIEF_NR:
				return "Alt.";
			case ResultatenModel.RESULTAAT_NR:
				return "Res.";
			default:
				return "Pog. " + pogingNummer;
		}
	}

	private static final long serialVersionUID = 1L;

	public ResultatenboomTable(M resultatenModel, IModel<Deelnemer> deelnemerModel,
			CollapsableRowFactoryDecorator<Toets> rowFactory,
			ResultaatColumnCreator<Toets, M> columnCreator)
	{
		super(resultatenModel, columnCreator);
		addColumn(new ToetsTreeColumn(rowFactory, resultatenModel.isEditable()));
		addColumn(new CustomPropertyColumn<Toets>("Toetsnaam", "Toetsnaam", "naam"));
		addColumn(new CustomPropertyColumn<Toets>("Onderwijsproduct", "Onderwijsproduct",
			"resultaatstructuur.onderwijsproduct.titel"));
		createCijferColumns(resultatenModel.getAbsoluutMaximumAantalPogingen(), deelnemerModel);
		addColumn(new CustomPropertyColumn<Toets>("Weging", "Weging", "weging", "weging")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass()
			{
				return "unit_40";
			}
		});
	}

	private void createCijferColumns(int aantalHerkansingen, IModel<Deelnemer> deelnemerModel)
	{
		createColumn(new ResultatenboomResolver(deelnemerModel, 0), 0);
		createColumn(new ResultatenboomResolver(deelnemerModel, -1), -1);
		for (int curCijfer = 1; curCijfer <= aantalHerkansingen; curCijfer++)
		{
			createColumn(new ResultatenboomResolver(deelnemerModel, curCijfer), curCijfer);
		}
	}

	private void createColumn(ResultatenboomResolver deelnemerToetsResolver, int pogingNr)
	{
		createColumn(getHeader(pogingNr), getHeader(pogingNr), pogingNr, deelnemerToetsResolver);
	}

	@Override
	public int calculateWidth(CustomDataPanel<Toets> datapanel)
	{
		return datapanel.getAantalZichtbareKolommen() * 50 + 250;
	}
}
