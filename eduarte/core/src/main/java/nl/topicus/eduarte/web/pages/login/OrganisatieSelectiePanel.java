package nl.topicus.eduarte.web.pages.login;

import java.util.ArrayList;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.OrganisatieDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.AbstractEntiteitDataProvider;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.GridView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

public class OrganisatieSelectiePanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public OrganisatieSelectiePanel(String id)
	{
		super(id);

		IDataProvider<Organisatie> provider = new AbstractEntiteitDataProvider<Organisatie>(null)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void createLijst()
			{
				OrganisatieDataAccessHelper helper =
					DataAccessRegistry.getHelper(OrganisatieDataAccessHelper.class);
				lijst = new ArrayList<Organisatie>(helper.getInlogDomeinen());
			}
		};
		add(new GridView<Organisatie>("organisaties", provider)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateEmptyItem(Item<Organisatie> item)
			{
				item.add(new EmptyFragment("org"));
			}

			@Override
			protected void populateItem(Item<Organisatie> item)
			{
				item.add(new LinkFragment("org", item.getModel()));
			}
		}.setColumns(4));
	}

	public class EmptyFragment extends Fragment
	{
		private static final long serialVersionUID = 1L;

		public EmptyFragment(String id)
		{
			super(id, "emptyFragment", OrganisatieSelectiePanel.this);
		}
	}

	public class LinkFragment extends Fragment
	{
		private static final long serialVersionUID = 1L;

		public LinkFragment(String id, IModel<Organisatie> model)
		{
			super(id, "linkFragment", OrganisatieSelectiePanel.this, model);

			Organisatie organisatie = model.getObject();

			PageParameters pars = new PageParameters();
			pars.add("0", organisatie.getNaam());

			BookmarkablePageLink<Void> link =
				new BookmarkablePageLink<Void>("link", LoginPage.class, pars);
			link.add(new Label("naam", organisatie.getNaam()));
			add(link);
		}
	}
}
