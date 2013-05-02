package nl.topicus.eduarte.web.pages.deelnemer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxActieButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.eduarte.core.principals.deelnemer.DeelnemerRapportages;
import nl.topicus.eduarte.dao.helpers.DeelnemerZoekOpdrachtDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.rapportage.DeelnemerZoekOpdracht;
import nl.topicus.eduarte.web.components.modalwindow.zoekopdracht.DeelnemerZoekOpdrachtSelectieModalWindow;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractDeelnemerSelectiePage;
import nl.topicus.eduarte.web.pages.shared.SelectieTarget;
import nl.topicus.eduarte.zoekfilters.DeelnemerZoekOpdrachtZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.LoadableDetachableModel;

@PageInfo(title = "Deelnemer(s) selecteren", menu = "Deelnemer > Rapportages")
@InPrincipal(DeelnemerRapportages.class)
public class RapportageVerbintenisSelectiePage extends AbstractDeelnemerSelectiePage<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	private DeelnemerZoekOpdrachtSelectieModalWindow modalWindow;

	@SuppressWarnings("unchecked")
	public RapportageVerbintenisSelectiePage(SecurePage returnPage, VerbintenisZoekFilter filter,
			DatabaseSelection<Verbintenis, Verbintenis> selection,
			SelectieTarget<Verbintenis, Verbintenis> target)
	{
		super(returnPage, filter, selection, target);
		modalWindow =
			new DeelnemerZoekOpdrachtSelectieModalWindow("zoekopdrachtselectiemodalwindow",
				new DeelnemerZoekOpdrachtListModel());
		modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target2)
			{
				if (modalWindow.getDefaultModelObject() == null)
					return;

				// Verzamel verbintenissen mbv opgeslagen filter
				DeelnemerZoekOpdracht opdracht =
					(DeelnemerZoekOpdracht) modalWindow.getDefaultModelObject();
				VerbintenisDataAccessHelper helper =
					DataAccessRegistry.getHelper(VerbintenisDataAccessHelper.class);
				VerbintenisZoekFilter savedFilter = opdracht.deserializeFilter();
				savedFilter
					.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
						RapportageVerbintenisSelectiePage.this));
				List<Verbintenis> verbintenissenOpgeslagenFilter = helper.list(savedFilter);
				if (verbintenissenOpgeslagenFilter.isEmpty())
					return;

				// Verzamel IDs van verbintenissen in het selectiepanel
				CustomDataPanel<Verbintenis> datapanel =
					RapportageVerbintenisSelectiePage.this.getSelectiePanel().getDataPanel();
				List<Long> idsInPanel = new ArrayList<Long>();
				Iterator i =
					datapanel.getDataProvider().iterator(0, datapanel.getDataProvider().size());
				while (i.hasNext())
				{
					Verbintenis v = (Verbintenis) i.next();
					idsInPanel.add(v.getId());
				}

				// Selecteer verbintenissen die gevonden zijn adhv het opgeslagen filter
				// en in het panel staan
				for (Verbintenis v : verbintenissenOpgeslagenFilter)
				{
					if (idsInPanel.contains(v.getId()))
					{
						RapportageVerbintenisSelectiePage.this.getSelection().add(v);
					}
				}

				target2.addComponent(RapportageVerbintenisSelectiePage.this);
				refreshFeedback(target2);
				refreshBottomRow(target2);
			}
		});
		add(modalWindow);
	}

	@Override
	public int getMaxResults()
	{
		return 5000;
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AjaxActieButton(panel, "Opgeslagen Zoekopdrachten")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				modalWindow.show(target);
			}
		});

		panel.addButton(new PageLinkButton(panel, "Uitgebreid zoeken", new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return DeelnemerUitgebreidZoekenPage.class;
			}

			@Override
			public Page getPage()
			{
				return new DeelnemerUitgebreidZoekenPage(getFilter(),
					RapportageVerbintenisSelectiePage.this, true);
			}
		}));
		super.fillBottomRow(panel);
	}

	private class DeelnemerZoekOpdrachtListModel extends
			LoadableDetachableModel<List<DeelnemerZoekOpdracht>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<DeelnemerZoekOpdracht> load()
		{
			DeelnemerZoekOpdrachtDataAccessHelper helper =
				DataAccessRegistry.getHelper(DeelnemerZoekOpdrachtDataAccessHelper.class);
			return helper.list(new DeelnemerZoekOpdrachtZoekFilter());
		}
	}
}
