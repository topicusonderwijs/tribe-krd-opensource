package nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.vooropleiding.buttons;

import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.vrijevelden.VooropleidingVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldOptieKeuze;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerVooropleidingenVerwijderen;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.model.IModel;
import org.apache.wicket.security.checks.ClassSecurityCheck;

@InPrincipal(DeelnemerVooropleidingenVerwijderen.class)
public class VooropleidingVerwijderenButton extends VerwijderButton
{
	private static final long serialVersionUID = 1L;

	private IModel<Vooropleiding> voorOpleiding;

	private SecurePage returnToPage;

	public VooropleidingVerwijderenButton(BottomRowPanel bottomRow,
			IModel<Vooropleiding> voorOpleiding, SecurePage returnToPage)
	{
		super(bottomRow);
		ComponentUtil.setSecurityCheck(this, new ClassSecurityCheck(
			VooropleidingVerwijderenButton.class));
		setConfirmMessage("Weet u zeker dat u deze vooropleiding wilt verwijderen?");
		this.voorOpleiding = voorOpleiding;
		this.returnToPage = returnToPage;
	}

	@Override
	public boolean isVisible()
	{
		return voorOpleiding.getObject().isSaved()
			&& !voorOpleiding.getObject().hasBronCommuniceerbareVerbintenissen();
	}

	@Override
	protected void onClick()
	{
		Deelnemer deelnemer = voorOpleiding.getObject().getDeelnemer();
		for (Verbintenis verbintenis : deelnemer.getVerbintenissen())
		{
			if (verbintenis.getRelevanteVooropleidingVooropleiding() != null)
			{
				if (verbintenis.getRelevanteVooropleidingVooropleiding().equals(voorOpleiding))
				{
					verbintenis.setRelevanteVooropleidingVooropleiding(null);
					verbintenis.saveOrUpdate();
				}
			}
		}

		deelnemer.getVooropleidingen().remove(voorOpleiding);

		for (VooropleidingVrijVeld curVrijVeld : voorOpleiding.getObject().getVrijVelden())
		{
			for (VrijVeldOptieKeuze curKeuze : curVrijVeld.getKeuzes())
				curKeuze.delete();
			curVrijVeld.delete();
		}
		voorOpleiding.getObject().delete();
		voorOpleiding.getObject().commit();
		setResponsePage(returnToPage);
	}
}