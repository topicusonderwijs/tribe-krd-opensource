package nl.topicus.eduarte.web.pages.beheer.onderwijs;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.cobra.web.validators.UniqueConstraintValidator;
import nl.topicus.eduarte.core.principals.onderwijs.CurriculumWijzigen;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductNiveauaanduiding;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Niveauaanduiding onderwijsproduct bewerken", menu = {
	"Beheer > Onderwijs > Niveauaanduiding > Toevoegen",
	"Beheer > Onderwijs > Niveauaanduiding > [niveau]"})
@InPrincipal(CurriculumWijzigen.class)
public class OnderwijsproductNiveauaanduidingEditPage extends
		AbstractBeheerPage<OnderwijsproductNiveauaanduiding> implements IEditPage
{
	private Form<Void> form;

	public OnderwijsproductNiveauaanduidingEditPage(OnderwijsproductNiveauaanduiding niveau,
			SecurePage returnPage)
	{
		super(ModelFactory.getModel(niveau, new DefaultModelManager(
			OnderwijsproductNiveauaanduiding.class)), BeheerMenuItem.OnderwijsproductNiveau);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<OnderwijsproductNiveauaanduiding> fieldset =
			new AutoFieldSet<OnderwijsproductNiveauaanduiding>("niveau", getContextModel(),
				"Niveauaanduiding");
		fieldset.setPropertyNames("code", "naam", "actief");
		fieldset.setRenderMode(RenderMode.EDIT);
		fieldset.setSortAccordingToPropertyNames(true);
		fieldset.addModifier("code", new UniqueConstraintValidator<String>(fieldset,
			"Niveauaanduiding", "code", "organisatie"));
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
				OnderwijsproductNiveauaanduiding niveau = getContextModelObject();
				niveau.saveOrUpdate();
				niveau.commit();
				setResponsePage(OnderwijsproductNiveauaanduidingZoekenPage.class);
			}
		});
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return OnderwijsproductNiveauaanduidingEditPage.this.getReturnPage();
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return OnderwijsproductNiveauaanduidingEditPage.this.getReturnPageClass();
			}

		}));
		panel.addButton(new VerwijderButton(panel, "Verwijder")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				OnderwijsproductNiveauaanduiding niveau = getContextModelObject();
				niveau.delete();
				niveau.commit();
				setResponsePage(OnderwijsproductNiveauaanduidingZoekenPage.class);
			}

			@Override
			public boolean isVisible()
			{
				OnderwijsproductNiveauaanduiding niveau = getContextModelObject();
				if (niveau.isSaved())
				{
					CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper< ? , ? > helper =
						DataAccessRegistry
							.getHelper(CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);
					return !helper.isOnderwijsproductNiveauInGebruik(niveau);
				}
				return false;
			}
		});
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}
}
