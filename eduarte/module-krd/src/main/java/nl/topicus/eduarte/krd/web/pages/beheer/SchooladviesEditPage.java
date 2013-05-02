package nl.topicus.eduarte.krd.web.pages.beheer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.validators.UniqueConstraintValidator;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.entities.inschrijving.Schooladvies;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.SchooladviesPrincipal;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.bottomrow.ProbeerTeVerwijderenButton;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Schooladvies", menu = "Beheer > Schooladvies > [Schooladvies]")
@InPrincipal(SchooladviesPrincipal.class)
public class SchooladviesEditPage extends AbstractBeheerPage<Schooladvies> implements
		IModuleEditPage<Schooladvies>
{
	private Form<Void> form;

	/**
	 * Constructor voor de ToevoegenButton.
	 * 
	 * @param returnPage
	 */
	public SchooladviesEditPage(SecurePage returnPage)
	{
		this(ModelFactory.getCompoundChangeRecordingModel(new Schooladvies(),
			new DefaultModelManager(Schooladvies.class)), returnPage);
	}

	/**
	 * Constructor voor de BewerkenButton.
	 * 
	 * @param schooladviesModel
	 * @param returnPage
	 */
	public SchooladviesEditPage(IModel<Schooladvies> schooladviesModel, SecurePage returnPage)
	{
		super(schooladviesModel, BeheerMenuItem.Schooladvies);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<Schooladvies> fieldSet =
			new AutoFieldSet<Schooladvies>("schooladvies", getContextModel(), "Schooladvies");
		fieldSet.setPropertyNames("naam", "actief");
		fieldSet.setRenderMode(RenderMode.EDIT);
		form.add(fieldSet);

		fieldSet.addModifier("naam", new UniqueConstraintValidator<String>(fieldSet,
			"Schooladvies", "naam", "organisatie"));

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
				Schooladvies advies =
					(Schooladvies) SchooladviesEditPage.this.getDefaultModelObject();
				advies.saveOrUpdate();
				advies.commit();

				EduArteRequestCycle.get().setResponsePage(
					SchooladviesEditPage.this.getReturnPageClass());
			}
		});

		panel.addButton(new AnnulerenButton(panel, SchooladviesEditPage.this.getReturnPageClass()));

		panel.addButton(new ProbeerTeVerwijderenButton(panel, getContextModel(),
			"dit schooladvies", SchooladviesZoekenPage.class));
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}
}
