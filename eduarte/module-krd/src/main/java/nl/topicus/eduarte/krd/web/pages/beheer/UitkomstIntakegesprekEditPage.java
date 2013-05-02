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
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.validators.UniqueConstraintValidator;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.entities.inschrijving.UitkomstIntakegesprek;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.UitkomtIntakegesprekPrincipal;
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

@PageInfo(title = "Uitkomst intakegesprek", menu = "Beheer > Beheer tabellen > [Uitkomst intakegesprek]")
@InPrincipal(UitkomtIntakegesprekPrincipal.class)
public class UitkomstIntakegesprekEditPage extends AbstractBeheerPage<UitkomstIntakegesprek>
		implements IModuleEditPage<UitkomstIntakegesprek>
{
	private Form<Void> form;

	/**
	 * Constructor voor de ToevoegenButton.
	 * 
	 * @param returnPage
	 */
	public UitkomstIntakegesprekEditPage(SecurePage returnPage)
	{
		this(ModelFactory.getModel(new UitkomstIntakegesprek()), returnPage);
	}

	/**
	 * Constructor voor de BewerkenButton.
	 * 
	 * @param uitkomstModel
	 * @param returnPage
	 */
	public UitkomstIntakegesprekEditPage(IModel<UitkomstIntakegesprek> uitkomstModel,
			SecurePage returnPage)
	{
		super(ModelFactory.getCompoundChangeRecordingModel(uitkomstModel.getObject(),
			new DefaultModelManager(UitkomstIntakegesprek.class)),
			BeheerMenuItem.UitkomstIntakegesprek);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<UitkomstIntakegesprek> uitkomstIntakegesprekFieldSet =
			new AutoFieldSet<UitkomstIntakegesprek>("uitkomstIntakegesprek", getContextModel(),
				"Uitkomst Intakegesprek");
		uitkomstIntakegesprekFieldSet.setPropertyNames("code", "naam", "succesvol", "actief");
		uitkomstIntakegesprekFieldSet.setRenderMode(RenderMode.EDIT);
		uitkomstIntakegesprekFieldSet.setSortAccordingToPropertyNames(true);
		uitkomstIntakegesprekFieldSet.addModifier("code", new UniqueConstraintValidator<String>(
			uitkomstIntakegesprekFieldSet, "uitkomst intakegesprek", "code", "organisatie"));

		form.add(uitkomstIntakegesprekFieldSet);

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
				UitkomstIntakegesprek uitkomstIntakegesprek =
					(UitkomstIntakegesprek) UitkomstIntakegesprekEditPage.this
						.getDefaultModelObject();
				uitkomstIntakegesprek.saveOrUpdate();
				uitkomstIntakegesprek.commit();

				EduArteRequestCycle.get().setResponsePage(UitkomstIntakegesprekZoekenPage.class);
			}
		});

		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				UitkomstIntakegesprek uitkomstIntakegesprek =
					(UitkomstIntakegesprek) UitkomstIntakegesprekEditPage.this
						.getDefaultModelObject();
				uitkomstIntakegesprek.saveOrUpdate();
				uitkomstIntakegesprek.commit();

				UitkomstIntakegesprekEditPage page =
					new UitkomstIntakegesprekEditPage(getReturnPage());
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

		panel.addButton(new AnnulerenButton(panel, UitkomstIntakegesprekEditPage.this
			.getReturnPageClass()));

		panel.addButton(new ProbeerTeVerwijderenButton(panel, getContextModel(), "deze uitkomst",
			UitkomstIntakegesprekZoekenPage.class));
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}
}
