package nl.topicus.eduarte.krdparticipatie.web.pages.verzuimloket;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.eduarte.dao.participatie.helpers.VerzuimmeldingDataAccessHelper;
import nl.topicus.eduarte.entities.ibgverzuimloket.IbgVerzuimmelding;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krdparticipatie.principals.deelnemer.DeelnemerVerzuimloketWrite;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieDeelnemerMenuItem;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.filter.CollectiviteitVerzuimloketZoekfilterPanel;
import nl.topicus.eduarte.krdparticipatie.web.components.tables.CollectiviteitVerzuimmeldingTable;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.DeelnemerVerzuimloketZoekfilter;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Verzuimloket", menu = "Deelnemer > Aanwezigheid > Verzuimloket")
@InPrincipal(DeelnemerVerzuimloketWrite.class)
public class CollectiviteitVerzuimloketPage extends SecurePage
{

	private Panel zoekPanel;

	private VerzuimmeldingDetailPanel detailPanel;

	private WebMarkupContainer panelContainer;

	private EduArteDataPanel<IbgVerzuimmelding> dataPanel;

	private PageLinkButton editButton;

	private static final DeelnemerVerzuimloketZoekfilter getDefaultFilter()
	{
		DeelnemerVerzuimloketZoekfilter filter = new DeelnemerVerzuimloketZoekfilter();

		return filter;
	}

	public CollectiviteitVerzuimloketPage()
	{
		this(getDefaultFilter());
	}

	public CollectiviteitVerzuimloketPage(DeelnemerVerzuimloketZoekfilter verzuimmeldingFilter)
	{
		super(CoreMainMenuItem.Deelnemer);

		IDataProvider<IbgVerzuimmelding> verzuimmeldingProvider =
			GeneralDataProvider.of(verzuimmeldingFilter, VerzuimmeldingDataAccessHelper.class);

		panelContainer = new WebMarkupContainer("panelContainer");
		panelContainer.setOutputMarkupId(true);

		zoekPanel = new CollectiviteitVerzuimloketZoekfilterPanel("filter", verzuimmeldingFilter);

		dataPanel =
			new EduArteDataPanel<IbgVerzuimmelding>("datapanel", verzuimmeldingProvider,
				new CollectiviteitVerzuimmeldingTable());
		dataPanel.setSelecteerKolommenButtonVisible(false);

		detailPanel =
			new VerzuimmeldingDetailPanel("detailPanel",
				ModelFactory.getModel((IbgVerzuimmelding) null));

		CustomDataPanelAjaxClickableRowFactory<IbgVerzuimmelding> rowFactory =
			new CustomDataPanelAjaxClickableRowFactory<IbgVerzuimmelding>(detailPanel.getModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onClick(AjaxRequestTarget target, Item<IbgVerzuimmelding> item)
				{
					editButton.setVisible(true);
					replaceBottomRow(createBottomPanel());
					refreshBottomRow(target);
					target.addComponent(panelContainer);
				}
			};

		dataPanel.setRowFactory(rowFactory);

		add(zoekPanel);
		panelContainer.add(dataPanel);
		panelContainer.add(detailPanel);
		add(panelContainer);

		createComponents();
	}

	public Verbintenis getGeselecteerdeVerbintenis()
	{
		IbgVerzuimmelding melding = (IbgVerzuimmelding) detailPanel.getDefaultModelObject();
		return melding == null ? null : melding.getVerbintenis();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, ParticipatieDeelnemerMenuItem.Verzuimloket);
	}

	private BottomRowPanel createBottomPanel()
	{

		BottomRowPanel panel = new BottomRowPanel("bottom");

		panel.setOutputMarkupId(true);

		panel.setDefaultModel(ModelFactory.getModel(getGeselecteerdeVerbintenis()));

		editButton = new PageLinkButton(panel, "Bewerken", new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				IbgVerzuimmelding melding = (IbgVerzuimmelding) detailPanel.getDefaultModelObject();
				return new EditIbgVerzuimmeldingMeldingPage(melding.getVerbintenis(), melding,
					CollectiviteitVerzuimloketPage.this);
			}

			@Override
			public Class< ? extends WebPage> getPageIdentity()
			{
				return EditIbgVerzuimmeldingMeldingPage.class;
			}

		});

		if (getGeselecteerdeVerbintenis() != null)
		{
			panel.addButton(editButton);
		}

		return panel;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		replaceBottomRow(createBottomPanel());
	}
}
