package nl.topicus.eduarte.web.pages.onderwijs.opleiding;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.cobra.web.validators.UniqueConstraintValidator;
import nl.topicus.eduarte.core.principals.onderwijs.StandaardToetsfiltersBeheren;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.resultaatstructuur.StandaardToetsCodeFilter;
import nl.topicus.eduarte.web.components.menu.OpleidingMenuItem;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.ToetsCodeFilterZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;

/**
 * @author loite
 */
@PageInfo(title = "Opleiding Toetsfilters bewerken", menu = {"Onderwijs > [opleiding] > Toetsfilters"})
@InPrincipal(StandaardToetsfiltersBeheren.class)
public class OpleidingToetsfilterEditPage extends AbstractOpleidingPage implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private Form<StandaardToetsCodeFilter> form;

	public OpleidingToetsfilterEditPage(StandaardToetsCodeFilter filter)
	{
		super(OpleidingMenuItem.Toetsfilters, filter.getOpleiding());

		add(form =
			new Form<StandaardToetsCodeFilter>("form", ModelFactory
				.getCompoundChangeRecordingModel(filter, new DefaultModelManager(
					StandaardToetsCodeFilter.class))));

		ToetsCodeFilterZoekFilter toetsCodeFilter = new ToetsCodeFilterZoekFilter();
		toetsCodeFilter.setPersoonlijk(false);
		toetsCodeFilter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			this));
		toetsCodeFilter.setOrganisatieEenheidLocatieList(getContextOpleiding().getAanbod());

		AutoFieldSet<StandaardToetsCodeFilter> inputfields =
			new AutoFieldSet<StandaardToetsCodeFilter>("inputfields", form.getModel(),
				"Standaard toetsfilter");
		inputfields.setRenderMode(RenderMode.EDIT);
		inputfields.addModifier("cohort", new UniqueConstraintValidator<Cohort>(form,
			"Standaard toetsfilter", "cohort", "opleiding").setLidwoord("het"));
		inputfields
			.addFieldModifier(new ConstructorArgModifier("toetsCodeFilter", toetsCodeFilter));
		form.add(inputfields);

		createComponents();
	}

	private StandaardToetsCodeFilter getFilter()
	{
		return form.getModelObject();
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
				IChangeRecordingModel< ? > model = (IChangeRecordingModel< ? >) form.getModel();
				model.saveObject();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				setResponsePage(new OpleidingToetsfiltersOverzichtPage(getContextOpleiding()));
			}
		});
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new OpleidingToetsfiltersOverzichtPage(getContextOpleiding());
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return OpleidingToetsfiltersOverzichtPage.class;
			}
		}));
		panel.addButton(new VerwijderButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return getFilter().isSaved();
			}

			@Override
			protected void onClick()
			{
				getFilter().delete();
				getFilter().commit();
				setResponsePage(new OpleidingToetsfiltersOverzichtPage(getContextOpleiding()));
			}
		});
	}
}
