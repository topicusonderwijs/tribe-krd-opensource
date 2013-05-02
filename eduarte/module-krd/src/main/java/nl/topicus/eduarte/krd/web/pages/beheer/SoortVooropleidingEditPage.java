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
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.SoortVooropleidingPrincipal;
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

@PageInfo(title = "Soort vooropleiding", menu = "Beheer > Soort vooropleiding > [Soort vooropleiding]")
@InPrincipal(SoortVooropleidingPrincipal.class)
public class SoortVooropleidingEditPage extends AbstractBeheerPage<SoortVooropleiding> implements
		IModuleEditPage<SoortVooropleiding>
{
	private Form<Void> form;

	/**
	 * Constructor voor de ToevoegenButton.
	 * 
	 * @param returnPage
	 */
	public SoortVooropleidingEditPage(SecurePage returnPage)
	{
		this(ModelFactory.getCompoundChangeRecordingModel(new SoortVooropleiding(),
			new DefaultModelManager(SoortVooropleiding.class)), returnPage);
	}

	/**
	 * Constructor voor de BewerkenButton.
	 * 
	 * @param soortVooropleidingModel
	 * @param returnPage
	 */
	public SoortVooropleidingEditPage(IModel<SoortVooropleiding> soortVooropleidingModel,
			SecurePage returnPage)
	{
		super(soortVooropleidingModel, BeheerMenuItem.SoortVooropleiding);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<SoortVooropleiding> fieldSet =
			new AutoFieldSet<SoortVooropleiding>("soortVooropleiding", getContextModel(),
				"Soort vooropleiding");
		fieldSet.setPropertyNames("code", "naam", "soortOnderwijsMetDiploma",
			"soortOnderwijsZonderDiploma", "actief");
		fieldSet.setRenderMode(RenderMode.EDIT);
		form.add(fieldSet);

		fieldSet.addModifier("naam", new UniqueConstraintValidator<String>(fieldSet,
			"Soort vooropleiding", "naam", "organisatie"));

		fieldSet.addModifier("code", new UniqueConstraintValidator<String>(fieldSet,
			"Soort vooropleiding", "code", "organisatie"));

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
				SoortVooropleiding soort =
					(SoortVooropleiding) SoortVooropleidingEditPage.this.getDefaultModelObject();
				soort.saveOrUpdate();
				soort.commit();

				EduArteRequestCycle.get().setResponsePage(
					SoortVooropleidingEditPage.this.getReturnPageClass());
			}
		});

		panel.addButton(new AnnulerenButton(panel, SoortVooropleidingEditPage.this
			.getReturnPageClass()));

		panel.addButton(new ProbeerTeVerwijderenButton(panel, getContextModel(),
			"deze soort vooropleiding", SoortVooropleidingZoekenPage.class));
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}
}
