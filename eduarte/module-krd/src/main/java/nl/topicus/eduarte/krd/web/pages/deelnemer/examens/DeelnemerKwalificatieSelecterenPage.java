package nl.topicus.eduarte.krd.web.pages.deelnemer.examens;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.entities.examen.ToegestaneExamenstatusOvergang;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.principals.deelnemer.examen.DeelnemerExamensCollectief;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.columns.ExamennummerColumn;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.DeelnemerCohortSelectiePanel;
import nl.topicus.eduarte.web.components.panels.filter.DeelnemerCohortExamenstatusSelectieZoekFilterPanel;
import nl.topicus.eduarte.web.pages.shared.AbstractDeelnemerSelectiePage;
import nl.topicus.eduarte.web.pages.shared.DeelnemerCollectiefPageContext;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Selectie pagina voor Deelnemers.
 * 
 * @author vandekamp
 */
@PageInfo(title = "Deelnemer(s) selecteren", menu = "Deelnemer -> Examen -> Acties-overzicht -> [Actie]")
@InPrincipal(DeelnemerExamensCollectief.class)
public class DeelnemerKwalificatieSelecterenPage extends AbstractDeelnemerSelectiePage<Verbintenis>
		implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private KwalificatieModel kwalificatieModel;

	private static final VerbintenisZoekFilter getDefaultFilter(KwalificatieModel kwalificatieModel)
	{
		VerbintenisZoekFilter filter = new VerbintenisZoekFilter();
		// Model gebruiken zodat dit tijdens de sessie onthouden wordt, en de gebruiker
		// niet elke keer heen en weer hoeft te switchen.
		filter.setCohortModel(EduArteSession.get().getSelectedCohortModel());
		filter.setToegestaneExamenstatusOvergang(kwalificatieModel
			.getToegestaneExamenstatusOvergang());
		filter.setTijdvak(kwalificatieModel.getTijdvak());
		filter.addOrderByProperty("deelnemer.deelnemernummer");
		filter.addOrderByProperty("persoon.roepnaam");
		filter.addOrderByProperty("persoon.achternaam");

		ToegestaneExamenstatusOvergang overgang =
			kwalificatieModel.getToegestaneExamenstatusOvergang();
		if (kwalificatieModel.getGeselecteerdeStatus() != null
			&& kwalificatieModel.getGeselecteerdeStatus().isGeslaagd()
			&& overgang.isBepaaltDatumUitslag())
		{
			filter.setExamenstatus(overgang.getExamenWorkflow().getVoorlopigGeslaagdStatus());
		}
		if (kwalificatieModel.getGeselecteerdeStatus() != null
			&& kwalificatieModel.getGeselecteerdeStatus().isAfgewezen()
			&& overgang.isBepaaltDatumUitslag())
		{
			filter.setExamenstatus(overgang.getExamenWorkflow().getVoorlopigAfgewezenStatus());
		}

		return filter;
	}

	public DeelnemerKwalificatieSelecterenPage(KwalificatieModel kwalifiModel)
	{
		this(getDefaultFilter(kwalifiModel), kwalifiModel);
	}

	private DeelnemerKwalificatieSelecterenPage(VerbintenisZoekFilter filter,
			KwalificatieModel kwalifiModel)
	{
		super(DeelnemerKwalificatiePage.class, new DeelnemerCollectiefPageContext(
			"Kwalificatie selecteren", DeelnemerCollectiefMenuItem.ActieOverzicht), filter,
			new HibernateSelection<Verbintenis>(Verbintenis.class),
			new DeelnemerKwalificatieSelecterenTarget(kwalifiModel));
		this.kwalificatieModel = kwalifiModel;
	}

	@Override
	protected AbstractSelectiePanel<Verbintenis, Verbintenis, VerbintenisZoekFilter> createSelectiePanel(
			String id, VerbintenisZoekFilter filter, Selection<Verbintenis, Verbintenis> selection)
	{
		DeelnemerCohortSelectiePanel<Verbintenis> ret =
			new DeelnemerCohortSelectiePanel<Verbintenis>(id, filter,
				(DatabaseSelection<Verbintenis, Verbintenis>) selection, false)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected Panel createZoekFilterPanel(String filterPanelId,
						VerbintenisZoekFilter fltr, CustomDataPanel<Verbintenis> customDataPanel)
				{
					return new DeelnemerCohortExamenstatusSelectieZoekFilterPanel(filterPanelId,
						fltr, customDataPanel, false, kwalificatieModel
							.getToegestaneExamenstatusOvergang().getExamenWorkflow());
				}

			};
		ret.getContentDescription().addColumn(
			new ExamennummerColumn("Examennummer", "Examennummer"));
		CustomPropertyColumn<Verbintenis> col =
			(CustomPropertyColumn<Verbintenis>) ret.getContentDescription().getColumn(
				"Examenstatus");
		col.setDefaultVisible(true);

		return ret;
	}

	@Override
	public int getMaxResults()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public Label createTitle(String id)
	{
		if (kwalificatieModel.getGeselecteerdeStatus() != null)
			return new Label(id, kwalificatieModel.getToegestaneExamenstatusOvergang().getActie()
				+ ": " + kwalificatieModel.getGeselecteerdeStatus().getNaam());
		return new Label(id, kwalificatieModel.getToegestaneExamenstatusOvergang().getActie());
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(kwalificatieModel);
		super.onDetach();
	}
}
