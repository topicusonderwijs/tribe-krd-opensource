package nl.topicus.eduarte.krd.web.pages.deelnemer.examens;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;

import org.apache.wicket.markup.html.link.Link;

public class DeelnemerKwalificatieSelecterenTarget extends
		AbstractSelectieTarget<Verbintenis, Verbintenis>
{
	private static final long serialVersionUID = 1L;

	private KwalificatieModel kwalificatieModel;

	public DeelnemerKwalificatieSelecterenTarget(KwalificatieModel kwalificatieModel)
	{
		super(DeelnemerKwalificatieGeselecteerdPage.class, "Volgende");
		this.kwalificatieModel = kwalificatieModel;
	}

	@Override
	public Link<Void> createLink(String linkId,
			final ISelectionComponent<Verbintenis, Verbintenis> base)
	{
		return new Link<Void>(linkId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				if (kwalificatieModel.getToegestaneExamenstatusOvergang()
					.isExamennummersToekennen())
					setResponsePage(new EigenschappenExamennummeringPage(kwalificatieModel, base
						.getSelectedElements(), (SecurePage) getPage()));
				else
					setResponsePage(new DeelnemerKwalificatieGeselecteerdPage(kwalificatieModel,
						base.getSelectedElements(), (SecurePage) getPage()));
			}
		};
	}

	@Override
	public void detach()
	{
		super.detach();
		ComponentUtil.detachQuietly(kwalificatieModel);
	}
}
