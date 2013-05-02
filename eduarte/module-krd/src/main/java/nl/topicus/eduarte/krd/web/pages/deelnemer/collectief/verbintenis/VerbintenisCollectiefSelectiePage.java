package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.verbintenis;

import java.util.Arrays;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieEnum;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerVerbintenissenWrite;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.CollectiefInfoPanel;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.CollectieveStatusovergangEditModel;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.CollectieveStatusovergangSelectieTarget;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.EditVerbintenisPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.AbstractVerbintenisSelectiePage;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.security.checks.ClassSecurityCheck;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.apache.wicket.security.components.SecureComponentHelper;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;

/**
 * Pagina om status van meerdere verbintenissen tegelijk te wijzigen
 * 
 * @author idserda
 */
@PageInfo(title = "Status van verbintenissen bewerken stap 2", menu = {"Deelnemer > Verbintenissen"})
@InPrincipal(DeelnemerVerbintenissenWrite.class)
public class VerbintenisCollectiefSelectiePage extends AbstractVerbintenisSelectiePage implements
		IEditPage
{
	private static final long serialVersionUID = 1L;

	public VerbintenisCollectiefSelectiePage(
			final CollectieveStatusovergangEditModel<VerbintenisStatus> model, SecurePage returnPage)
	{
		super(returnPage, getDefaultFilter(model), getSelectieTarget(model));
		createInfoPanel("infoPanel", model);
		setDefaultModel(model);
	}

	private void createInfoPanel(String id,
			CollectieveStatusovergangEditModel<VerbintenisStatus> model)
	{
		add(new CollectiefInfoPanel<VerbintenisStatus>(id, model));
	}

	private static VerbintenisZoekFilter getDefaultFilter(
			CollectieveStatusovergangEditModel<VerbintenisStatus> model)
	{
		VerbintenisZoekFilter filter = new VerbintenisZoekFilter();
		filter.setVerbintenisStatus(model.getBeginstatus());
		if (model.getTaxonomie() != null)
			filter.setTaxonomie(model.getTaxonomie());
		if (model.getEinddatum() != null)
			filter.setDatumInschrijvingTotEnMet(model.getEinddatum());

		// Deze statusovergang is alleen toegstaan bij VO verbintenissen
		if (VerbintenisStatus.Volledig.equals(model.getBeginstatus())
			&& VerbintenisStatus.Definitief.equals(model.getEindstatus()))
		{
			filter.setTaxonomie(Taxonomie.getLandelijkeTaxonomie(TaxonomieEnum.VO.getCode()));
		}

		return filter;
	}

	private static CollectieveStatusovergangSelectieTarget<Verbintenis, Verbintenis, VerbintenisStatus> getSelectieTarget(
			final CollectieveStatusovergangEditModel<VerbintenisStatus> model)
	{
		return new CollectieveStatusovergangSelectieTarget<Verbintenis, Verbintenis, VerbintenisStatus>(
			VerbintenisCollectiefEditOverzichtPage.class, model)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public ISecurityCheck getSecurityCheck()
			{
				if (extraAuthorisatieNodig())
				{
					DataSecurityCheck dataCheck =
						new DataSecurityCheck(SecureComponentHelper
							.alias(EditVerbintenisPage.class)
							+ EditVerbintenisPage.EXTRA_STATUSOVERGANGEN);
					return dataCheck;
				}
				else
				{
					return new ClassSecurityCheck(VerbintenisCollectiefEditOverzichtPage.class);
				}
			}

			private boolean extraAuthorisatieNodig()
			{
				return !Arrays.asList((model.getBeginstatus()).getVervolg(false)).contains(
					model.getEindstatus());
			}
		};
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new AnnulerenButton(panel, VerbintenisCollectiefEditOverzichtPage.class));
	}
}