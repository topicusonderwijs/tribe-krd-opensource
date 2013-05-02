package nl.topicus.eduarte.krd.web.components.verbintenis.buttons;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractPageBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.EditVerbintenisPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.VerbintenisBeeindigenPage;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.DeelnemerVerbintenisPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;

/**
 * 
 * 
 * @author vanharen
 */
public class VerbintenisBewerkOfBeeindigButton extends AbstractPageBottomRowButton
{
	private static final long serialVersionUID = 1L;

	private IModel<Boolean> visibilityModel;

	private IModel<Verbintenis> verbintenisModel;

	public VerbintenisBewerkOfBeeindigButton(BottomRowPanel bottomRow,
			final IModel<Verbintenis> verbintenisModel, IModel<Boolean> visibilityModel,
			final DeelnemerVerbintenisPage returnPage)
	{
		super(bottomRow, "Bewerken", CobraKeyAction.GEEN, ButtonAlignment.RIGHT, new IPageLink()
		{

			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				Verbintenis verbintenis = verbintenisModel.getObject();
				if (verbintenis.getStatus().equals(VerbintenisStatus.Beeindigd))
				{
					return new VerbintenisBeeindigenPage(verbintenisModel, true);
				}
				else
				{
					return new EditVerbintenisPage(verbintenis, returnPage);
				}

			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				Verbintenis verbintenis = verbintenisModel.getObject();
				if (verbintenis.getStatus().equals(VerbintenisStatus.Beeindigd))
				{
					return VerbintenisBeeindigenPage.class;
				}
				else
				{
					return EditVerbintenisPage.class;
				}
			}

		});
		this.visibilityModel = visibilityModel;
		this.verbintenisModel = verbintenisModel;
	}

	@Override
	public boolean isVisible()
	{
		return super.isVisible() && (Boolean) visibilityModel.getObject();
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(visibilityModel);
		ComponentUtil.detachQuietly(verbintenisModel);
	}

}
