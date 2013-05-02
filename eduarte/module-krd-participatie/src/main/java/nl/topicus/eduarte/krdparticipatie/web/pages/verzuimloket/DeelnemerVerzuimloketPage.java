package nl.topicus.eduarte.krdparticipatie.web.pages.verzuimloket;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.eduarte.dao.participatie.helpers.VerzuimmeldingDataAccessHelper;
import nl.topicus.eduarte.entities.ibgverzuimloket.IbgVerzuimmelding;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krdparticipatie.principals.deelnemer.DeelnemerVerzuimloketWrite;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieDeelnemerMenuItem;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.filter.DeelnemerVerzuimloketZoekfilterPanel;
import nl.topicus.eduarte.krdparticipatie.web.components.tables.DeelnemerVerzuimmeldingTable;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.zoekfilters.DeelnemerVerzuimloketZoekfilter;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Relatief verzuim", menu = "Verzuimloket")
@InPrincipal(DeelnemerVerzuimloketWrite.class)
public class DeelnemerVerzuimloketPage extends AbstractDeelnemerPage
{
	private Panel zoekPanel;

	private EduArteDataPanel<IbgVerzuimmelding> dataPanel;

	private VerzuimmeldingDetailPanel detailPanel;

	private WebMarkupContainer panelContainer;

	private PageLinkButton newButton;

	private PageLinkButton editButton;

	public DeelnemerVerzuimloketPage(PageParameters parameters)
	{
		this(getDeelnemerFromPageParameters(DeelnemerVerzuimloketPage.class, parameters));
	}

	public DeelnemerVerzuimloketPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer(), provider.getDeelnemer()
			.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerVerzuimloketPage(Verbintenis verbintenis)
	{
		this(verbintenis.getDeelnemer(), verbintenis);
	}

	public DeelnemerVerzuimloketPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerVerzuimloketPage(Deelnemer deelnemer, Verbintenis inschrijving)
	{
		super(ParticipatieDeelnemerMenuItem.Verzuimloket, deelnemer, inschrijving);
		DeelnemerVerzuimloketZoekfilter verzuimmeldingFilter =
			new DeelnemerVerzuimloketZoekfilter(inschrijving);

		IDataProvider<IbgVerzuimmelding> verzuimmeldingProvider =
			GeneralDataProvider.of(verzuimmeldingFilter, VerzuimmeldingDataAccessHelper.class);

		panelContainer = new WebMarkupContainer("panelContainer");
		panelContainer.setOutputMarkupId(true);

		zoekPanel = new DeelnemerVerzuimloketZoekfilterPanel("filter", verzuimmeldingFilter);

		dataPanel =
			new EduArteDataPanel<IbgVerzuimmelding>("datapanel", verzuimmeldingProvider,
				new DeelnemerVerzuimmeldingTable());
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

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		addBewerkenButton(panel);
		addNieuweMeldingButton(panel);

	}

	public void addNieuweMeldingButton(BottomRowPanel panel)
	{
		newButton = new PageLinkButton(panel, "Melding Toevoegen", new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new EditIbgVerzuimmeldingPersonaliaPage(getContextVerbintenis(),
					DeelnemerVerzuimloketPage.this);
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return EditIbgVerzuimmeldingPersonaliaPage.class;
			}

		});
		panel.addButton(newButton);
	}

	private void addBewerkenButton(BottomRowPanel panel)
	{
		editButton = new PageLinkButton(panel, "Bewerken", new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				IbgVerzuimmelding melding = (IbgVerzuimmelding) detailPanel.getDefaultModelObject();
				return new EditIbgVerzuimmeldingMeldingPage(getContextVerbintenis(), melding,
					DeelnemerVerzuimloketPage.this);
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return EditIbgVerzuimmeldingMeldingPage.class;
			}

		});
		editButton.setVisible(false);
		panel.addButton(editButton);
	}
}
