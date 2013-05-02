package nl.topicus.eduarte.web.components.panels;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.behaviors.AjaxFormComponentValidatingBehavior;
import nl.topicus.cobra.web.components.link.VerwijderAjaxLink;
import nl.topicus.cobra.web.validators.LekenUrlValidator;
import nl.topicus.eduarte.dao.helpers.SoortContactgegevenDataAccessHelper;
import nl.topicus.eduarte.entities.Contacteerbaar;
import nl.topicus.eduarte.entities.IContactgegevenEntiteit;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.entities.adres.StandaardContactgegeven;
import nl.topicus.eduarte.entities.adres.TypeContactgegeven;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.web.components.modalwindow.contactgegeven.SoortContactgegevenSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.SoortContactgegevenZoekFilter;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

/**
 * @author hoeve
 */
public class ContactgegevenEntiteitEditPanel<E extends IContactgegevenEntiteit, T extends Contacteerbaar<E>>
		extends FormComponentPanel<T>
{
	private static final long serialVersionUID = 1L;

	private WebMarkupContainer container;

	private WebMarkupContainer headerGeheim;

	private WebMarkupContainer geenContactGegevensTekst;

	private ListView<E> list;

	private boolean giveNewContactgegevenFocus;

	private SoortContactgegevenZoekFilter getDefaultFilter()
	{
		SoortContactgegevenZoekFilter zoekFilter = new SoortContactgegevenZoekFilter();
		zoekFilter.setActief(true);

		if (Persoon.class.isAssignableFrom(getContacteerbaar().getClass()))
			zoekFilter.setStandaardContactgegeven(StandaardContactgegeven.StandaardTonen,
				StandaardContactgegeven.StandaardTonenBijPersoon);
		else if (ExterneOrganisatie.class.isAssignableFrom(getContacteerbaar().getClass())
			|| Locatie.class.isAssignableFrom(getContacteerbaar().getClass())
			|| OrganisatieEenheid.class.isAssignableFrom(getContacteerbaar().getClass()))
		{
			zoekFilter.setStandaardContactgegeven(StandaardContactgegeven.StandaardTonen,
				StandaardContactgegeven.StandaardTonenBijOrganisatie);
		}
		else
			zoekFilter.setStandaardContactgegeven(StandaardContactgegeven.StandaardTonen);

		return zoekFilter;
	}

	/**
	 * @param id
	 *            het id van dit panel
	 * @param contacteerbaarModel
	 *            het model met een {@link Contacteerbaar} entiteit.
	 */

	public ContactgegevenEntiteitEditPanel(String id, IModel<T> contacteerbaarModel)
	{
		this(id, contacteerbaarModel, true);
	}

	public ContactgegevenEntiteitEditPanel(String id, IModel<T> contacteerbaarModel,
			boolean toonToevoegKnop)
	{
		super(id, contacteerbaarModel);

		// Haal default contactgegevens op.
		SoortContactgegevenZoekFilter contactgegevenZoekFilter = getDefaultFilter();
		if (getContactgegevens().size() > 0)
			contactgegevenZoekFilter.setExclude(getSoortContactgegevens(getContactgegevens()));
		List<SoortContactgegeven> newcontactgegevens =
			DataAccessRegistry.getHelper(SoortContactgegevenDataAccessHelper.class).list(
				contactgegevenZoekFilter);
		for (SoortContactgegeven vv : newcontactgegevens)
			newContactgegevenEntiteit(vv);

		container = new WebMarkupContainer("gegevens");
		container.setOutputMarkupId(true);

		headerGeheim = new WebMarkupContainer("headerGegevens");
		container.add(headerGeheim);

		geenContactGegevensTekst = new WebMarkupContainer("geenContactGegevens");
		container.add(geenContactGegevensTekst);

		list =
			new ListView<E>("contactgegevenList", new PropertyModel<List<E>>(contacteerbaarModel,
				"contactgegevens"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<E> item)
				{
					E contactgegeven = item.getModelObject();

					// 1
					item.add(new Label("contactgegevenSoort", contactgegeven
						.getSoortContactgegeven().getNaam()
						+ " (" + contactgegeven.getSoortContactgegeven().getCode() + ")"));

					// 2
					TextField<String> waardeField =
						new TextField<String>("contactgegevenWaarde", new PropertyModel<String>(
							item.getModel(), "contactgegeven"));
					waardeField.setLabel(new Model<String>(contactgegeven.getSoortContactgegeven()
						.getNaam()
						+ " (" + contactgegeven.getSoortContactgegeven().getCode() + ")"));
					item.add(waardeField);

					if (TypeContactgegeven.Email.equals(contactgegeven.getSoortContactgegeven()
						.getTypeContactgegeven()))
					{
						waardeField.add(EmailAddressValidator.getInstance());
						waardeField.setRequired(isEmailRequired());
					}
					else if (TypeContactgegeven.Homepage.equals(contactgegeven
						.getSoortContactgegeven().getTypeContactgegeven()))
					{
						waardeField.add(new LekenUrlValidator(waardeField));
					}

					waardeField.add(new AjaxFormComponentValidatingBehavior("onchange"));

					// 3
					item.add(new CheckBox("contactgegevenGeheim", new PropertyModel<Boolean>(item
						.getModel(), "geheim")));

					// 4
					item.add(new VerwijderAjaxLink<E>("verwijder", item.getModel())
					{
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick(AjaxRequestTarget target)
						{
							getContactgegevens().remove(getModelObject());
							list.removeAll(); // omdat reuseitems op true staat
							if (getContactgegevens().size() == 0)
							{
								list.setVisible(false);
								headerGeheim.setVisible(false);
								geenContactGegevensTekst.setVisible(true);
							}

							target.addComponent(container);
						}

						@Override
						public boolean isVisible()
						{
							return super.isVisible() && isDeletable(getModelObject());
						}
					});

					if (giveNewContactgegevenFocus
						&& RequestCycle.get().getRequestTarget() instanceof AjaxRequestTarget
						&& item.getIndex() == getContactgegevens().size() - 1)
					{
						AjaxRequestTarget target =
							(AjaxRequestTarget) RequestCycle.get().getRequestTarget();
						target.focusComponent(waardeField);
						giveNewContactgegevenFocus = false;
					}
				}
			};
		container.add(list);
		list.setReuseItems(true);

		if ((list.getModelObject()).isEmpty())
		{
			geenContactGegevensTekst.setVisible(true);
			headerGeheim.setVisible(false);
		}
		else
		{
			headerGeheim.setVisible(true);
			geenContactGegevensTekst.setVisible(false);
		}

		add(container);

		SoortContactgegevenZoekFilter filter = new SoortContactgegevenZoekFilter()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public List<Long> getExcludeIds()
			{
				List<Long> ids = new ArrayList<Long>();
				// contactgegevens gekoppeld aan persoon
				for (SoortContactgegeven curSoort : getSoortContactgegevens(getContactgegevens()))
				{
					ids.add(curSoort.getId());
				}
				return ids;
			}

			@Override
			public Boolean getActief()
			{
				return true;
			}
		};
		filter.addOrderByProperty("code");
		final SoortContactgegevenSelectieModalWindow modalWindow =
			new SoortContactgegevenSelectieModalWindow("modalWindow", ModelFactory
				.getModel((SoortContactgegeven) null), filter);
		modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				if (modalWindow.getDefaultModelObject() == null)
					return;

				SoortContactgegeven modelObject =
					(SoortContactgegeven) modalWindow.getDefaultModelObject();

				newContactgegevenEntiteit(modelObject);

				list.setVisible(true);
				headerGeheim.setVisible(true);
				geenContactGegevensTekst.setVisible(false);
				giveNewContactgegevenFocus = true;
				target.addComponent(container);
			}
		});
		add(modalWindow);

		add(new AjaxLink<Void>("contactgegevenToevoegenButton")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				modalWindow.setDefaultModelObject(null);
				modalWindow.show(target);
			}
		}.setVisible(toonToevoegKnop));
	}

	protected boolean isEmailRequired()
	{
		return false;
	}

	/**
	 * Bedoeld om overschreven te worden in subklasse
	 */
	@SuppressWarnings("unused")
	protected boolean isDeletable(Object o)
	{
		return true;
	}

	private List<E> getContactgegevens()
	{
		return getContacteerbaar().getContactgegevens();
	}

	private T getContacteerbaar()
	{
		return getModelObject();
	}

	public List<SoortContactgegeven> getSoortContactgegevens(List<E> ts)
	{
		List<SoortContactgegeven> contactgegevens = new ArrayList<SoortContactgegeven>();
		if (ts != null)
			for (IContactgegevenEntiteit ice : ts)
			{
				contactgegevens.add(ice.getSoortContactgegeven());
			}

		return contactgegevens;
	}

	/**
	 * subclass implementatie voor een nieuwe T.
	 * 
	 * @param soort
	 * @return nieuw contactgegeven
	 */
	protected IContactgegevenEntiteit newContactgegevenEntiteit(SoortContactgegeven soort)
	{
		E contactgegeven = getContacteerbaar().newContactgegeven();
		contactgegeven.setSoortContactgegeven(soort);

		if (getContactgegevens() != null)
			getContactgegevens().add(contactgegeven);

		return contactgegeven;
	}

	/**
	 * Controleer of er nog {@link IContactgegevenEntiteit}en zijn die geen waarde hebben,
	 * verwijder deze dan.
	 */
	@Override
	public void updateModel()
	{
		List<E> contactgegevens = getContactgegevens();
		for (int i = 0; i < contactgegevens.size();)
		{
			IContactgegevenEntiteit veld = contactgegevens.get(i);
			if (StringUtil.isEmpty(veld.getContactgegeven()))
				contactgegevens.remove(i);
			else
				i++;
		}
	}

	@Override
	public void onDetach()
	{
		ComponentUtil.detachQuietly(getModel());
		super.onDetach();
	}

}
