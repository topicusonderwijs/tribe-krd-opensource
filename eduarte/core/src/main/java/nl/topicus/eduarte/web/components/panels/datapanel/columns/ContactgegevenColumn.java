/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.datapanel.columns;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.cobra.web.components.link.SecureLink;
import nl.topicus.eduarte.entities.Contacteerbaar;
import nl.topicus.eduarte.entities.IContactgegevenEntiteit;
import nl.topicus.eduarte.entities.adres.Adresseerbaar;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.providers.PersoonProvider;
import nl.topicus.eduarte.web.components.datapanel.ContactgegevenWaardePropertyColumn;

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
 * Column welke op bv een zoeken pagina alle gekoppelde waarden van 1 soort contactgegeven
 * toont. Deze column werkt alleen op een {@link Adresseerbaar} entiteit!
 * 
 * Voor een lijst van {@link IContactgegevenEntiteit}en zie
 * {@link ContactgegevenWaardePropertyColumn}.
 * 
 * @author hoeve
 */
public class ContactgegevenColumn<T> extends AbstractCustomColumn<T>
{
	private static final long serialVersionUID = 1L;

	private final IModel<SoortContactgegeven> soortContactgegevenModel;

	/**
	 * @param soortContactgegeven
	 * @param header
	 * @param repeatWhenEqualToPrevRow
	 */
	public ContactgegevenColumn(SoortContactgegeven soortContactgegeven, String header,
			boolean repeatWhenEqualToPrevRow)
	{
		super(soortContactgegeven.getCode() + " " + soortContactgegeven.getNaam(), header,
			repeatWhenEqualToPrevRow);
		this.soortContactgegevenModel = ModelFactory.getModel(soortContactgegeven);
	}

	private SoortContactgegeven getContactgegeven()
	{
		return soortContactgegevenModel.getObject();
	}

	/**
	 * @see org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator#populateItem(org.apache.wicket.markup.repeater.Item,
	 *      java.lang.String, org.apache.wicket.model.IModel)
	 */
	@Override
	public void populateItem(WebMarkupContainer cellItem, String componentId,
			WebMarkupContainer row, IModel<T> rowModel, int span)
	{
		String contactgegevenWaarde = "";
		List<IContactgegevenEntiteit> contactgegevens =
			getContacteerbaar(rowModel).getContactgegevens(getContactgegeven());
		if (contactgegevens.size() > 0)
		{
			if (contactgegevens.size() == 1)
			{
				contactgegevenWaarde = contactgegevens.get(0).getFormattedContactgegeven();
			}
			else
			{
				StringBuilder tlf = new StringBuilder();
				boolean first = true;
				for (IContactgegevenEntiteit telefoon : contactgegevens)
				{
					if (!first)
					{
						tlf.append(", ");
					}
					first = false;
					tlf.append(telefoon.getFormattedContactgegeven());
				}
				contactgegevenWaarde = tlf.toString();
			}
		}

		if (isHref(contactgegevenWaarde) || isEmail(contactgegevenWaarde))
			cellItem.add(new LinkPanel(componentId, new Model<String>(contactgegevenWaarde)));
		else
			cellItem.add(new Label(componentId, new Model<String>(contactgegevenWaarde))
				.setRenderBodyOnly(true));
	}

	@SuppressWarnings("unchecked")
	private Contacteerbaar<IContactgegevenEntiteit> getContacteerbaar(IModel rowModel)
	{
		if (rowModel.getObject() instanceof Contacteerbaar)
		{
			return (Contacteerbaar) rowModel.getObject();
		}
		else if (rowModel.getObject() instanceof PersoonProvider)
		{
			return (Contacteerbaar) ((PersoonProvider) rowModel.getObject()).getPersoon();
		}

		return null;
	}

	@Override
	public void detach()
	{
		super.detach();
		ComponentUtil.detachQuietly(soortContactgegevenModel);
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

	private final class LinkPanel extends Panel
	{
		private static final long serialVersionUID = 1L;

		/**
		 * @param id
		 * @param rowModel
		 */
		public LinkPanel(String id, final IModel<String> rowModel)
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

			String contactgegevenWaarde = rowModel.getObject().toString();

			link.add(ComponentFactory.getDataLabel("label", contactgegevenWaarde));
			link.add(new AttributeModifier("title", true, new Model<String>(contactgegevenWaarde)));

			// bij een e-mail gebruiken we alleen href zodat we geen leeg nieuw scherm
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
