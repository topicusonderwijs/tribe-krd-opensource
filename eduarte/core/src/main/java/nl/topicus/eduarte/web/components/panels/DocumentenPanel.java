package nl.topicus.eduarte.web.components.panels;

import java.util.List;

import nl.topicus.cobra.dataproviders.SortableCollectionDataProvider;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelRowFactory;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.entities.bijlage.BijlageEntiteit;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.BijlageLinkColumn;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.ModuleDependentDeleteColumn;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.security.checks.ISecurityCheck;

public class DocumentenPanel<E extends BijlageEntiteit, T extends IBijlageKoppelEntiteit<E>>
		extends TypedPanel<T>
{
	private static final long serialVersionUID = 1L;

	private final class BijlagenModel extends LoadableDetachableModel<List<E>>
	{
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		protected List<E> load()
		{
			T entiteit = getBijlageKoppelEntiteit();
			if (entiteit instanceof Deelnemer)
				return (List<E>) ((Deelnemer) entiteit).getFilteredBijlagen();
			else
				return entiteit.getBijlagen();
		}

		private T getBijlageKoppelEntiteit()
		{
			return getModelObject();
		}
	}

	/**
	 * @param simpleBijlage
	 *            geeft aan dat het om simpele bijlages gaat, die enkel het bestand
	 *            bevatten met een omschrijving
	 */
	@SuppressWarnings("unchecked")
	public DocumentenPanel(String id, IModel<T> model, boolean simpleBijlage)
	{
		super(id, model);
		setOutputMarkupId(true);

		CustomDataPanelContentDescription<BijlageEntiteit> cdpcd =
			new CustomDataPanelContentDescription<BijlageEntiteit>("Documenten");
		cdpcd.addColumn(new CustomPropertyColumn<BijlageEntiteit>("Documenttype", "Documenttype",
			"bijlage.documentType.naam", "bijlage.documentType.naam").setVisible(!simpleBijlage));
		cdpcd.addColumn(new CustomPropertyColumn<BijlageEntiteit>("Documentnummer", "Nr.",
			"bijlage.documentnummer", "bijlage.documentnummer").setVisible(!simpleBijlage));
		cdpcd.addColumn(new CustomPropertyColumn<BijlageEntiteit>("Omschrijving", "Omschrijving",
			"bijlage.omschrijving", "bijlage.omschrijving"));
		cdpcd.addColumn(new CustomPropertyColumn<BijlageEntiteit>("Ontvangstdatum", "Datum",
			"bijlage.ontvangstdatum", "bijlage.ontvangstdatum").setVisible(!simpleBijlage));
		cdpcd.addColumn(new CustomPropertyColumn<BijlageEntiteit>("Geldig tot", "Geldig tot",
			"bijlage.geldigTot", "bijlage.geldigTot").setVisible(!simpleBijlage));
		cdpcd.addColumn(new CustomPropertyColumn<BijlageEntiteit>("Categorie", "Categorie",
			"bijlage.documentType.categorie.naam", "bijlage.documentType.categorie.naam")
			.setVisible(!simpleBijlage));
		cdpcd.addColumn(new CustomPropertyColumn<BijlageEntiteit>("Locatie", "Locatie",
			"bijlage.locatie", "bijlage.locatie").setVisible(!simpleBijlage));
		cdpcd.addColumn(new CustomPropertyColumn<BijlageEntiteit>("Bestandsnaam", "Bestandsnaam",
			"bijlage.bestandsnaam", "bijlage.bestandsnaam").setDefaultVisible(false));
		cdpcd.addColumn(new CustomPropertyColumn<BijlageEntiteit>("Grootte", "Grootte",
			"bijlage.resultaatSizeFormatted", "bijlage.resultaatSizeFormatted"));
		cdpcd.addColumn(new BijlageLinkColumn<BijlageEntiteit>("Download", "Download",
			getBijlageLinkSecurityCheck()));
		if (simpleBijlage)
		{
			cdpcd.addColumn(new ModuleDependentDeleteColumn<BijlageEntiteit>("Verwijder",
				"Verwijder", EduArteModuleKey.ONDERWIJSCATALOGUS)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(WebMarkupContainer item, IModel<BijlageEntiteit> rowModel)
				{
					BijlageEntiteit bijlage = rowModel.getObject();
					bijlage.getEntiteit().getBijlagen().remove(bijlage);
					bijlage.delete();
					bijlage.commit();
				}
			});
		}

		cdpcd.addGroupProperty(new GroupProperty<BijlageEntiteit>("bijlage.documentType.naam",
			"Documenttype", "bijlage.documentType.naam"));
		cdpcd.addGroupProperty(new GroupProperty<BijlageEntiteit>(
			"bijlage.documentType.categorie.naam", "Categorie",
			"bijlage.documentType.categorie.naam"));
		SortableCollectionDataProvider<E> provider =
			new SortableCollectionDataProvider<E>(new BijlagenModel());

		CustomDataPanel<BijlageEntiteit> datapanel =
			new EduArteDataPanel<BijlageEntiteit>("datapanel",
				(IDataProvider<BijlageEntiteit>) provider, cdpcd);
		CustomDataPanelRowFactory<BijlageEntiteit> rowFactory = createRowFactory();
		if (rowFactory != null)
			datapanel.setRowFactory(rowFactory);
		add(datapanel);
	}

	protected CustomDataPanelRowFactory<BijlageEntiteit> createRowFactory()
	{
		return null;
	}

	protected ISecurityCheck getBijlageLinkSecurityCheck()
	{
		return null;
	}
}
