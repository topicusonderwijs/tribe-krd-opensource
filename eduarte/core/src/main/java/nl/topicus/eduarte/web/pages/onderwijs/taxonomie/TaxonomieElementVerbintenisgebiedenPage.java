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
import nl.topicus.eduarte.core.principals.onderwijs.taxonomie.TaxonomieElementVerbintenisgebiedenZoeken;
import nl.topicus.eduarte.dao.helpers.TaxonomieElementDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
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
 * Pagina die alle direct onderliggende verbintenisgebieden van een taxonomie-element
 * toont.
 * 
 * @author loite
 */
@PageInfo(title = "Taxonomie-element verbintenisgebieden", menu = {"Onderwijs > Taxonomie > [taxonomie-element] > Verbintenisgebieden"})
@InPrincipal(TaxonomieElementVerbintenisgebiedenZoeken.class)
@RechtenSoorten({RechtenSoort.INSTELLING, RechtenSoort.BEHEER})
public class TaxonomieElementVerbintenisgebiedenPage extends AbstractTaxonomieElementPage
{
	private static final long serialVersionUID = 1L;

	private static final TaxonomieElementZoekFilter getDefaultFilter(TaxonomieElement parent)
	{
		TaxonomieElementZoekFilter filter = new TaxonomieElementZoekFilter(Verbintenisgebied.class);
		filter.setTaxonomie(parent.getTaxonomie());
		filter.setParent(parent);
		return filter;
	}

	public TaxonomieElementVerbintenisgebiedenPage(TaxonomieElement taxonomieElement)
	{
		this(taxonomieElement, getDefaultFilter(taxonomieElement));
	}

	public TaxonomieElementVerbintenisgebiedenPage(TaxonomieElement taxonomieElement,
			TaxonomieElementZoekFilter filter)
	{
		super(TaxonomieElementMenuItem.Verbintenisgebieden, ModelFactory
			.getCompoundModel(taxonomieElement));
		Asserts.assertEquals("filter.parent", filter.getParent(), taxonomieElement);
		IDataProvider<TaxonomieElement> provider =
			GeneralDataProvider.of(filter, TaxonomieElementDataAccessHelper.class);
		final CustomDataPanel<TaxonomieElement> datapanel =
			new EduArteDataPanel<TaxonomieElement>("datapanel", provider,
				new TaxonomieElementTable("Verbintenisgebieden"));

		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<TaxonomieElement>(
			TaxonomieElementkaartPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<TaxonomieElement> item)
			{
				TaxonomieElement verbintenisgebied = item.getModelObject();
				pushSearchResultToNavigationLevel(datapanel, item.getIndex());
				setResponsePage(new TaxonomieElementkaartPage(verbintenisgebied));
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
			new ModuleEditPageButton<TaxonomieElement>(panel, "Nieuw verbintenisgebied toevoegen",
				CobraKeyAction.TOEVOEGEN, TaxonomieElement.class, getSelectedMenuItem(),
				TaxonomieElementVerbintenisgebiedenPage.this)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected TaxonomieElement getEntity()
				{
					Verbintenisgebied verbintenisgebied =
						new Verbintenisgebied(EntiteitContext.INSTELLING);
					verbintenisgebied.setTaxonomie(getContextTaxonomie());
					verbintenisgebied
						.setTaxonomieElementType(getContextTaxonomieElement()
							.getTaxonomieElementType().getVerbintenisgebiedChild(
								getContextTaxonomie()));
					verbintenisgebied.setParent(getContextTaxonomieElement());
					return verbintenisgebied;
				}

			};
		toevoegen.setVisible(getContextTaxonomieElement().getTaxonomieElementType()
			.getVerbintenisgebiedChild(getContextTaxonomie()) != null);
		toevoegen.add(new NullableInstellingEntiteitEditLinkVisibleBehavior(
			getContextTaxonomieElementModel()));
		panel.addButton(toevoegen);
	}

}
