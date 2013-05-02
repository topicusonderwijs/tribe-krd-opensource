package nl.topicus.eduarte.krd.web.pages.deelnemer.examens;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.krd.principals.deelnemer.examen.DeelnemerExamensCollectief;
import nl.topicus.eduarte.krd.web.components.choice.TijdvakComboBox;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;

/**
 * Pagina waarop een tijdvak geselecteerd kan worden
 */
@PageInfo(title = "Examens", menu = "Deelnemer -> Examens -> Actie overzicht -> [Actie]")
@InPrincipal(DeelnemerExamensCollectief.class)
public class TijdvakAangevenPage extends SecurePage implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private Form<Void> form;

	private KwalificatieModel kwalificatieModel;

	public TijdvakAangevenPage(KwalificatieModel kwalificatieModel)
	{
		super(CoreMainMenuItem.Deelnemer);
		this.kwalificatieModel = kwalificatieModel;
		form = new Form<Void>("form");
		form.add(new TijdvakComboBox("tijdvak", new PropertyModel<Integer>(kwalificatieModel,
			"tijdvak")).setRequired(true));
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
		panel.addButton(new OpslaanButton(panel, form, "Volgende stap")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				setResponsePage(new DeelnemerKwalificatieSelecterenPage(kwalificatieModel));
			}
		});
		panel.addButton(new AnnulerenButton(panel, DeelnemerKwalificatiePage.class));
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(kwalificatieModel);
		super.onDetach();
	}
}
