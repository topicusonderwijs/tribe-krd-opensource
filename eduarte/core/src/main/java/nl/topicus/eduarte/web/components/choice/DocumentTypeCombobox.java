/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.DocumentTypeDataAccessHelper;
import nl.topicus.eduarte.entities.bijlage.DocumentCategorie;
import nl.topicus.eduarte.entities.bijlage.DocumentType;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitToStringRenderer;
import nl.topicus.eduarte.zoekfilters.DocumentTypeZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author loite
 */
public class DocumentTypeCombobox extends AbstractAjaxDropDownChoice<DocumentType>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<DocumentType>>
	{
		private static final long serialVersionUID = 1L;

		private final IModel<DocumentCategorie> categorieModel;

		public ListModel(IModel<DocumentCategorie> categorieModel)
		{
			this.categorieModel = categorieModel;
		}

		@Override
		protected List<DocumentType> load()
		{
			DocumentTypeZoekFilter filter = new DocumentTypeZoekFilter();
			filter.setActief(Boolean.TRUE);
			filter.setCategorie(categorieModel == null ? null : (DocumentCategorie) categorieModel
				.getObject());
			filter.addOrderByProperty("naam");

			return DataAccessRegistry.getHelper(DocumentTypeDataAccessHelper.class).list(filter);
		}
	}

	public DocumentTypeCombobox(String id, IModel<DocumentCategorie> categorieModel)
	{
		this(id, null, categorieModel);
	}

	public DocumentTypeCombobox(String id, IModel<DocumentType> model,
			IModel<DocumentCategorie> categorieModel)
	{
		super(id, model, new ListModel(categorieModel), new EntiteitToStringRenderer());
	}
}
