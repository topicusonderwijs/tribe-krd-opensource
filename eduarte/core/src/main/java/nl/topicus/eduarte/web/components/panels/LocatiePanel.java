package nl.topicus.eduarte.web.components.panels;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.link.VerwijderAjaxLink;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.web.components.modalwindow.locatie.LocatieSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.LocatieZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

/**
 * 
 * 
 * @author vanharen
 */
public class LocatiePanel extends FormComponentPanel<List<Locatie>>
{

	private static final long serialVersionUID = 1L;

	private WebMarkupContainer container;

	private WebMarkupContainer geenLocatieGegevensTekst;

	private WebMarkupContainer headers;

	private ListView<Locatie> list;

	private LocatieZoekFilter getDefaultFilter()
	{
		LocatieZoekFilter zoekFilter = new LocatieZoekFilter();
		zoekFilter.addOrderByProperty("naam");
		return zoekFilter;
	}

	public LocatiePanel(String id, IModel<List<Locatie>> locatiesModel)
	{
		super(id, locatiesModel);
		LocatieZoekFilter locatieZoekFilter = getDefaultFilter();

		container = new WebMarkupContainer("gegevens");
		container.setOutputMarkupId(true);

		headers = new WebMarkupContainer("headerGegevens");
		container.add(headers);

		geenLocatieGegevensTekst = new WebMarkupContainer("geenLocatieGegevens");
		container.add(geenLocatieGegevensTekst);

		list = new ListView<Locatie>("locatieList", locatiesModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Locatie> item)
			{
				Locatie locatie = item.getModelObject();
				item.add(new Label("locatieBeginWaarde", locatie.getBegindatum().toString()));
				item.add(new Label("locatieAfkWaarde", locatie.getAfkorting()));
				item.add(new Label("locatieWaarde", locatie.getNaam()));
				item.add(new VerwijderAjaxLink<Locatie>("verwijder", item.getModel())
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getLocaties().remove(getModelObject());

						if (getLocaties().size() == 0)
						{
							list.setVisible(false);
							geenLocatieGegevensTekst.setVisible(true);
						}

						target.addComponent(container);
					}
				});
			}
		};
		container.add(list);
		if (((list.getModelObject())).isEmpty())
		{
			geenLocatieGegevensTekst.setVisible(true);
			headers.setVisible(false);
		}
		else
		{
			geenLocatieGegevensTekst.setVisible(false);
			headers.setVisible(true);

		}

		add(container);

		final LocatieSelectieModalWindow modalWindow =
			new LocatieSelectieModalWindow("modalWindow", ModelFactory.getModel((Locatie) null),
				locatieZoekFilter);
		modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				if (modalWindow.getDefaultModelObject() == null)
					return;

				Locatie locatie = (Locatie) modalWindow.getDefaultModelObject();
				getLocaties().add(locatie);
				list.setVisible(true);
				geenLocatieGegevensTekst.setVisible(false);
				target.addComponent(container);
			}
		});
		add(modalWindow);

		add(new AjaxLink<Void>("locatieToevoegenButton")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				modalWindow.setDefaultModelObject(null);
				modalWindow.show(target);
			}
		});

	}

	@Override
	public void updateModel()
	{
		List<Locatie> locaties = getLocaties();
		for (int i = 0; i < locaties.size();)
		{
			Locatie veld = locaties.get(i);
			if (StringUtil.isEmpty(veld.getNaam()))
				locaties.remove(i);
			else
				i++;
		}
	}

	private List<Locatie> getLocaties()
	{
		return getModelObject();
	}

}
