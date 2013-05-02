/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.templates;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.modelsv2.ReadOnlyListPropertyModel;
import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteDocumentTemplateModule;
import nl.topicus.eduarte.dao.helpers.DocumentTemplateDataAccessHelper;
import nl.topicus.eduarte.entities.bijlage.BijlageEntiteit;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateCategorie;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateContext;
import nl.topicus.eduarte.web.components.link.IRapportageReturnPage;
import nl.topicus.eduarte.web.components.link.MultiEntityRapportageSelectieTarget;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.MultiEntityRapportageGenereerLinkColumn;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.DocumentTemplateZoekFilter;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class RapportagesPanel<R extends IBijlageKoppelEntiteit< ? extends BijlageEntiteit>, S extends IdObject, ZF extends DetachableZoekFilter<S>>
		extends Panel
{
	private static final long serialVersionUID = 1L;

	private DocumentTemplateContext context;

	public RapportagesPanel(String id, DocumentTemplateContext context,
			DocumentTemplateCategorie... categorieen)
	{
		super(id);
		this.context = context;
		setRenderBodyOnly(true);

		RepeatingView datapanels = new RepeatingView("datapanels");
		add(datapanels);

		for (DocumentTemplateCategorie categorie : categorieen)
		{
			if (categorie.getBeperkteContext() == null || categorie.getBeperkteContext() == context)
			{
				EduArteDataPanel<DocumentTemplate> datapanel =
					addDataPanel(datapanels.newChildId(), categorie);
				datapanels.add(datapanel);
			}
		}
	}

	private EduArteDataPanel<DocumentTemplate> addDataPanel(String id,
			final DocumentTemplateCategorie categorie)
	{
		IModel<List<DocumentTemplate>> templatesModel =
			new LoadableDetachableModel<List<DocumentTemplate>>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected List<DocumentTemplate> load()
				{
					DocumentTemplateZoekFilter filter = getDefaultZoekFilter(categorie);
					List<DocumentTemplate> templates = new ArrayList<DocumentTemplate>();
					templates.addAll(DataAccessRegistry.getHelper(
						DocumentTemplateDataAccessHelper.class).list(filter));
					for (EduArteDocumentTemplateModule module : EduArteApp.get().getActiveModules(
						EduArteDocumentTemplateModule.class, EduArteContext.get().getOrganisatie()))
					{
						for (DocumentTemplate curTemplate : module.getRegisteredDocumentTemplates())
							if (curTemplate.matchesFilter(filter))
								templates.add(curTemplate);
					}
					return templates;
				}
			};
		ListModelDataProvider<DocumentTemplate> provider =
			new ListModelDataProvider<DocumentTemplate>(templatesModel)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<DocumentTemplate> model(DocumentTemplate object)
				{
					IModel<DocumentTemplate> ret =
						new ReadOnlyListPropertyModel<DocumentTemplate>(getListModel());
					ret.setObject(object);
					return ret;
				}
			};
		return new EduArteDataPanel<DocumentTemplate>(id, provider, new DataPanelTable(
			categorie.name()));

	}

	protected DocumentTemplateZoekFilter getDefaultZoekFilter(DocumentTemplateCategorie categorie)
	{
		DocumentTemplateZoekFilter filter = new DocumentTemplateZoekFilter();
		filter.setValide(true);
		filter.setAccount(EduArteContext.get().getAccount());
		filter.setContext(context);
		filter.setCategorie(categorie);
		filter.addOrderByProperty("omschrijving");
		return filter;
	}

	@SuppressWarnings("unchecked")
	private IRapportageReturnPage<R, S, ZF> getSuperPage()
	{
		return (IRapportageReturnPage<R, S, ZF>) getPage();
	}

	private class DataPanelTable extends CustomDataPanelContentDescription<DocumentTemplate>
	{
		private static final long serialVersionUID = 1L;

		public DataPanelTable(String title)
		{
			super(title);
			addColumn(new CustomPropertyColumn<DocumentTemplate>("Omschrijving", "Omschrijving",
				"Omschrijving"));
			addColumn(new MultiEntityRapportageGenereerLinkColumn<DocumentTemplate>("Download1", "")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void populateItem(WebMarkupContainer cellItem, String componentId,
						WebMarkupContainer row, IModel<DocumentTemplate> rowModel, int span)
				{
					super.populateItem(cellItem, componentId, row, rowModel, span);
					DocumentTemplate modelObject = (rowModel.getObject());

					// niet tonen wanneer dit niet mag van het document
					cellItem.get(componentId).setVisible(
						modelObject.getForceerType() == null
							|| !modelObject.getForceerType().equals(DocumentTemplateType.PDF));
				}

				/**
				 * Functie die een linkcolumn maakt die redirect naar een selectie pagina,
				 * inclusief return links en genereer button.
				 * 
				 * @see nl.topicus.eduarte.web.components.panels.datapanel.columns.
				 *      MultiEntityRapportageGenereerLinkColumn#getRedirectLink(String,
				 *      org.apache.wicket.model.IModel)
				 */
				@Override
				protected Link<Void> getRedirectLink(String id,
						final IModel<DocumentTemplate> rowModel)
				{
					Link<Void> ipl = new Link<Void>(id)
					{
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick()
						{
							DatabaseSelection<R, S> selection = getSuperPage().createSelection();
							getSuperPage().setResponsePage(
								selection,
								new MultiEntityRapportageSelectieTarget<R, S, ZF>(rowModel,
									(SecurePage) getPage(), getEindType()));
						}
					};

					return ipl;
				}
			});
			addColumn(new MultiEntityRapportageGenereerLinkColumn<DocumentTemplate>("Download2",
				"", DocumentTemplateType.PDF)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void populateItem(WebMarkupContainer cellItem, String componentId,
						WebMarkupContainer row, IModel<DocumentTemplate> rowModel, int span)
				{
					super.populateItem(cellItem, componentId, row, rowModel, span);
					DocumentTemplate modelObject = (rowModel.getObject());

					// niet tonen wanneer we zowiezo al een pdf maken.
					cellItem.get(componentId).setVisible(
						!modelObject.getType().equals(DocumentTemplateType.PDF)
							&& !modelObject.getType().equals(DocumentTemplateType.JRXML));
				}

				/**
				 * Functie welke een linkcolumn maakt die redirect naar een selectie
				 * pagina, inclusief return links en genereer button.
				 * 
				 * @see nl.topicus.eduarte.web.components.panels.datapanel.columns.
				 *      MultiEntityRapportageGenereerLinkColumn#getRedirectLink(String,
				 *      org.apache.wicket.model.IModel)
				 */
				@Override
				protected Link<Void> getRedirectLink(String id,
						final IModel<DocumentTemplate> rowModel)
				{
					Link<Void> ipl = new Link<Void>(id)
					{
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick()
						{
							DatabaseSelection<R, S> selection = getSuperPage().createSelection();
							getSuperPage().setResponsePage(
								selection,
								new MultiEntityRapportageSelectieTarget<R, S, ZF>(rowModel,
									(SecurePage) getPage(), getEindType()));
						}
					};

					return ipl;
				}
			});
		}
	}
}
