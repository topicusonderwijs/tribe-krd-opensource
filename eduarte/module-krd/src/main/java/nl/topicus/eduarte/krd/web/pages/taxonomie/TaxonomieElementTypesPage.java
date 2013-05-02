package nl.topicus.eduarte.krd.web.pages.taxonomie;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractAjaxLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.eduarte.core.principals.onderwijs.taxonomie.TaxonomieElementTypeRead;
import nl.topicus.eduarte.dao.helpers.TaxonomieElementTypeDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.taxonomie.SoortTaxonomieElement;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElementType;
import nl.topicus.eduarte.web.behavior.NullableInstellingEntiteitEditLinkVisibleBehavior;
import nl.topicus.eduarte.web.components.menu.TaxonomieElementMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.taxonomie.EditTaxonomieElementTypeModalWindow;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.TaxonomieElementTypeTable;
import nl.topicus.eduarte.web.pages.onderwijs.taxonomie.AbstractTaxonomieElementPage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Pagina die de taxonomieelementtypes van een bepaalde taxonomie weergeeft. Deze pagina
 * wordt alleen weergegeven op het moment dat een taxonomie geselecteerd is.
 * 
 * @author loite
 */
@PageInfo(title = "Taxonomie-elementtypes", menu = {"Onderwijs > Taxonomie > [taxonomie] > Taxonomie-elementtypes"})
@InPrincipal(TaxonomieElementTypeRead.class)
@RechtenSoorten( {RechtenSoort.INSTELLING, RechtenSoort.BEHEER})
public class TaxonomieElementTypesPage extends AbstractTaxonomieElementPage
{
	private static final long serialVersionUID = 1L;

	private final class ListModel extends LoadableDetachableModel<List<TaxonomieElementType>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<TaxonomieElementType> load()
		{
			Taxonomie taxonomie = (Taxonomie) getContextTaxonomieElement();
			return DataAccessRegistry.getHelper(TaxonomieElementTypeDataAccessHelper.class).list(
				taxonomie, null);
		}

	}

	private final EditTaxonomieElementTypeModalWindow modalWindow;

	/**
	 * Constructor die nodig is om aan de afspraak van het taxonomieelementmenu te kunnen
	 * voldoen. Hier mag alleen een taxonomie meegegeven worden.
	 * 
	 * @param taxonomieElement
	 *            De taxonomie waarvoor de types getoond moeten worden.
	 */
	public TaxonomieElementTypesPage(TaxonomieElement taxonomieElement)
	{
		super(TaxonomieElementMenuItem.TaxonomieElementTypes, ModelFactory
			.getCompoundModel(taxonomieElement));
		Asserts.assertEquals("taxonomieElement.class", taxonomieElement.doUnproxy().getClass(),
			Taxonomie.class);

		CollectionDataProvider<TaxonomieElementType> provider =
			new CollectionDataProvider<TaxonomieElementType>(new ListModel());
		final WebMarkupContainer datapanelContainer = new WebMarkupContainer("datapanelContainer");
		datapanelContainer.setOutputMarkupId(true);
		add(datapanelContainer);
		CustomDataPanel<TaxonomieElementType> datapanel =
			new EduArteDataPanel<TaxonomieElementType>("datapanel", provider,
				new TaxonomieElementTypeTable());
		datapanel.setRowFactory(new CustomDataPanelAjaxClickableRowFactory<TaxonomieElementType>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target, Item<TaxonomieElementType> item)
			{
				target.addComponent(datapanelContainer);
				modalWindow.show(target, item.getModelObject());
			}

		});
		datapanelContainer.add(datapanel);
		modalWindow = new EditTaxonomieElementTypeModalWindow("modalWindow");
		modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				// Refresh het datapanel.
				target.addComponent(datapanelContainer);
			}

		});
		add(modalWindow);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		AbstractAjaxLinkButton verbintenisgebiedToevoegen =
			new AbstractAjaxLinkButton(panel, "Verbintenisgebied toevoegen", null,
				ButtonAlignment.RIGHT)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onClick(AjaxRequestTarget target)
				{
					TaxonomieElementType type =
						new TaxonomieElementType(EntiteitContext.INSTELLING);
					type.setSoort(SoortTaxonomieElement.Verbintenisgebied);
					type.setTaxonomie(getContextTaxonomie());
					type.setParent(getContextTaxonomie().getOndersteVerbintenisgebied());
					type.setVolgnummer(getContextTaxonomie()
						.getHoogsteTaxonomieElementTypeVolgnummer() + 1);
					modalWindow.show(target, type);
				}

			};
		verbintenisgebiedToevoegen.add(new NullableInstellingEntiteitEditLinkVisibleBehavior(
			getContextTaxonomieElementModel()));
		panel.addButton(verbintenisgebiedToevoegen);
		AbstractAjaxLinkButton deelgebiedToevoegen =
			new AbstractAjaxLinkButton(panel, "Deelgebied toevoegen", null, ButtonAlignment.RIGHT)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onClick(AjaxRequestTarget target)
				{
					TaxonomieElementType type =
						new TaxonomieElementType(EntiteitContext.INSTELLING);
					type.setSoort(SoortTaxonomieElement.Deelgebied);
					type.setTaxonomie(getContextTaxonomie());
					type.setParent(getContextTaxonomie().getOndersteDeelgebied());
					type.setVolgnummer(getContextTaxonomie()
						.getHoogsteTaxonomieElementTypeVolgnummer() + 1);
					modalWindow.show(target, type);
				}

			};
		deelgebiedToevoegen.add(new NullableInstellingEntiteitEditLinkVisibleBehavior(
			getContextTaxonomieElementModel()));
		panel.addButton(deelgebiedToevoegen);
	}
}
