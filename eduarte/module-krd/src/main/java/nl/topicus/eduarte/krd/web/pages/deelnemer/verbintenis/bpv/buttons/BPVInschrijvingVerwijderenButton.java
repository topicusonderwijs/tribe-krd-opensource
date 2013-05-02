package nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.bpv.buttons;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.eduarte.app.security.checks.DeelnemerSecurityCheck;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerBPVInschrijvingVerwijderen;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.providers.BPVInschrijvingProvider;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;
import org.apache.wicket.security.checks.ClassSecurityCheck;

@InPrincipal(DeelnemerBPVInschrijvingVerwijderen.class)
public class BPVInschrijvingVerwijderenButton extends VerwijderButton
{
	private static final long serialVersionUID = 1L;

	private BPVInschrijvingProvider provider;

	public BPVInschrijvingVerwijderenButton(BottomRowPanel bottomRow,
			BPVInschrijvingProvider provider)
	{
		super(bottomRow, "Verwijderen", "Weet u zeker dat u deze BPV wilt verwijderen?");
		this.provider = provider;
		ComponentUtil.setSecurityCheck(this, new DeelnemerSecurityCheck(new ClassSecurityCheck(
			BPVInschrijvingVerwijderenButton.class), provider.getBPV()));
	}

	@Override
	public boolean isVisible()
	{
		return provider.getBPV().isSaved()
			&& provider.getBPV().getStatus().equals(BPVStatus.Voorlopig) && !heeftBronMeldingen();
	}

	private boolean heeftBronMeldingen()
	{
		BronMeldingZoekFilter filter = new BronMeldingZoekFilter(provider.getBPV());
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			new AlwaysGrantedSecurityCheck()));
		// BPV inschrijvingen worden alleen voor BVE verbintenissen aan BRON doorgegeven.
		long aantalMeldingen =
			DataAccessRegistry.getHelper(BronDataAccessHelper.class).getAantalBVEMeldingen(filter);
		return aantalMeldingen > 0;
	}

	@Override
	protected void onClick()
	{
		// implementeren in subclass
	}
}