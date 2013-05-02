package nl.topicus.eduarte.web.pages.beheer.organisatie;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.validators.UniqueConstraintFormValidator;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.core.principals.groep.GroepsTypePrincipal;
import nl.topicus.eduarte.entities.groep.Groepstype;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.bottomrow.ProbeerTeVerwijderenButton;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

@PageInfo(title = "Groepstype bewerken", menu = "Beheer > Beheer tabellen > [Groepstype]")
@InPrincipal(GroepsTypePrincipal.class)
public class GroepstypeEditPage extends AbstractBeheerPage<Groepstype> implements
		IModuleEditPage<Groepstype>
{
	private Form<Void> form;

	/**
	 * Constructor voor de ToevoegenButton.
	 * 
	 * @param returnPage
	 */
	public GroepstypeEditPage(SecurePage returnPage)
	{
		this(ModelFactory.getModel(new Groepstype(), new DefaultModelManager(Groepstype.class)),
			returnPage);
	}

	public GroepstypeEditPage(IModel<Groepstype> groepstype, SecurePage returnPage)
	{
		super(groepstype, BeheerMenuItem.Groepstype);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<Groepstype> fieldset =
			new AutoFieldSet<Groepstype>("groepstype", getContextModel(), "Groepstype");
		fieldset.setPropertyNames("code", "naam", "plaatsingsgroep", "actief");
		fieldset.setRenderMode(RenderMode.EDIT);
		fieldset.setSortAccordingToPropertyNames(true);
		form.add(fieldset);

		form.add(new UniqueConstraintFormValidator(fieldset, "groepstype", "code"));
		form.add(new UniqueConstraintFormValidator(fieldset, "groepstype", "naam"));

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				Groepstype groepstype =
					(Groepstype) GroepstypeEditPage.this.getDefaultModelObject();
				groepstype.saveOrUpdate();
				groepstype.commit();

				EduArteRequestCycle.get().setResponsePage(GroepstypeEditPage.this.getReturnPage());
			}
		});

		panel.addButton(new AnnulerenButton(panel, GroepstypeEditPage.this.getReturnPage()));

		panel.addButton(new ProbeerTeVerwijderenButton(panel, getContextModel(), "dit groepstype",
			GroepstypeZoekenPage.class));
	}
}
