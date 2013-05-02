package nl.topicus.eduarte.krd.web.pages.beheer.contract;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.validators.UniqueConstraintValidator;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.entities.contract.TypeFinanciering;
import nl.topicus.eduarte.krd.principals.beheer.TypeFinancieringen;
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

@PageInfo(title = "Soort contract financiering", menu = "Beheer > Beheer tabellen > [Soort contract financiering]")
@InPrincipal(TypeFinancieringen.class)
public class TypeFinancieringEditPage extends AbstractBeheerPage<TypeFinanciering> implements
		IModuleEditPage<TypeFinanciering>
{
	private Form<Void> form;

	/**
	 * Constructor voor de ToevoegenButton.
	 * 
	 * @param returnPage
	 */
	public TypeFinancieringEditPage(SecurePage returnPage)
	{
		this(ModelFactory.getModel(new TypeFinanciering()), returnPage);
	}

	/**
	 * Constructor voor de BewerkenButton.
	 * 
	 * @param functieModel
	 * @param returnPage
	 */
	public TypeFinancieringEditPage(IModel<TypeFinanciering> functieModel, SecurePage returnPage)
	{
		super(ModelFactory.getCompoundChangeRecordingModel(functieModel.getObject(),
			new DefaultModelManager(TypeFinanciering.class)), BeheerMenuItem.TypeFinancieringen);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<TypeFinanciering> typeFinancieringFieldSet =
			new AutoFieldSet<TypeFinanciering>("typeFinanciering", getContextModel(),
				"Soort financiering");
		typeFinancieringFieldSet.setPropertyNames("code", "naam", "actief");
		typeFinancieringFieldSet.setRenderMode(RenderMode.EDIT);
		typeFinancieringFieldSet.addModifier("code", new UniqueConstraintValidator<String>(
			typeFinancieringFieldSet, "financiering soort", "code", "organisatie"));

		form.add(typeFinancieringFieldSet);

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
				TypeFinanciering typeFinanciering =
					(TypeFinanciering) TypeFinancieringEditPage.this.getDefaultModelObject();
				typeFinanciering.saveOrUpdate();
				typeFinanciering.commit();

				EduArteRequestCycle.get().setResponsePage(TypeFinancieringZoekenPage.class);
			}
		});

		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				TypeFinanciering typeFinanciering =
					(TypeFinanciering) TypeFinancieringEditPage.this.getDefaultModelObject();
				typeFinanciering.saveOrUpdate();
				typeFinanciering.commit();

				TypeFinancieringEditPage page = new TypeFinancieringEditPage(getReturnPage());
				EduArteRequestCycle.get().setResponsePage(page);
			}

			@Override
			public String getLabel()
			{
				return "Opslaan en nieuwe toevoegen";
			}

			@Override
			public ActionKey getAction()
			{
				return CobraKeyAction.VOLGENDE;
			}
		});

		panel.addButton(new AnnulerenButton(panel, TypeFinancieringEditPage.this
			.getReturnPageClass()));

		panel.addButton(new ProbeerTeVerwijderenButton(panel, getContextModel(),
			"deze soort financiering", TypeFinancieringZoekenPage.class));
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getContextModel(), "naam"));
	}
}
