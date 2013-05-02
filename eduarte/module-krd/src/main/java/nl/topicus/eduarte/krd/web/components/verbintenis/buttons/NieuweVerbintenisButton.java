package nl.topicus.eduarte.krd.web.components.verbintenis.buttons;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractPageBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.security.DisableSecurityCheckMarker;
import nl.topicus.eduarte.app.security.checks.OrganisatieEenheidLocatieSecurityCheck;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.EditVerbintenisPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.DeelnemerVerbintenisPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;

public class NieuweVerbintenisButton extends AbstractPageBottomRowButton
{
	private static final long serialVersionUID = 1L;

	private IModel<Deelnemer> deelnemerModel;

	public NieuweVerbintenisButton(BottomRowPanel parent, final IModel<Deelnemer> deelnemerModel,
			final DeelnemerVerbintenisPage returnPage)
	{
		super(parent, "Nieuwe verbintenis", CobraKeyAction.TOEVOEGEN, ButtonAlignment.RIGHT,
			new IPageLink()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Page getPage()
				{
					Deelnemer deelnemer = deelnemerModel.getObject();
					Verbintenis verbintenis = deelnemer.nieuweVerbintenis(true);
					return new EditVerbintenisPage(verbintenis, returnPage, true);
				}

				@Override
				public Class< ? extends SecurePage> getPageIdentity()
				{
					return EditVerbintenisPage.class;
				}

			});
		this.deelnemerModel = deelnemerModel;
		DisableSecurityCheckMarker.place(this, OrganisatieEenheidLocatieSecurityCheck.class);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(deelnemerModel);
	}

}
