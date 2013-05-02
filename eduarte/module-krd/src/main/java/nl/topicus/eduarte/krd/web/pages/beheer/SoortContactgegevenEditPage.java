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
import nl.topicus.cobra.web.validators.UniqueConstraintFormValidator;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.SoortContactgevenPrincipal;
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

@PageInfo(title = "Soort contactgegeven", menu = "Beheer > Soort contactgegevens > [SoortContactgegeven]")
@InPrincipal(SoortContactgevenPrincipal.class)
public class SoortContactgegevenEditPage extends AbstractBeheerPage<SoortContactgegeven> implements
		IModuleEditPage<SoortContactgegeven>
{
	private Form<Void> form;

	/**
	 * Constructor voor de ToevoegenButton.
	 * 
	 * @param returnPage
	 */
	public SoortContactgegevenEditPage(SecurePage returnPage)
	{
		this(ModelFactory.getModel(new SoortContactgegeven()), returnPage);
	}

	/**
	 * Constructor voor de BewerkenButton.
	 * 
	 * @param soortContactgegevenModel
	 * @param returnPage
	 */
	public SoortContactgegevenEditPage(IModel<SoortContactgegeven> soortContactgegevenModel,
			SecurePage returnPage)
	{
		super(ModelFactory.getCompoundChangeRecordingModel(soortContactgegevenModel.getObject(),
			new DefaultModelManager(SoortContactgegeven.class)),
			BeheerMenuItem.SoortContactgegevens);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		final AutoFieldSet<SoortContactgegeven> soortContactgegevenFieldSet =
			new AutoFieldSet<SoortContactgegeven>("soortContactgegeven", getContextModel(),
				"Soort contactgegeven");
		soortContactgegevenFieldSet.setPropertyNames("code", "naam", "typeContactgegeven",
			"standaardContactgegeven", "actief");
		soortContactgegevenFieldSet.setRenderMode(RenderMode.EDIT);
		soortContactgegevenFieldSet.setSortAccordingToPropertyNames(true);
		form.add(soortContactgegevenFieldSet);

		form.add(new UniqueConstraintFormValidator(soortContactgegevenFieldSet,
			"Soort contactgegeven", "code"));

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
				SoortContactgegeven soortContactgegeven =
					(SoortContactgegeven) SoortContactgegevenEditPage.this.getDefaultModelObject();
				soortContactgegeven.saveOrUpdate();
				soortContactgegeven.commit();

				EduArteRequestCycle.get().setResponsePage(SoortContactgegevenZoekenPage.class);
			}
		});

		panel.addButton(new AnnulerenButton(panel, SoortContactgegevenEditPage.this
			.getReturnPageClass()));

		panel.addButton(new ProbeerTeVerwijderenButton(panel, getContextModel(),
			"deze soort contact gegevens", SoortContactgegevenZoekenPage.class));
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}
}
