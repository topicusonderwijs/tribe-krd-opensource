package nl.topicus.eduarte.krdparticipatie.web.pages.beheer.lesweek;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralDataProvider;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomColumn;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.participatie.helpers.LesweekIndelingDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.LesdagIndeling;
import nl.topicus.eduarte.entities.participatie.LesuurIndeling;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.krdparticipatie.principals.beheer.LesweekindelingWrite;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieBeheerMenuItem;
import nl.topicus.eduarte.participatie.zoekfilters.LesweekindelingZoekFilter;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@InPrincipal(LesweekindelingWrite.class)
@PageInfo(title = "Lesweekindeling", menu = "Beheer > Participatie > Lesweekindeling")
public class LesweekindelingOverzichtPage extends AbstractBeheerPage<Void>
{
	private EduArteDataPanel<LesweekIndeling> datapanel;

	private IDataProvider<LesweekIndeling> provider;

	private LesweekindelingZoekFilter filter;

	private static LesweekindelingZoekFilter getDefaultFilter()
	{
		return new LesweekindelingZoekFilter();
	}

	public LesweekindelingOverzichtPage()
	{
		this(getDefaultFilter());
	}

	private static final List<CustomColumn<LesweekIndeling>> getColumns()
	{
		List<CustomColumn<LesweekIndeling>> cols = new ArrayList<CustomColumn<LesweekIndeling>>(10);
		cols.add(new CustomPropertyColumn<LesweekIndeling>("Naam", "Naam", "naam", "naam"));
		cols.add(new CustomPropertyColumn<LesweekIndeling>("Actief", "Actief", "actief",
			"actiefOmschrijving"));
		cols.add(new CustomPropertyColumn<LesweekIndeling>("Omschrijving", "Omschrijving",
			"omschrijving", "omschrijving"));
		return cols;
	}

	public LesweekindelingOverzichtPage(LesweekindelingZoekFilter filter)
	{
		super(ParticipatieBeheerMenuItem.Lesweekindeling);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		this.setFilter(filter);

		provider = GeneralDataProvider.of(filter, LesweekIndelingDataAccessHelper.class);

		CustomDataPanelContentDescription<LesweekIndeling> desc =
			new CustomDataPanelContentDescription<LesweekIndeling>("Lesweekindeling");

		desc.setColumns(getColumns());

		datapanel = new EduArteDataPanel<LesweekIndeling>("datapanel", provider, desc);

		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<LesweekIndeling>(
			LesweekIndelingBewerkenPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<LesweekIndeling> item)
			{
				IChangeRecordingModel<LesweekIndeling> changeModel =
					ModelFactory.getCompoundChangeRecordingModel(item.getModelObject(),
						new DefaultModelManager(LesuurIndeling.class, LesdagIndeling.class,
							LesweekIndeling.class));

				setResponsePage(new LesweekIndelingBewerkenPage(changeModel,
					new LesweekindelingOverzichtPage()));
			}
		});

		add(datapanel);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ToevoegenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Class<LesweekIndelingBewerkenPage> getPageIdentity()
			{
				return LesweekIndelingBewerkenPage.class;
			}

			@Override
			public Page getPage()
			{
				return new LesweekIndelingBewerkenPage(LesweekindelingOverzichtPage.this);
			}
		}));
	}

	public void setFilter(LesweekindelingZoekFilter filter)
	{
		this.filter = filter;
	}

	public LesweekindelingZoekFilter getFilter()
	{
		return filter;
	}
}
