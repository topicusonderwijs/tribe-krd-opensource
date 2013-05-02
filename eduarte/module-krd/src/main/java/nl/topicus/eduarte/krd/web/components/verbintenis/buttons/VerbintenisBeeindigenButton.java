package nl.topicus.eduarte.krd.web.components.verbintenis.buttons;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractPageBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.VerbintenisBeeindigenPage;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;

public class VerbintenisBeeindigenButton extends AbstractPageBottomRowButton
{
	private static final long serialVersionUID = 1L;

	private IModel<Boolean> visibilityModel;

	private IModel<Verbintenis> verbintenisModel;

	public VerbintenisBeeindigenButton(BottomRowPanel bottomRow,
			final IModel<Verbintenis> verbintenisModel, IModel<Boolean> visibilityModel)
	{
		super(bottomRow, "Verbintenis beëindigen", CobraKeyAction.GEEN, ButtonAlignment.RIGHT,
			new IPageLink()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Page getPage()
				{
					return new VerbintenisBeeindigenPage(verbintenisModel);
				}

				@Override
				public Class< ? extends SecurePage> getPageIdentity()
				{
					return VerbintenisBeeindigenPage.class;
				}
			});

		this.visibilityModel = visibilityModel;
		this.verbintenisModel = verbintenisModel;

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
		ComponentUtil.detachQuietly(verbintenisModel);
	}

}
