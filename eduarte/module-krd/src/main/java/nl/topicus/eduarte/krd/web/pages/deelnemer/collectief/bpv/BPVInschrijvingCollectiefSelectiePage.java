package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.bpv;

import java.util.Arrays;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerBPVWrite;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.selectie.BPVInschrijvingSelectiePanel;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.CollectiefInfoPanel;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.CollectieveStatusovergangEditModel;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.CollectieveStatusovergangSelectieTarget;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.bpv.EditBPVInschrijvingPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectiePage;
import nl.topicus.eduarte.zoekfilters.BPVInschrijvingZoekFilter;

import org.apache.wicket.security.checks.ClassSecurityCheck;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.apache.wicket.security.components.SecureComponentHelper;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;

/**
 * @author idserda
 */
@PageInfo(title = "BPV inschrijving collectief bewerken stap 2", menu = "Deelnemer > Collectief > BPVs")
@InPrincipal(DeelnemerBPVWrite.class)
public class BPVInschrijvingCollectiefSelectiePage extends
		AbstractSelectiePage<BPVInschrijving, BPVInschrijving, BPVInschrijvingZoekFilter> implements
		IEditPage
{
	private static final long serialVersionUID = 1L;

	private static BPVInschrijvingZoekFilter getDefaultFilter(
			CollectieveStatusovergangEditModel<BPVStatus> model)
	{
		BPVInschrijvingZoekFilter filter = new BPVInschrijvingZoekFilter();
		filter.setStatus(model.getBeginstatus());

		if (model.getEinddatum() != null)
			filter.setPeildatum(model.getEinddatum());

		return filter;
	}

	public BPVInschrijvingCollectiefSelectiePage(
			final CollectieveStatusovergangEditModel<BPVStatus> model, SecurePage returnPage)
	{
		super(
			returnPage,
			getDefaultFilter(model),
			new HibernateSelection<BPVInschrijving>(BPVInschrijving.class),
			new CollectieveStatusovergangSelectieTarget<BPVInschrijving, BPVInschrijving, BPVStatus>(
				BPVInschrijvingCollectiefEditOverzichtPage.class, model)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public ISecurityCheck getSecurityCheck()
				{
					if (extraAuthorisatieNodig())
					{
						DataSecurityCheck dataCheck =
							new DataSecurityCheck(SecureComponentHelper
								.alias(EditBPVInschrijvingPage.class)
								+ EditBPVInschrijvingPage.EXTRA_STATUSOVERGANGEN);
						return dataCheck;
					}
					else
					{
						return new ClassSecurityCheck(
							BPVInschrijvingCollectiefEditOverzichtPage.class);
					}
				}

				protected boolean extraAuthorisatieNodig()
				{
					return !Arrays.asList((model.getBeginstatus()).getVervolgNormaal()).contains(
						model.getEindstatus());
				}
			});
		createInfoPanel("infoPanel", model);
	}

	private void createInfoPanel(String id, CollectieveStatusovergangEditModel<BPVStatus> model)
	{
		add(new CollectiefInfoPanel<BPVStatus>(id, model));
	}

	@Override
	public int getMaxResults()
	{
		return 999;
	}

	@Override
	protected AbstractSelectiePanel<BPVInschrijving, BPVInschrijving, BPVInschrijvingZoekFilter> createSelectiePanel(
			String id, BPVInschrijvingZoekFilter filter,
			Selection<BPVInschrijving, BPVInschrijving> selection)
	{
		return new BPVInschrijvingSelectiePanel(id, filter,
			(DatabaseSelection<BPVInschrijving, BPVInschrijving>) selection);
	}
}
