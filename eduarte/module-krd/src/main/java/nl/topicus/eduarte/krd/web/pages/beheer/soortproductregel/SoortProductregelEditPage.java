package nl.topicus.eduarte.krd.web.pages.beheer.soortproductregel;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.RenderModeModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.cobra.web.validators.UniqueConstraintValidator;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.dao.helpers.SoortProductregelDataAccessHelper;
import nl.topicus.eduarte.entities.productregel.SoortProductregel;
import nl.topicus.eduarte.krd.principals.onderwijs.BeheerSoortProductregels;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Soort productregel", menu = "Beheer > Beheer tabellen > Soort productregel > Nieuw soort productregel")
@InPrincipal(BeheerSoortProductregels.class)
public class SoortProductregelEditPage extends AbstractBeheerPage<SoortProductregel> implements
		IEditPage
{
	private Form<Void> form;

	public SoortProductregelEditPage(SoortProductregel soortProductregel, SecurePage returnPage)
	{
		super(ModelFactory.getModel(soortProductregel, new DefaultModelManager(
			SoortProductregel.class)), BeheerMenuItem.SoortProductregels);
		setReturnPage(returnPage);
		createForm(soortProductregel.isLandelijk());
	}

	private void createForm(boolean readonly)
	{
		form = new Form<Void>("form");
		add(form);
		AutoFieldSet<SoortProductregel> fieldset =
			new AutoFieldSet<SoortProductregel>("soortProductregels", getContextModel(),
				"Soort productregels");
		fieldset.setPropertyNames("volgnummer", "naam", "diplomanaam", "actief", "taxonomie");
		if (readonly)
			fieldset.setRenderMode(RenderMode.DISPLAY);
		else
			fieldset.setRenderMode(RenderMode.EDIT);
		fieldset.setSortAccordingToPropertyNames(true);
		fieldset.addModifier("volgnummer", new UniqueConstraintValidator<Integer>(fieldset,
			"Soort productregel", "volgnummer", "taxonomie", "organisatie"));
		fieldset.addModifier("naam", new UniqueConstraintValidator<String>(fieldset,
			"Soort productregel", "naam", "diplomanaam", "taxonomie", "organisatie"));
		fieldset.addFieldModifier(new RenderModeModifier(RenderMode.EDIT, "diplomanaam"));
		form.add(fieldset);
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
				SoortProductregel soortProductregel =
					(SoortProductregel) SoortProductregelEditPage.this.getDefaultModelObject();
				soortProductregel.saveOrUpdate();
				soortProductregel.commit();
				EduArteRequestCycle.get().setResponsePage(SoortProductregelZoekenPage.class);
			}

			@Override
			public boolean isVisible()
			{
				return !((SoortProductregel) SoortProductregelEditPage.this.getDefaultModelObject())
					.isLandelijk();
			}
		});
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return SoortProductregelEditPage.this.getReturnPage();
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return SoortProductregelEditPage.this.getReturnPageClass();
			}
		}));
		panel.addButton(new VerwijderButton(panel, "Verwijder")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				SoortProductregel soortProductregel =
					(SoortProductregel) SoortProductregelEditPage.this.getDefaultModelObject();
				soortProductregel.delete();
				soortProductregel.commit();
				EduArteRequestCycle.get().setResponsePage(SoortProductregelZoekenPage.class);
			}

			@Override
			public boolean isVisible()
			{
				SoortProductregel soortProductregel =
					(SoortProductregel) SoortProductregelEditPage.this.getDefaultModelObject();
				SoortProductregelDataAccessHelper helper =
					DataAccessRegistry.getHelper(SoortProductregelDataAccessHelper.class);
				return soortProductregel.isSaved() && !soortProductregel.isLandelijk()
					&& !helper.isSoortProductregelInGebruik(soortProductregel);
			}
		});
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}
}
