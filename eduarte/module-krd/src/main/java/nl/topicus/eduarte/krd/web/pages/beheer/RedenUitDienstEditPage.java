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
import nl.topicus.eduarte.entities.personen.RedenUitDienst;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.RedenUitDienstPrincipal;
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

@PageInfo(title = "Reden uit dienst", menu = "Beheer > Reden uit dienst > [RedenUitDienst]")
@InPrincipal(RedenUitDienstPrincipal.class)
public class RedenUitDienstEditPage extends AbstractBeheerPage<RedenUitDienst> implements
		IModuleEditPage<RedenUitDienst>
{
	private Form<Void> form;

	/**
	 * Constructor voor de ToevoegenButton.
	 * 
	 * @param returnPage
	 */
	public RedenUitDienstEditPage(SecurePage returnPage)
	{
		this(ModelFactory.getCompoundChangeRecordingModel(new RedenUitDienst(),
			new DefaultModelManager(RedenUitDienst.class)), returnPage);
	}

	/**
	 * Constructor voor de BewerkenButton.
	 * 
	 * @param redenUitDienstModel
	 * @param returnPage
	 */
	public RedenUitDienstEditPage(IModel<RedenUitDienst> redenUitDienstModel, SecurePage returnPage)
	{
		super(redenUitDienstModel, BeheerMenuItem.RedenUitDienst);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<RedenUitDienst> fieldSet =
			new AutoFieldSet<RedenUitDienst>("redenUitDienst", getContextModel(),
				"Reden uit dienst");
		fieldSet.setPropertyNames("code", "naam", "actief");
		fieldSet.setSortAccordingToPropertyNames(true);
		fieldSet.setRenderMode(RenderMode.EDIT);
		form.add(fieldSet);

		fieldSet.addModifier("naam", new UniqueConstraintValidator<String>(fieldSet,
			"Reden uit dienst", "naam", "organisatie"));
		fieldSet.addModifier("code", new UniqueConstraintValidator<String>(fieldSet,
			"Reden uit dienst", "code", "organisatie"));

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
				RedenUitDienst reden =
					(RedenUitDienst) RedenUitDienstEditPage.this.getDefaultModelObject();
				reden.saveOrUpdate();
				reden.commit();

				EduArteRequestCycle.get().setResponsePage(
					RedenUitDienstEditPage.this.getReturnPageClass());
			}
		});

		panel
			.addButton(new AnnulerenButton(panel, RedenUitDienstEditPage.this.getReturnPageClass()));

		panel.addButton(new ProbeerTeVerwijderenButton(panel, getContextModel(),
			"deze reden uit dienst", RedenUitDienstZoekenPage.class));
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}
}
