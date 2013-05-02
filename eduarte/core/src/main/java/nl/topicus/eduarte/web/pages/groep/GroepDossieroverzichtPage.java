/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.groep;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.cobra.web.components.datapanel.columns.BorderColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.core.principals.groep.GroepDocumenten;
import nl.topicus.eduarte.dao.helpers.DocumentCategorieDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.DocumentTypeDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.GroepsdeelnameDataAccessHelper;
import nl.topicus.eduarte.entities.bijlage.DocumentCategorie;
import nl.topicus.eduarte.entities.bijlage.DocumentType;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.providers.GroepProvider;
import nl.topicus.eduarte.web.components.menu.GroepMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.filter.GroepsdeelnameZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.DocumentCategorieZoekFilter;
import nl.topicus.eduarte.zoekfilters.DocumentTypeZoekFilter;
import nl.topicus.eduarte.zoekfilters.GroepsdeelnameZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Dossieroverzicht", menu = "Groep > [groep] -> Dossiers")
@InPrincipal(GroepDocumenten.class)
public class GroepDossieroverzichtPage extends AbstractGroepPage
{
	private static final long serialVersionUID = 1L;

	private static final class DossierCategorieColumn extends AbstractCustomColumn<Groepsdeelname>
			implements BorderColumn<Groepsdeelname>
	{
		private static final long serialVersionUID = 1L;

		private final IModel<DocumentCategorie> categorie;

		private DocumentTypeZoekFilter filter = new DocumentTypeZoekFilter();

		private IModel<List<DocumentType>> types;

		public DossierCategorieColumn(DocumentCategorie categorie)
		{
			super(categorie.getNaam() + " (categorie)", categorie.getNaam());
			filter = new DocumentTypeZoekFilter();
			filter.setActief(true);
			filter.setCategorie(categorie);
			types = new LoadableDetachableModel<List<DocumentType>>()
			{

				private static final long serialVersionUID = 1L;

				@Override
				protected List<DocumentType> load()
				{
					return DataAccessRegistry.getHelper(DocumentTypeDataAccessHelper.class).list(
						filter);
				}
			};
			this.categorie = ModelFactory.getModel(categorie);
		}

		@Override
		public void populateItem(WebMarkupContainer cell, String componentId,
				WebMarkupContainer row, IModel<Groepsdeelname> rowModel, int span)
		{
			Groepsdeelname deelname = rowModel.getObject();

			List<String> typesList = new ArrayList<String>();

			for (DocumentType docType : types.getObject())
			{
				int aantal = deelname.getDeelnemer().getAantalBijlagenVanType(docType);
				if (aantal == 1)
				{
					typesList.add(docType.getNaam());
				}
				else if (aantal > 1)
				{
					typesList.add("" + docType.getNaam() + " (" + aantal + ")");
				}
			}
			cell.add(new Label(componentId, StringUtil.maakCommaSeparatedString(typesList)));
		}

		@Override
		public boolean hasBorder()
		{
			return true;
		}

		@Override
		public void detach()
		{
			super.detach();
			ComponentUtil.detachQuietly(categorie);
			ComponentUtil.detachQuietly(types);
			ComponentUtil.detachQuietly(filter);
		}

	}

	private static final class DossierColumn extends AbstractCustomColumn<Groepsdeelname> implements
			BorderColumn<Groepsdeelname>
	{
		private static final long serialVersionUID = 1L;

		private final IModel<DocumentType> type;

		public DossierColumn(DocumentType type)
		{
			super(type.getNaam(), type.getCode());
			this.type = ModelFactory.getModel(type);
		}

		@Override
		public void populateItem(WebMarkupContainer cell, String componentId,
				WebMarkupContainer row, IModel<Groepsdeelname> rowModel, int span)
		{
			Groepsdeelname deelname = rowModel.getObject();
			DocumentType docType = type.getObject();
			int aantal = deelname.getDeelnemer().getAantalBijlagenVanType(docType);
			String res = aantal == 0 ? "N" : "J";
			if (aantal > 1)
				res = "J (" + aantal + ")";
			cell.add(new Label(componentId, res));
		}

		@Override
		public boolean hasBorder()
		{
			return true;
		}

		@Override
		public void detach()
		{
			super.detach();
			ComponentUtil.detachQuietly(type);
		}

	}

	/**
	 * Bookmarkable constructor.
	 * 
	 * @see AbstractGroepPage#getGroepFromPageParameters(PageParameters)
	 */
	public GroepDossieroverzichtPage(PageParameters parameters)
	{
		this(AbstractGroepPage.getGroepFromPageParameters(parameters));
	}

	public GroepDossieroverzichtPage(GroepProvider provider)
	{
		this(provider.getGroep());
	}

	public GroepDossieroverzichtPage(Groep groep)
	{
		super(GroepMenuItem.Dossieroverzicht, groep);
		// Bepaal de verschillende soorten documenten.
		List<DocumentCategorie> categorieen =
			DataAccessRegistry.getHelper(DocumentCategorieDataAccessHelper.class).list(
				new DocumentCategorieZoekFilter());

		GroepsdeelnameZoekFilter deelnameFilter = new GroepsdeelnameZoekFilter(groep);
		deelnameFilter.addOrderByProperty("deelnemer.deelnemernummer");
		deelnameFilter.addOrderByProperty("persoon.roepnaam");
		deelnameFilter.addOrderByProperty("persoon.achternaam");
		deelnameFilter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			this));
		IDataProvider<Groepsdeelname> provider =
			GeneralFilteredSortableDataProvider.of(deelnameFilter,
				GroepsdeelnameDataAccessHelper.class);
		CustomDataPanelContentDescription<Groepsdeelname> table =
			new CustomDataPanelContentDescription<Groepsdeelname>("Dossierstukken");
		table.addColumn(new CustomPropertyColumn<Groepsdeelname>("Deelnemernummer", "Nummer",
			"deelnemer.deelnemernummer", "deelnemer.deelnemernummer"));
		table.addColumn(new CustomPropertyColumn<Groepsdeelname>("Naam", "Naam",
			"persoon.achternaam", "deelnemer.persoon.volledigeNaam"));
		for (DocumentCategorie categorie : categorieen)
		{
			table.addColumn(new DossierCategorieColumn(categorie));
		}

		DocumentTypeZoekFilter filter = new DocumentTypeZoekFilter();
		filter.setActief(Boolean.TRUE);
		filter.addOrderByProperty("naam");
		List<DocumentType> types =
			DataAccessRegistry.getHelper(DocumentTypeDataAccessHelper.class).list(filter);
		for (DocumentType type : types)
			table.addColumn(new DossierColumn(type).setDefaultVisible(false));

		final CustomDataPanel<Groepsdeelname> datapanel =
			new EduArteDataPanel<Groepsdeelname>("datapanel", provider, table);
		datapanel.setItemsPerPage(35);
		add(datapanel);

		GroepsdeelnameZoekFilterPanel filterPanel =
			new GroepsdeelnameZoekFilterPanel("filter", deelnameFilter, datapanel);
		add(filterPanel);

		createComponents();
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}
}
