package nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs.keuzescontroleren;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.link.ConfirmationAjaxLink;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerVerbintenissenWrite;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;

/**
 * @author idserda
 */
@PageInfo(title = "Collectief aanmaken", menu = {"Deelnemer > Collectief > Aanmaken"})
@InPrincipal(DeelnemerVerbintenissenWrite.class)
public class DeelnemerKeuzesControlerenPage extends SecurePage
{
	private Form<Void> form;

	public DeelnemerKeuzesControlerenPage()
	{
		super("hop", CoreMainMenuItem.Deelnemer);

		form = new Form<Void>("form");

		add(form);

		createComponents();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.Aanmaken);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AbstractBottomRowButton(panel, "Controle starten", CobraKeyAction.GEEN,
			ButtonAlignment.RIGHT)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected WebMarkupContainer getLink(String linkId)
			{
				return new ConfirmationAjaxLink<Void>(linkId, "Nieuwe controle starten?")
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						DeelnemerKeuzesControlerenOverzichtPage controlerenPage =
							new DeelnemerKeuzesControlerenOverzichtPage();
						controlerenPage.startJob(new DeelnemerKeuzesControlerenDataMap());
						setResponsePage(controlerenPage);
					}
				};
			}
		});
	}
}