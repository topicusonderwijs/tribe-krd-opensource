/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.datapanel.columns;

import java.util.List;

import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.providers.PersoonProvider;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.PropertyResolver;

/**
 * @author loite
 */
public class AdresColumn<T extends PersoonProvider> extends AbstractCustomColumn<T>
{
	private static final long serialVersionUID = 1L;

	private static final IModel<String> EMPTY_MODEL = new Model<String>();

	private final String propertyExpression;

	public AdresColumn(String header, String propertyExpression, String propertyOmschrijving,
			boolean repeatWhenEqualToPrevRow)
	{
		super(propertyOmschrijving, header, repeatWhenEqualToPrevRow);
		this.propertyExpression = propertyExpression;
	}

	@Override
	public void populateItem(WebMarkupContainer cellItem, String componentId,
			WebMarkupContainer row, IModel<T> rowModel, int span)
	{
		PersoonProvider persoonProvider = rowModel.getObject();
		IModel<String> adresModel = EMPTY_MODEL;
		List<PersoonAdres> adressen = persoonProvider.getPersoon().getFysiekAdressenOpPeildatum();
		if (adressen.size() > 0)
		{
			if (adressen.size() == 1)
			{
				adresModel =
					new Model<String>(PropertyResolver
						.getValue(propertyExpression, adressen.get(0)).toString());
			}
			else
			{
				StringBuilder res = new StringBuilder();
				boolean first = true;
				for (PersoonAdres adres : adressen)
				{
					if (!first)
					{
						res.append(", ");
					}
					first = false;
					res.append(PropertyResolver.getValue(propertyExpression, adres));
				}
				adresModel = new Model<String>(res.toString());
			}
		}
		cellItem.add(ComponentFactory.getDataLabel(componentId, adresModel));
	}
}
