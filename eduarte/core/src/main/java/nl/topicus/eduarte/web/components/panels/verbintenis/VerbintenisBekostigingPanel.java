package nl.topicus.eduarte.web.components.panels.verbintenis;

import java.util.List;

import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelRowFactory;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class VerbintenisBekostigingPanel extends TypedPanel<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	public VerbintenisBekostigingPanel(String id, IModel<Verbintenis> model)
	{
		super(id, model);

		CustomDataPanel<Bekostigingsperiode> datapanel =
			new EduArteDataPanel<Bekostigingsperiode>("datapanel",
				new ListModelDataProvider<Bekostigingsperiode>(
					new PropertyModel<List<Bekostigingsperiode>>(getDefaultModel(),
						"bekostigingsperiodes")), new BekostigingsPeriodeTable(
					"Bekostigingsperiodes"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return super.isVisible() && getVerbintenis() != null
						&& Bekostigd.Gedeeltelijk.equals(getVerbintenis().getBekostigd());
				}

				@Override
				protected IModel<String> createTitleModel(String title)
				{
					return new Model<String>("");
				}

			};

		datapanel.setRowFactory(new CustomDataPanelRowFactory<Bekostigingsperiode>());
		datapanel.setButtonsVisible(false);
		add(datapanel);

	}

	private Verbintenis getVerbintenis()
	{
		return getModelObject();
	}

	public class BekostigingsPeriodeTable extends
			CustomDataPanelContentDescription<Bekostigingsperiode>
	{
		private static final long serialVersionUID = 1L;

		public BekostigingsPeriodeTable(String title)
		{
			super(title);
			addColumn(new CustomPropertyColumn<Bekostigingsperiode>("Van", "Van", "begindatum"));
			addColumn(new CustomPropertyColumn<Bekostigingsperiode>("Tot", "Tot", "einddatum"));
			addColumn(new BooleanPropertyColumn<Bekostigingsperiode>("Bekostigd", "Bekostigd",
				"begindatum", "bekostigd"));
		}
	}

}