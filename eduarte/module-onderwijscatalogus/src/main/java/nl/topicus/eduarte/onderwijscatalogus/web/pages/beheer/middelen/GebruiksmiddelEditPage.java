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
import nl.topicus.eduarte.entities.onderwijsproduct.Gebruiksmiddel;
import nl.topicus.eduarte.entities.onderwijsproduct.TypeToets;
import nl.topicus.eduarte.onderwijscatalogus.principals.beheer.Gebruiksmiddelen;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Gebruiksmiddel", menu = {
	"Beheer > Onderwijs > Gebruiksmiddelen > Nieuw gebruiksmiddel",
	"Beheer > Onderwijs > Gebruiksmiddelen > [gebruiksmiddel]"})
@InPrincipal(Gebruiksmiddelen.class)
public class GebruiksmiddelEditPage extends AbstractBeheerPage<Gebruiksmiddel> implements IEditPage
{
	private Form<Void> form;

	public GebruiksmiddelEditPage(Gebruiksmiddel gebruiksmiddel, SecurePage returnPage)
	{
		super(ModelFactory.getModel(gebruiksmiddel, new DefaultModelManager(Gebruiksmiddel.class)),
			BeheerMenuItem.Gebruiksmiddelen);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<Gebruiksmiddel> fieldset =
			new AutoFieldSet<Gebruiksmiddel>("gebruiksmiddel", getContextModel(), "Gebruiksmiddel");
		fieldset.setPropertyNames("code", "naam", "actief");
		fieldset.setRenderMode(RenderMode.EDIT);
		fieldset.setSortAccordingToPropertyNames(true);
		fieldset.addModifier("code", new UniqueConstraintValidator<String>(fieldset,
			"Gebruiksmiddel", "code", "organisatie"));
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
				Gebruiksmiddel middel = getContextModelObject();
				middel.saveOrUpdate();
				middel.commit();
				setResponsePage(GebruiksmiddelZoekenPage.class);
			}
		});
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return GebruiksmiddelEditPage.this.getReturnPage();
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return GebruiksmiddelEditPage.this.getReturnPageClass();
			}

		}));
		panel.addButton(new VerwijderButton(panel, "Verwijder")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				Gebruiksmiddel middel = getContextModelObject();
				middel.delete();
				middel.commit();
				setResponsePage(GebruiksmiddelZoekenPage.class);
			}

			@SuppressWarnings("unchecked")
			@Override
			public boolean isVisible()
			{
				Gebruiksmiddel middel = getContextModelObject();
				if (middel.isSaved())
				{
					CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper<TypeToets, ? > helper =
						DataAccessRegistry
							.getHelper(CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);
					return !helper.isGebruiksmiddelInGebruik(middel);
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
