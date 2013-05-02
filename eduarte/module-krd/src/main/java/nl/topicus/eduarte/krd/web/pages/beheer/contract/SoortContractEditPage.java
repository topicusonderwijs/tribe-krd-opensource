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
import nl.topicus.cobra.web.validators.UniqueConstraintFormValidator;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.entities.contract.SoortContract;
import nl.topicus.eduarte.krd.principals.beheer.SoortContracten;
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

@PageInfo(title = "Soort contract", menu = "Beheer > Beheer tabellen > [Soort contract]")
@InPrincipal(SoortContracten.class)
public class SoortContractEditPage extends AbstractBeheerPage<SoortContract> implements
		IModuleEditPage<SoortContract>
{
	private Form<Void> form;

	/**
	 * Constructor voor de ToevoegenButton.
	 * 
	 * @param returnPage
	 */
	public SoortContractEditPage(SecurePage returnPage)
	{
		this(ModelFactory.getModel(new SoortContract()), returnPage);
	}

	/**
	 * Constructor voor de BewerkenButton.
	 * 
	 * @param functieModel
	 * @param returnPage
	 */
	public SoortContractEditPage(IModel<SoortContract> functieModel, SecurePage returnPage)
	{
		super(ModelFactory.getCompoundChangeRecordingModel(functieModel.getObject(),
			new DefaultModelManager(SoortContract.class)), BeheerMenuItem.SoortContracten);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<SoortContract> soortContractFieldSet =
			new AutoFieldSet<SoortContract>("soortContract", getContextModel(), "Soort contract");
		soortContractFieldSet.setPropertyNames("code", "naam", "inburgering", "actief");
		soortContractFieldSet.setSortAccordingToPropertyNames(true);
		soortContractFieldSet.setRenderMode(RenderMode.EDIT);
		form.add(soortContractFieldSet);

		form
			.add(new UniqueConstraintFormValidator(soortContractFieldSet, "Soort contract", "code"));

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
				SoortContract soortContract =
					(SoortContract) SoortContractEditPage.this.getDefaultModelObject();
				soortContract.saveOrUpdate();
				soortContract.commit();

				EduArteRequestCycle.get().setResponsePage(SoortContractZoekenPage.class);
			}
		});

		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				SoortContract soortContract =
					(SoortContract) SoortContractEditPage.this.getDefaultModelObject();
				soortContract.saveOrUpdate();
				soortContract.commit();

				SoortContractEditPage page = new SoortContractEditPage(getReturnPage());
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

		panel
			.addButton(new AnnulerenButton(panel, SoortContractEditPage.this.getReturnPageClass()));

		panel.addButton(new ProbeerTeVerwijderenButton(panel, getContextModel(),
			"dit soort contract", SoortContractZoekenPage.class));
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}
}
