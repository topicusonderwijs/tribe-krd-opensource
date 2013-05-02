package nl.topicus.eduarte.onderwijscatalogus.web.pages.beheer.middelen;

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
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.TypeToets;
import nl.topicus.eduarte.entities.onderwijsproduct.Verbruiksmiddel;
import nl.topicus.eduarte.onderwijscatalogus.principals.beheer.Verbruiksmiddelen;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Verbruiksmiddel", menu = {
	"Beheer > Onderwijs > Verbruiksmiddelen > Nieuw verbruiksmiddel",
	"Beheer > Onderwijs > Verbruiksmiddelen > [verbruiksmiddel]"})
@InPrincipal(Verbruiksmiddelen.class)
public class VerbruiksmiddelEditPage extends AbstractBeheerPage<Verbruiksmiddel> implements
		IEditPage
{
	private Form<Void> form;

	public VerbruiksmiddelEditPage(Verbruiksmiddel middel, SecurePage returnPage)
	{
		super(ModelFactory.getModel(middel, new DefaultModelManager(Verbruiksmiddel.class)),
			BeheerMenuItem.Verbruiksmiddelen);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<Verbruiksmiddel> fieldset =
			new AutoFieldSet<Verbruiksmiddel>("verbruiksmiddel", getContextModel(),
				"Verbruiksmiddel");
		fieldset.setPropertyNames("code", "naam", "actief");
		fieldset.addModifier("code", new UniqueConstraintValidator<String>(fieldset,
			"Verbruiksmiddel", "code", "organisatie"));
		fieldset.setRenderMode(RenderMode.EDIT);
		fieldset.setSortAccordingToPropertyNames(true);
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
				Verbruiksmiddel middel = getContextModelObject();
				middel.saveOrUpdate();
				middel.commit();
				setResponsePage(VerbruiksmiddelZoekenPage.class);
			}
		});
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return VerbruiksmiddelEditPage.this.getReturnPage();
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return VerbruiksmiddelEditPage.this.getReturnPageClass();
			}

		}));
		panel.addButton(new VerwijderButton(panel, "Verwijder")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				Verbruiksmiddel middel = getContextModelObject();
				middel.delete();
				middel.commit();
				setResponsePage(VerbruiksmiddelZoekenPage.class);
			}

			@SuppressWarnings("unchecked")
			@Override
			public boolean isVisible()
			{
				Verbruiksmiddel middel = getContextModelObject();
				if (middel.isSaved())
				{
					CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper<TypeToets, ? > helper =
						DataAccessRegistry
							.getHelper(CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);
					return !helper.isVerbruiksmiddelInGebruik(middel);
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
