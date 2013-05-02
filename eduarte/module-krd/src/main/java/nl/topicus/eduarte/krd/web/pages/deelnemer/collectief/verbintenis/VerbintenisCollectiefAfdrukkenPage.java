package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.verbintenis;

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
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateCategorie;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateContext;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerVerbintenissenWrite;
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
 * Pagina om meerdere verbintenissen tegelijk te beeindigen.
 * 
 * @author idserda
 */
@PageInfo(title = "Verbintenissen collectief afdrukken", menu = {"Deelnemer > Verbintenissen"})
@InPrincipal(DeelnemerVerbintenissenWrite.class)
public class VerbintenisCollectiefAfdrukkenPage extends
		AbstractCollectieveStatusovergangPage<VerbintenisStatus>
{
	private Form<Void> form;

	private EduArteDataPanel<DocumentTemplate> datapanel;

	public VerbintenisCollectiefAfdrukkenPage()
	{
		this(new CollectieveStatusovergangEditModel<VerbintenisStatus>());

		CollectieveStatusovergangEditModel<VerbintenisStatus> model = getStatusovergangModel();
		model.setBeginstatus(VerbintenisStatus.Volledig);
		model.setEindstatus(VerbintenisStatus.Afgedrukt);
	}

	@SuppressWarnings("unchecked")
	private CollectieveStatusovergangEditModel<VerbintenisStatus> getStatusovergangModel()
	{
		return (CollectieveStatusovergangEditModel<VerbintenisStatus>) getDefaultModel();
	}

	public VerbintenisCollectiefAfdrukkenPage(
			CollectieveStatusovergangEditModel<VerbintenisStatus> model)
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
						filter.setContext(DocumentTemplateContext.Verbintenis);
						filter.setCategorie(DocumentTemplateCategorie.Onderwijsovereenkomst);

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
			VerbintenisCollectiefSelectiePage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<DocumentTemplate> item)
			{
				getCollectieveStatusovergangEditModel().setDocumentTemplateModel(item.getModel());
				setResponsePage(new VerbintenisCollectiefSelectiePage(
					getCollectieveStatusovergangEditModel(),
					VerbintenisCollectiefAfdrukkenPage.this));
			}
		});
		add(datapanel);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new TerugButton(panel, VerbintenisCollectiefEditOverzichtPage.class));
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.Verbintenissen);
	}
}
