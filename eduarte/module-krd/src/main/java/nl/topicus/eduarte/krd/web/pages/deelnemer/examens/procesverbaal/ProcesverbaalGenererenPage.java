package nl.topicus.eduarte.krd.web.pages.deelnemer.examens.procesverbaal;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.krd.principals.deelnemer.examen.DeelnemerExamensCollectief;
import nl.topicus.eduarte.krd.web.pages.deelnemer.examens.DeelnemerKwalificatiePage;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.MedewerkerZoekFilter;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * Pagina waarop een procesverbaal aangemaakt kan worden
 * 
 * @author vandekamp
 */
@PageInfo(title = "Procesverbaal genereren", menu = "Deelnemer -> Examens -> Actie overzicht -> Procesverbaal genereren")
@InPrincipal(DeelnemerExamensCollectief.class)
public class ProcesverbaalGenererenPage extends SecurePage implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private Form<Void> form;

	private Procesverbaal procesverbaal;

	public ProcesverbaalGenererenPage()
	{
		super(CoreMainMenuItem.Deelnemer);
		procesverbaal = new Procesverbaal();
		form = new Form<Void>("form");
		AutoFieldSet<Procesverbaal> fieldset =
			new AutoFieldSet<Procesverbaal>("fieldset", Model.of(procesverbaal));
		fieldset.setPropertyNames("organisatieEenheid", "opleiding", "onderwijsproduct", "lokaal",
			"examenDatum", "begintijd", "eindtijd");
		OpleidingZoekFilter opleidingFilter = new OpleidingZoekFilter();
		opleidingFilter.setOrganisatieEenheidModel(new PropertyModel<OrganisatieEenheid>(fieldset
			.getModel(), "organisatieEenheid"));
		fieldset.addFieldModifier(new ConstructorArgModifier("opleiding", opleidingFilter));
		fieldset.setRenderMode(RenderMode.EDIT);
		form.add(fieldset);
		add(form);
		MedewerkerZoekFilter filter = new MedewerkerZoekFilter();
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		add(new MedewerkerToevoegenPanel("medewerkerToevoegen", procesverbaal, filter));
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
				setResponsePage(new ProcesverbaalDeelnemersSelecterenPage(procesverbaal));
			}
		});
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(procesverbaal);
		super.onDetach();
	}
}
