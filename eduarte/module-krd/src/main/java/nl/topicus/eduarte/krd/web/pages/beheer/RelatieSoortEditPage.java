package nl.topicus.eduarte.krd.web.pages.beheer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.transformers.HoofdletterMode;
import nl.topicus.cobra.web.behaviors.HoofdletterAjaxHandler;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.entities.personen.RelatieSoort;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.RelatieSoortPrincipal;
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

/**
 * 
 * 
 * @author vanharen
 */

@PageInfo(title = "Relatie soort", menu = "Beheer > Relatie sooort > [Relatie soort]")
@InPrincipal(RelatieSoortPrincipal.class)
public class RelatieSoortEditPage extends AbstractBeheerPage<RelatieSoort> implements
		IModuleEditPage<RelatieSoort>
{
	private Form<Void> form;

	/**
	 * Constructor voor de ToevoegenButton.
	 * 
	 * @param returnPage
	 */
	public RelatieSoortEditPage(SecurePage returnPage)
	{
		this(ModelFactory.getModel(new RelatieSoort()), returnPage);
	}

	public RelatieSoortEditPage(IModel<RelatieSoort> relatieSoortModel, SecurePage returnPage)
	{
		super(ModelFactory.getCompoundChangeRecordingModel(relatieSoortModel.getObject(),
			new DefaultModelManager(RelatieSoort.class)), BeheerMenuItem.Relatiesoort);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<RelatieSoort> fieldSet =
			new AutoFieldSet<RelatieSoort>("relatieSoort", getContextModel(), "Relatie Soort");
		fieldSet.setPropertyNames("code", "naam", "actief", "persoonOpname", "organisatieOpname");
		fieldSet.setSortAccordingToPropertyNames(true);
		fieldSet.setRenderMode(RenderMode.EDIT);
		form.add(fieldSet);

		fieldSet.addModifier("code", new HoofdletterAjaxHandler(HoofdletterMode.Alles));

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
				RelatieSoort relatieSoort =
					(RelatieSoort) RelatieSoortEditPage.this.getDefaultModelObject();
				relatieSoort.saveOrUpdate();
				relatieSoort.commit();

				EduArteRequestCycle.get().setResponsePage(
					RelatieSoortEditPage.this.getReturnPageClass());
			}
		});

		panel.addButton(new AnnulerenButton(panel, RelatieSoortEditPage.this.getReturnPageClass()));

		panel.addButton(new ProbeerTeVerwijderenButton(panel, getContextModel(),
			"deze relatie soort", RelatieSoortZoekenPage.class));
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}
}
