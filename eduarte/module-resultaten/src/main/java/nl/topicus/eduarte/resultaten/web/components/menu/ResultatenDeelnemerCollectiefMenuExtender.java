package nl.topicus.eduarte.resultaten.web.components.menu;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.dao.hibernate.ProjectedSelection;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.resultaten.web.pages.deelnemer.DeelnemerCollectiefResultatenmatrixPage;
import nl.topicus.eduarte.resultaten.web.pages.deelnemer.DeelnemerToetsenBevriezenPage;
import nl.topicus.eduarte.resultaten.web.pages.deelnemer.ResultatenDeelnemerSelectiePage;
import nl.topicus.eduarte.resultaten.web.pages.deelnemer.ResultatenImporterenPage;
import nl.topicus.eduarte.resultaten.web.pages.deelnemer.SeResultatenImporterenPage;
import nl.topicus.eduarte.resultaten.web.pages.deelnemer.ToetsBevriezenDeelnemerSelectiePage;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.pages.PageContext;
import nl.topicus.eduarte.web.pages.deelnemer.DeelnemerZoekenPage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;
import nl.topicus.eduarte.web.pages.shared.DeelnemerCollectiefPageContext;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.Link;

public class ResultatenDeelnemerCollectiefMenuExtender extends
		AbstractMenuExtender<DeelnemerCollectiefMenu>
{
	private static final class ResultatenInvoerenPageLink implements IPageLink
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Page getPage()
		{
			final PageContext context =
				new DeelnemerCollectiefPageContext("Resultatenmatrix",
					DeelnemerCollectiefMenuItem.Invoeren);

			DatabaseSelection<Deelnemer, Verbintenis> selection =
				new ProjectedSelection<Deelnemer, Verbintenis>(Verbintenis.class, "deelnemer");
			return new ResultatenDeelnemerSelectiePage(DeelnemerZoekenPage.class, context,
				VerbintenisZoekFilter.getDefaultFilter(), selection,
				new AbstractSelectieTarget<Deelnemer, Verbintenis>(
					DeelnemerCollectiefResultatenmatrixPage.class, "Volgende")
				{
					private static final long serialVersionUID = 1L;

					@Override
					public Link<Void> createLink(String linkId,
							final ISelectionComponent<Deelnemer, Verbintenis> base)
					{
						return new Link<Void>(linkId)
						{
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick()
							{
								setResponsePage(new DeelnemerCollectiefResultatenmatrixPage(
									context, base.getSelectedElements()));
							}
						};
					}
				});
		}

		@Override
		public Class< ? extends Page> getPageIdentity()
		{
			return ResultatenDeelnemerSelectiePage.class;
		}
	}

	private static final class ResultatenBevriezenPageLink implements IPageLink
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Page getPage()
		{
			final PageContext context =
				new DeelnemerCollectiefPageContext("Resultaten bevriezen",
					DeelnemerCollectiefMenuItem.Bevriezen);
			DatabaseSelection<Deelnemer, Verbintenis> selection =
				new ProjectedSelection<Deelnemer, Verbintenis>(Verbintenis.class, "deelnemer");

			return new ToetsBevriezenDeelnemerSelectiePage(DeelnemerZoekenPage.class, context,
				VerbintenisZoekFilter.getDefaultFilter(), selection,
				new AbstractSelectieTarget<Deelnemer, Verbintenis>(
					DeelnemerToetsenBevriezenPage.class, "Volgende")
				{
					private static final long serialVersionUID = 1L;

					@Override
					public Link<Void> createLink(String linkId,
							final ISelectionComponent<Deelnemer, Verbintenis> base)
					{
						return new Link<Void>(linkId)
						{
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick()
							{
								setResponsePage(new DeelnemerToetsenBevriezenPage(context, base
									.getSelectedElements()));
							}
						};
					}
				});
		}

		@Override
		public Class< ? extends Page> getPageIdentity()
		{
			return ToetsBevriezenDeelnemerSelectiePage.class;
		}
	}

	private static final long serialVersionUID = 1L;

	@Override
	public List<IMenuItem> getMenuExtension(DeelnemerCollectiefMenu menu)
	{
		List<IMenuItem> res = new ArrayList<IMenuItem>();
		DropdownMenuItem resultatenDropdown = new DropdownMenuItem("Resultaten");
		resultatenDropdown.add(new MenuItem(new ResultatenInvoerenPageLink(),
			DeelnemerCollectiefMenuItem.Invoeren));
		resultatenDropdown.add(new MenuItem(new ResultatenBevriezenPageLink(),
			DeelnemerCollectiefMenuItem.Bevriezen));
		resultatenDropdown.add(new MenuItem(SeResultatenImporterenPage.class,
			DeelnemerCollectiefMenuItem.SeResultatenInlezen));
		resultatenDropdown.add(new MenuItem(ResultatenImporterenPage.class,
			DeelnemerCollectiefMenuItem.ResultatenImporteren));
		res.add(resultatenDropdown);
		return res;
	}
}
