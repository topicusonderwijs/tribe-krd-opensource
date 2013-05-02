package nl.topicus.eduarte.krd.web.pages.deelnemer.examens;

import java.util.Collection;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.principals.deelnemer.examen.DeelnemerExamensCollectief;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Pagina waarop een tijdvak geselecteerd kan worden
 */
@PageInfo(title = "Examens", menu = "Deelnemer -> Examens -> Actie overzicht -> [Actie]")
@InPrincipal(DeelnemerExamensCollectief.class)
public class EigenschappenExamennummeringPage extends SecurePage implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private Form<Void> form;

	private IModel<Collection<Verbintenis>> selection;

	private KwalificatieModel kwalificatieModel;

	private SecurePage returnPage;

	public EigenschappenExamennummeringPage(KwalificatieModel kwalificatieModel,
			Collection<Verbintenis> selection, SecurePage returnPage)
	{
		super(CoreMainMenuItem.Deelnemer);
		this.kwalificatieModel = kwalificatieModel;
		this.selection = ModelFactory.getListModel(selection);
		this.returnPage = returnPage;
		form = new Form<Void>("form");
		kwalificatieModel.setExamennummering(new Examennummering());
		AutoFieldSet<Examennummering> autoFieldSet =
			new AutoFieldSet<Examennummering>("inputFields", Model.of(kwalificatieModel
				.getExamennummering()), "Eigenschappen van de nummering instellen");
		autoFieldSet.setRenderMode(RenderMode.EDIT);
		autoFieldSet.addModifier("prefix", new SimpleAttributeModifier("maxlength", Integer
			.toString(ComponentUtil.findLengthInAnnotations(Examendeelname.class,
				"examennummerPrefix"))));
		form.add(autoFieldSet);
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
		panel.addButton(new TerugButton(panel, returnPage));
		panel.addButton(new OpslaanButton(panel, form, "Volgende stap")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				setResponsePage(new DeelnemerKwalificatieGeselecteerdPage(kwalificatieModel,
					selection.getObject(), EigenschappenExamennummeringPage.this));
			}
		});
		panel.addButton(new AnnulerenButton(panel, DeelnemerKwalificatiePage.class));
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(kwalificatieModel);
		ComponentUtil.detachQuietly(selection);
		super.onDetach();
	}
}
