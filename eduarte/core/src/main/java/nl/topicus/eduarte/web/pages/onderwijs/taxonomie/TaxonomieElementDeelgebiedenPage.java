package nl.topicus.eduarte.web.pages.onderwijs.taxonomie;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.core.principals.onderwijs.taxonomie.TaxonomieElementDeelgebiedenZoeken;
import nl.topicus.eduarte.dao.helpers.TaxonomieElementDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.taxonomie.Deelgebied;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.web.behavior.NullableInstellingEntiteitEditLinkVisibleBehavior;
import nl.topicus.eduarte.web.components.menu.TaxonomieElementMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.TaxonomieElementTable;
import nl.topicus.eduarte.web.components.panels.filter.TaxonomieElementZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.TaxonomieElementZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

/**
 * Pagina die alle direct onderliggende deelgebieden van een taxonomie-element toont.
 * 
 * @author loite
 */
@PageInfo(title = "Taxonomie-element deelgebieden", menu = {"Onderwijs > Taxonomie > [taxonomie-element] > Deelgebieden"})
@InPrincipal(TaxonomieElementDeelgebiedenZoeken.class)
@RechtenSoorten({RechtenSoort.INSTELLING, RechtenSoort.BEHEER})
public class TaxonomieElementDeelgebiedenPage extends AbstractTaxonomieElementPage
{
	private static final long serialVersionUID = 1L;

	private static final TaxonomieElementZoekFilter getDefaultFilter(TaxonomieElement parent)
	{
		TaxonomieElementZoekFilter filter = new TaxonomieElementZoekFilter(Deelgebied.class);
		filter.setParent(parent);
		filter.setTaxonomie(parent.getTaxonomie());
		return filter;
	}

	public TaxonomieElementDeelgebiedenPage(TaxonomieElement taxonomieElement)
	{
		this(taxonomieElement, getDefaultFilter(taxonomieElement));
	}

	public TaxonomieElementDeelgebiedenPage(TaxonomieElement taxonomieElement,
			TaxonomieElementZoekFilter filter)
	{
		super(TaxonomieElementMenuItem.Deelgebieden, ModelFactory
			.getCompoundModel(taxonomieElement));
		Asserts.assertEquals("filter.parent", filter.getParent(), taxonomieElement);
		IDataProvider<TaxonomieElement> provider =
			GeneralDataProvider.of(filter, TaxonomieElementDataAccessHelper.class);
		final CustomDataPanel<TaxonomieElement> datapanel =
			new EduArteDataPanel<TaxonomieElement>("datapanel", provider,
				new TaxonomieElementTable("Deelgebieden"));
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<TaxonomieElement>(
			TaxonomieElementkaartPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<TaxonomieElement> item)
			{
				TaxonomieElement deelgebied = item.getModelObject();
				pushSearchResultToNavigationLevel(datapanel, item.getIndex());
				setResponsePage(new TaxonomieElementkaartPage(deelgebied));
			}

		});
		add(datapanel);
		TaxonomieElementZoekFilterPanel filterPanel =
			new TaxonomieElementZoekFilterPanel("filter", filter, datapanel, false);
		add(filterPanel);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		ModuleEditPageButton<TaxonomieElement> toevoegen =
			new ModuleEditPageButton<TaxonomieElement>(panel, "Nieuw deelgebied toevoegen",
				CobraKeyAction.TOEVOEGEN, TaxonomieElement.class, getSelectedMenuItem(),
				TaxonomieElementDeelgebiedenPage.this)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected TaxonomieElement getEntity()
				{
					Deelgebied deelgebied = new Deelgebied(EntiteitContext.INSTELLING);
					deelgebied.setTaxonomie(getContextTaxonomie());
					deelgebied.setTaxonomieElementType(getContextTaxonomieElement()
						.getTaxonomieElementType().getDeelgebiedChild(getContextTaxonomie()));
					deelgebied.setParent(getContextTaxonomieElement());
					return deelgebied;
				}

			};
		toevoegen.setVisible(getContextTaxonomieElement().getTaxonomieElementType()
			.getDeelgebiedChild(getContextTaxonomie()) != null);
		toevoegen.add(new NullableInstellingEntiteitEditLinkVisibleBehavior(
			getContextTaxonomieElementModel()));
		panel.addButton(toevoegen);
	}

}
