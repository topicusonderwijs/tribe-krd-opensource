package nl.topicus.eduarte.web.components.panels.sidebar.deelnemer;

import java.io.Serializable;
import java.lang.reflect.Constructor;

import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.listview.ClickableIdObjectListView;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.sidebar.datastores.RecenteDeelnemersDataStore;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.components.panels.sidebar.AbstractSideBar;
import nl.topicus.eduarte.web.components.panels.sidebar.GeenElementenPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.deelnemer.deelnemerkaart.DeelnemerkaartPage;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.string.Strings;

/**
 * Sidebar panel die de 5 meest recente deelnemers toont.
 * 
 * @author loite
 */
public class RecenteDeelnemersSideBar extends AbstractSideBar
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param owningPage
	 */
	public RecenteDeelnemersSideBar(String id, final SecurePage owningPage)
	{
		super(id, owningPage);
		RecenteDeelnemersDataStore datastore = (RecenteDeelnemersDataStore) getDataStore();
		final ClickableIdObjectListView<Verbintenis> listview =
			new ClickableIdObjectListView<Verbintenis>("listview", datastore
				.getInschrijvingenModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<Verbintenis> item)
				{
					Verbintenis inschrijving = item.getModelObject();
					item.add(ComponentFactory.getDataLabel("nummer", new PropertyModel<Integer>(
						item.getModel(), "deelnemer.deelnemernummer")));
					item.add(ComponentFactory.getDataLabel("naam", new PropertyModel<String>(item
						.getModel(), "deelnemer.persoon.volledigeNaam")));
					/*
					 * Handmatige escapeMarkup omdat AttributeModifier() dat nog niet doet
					 * voor de waarde van het model
					 */
					if (inschrijving != null)
						item.add(new AttributeModifier("title", true,
							new Model<Serializable>((Serializable) Strings
								.escapeMarkup(inschrijving.getOmschrijving(null)))));
				}

				@Override
				public void onClick(ListItem<Verbintenis> item)
				{
					Verbintenis inschrijving = item.getModelObject();
					Class< ? extends AbstractDeelnemerPage> pageClass =
						((RecenteDeelnemersDataStore) getDataStore()).getDeelnemerPageClass(item
							.getIndex());
					try
					{
						Constructor< ? extends AbstractDeelnemerPage> constructor =
							pageClass.getConstructor(Verbintenis.class);
						AbstractDeelnemerPage page = constructor.newInstance(inschrijving);
						setResponsePage(page);
						return;
					}
					catch (Exception e)
					{
						// Ignore
					}
					setResponsePage(new DeelnemerkaartPage(inschrijving));
				}

				@Override
				public boolean isEnabled()
				{
					return super.isEnabled() && !owningPage.isEditable();
				}
			};
		add(listview);
		String deelnemers = EduArteApp.get().getDeelnemerTermMeervoud();
		add(new Label("caption", "Recente " + deelnemers));
		add(new GeenElementenPanel("geen", "Geen " + deelnemers + " bezocht", 2)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return listview.size() == 0;
			}
		});
	}

	/**
	 * @see nl.topicus.eduarte.web.components.panels.sidebar.AbstractSideBar#getDataStoreClass()
	 */
	@Override
	protected Class<RecenteDeelnemersDataStore> getDataStoreClass()
	{
		return RecenteDeelnemersDataStore.class;
	}

}
