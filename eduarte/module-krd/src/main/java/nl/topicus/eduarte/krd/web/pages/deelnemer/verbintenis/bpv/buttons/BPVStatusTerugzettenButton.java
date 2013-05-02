package nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.bpv.buttons;

import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractConfirmationLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.security.checks.DeelnemerSecurityCheck;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerBPVInschrijvingStatusovergang;
import nl.topicus.eduarte.providers.BPVInschrijvingProvider;

import org.apache.wicket.security.checks.ClassSecurityCheck;

@InPrincipal(DeelnemerBPVInschrijvingStatusovergang.class)
public class BPVStatusTerugzettenButton extends AbstractConfirmationLinkButton
{
	private static final long serialVersionUID = 1L;

	private BPVInschrijvingProvider provider;

	public BPVStatusTerugzettenButton(BottomRowPanel bottomRow, BPVInschrijvingProvider provider)
	{
		super(
			bottomRow,
			"Status terugzetten",
			CobraKeyAction.GEEN,
			ButtonAlignment.LEFT,
			"Weet u zeker dat u de status wilt terugzetten naar Voorlopig? Dit resulteert in een Verwijdering bij BRON.");
		ComponentUtil.setSecurityCheck(this, new DeelnemerSecurityCheck(new ClassSecurityCheck(
			BPVStatusTerugzettenButton.class), provider.getBPV()));
		this.provider = provider;
	}

	@Override
	protected void onClick()
	{
		// implementeren in subclass

	}

	@Override
	public boolean isVisible()
	{
		return provider.getBPV() != null && provider.getBPV().getStatus().isBronCommuniceerbaar()
			&& !provider.getBPV().getVerbintenis().isBeeindigd();
	}
}