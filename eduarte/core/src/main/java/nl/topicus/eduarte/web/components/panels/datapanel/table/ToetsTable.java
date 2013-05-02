package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.ToetsCodeColumn;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public class ToetsTable extends CustomDataPanelContentDescription<Toets>
{
	private static final long serialVersionUID = 1L;

	public ToetsTable(IModel<Integer> spanCountModel)
	{
		this(spanCountModel, true);
	}

	public ToetsTable(IModel<Integer> spanCountModel, boolean boom)
	{
		this(spanCountModel, boom, true);
	}

	public ToetsTable(IModel<Integer> spanCountModel, boolean boom, boolean toonCode)
	{
		super("Resultaatstructuur (toetsen)");
		createColumns(spanCountModel, boom, toonCode);
	}

	public static ToetsTable getToetstableVoorMeerdereOnderwijsproducten(
			IModel<Integer> spanCountModel)
	{
		ToetsTable table = new ToetsTable(spanCountModel, false, false);
		table.getColumns().add(
			0,
			new CustomPropertyColumn<Toets>("Code", "Code", "code", "codeHierarchisch")
				.setEscapeModelStrings(false));
		table.createGroupProperties();
		return table;
	}

	private void createColumns(IModel<Integer> spanCountModel, boolean boom, boolean toonCode)
	{
		if (toonCode)
		{
			if (boom)
			{
				addColumn(new ToetsCodeColumn(spanCountModel));
			}
			addColumn(new CustomPropertyColumn<Toets>("Code", "Code", "code", "code")
				.setDefaultVisible(!boom));
		}
		addColumn(new CustomPropertyColumn<Toets>("Onderwijsproduct", "Onderwijsproduct",
			"resultaatstructuur.onderwijsproduct").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Toets>("Naam", "Naam", "naam", "naam"));
		addColumn(new CustomPropertyColumn<Toets>("Schaal", "Schaal", "schaal.naam", "schaal.naam"));
		addColumn(new CustomPropertyColumn<Toets>("Studiepunten", "Studiepunten", "studiepunten",
			"studiepunten").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Toets>("Weging", "Weging", "weging", "weging")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel< ? > createLabelModel(final IModel<Toets> rowModel)
			{
				final IModel< ? > defaultModel = super.createLabelModel(rowModel);
				return new AbstractReadOnlyModel<Object>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public Object getObject()
					{
						if (rowModel.getObject().isAutomatischeWeging())
							return "Som onderliggend";
						return defaultModel.getObject();
					}
				};
			}
		});
		addColumn(new BooleanPropertyColumn<Toets>("Samengesteld", "Samengesteld", "samengesteld",
			"samengesteld").setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<Toets>("Verplicht", "Verplicht", "verplicht",
			"verplicht").setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<Toets>("Samengesteld met herkansingen", "Herkansingen",
			"samengesteldMetHerkansing", "samengesteldMetHerkansing").setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<Toets>("Samengesteld met varianten", "Varianten",
			"samengesteldMetVarianten", "samengesteldMetVarianten").setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<Toets>("Overschrijfbaar", "Overschrijfbaar",
			"overschrijfbaar", "overschrijfbaar").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Toets>("Max. niet behaald", "Max. niet behaald",
			"maxAantalNietBehaald", "maxAantalNietBehaald").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Toets>("Min. ingevuld", "Min. ingevuld",
			"minAantalIngevuld", "minAantalIngevuld").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Toets>("Max. ingevuld", "Max. ingevuld",
			"maxAantalIngevuld", "maxAantalIngevuld").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Toets>("Min. studiepunten voor behaald",
			"Min. studiepunten", "minStudiepuntenVoorBehaald", "minStudiepuntenVoorBehaald")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Toets>("Aantal herkansingen", "Aantal herkansingen",
			"aantalHerkansingen", "aantalHerkansingen").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Toets>("Score bij herkansing", "Score bij herkansing",
			"scoreBijHerkansing", "scoreBijHerkansing").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Toets>("Scoreschaal", "Scoreschaal", "scoreschaal",
			"scoreschaal").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Toets>("Scoreschaal lengte tijdvak 1",
			"Scoreschaal lengte t1", "scoreschaalLengteTijdvak1", "scoreschaalLengteTijdvak1")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Toets>("Scoreschaal normering tijdvak 1",
			"Scoreschaal normering t1", "scoreschaalNormeringTijdvak1",
			"scoreschaalNormeringTijdvak1").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Toets>("Scoreschaal lengte tijdvak 2",
			"Scoreschaal lengte t2", "scoreschaalLengteTijdvak2", "scoreschaalLengteTijdvak2")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Toets>("Scoreschaal normering tijdvak 2",
			"Scoreschaal normering t2", "scoreschaalNormeringTijdvak2",
			"scoreschaalNormeringTijdvak2").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Toets>("Scoreschaal lengte tijdvak 3",
			"Scoreschaal lengte t3", "scoreschaalLengteTijdvak3", "scoreschaalLengteTijdvak3")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Toets>("Scoreschaal normering tijdvak 3",
			"Scoreschaal normering t3", "scoreschaalNormeringTijdvak3",
			"scoreschaalNormeringTijdvak3").setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<Toets>("Alternatief resultaat", "Alternatief res.",
			"alternatiefResultaatMogelijk", "alternatiefResultaatMogelijk")
			.setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<Toets>("Combineren met hoofdresultaat",
			"Comb. met hoofdres.", "alternatiefCombinerenMetHoofd", "alternatiefCombinerenMetHoofd")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Toets>("Aantal resultaten, inclusief historie",
			"Aant. resultaten", "aantalResultaten").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Toets>("Toetsverwijzingen", "Toetsverwijzingen",
			"toetsverwijzingenFormatted").setDefaultVisible(false));
	}

	private void createGroupProperties()
	{
		addGroupProperty(new GroupProperty<Toets>(
			"resultaatstructuur.onderwijsproduct.codeAndTitle", "Onderwijsproduct",
			"resultaatstructuur.onderwijsproduct"));
	}
}
