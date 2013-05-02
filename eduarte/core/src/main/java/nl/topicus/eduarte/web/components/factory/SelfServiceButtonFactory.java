package nl.topicus.eduarte.web.components.factory;

import nl.topicus.cobra.modules.AbstractModuleComponentFactory;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.eduarte.entities.security.authentication.DeelnemerAccount;
import nl.topicus.eduarte.entities.security.authorization.AuthorisatieNiveau;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.account.RolEditPage;
import nl.topicus.eduarte.web.pages.shared.AccountEditPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;

/**
 * Factory voor componenten binnen de selfservice module.
 */
public class SelfServiceButtonFactory extends AbstractModuleComponentFactory implements
		NieuweAccountButtonFactory, NieuweRolButtonFactory
{
	private static class NieuweAccountPageLink implements IPageLink
	{
		private final SecurePage returnPage;

		private static final long serialVersionUID = 1L;

		private NieuweAccountPageLink(SecurePage returnPage)
		{
			this.returnPage = returnPage;
		}

		@Override
		public Class< ? extends SecurePage> getPageIdentity()
		{
			return AccountEditPage.class;
		}

		@Override
		public Page getPage()
		{
			return new AccountEditPage(returnPage, new DeelnemerAccount());
		}
	}

	private static class NieuweRolPageLink implements IPageLink
	{
		private static final long serialVersionUID = 1L;

		private NieuweRolPageLink()
		{
		}

		@Override
		public Class< ? extends SecurePage> getPageIdentity()
		{
			return RolEditPage.class;
		}

		@Override
		public Page getPage()
		{
			Rol rol = new Rol();
			rol.setRechtenSoort(RechtenSoort.DEELNEMER);
			rol.setAuthorisatieNiveau(AuthorisatieNiveau.REST);
			return new RolEditPage(rol);
		}
	}

	private static final long serialVersionUID = 1L;

	public SelfServiceButtonFactory()
	{
		super(1);
	}

	@Override
	public void createNieuweAccountButton(BottomRowPanel parent, SecurePage returnPage)
	{
		parent.addButton(new PageLinkButton(parent, "Deelnemer account toevoegen",
			new NieuweAccountPageLink(returnPage)));
	}

	@Override
	public void createNieuweRolButton(BottomRowPanel parent)
	{
		parent.addButton(new PageLinkButton(parent, "Deelnemer rol toevoegen",
			new NieuweRolPageLink()));
	}
}
