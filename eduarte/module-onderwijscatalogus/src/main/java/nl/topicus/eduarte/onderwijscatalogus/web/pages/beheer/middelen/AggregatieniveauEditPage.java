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
import nl.topicus.eduarte.entities.onderwijsproduct.Aggregatieniveau;
import nl.topicus.eduarte.entities.onderwijsproduct.TypeToets;
import nl.topicus.eduarte.onderwijscatalogus.principals.beheer.Aggregatieniveaus;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Aggregatieniveau", menu = {
	"Beheer > Onderwijs > Aggregatieniveau's > Nieuw aggregatieniveau",
	"Beheer > Onderwijs > Aggregatieniveau's > [aggregatieniveau]"})
@InPrincipal(Aggregatieniveaus.class)
public class AggregatieniveauEditPage extends AbstractBeheerPage<Aggregatieniveau> implements
		IEditPage
{
	private Form<Void> form;

	public AggregatieniveauEditPage(Aggregatieniveau aggregatieniveau, SecurePage returnPage)
	{
		super(ModelFactory.getModel(aggregatieniveau, new DefaultModelManager(
			Aggregatieniveau.class)), BeheerMenuItem.Aggregatieniveaus);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<Aggregatieniveau> fieldset =
			new AutoFieldSet<Aggregatieniveau>("aggregatieniveau", getContextModel(),
				"Aggregatieniveau");
		fieldset.setPropertyNames("code", "naam", "niveau", "actief");
		fieldset.setRenderMode(RenderMode.EDIT);
		fieldset.setSortAccordingToPropertyNames(true);
		fieldset.addModifier("code", new UniqueConstraintValidator<String>(fieldset,
			"Aggregatieniveau", "code", "organisatie"));
		fieldset.addModifier("niveau", new UniqueConstraintValidator<Integer>(fieldset,
			"Aggregatieniveau", "niveau", "organisatie"));
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
				Aggregatieniveau niveau = getContextModelObject();
				niveau.saveOrUpdate();
				niveau.commit();
				setResponsePage(AggregatieniveauZoekenPage.class);
			}
		});
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return AggregatieniveauEditPage.this.getReturnPage();
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return AggregatieniveauEditPage.this.getReturnPageClass();
			}

		}));
		panel.addButton(new VerwijderButton(panel, "Verwijder")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				Aggregatieniveau niveau = getContextModelObject();
				niveau.delete();
				niveau.commit();
				setResponsePage(AggregatieniveauZoekenPage.class);
			}

			@SuppressWarnings("unchecked")
			@Override
			public boolean isVisible()
			{
				Aggregatieniveau niveau = getContextModelObject();
				if (niveau.isSaved())
				{
					CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper<TypeToets, ? > helper =
						DataAccessRegistry
							.getHelper(CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);
					return !helper.isAggregatieniveauInGebruik(niveau);
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
