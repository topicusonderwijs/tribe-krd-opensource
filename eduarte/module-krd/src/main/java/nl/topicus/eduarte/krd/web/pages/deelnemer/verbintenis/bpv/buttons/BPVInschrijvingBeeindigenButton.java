package nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.bpv.buttons;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractPageBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.bpv.BPVBeeindigenPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;

public class BPVInschrijvingBeeindigenButton extends AbstractPageBottomRowButton
{
	private static final long serialVersionUID = 1L;

	private IModel<Boolean> visibilityModel;

	public BPVInschrijvingBeeindigenButton(BottomRowPanel bottomRow,
			final IModel<BPVInschrijving> bpInschrijvingModel,
			final AbstractDeelnemerPage returnPage, IModel<Boolean> visibilityModel, String label)
	{
		super(bottomRow, label, CobraKeyAction.GEEN, ButtonAlignment.RIGHT, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new BPVBeeindigenPage(bpInschrijvingModel, returnPage);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return BPVBeeindigenPage.class;
			}
		});

		this.visibilityModel = visibilityModel;
	}

	@Override
	public boolean isVisible()
	{
		return super.isVisible() && visibilityModel.getObject();
	}

	@Override
	protected void detachModel()
	{
		ComponentUtil.detachQuietly(visibilityModel);
	}

}
