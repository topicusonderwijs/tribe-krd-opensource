/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.datapanel;

import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.link.SecureLink;
import nl.topicus.eduarte.entities.IContactgegevenEntiteit;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.Validatable;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.UrlValidator;

/**
 * Column welke de waarde van een {@link IContactgegevenEntiteit} toont. * Deze toont een
 * link wanneer dit mogelijk is of anders gewoon een label.
 * 
 * @author hoeve
 */
public class ContactgegevenWaardePropertyColumn<T extends IContactgegevenEntiteit> extends
		CustomPropertyColumn<T>
{
	private static final long serialVersionUID = 1L;

	private final boolean verbergGeheim;

	public ContactgegevenWaardePropertyColumn(String id, String header, String sortProperty,
			String propertyExpression, boolean verbergGeheim)
	{
		super(id, header, sortProperty, propertyExpression);
		this.verbergGeheim = verbergGeheim;
	}

	@Override
	public void populateItem(WebMarkupContainer cellItem, String componentId,
			WebMarkupContainer row, final IModel<T> rowModel, int span)
	{
		if (verbergGeheim && getContactgegevenEntiteit(rowModel).isGeheim())
			cellItem.add(new Label(componentId, new Model<String>("*****")));
		else if (isHref(getContactgegevenEntiteit(rowModel).getContactgegeven())
			|| isEmail(getContactgegevenEntiteit(rowModel).getContactgegeven()))
			cellItem.add(new LinkPanel(componentId, rowModel));
		else
			cellItem
				.add(new Label(componentId, createLabelModel(rowModel)).setRenderBodyOnly(true));
	}

	private boolean isHref(String link)
	{
		Validatable<String> validate = new Validatable<String>();
		validate.setValue(link);

		UrlValidator urlValidator = new UrlValidator();
		urlValidator.validate(validate);

		if (validate.getErrors().size() == 0)
			return true;

		return false;
	}

	private boolean isEmail(String link)
	{
		Validatable<String> validate = new Validatable<String>();
		validate.setValue(link);

		EmailAddressValidator emailValidator = EmailAddressValidator.getInstance();
		emailValidator.validate(validate);

		if (validate.getErrors().size() == 0)
			return true;

		return false;
	}

	protected String transformMailto(String contactgegeven)
	{
		if (isEmail(contactgegeven))
			return "mailto:" + contactgegeven;

		return contactgegeven;
	}

	protected String transformHref(String contactgegeven)
	{
		if (isHref(contactgegeven))
			return "javascript:window.open('" + contactgegeven + "', '" + contactgegeven + "')";

		return contactgegeven;
	}

	protected IContactgegevenEntiteit getContactgegevenEntiteit(IModel<T> rowModel)
	{
		return rowModel.getObject();
	}

	private final class LinkPanel extends Panel
	{
		private static final long serialVersionUID = 1L;

		public LinkPanel(String id, final IModel<T> rowModel)
		{
			super(id);

			// link welke zelf niks doet.
			SecureLink<Void> link = new SecureLink<Void>("link")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick()
				{
				}
			};

			String contactgegevenWaarde = getContactgegevenEntiteit(rowModel).getContactgegeven();

			link.add(ComponentFactory.getDataLabel("label", contactgegevenWaarde));
			link.add(new AttributeModifier("title", true, new Model<String>(contactgegevenWaarde)));

			// bij een email gebruiken we alleen href zodat we geen leeg nieuw scherm
			// krijgen
			if (isEmail(contactgegevenWaarde))
			{
				link.add(new AttributeModifier("href", true, new Model<String>(
					transformMailto(contactgegevenWaarde))));

			}
			// bij een href gebruiken we de onclick zodat we de pagina in een nieuw scherm
			// krijgen
			else if (isHref(contactgegevenWaarde))
			{
				link.add(new AttributeModifier("href", true, new Model<String>("#")));
				link.add(new AttributeModifier("onclick", true, new Model<String>(
					transformHref(contactgegevenWaarde))));
			}

			add(link);
		}
	}
}
