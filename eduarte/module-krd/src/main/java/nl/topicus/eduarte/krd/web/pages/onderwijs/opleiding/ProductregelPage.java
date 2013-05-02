package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import java.util.List;
import java.util.SortedSet;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractPageBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BewerkenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.krd.principals.onderwijs.OpleidingWrite;
import nl.topicus.eduarte.web.behavior.NullableInstellingEntiteitEditLinkVisibleBehavior;
import nl.topicus.eduarte.web.components.menu.OpleidingMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OnderwijsproductTable;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.DeelnemerZoekenPage;
import nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.OnderwijsproductKaartPage;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.AbstractOpleidingPage;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Detailpagina van een productregel. Dit kan zowel een landelijke als een
 * opleidingsspecifieke productregel zijn.
 * 
 * @author loite
 */
@PageInfo(title = "Productregel", menu = {})
@InPrincipal(OpleidingWrite.class)
@RechtenSoorten( {RechtenSoort.INSTELLING, RechtenSoort.BEHEER})
public class ProductregelPage extends AbstractOpleidingPage
{
	private static final long serialVersionUID = 1L;

	private final SecurePage returnToPage;

	/**
	 * Class is static gemaakt en de models zijn erin geplaatst omdat het model ook
	 * indirect gebruikt wordt op andere pagina's als dataprovider.
	 * 
	 * @author loite
	 */
	private static final class ProductenModel extends
			LoadableDetachableModel<SortedSet<Onderwijsproduct>>
	{
		private static final long serialVersionUID = 1L;

		private final IModel<Opleiding> opleidingModel;

		private final IModel<Productregel> productregelModel;

		private ProductenModel(IModel<Opleiding> opleidingModel,
				IModel<Productregel> productregelModel)
		{
			this.opleidingModel = opleidingModel;
			this.productregelModel = productregelModel;
		}

		@Override
		protected SortedSet<Onderwijsproduct> load()
		{
			return getProductregel().getOnderwijsproducten(getOpleiding());
		}

		private Productregel getProductregel()
		{
			return productregelModel.getObject();
		}

		private Opleiding getOpleiding()
		{
			return opleidingModel.getObject();
		}

		@Override
		protected void onDetach()
		{
			ComponentUtil.detachQuietly(opleidingModel);
			ComponentUtil.detachQuietly(productregelModel);
		}

	}

	public ProductregelPage(Opleiding opleiding, Productregel productregel)
	{
		this(opleiding, productregel, null);
	}

	public ProductregelPage(Opleiding opleiding, Productregel productregel, SecurePage returnToPage)
	{
		super(OpleidingMenuItem.Productregels, opleiding);
		this.returnToPage = returnToPage;
		setDefaultModel(ModelFactory.getCompoundModel(productregel));
		add(ComponentFactory.getDataLabel("typeProductregel"));
		add(ComponentFactory.getDataLabel("volgnummer"));
		add(ComponentFactory.getDataLabel("afkorting"));
		add(ComponentFactory.getDataLabel("naam"));
		add(ComponentFactory.getDataLabel("soortProductregel.naam"));
		add(ComponentFactory.getDataLabel("verplichtOmschrijving"));
		add(ComponentFactory.getDataLabel("cohort.naam"));

		// Voeg een lijst van de onderwijsproducten toe.
		CollectionDataProvider<Onderwijsproduct> provider =
			new CollectionDataProvider<Onderwijsproduct>(new ProductenModel(
				getContextOpleidingModel(), getProductregelModel()));
		final EduArteDataPanel<Onderwijsproduct> producten =
			new EduArteDataPanel<Onderwijsproduct>("onderwijsproducten", provider,
				new OnderwijsproductTable(false));
		producten.setRowFactory(new CustomDataPanelPageLinkRowFactory<Onderwijsproduct>(
			OnderwijsproductKaartPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<Onderwijsproduct> item)
			{
				Onderwijsproduct product = item.getModelObject();
				pushSearchResultToNavigationLevel(producten, item.getIndex());
				setResponsePage(new OnderwijsproductKaartPage(product));
			}

		});
		producten.setItemsPerPage(Integer.MAX_VALUE);
		add(producten);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.setDefaultModel(getContextOpleidingModel());
		BewerkenButton<Void> bewerken = new BewerkenButton<Void>(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new EditProductregelPage(getProductregel(), ProductregelPage.this);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return EditProductregelPage.class;
			}

		});
		bewerken.add(new NullableInstellingEntiteitEditLinkVisibleBehavior(getProductregelModel()));
		// als de productregel niet hoort bij deze opleiding(svariant) maar bij de parent,
		// dan kan de productregel niet worden bewerkt
		bewerken.setVisible(getContextOpleiding().equals(getProductregel().getOpleiding()));
		panel.addButton(bewerken);
		if (returnToPage != null)
			panel.addButton(new TerugButton(panel, returnToPage));
		AbstractPageBottomRowButton gekoppeldeDeelnemersButton =
			new AbstractPageBottomRowButton(panel, "Gekoppelde deelnemers", CobraKeyAction.GEEN,
				ButtonAlignment.LEFT, new IPageLink()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public Page getPage()
					{
						VerbintenisZoekFilter filter = DeelnemerZoekenPage.getDefaultFilter();
						filter.setProductregel(getProductregel());

						// zorg ervoor dat alle gekoppelde deelnemers getoond worden, ook
						// als de betreffende verbintenis (in het verleden) beeindigd is
						filter.setPeildatum(TimeUtil.getInstance().getMinDate());

						return new DeelnemerZoekenPage(filter);
					}

					@Override
					public Class<DeelnemerZoekenPage> getPageIdentity()
					{
						return DeelnemerZoekenPage.class;
					}

				});
		panel.addButton(gekoppeldeDeelnemersButton);
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(Productregel.class);
		ctorArgValues.add(getDefaultModel());
	}

	private Productregel getProductregel()
	{
		return (Productregel) getDefaultModelObject();
	}

	@SuppressWarnings("unchecked")
	public IModel<Productregel> getProductregelModel()
	{
		return (IModel<Productregel>) getDefaultModel();
	}
}
