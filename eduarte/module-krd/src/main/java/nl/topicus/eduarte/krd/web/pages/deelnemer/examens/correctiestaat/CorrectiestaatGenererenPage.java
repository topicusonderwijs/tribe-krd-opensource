package nl.topicus.eduarte.krd.web.pages.deelnemer.examens.correctiestaat;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.krd.principals.deelnemer.examen.DeelnemerExamensCollectief;
import nl.topicus.eduarte.krd.web.pages.deelnemer.examens.DeelnemerKwalificatiePage;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;

/**
 * Pagina waarop een procesverbaal aangemaakt kan worden
 * 
 * @author vandekamp
 */
@PageInfo(title = "Correctiestaat genereren", menu = "Deelnemer -> Examens -> Actie overzicht -> VO Correctiestaat genereren")
@InPrincipal(DeelnemerExamensCollectief.class)
public class CorrectiestaatGenererenPage extends SecurePage implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private Form<Void> form;

	private Correctiestaat correctiestaat;

	public CorrectiestaatGenererenPage()
	{
		super(CoreMainMenuItem.Deelnemer);
		correctiestaat = new Correctiestaat();
		form = new Form<Void>("form");
		AutoFieldSet<Correctiestaat> fieldset =
			new AutoFieldSet<Correctiestaat>("fieldset", Model.of(correctiestaat));
		fieldset.setPropertyNames("onderwijsproduct", "tijdvak");
		fieldset.setRenderMode(RenderMode.EDIT);
		form.add(fieldset);
		add(form);
		createComponents();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.ActieOverzicht);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AnnulerenButton(panel, DeelnemerKwalificatiePage.class));
		panel.addButton(new OpslaanButton(panel, form, "Deelnemers selecteren")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				setResponsePage(new CorrectiestaatDeelnemersSelecterenPage(correctiestaat));
			}
		});
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(correctiestaat);
		super.onDetach();
	}
}
