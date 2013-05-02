package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.bpv;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.dao.helpers.DocumentTemplateDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateCategorie;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateContext;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerBPVWrite;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.AbstractCollectieveStatusovergangPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.CollectieveStatusovergangEditModel;
import nl.topicus.eduarte.web.components.choice.TaxonomieCombobox;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DocumentTemplateTable;
import nl.topicus.eduarte.zoekfilters.DocumentTemplateZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Pagina om overeenkomst voor meerdere BPV's af te drukken.
 * 
 * @author idserda
 */
@PageInfo(title = "BPV inschrijving collectief afdrukken", menu = {"Deelnemer > Collectief > BPV's"})
@InPrincipal(DeelnemerBPVWrite.class)
public class BPVInschrijvingCollectiefAfdrukkenPage extends
		AbstractCollectieveStatusovergangPage<BPVStatus>
{
	private Form<Void> form;

	private EduArteDataPanel<DocumentTemplate> datapanel;

	public BPVInschrijvingCollectiefAfdrukkenPage()
	{
		this(new CollectieveStatusovergangEditModel<BPVStatus>());

		CollectieveStatusovergangEditModel<BPVStatus> model = getStatusovergangModel();
		model.setBeginstatus(BPVStatus.Volledig);
		model.setEindstatus(BPVStatus.OvereenkomstAfgedrukt);
	}

	@SuppressWarnings("unchecked")
	private CollectieveStatusovergangEditModel<BPVStatus> getStatusovergangModel()
	{
		return (CollectieveStatusovergangEditModel<BPVStatus>) getDefaultModel();
	}

	public BPVInschrijvingCollectiefAfdrukkenPage(
			CollectieveStatusovergangEditModel<BPVStatus> model)
	{
		super(model);

		form = new Form<Void>("form");

		createTaxonomieCombobox("taxonomie");
		createDatapanel("datapanel");

		add(form);

		createComponents();
	}

	private void createTaxonomieCombobox(String id)
	{
		TaxonomieCombobox combobox =
			new TaxonomieCombobox(id, new PropertyModel<Taxonomie>(getDefaultModel(), "taxonomie"))
			{

				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target, Taxonomie newSelection)
				{
					target.addComponent(datapanel);
				}

			};
		form.add(combobox);
	}

	private void createDatapanel(String id)
	{
		LoadableDetachableModel<List<DocumentTemplate>> listmodel =
			new LoadableDetachableModel<List<DocumentTemplate>>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected List<DocumentTemplate> load()
				{
					if (getTaxonomie() != null)
					{
						DocumentTemplateZoekFilter filter = new DocumentTemplateZoekFilter();
						filter.setTaxonomie(getTaxonomie());
						filter.setAccount(getIngelogdeAccount());
						filter.setContext(DocumentTemplateContext.BPVVerbintenis);
						filter.setCategorie(DocumentTemplateCategorie.BPVOvereenkomst);

						return DataAccessRegistry.getHelper(DocumentTemplateDataAccessHelper.class)
							.list(filter);
					}
					else
					{
						return new ArrayList<DocumentTemplate>();
					}
				}

			};

		ListModelDataProvider<DocumentTemplate> provider =
			new ListModelDataProvider<DocumentTemplate>(listmodel);

		datapanel =
			new EduArteDataPanel<DocumentTemplate>(id, provider, new DocumentTemplateTable());

		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<DocumentTemplate>(
			BPVInschrijvingCollectiefSelectiePage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<DocumentTemplate> item)
			{
				getCollectieveStatusovergangEditModel().setDocumentTemplateModel(item.getModel());
				setResponsePage(new BPVInschrijvingCollectiefSelectiePage(
					getCollectieveStatusovergangEditModel(),
					BPVInschrijvingCollectiefAfdrukkenPage.this));
			}
		});
		add(datapanel);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new TerugButton(panel, BPVInschrijvingCollectiefEditOverzichtPage.class));
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.BPVs);
	}
}
