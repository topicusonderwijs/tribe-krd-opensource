package nl.topicus.eduarte.krd.web.components.verbintenis.buttons;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.BewerkenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs.OnderwijsproductAfnameEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;

public class EditOnderwijsproductAfnameButton extends BewerkenButton<Void>
{
	private static final long serialVersionUID = 1L;

	private IModel<Boolean> visibilityModel;

	public EditOnderwijsproductAfnameButton(BottomRowPanel bottomRow,
			final IModel<Verbintenis> verbintenisModel,
			final IModel<OnderwijsproductAfname> afnameModel, IModel<Boolean> visibilityModel)
	{
		super(bottomRow, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new OnderwijsproductAfnameEditPage(verbintenisModel.getObject(), afnameModel
					.getObject());
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return OnderwijsproductAfnameEditPage.class;
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
