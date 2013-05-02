package nl.topicus.eduarte.web.pages.beheer.organisatie;

import java.util.Date;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.validators.UniqueConstraintFormValidator;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.core.principals.beheer.organisatie.OrganisatiemodelPrincipal;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.LocatieAdres;
import nl.topicus.eduarte.entities.organisatie.LocatieContactgegeven;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.adres.AdressenEditPanel;
import nl.topicus.eduarte.web.components.panels.ContactgegevenEntiteitEditPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ProbeerTeVerwijderenButton;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Locatie", menu = "Beheer > Locatie > [Locatie]")
@InPrincipal(OrganisatiemodelPrincipal.class)
public class LocatieEditPage extends AbstractBeheerPage<Locatie> implements
		IModuleEditPage<Locatie>
{
	private Form<Void> form;

	private LocatieModel locatieModel;

	/**
	 * Constructor voor de ToevoegenButton.
	 * 
	 * @param returnPage
	 */
	public LocatieEditPage(SecurePage returnPage)
	{
		this(ModelFactory.getModel(new Locatie(new Date())), returnPage);
	}

	/**
	 * Constructor voor de BewerkenButton.
	 * 
	 * @param locatieModel
	 * @param returnPage
	 */
	public LocatieEditPage(IModel<Locatie> locatieModel, SecurePage returnPage)
	{
		this(new LocatieModel(locatieModel.getObject()), returnPage);
	}

	private LocatieEditPage(LocatieModel locatie, SecurePage returnPage)
	{
		super(locatie.getEntiteitModel(), BeheerMenuItem.Locaties);
		locatieModel = locatie;
		setReturnPage(returnPage);

		form = new Form<Void>("form");
		add(form);

		addAlgemeenFieldSet();
		addFieldsetAdres();
		addFieldsetContactGegevens();
		createComponents();
	}

	private void addAlgemeenFieldSet()
	{
		final AutoFieldSet<Locatie> algemeenFieldSet =
			new AutoFieldSet<Locatie>("locatie", getContextModel(), "Locatie");
		algemeenFieldSet.setPropertyNames("brincode", "afkorting", "naam", "begindatum",
			"einddatum");

		algemeenFieldSet.setRenderMode(RenderMode.EDIT);
		algemeenFieldSet.setSortAccordingToPropertyNames(true);
		form.add(algemeenFieldSet);

		form.add(new UniqueConstraintFormValidator(algemeenFieldSet, "Locatie", "afkorting"));
	}

	private void addFieldsetAdres()
	{
		form.add(new AdressenEditPanel<LocatieAdres, Locatie>("inputFieldsAdres", locatieModel
			.getEntiteitModel(), locatieModel.getEntiteitManager()));
	}

	private void addFieldsetContactGegevens()
	{
		form.add(new ContactgegevenEntiteitEditPanel<LocatieContactgegeven, Locatie>(
			"inputFieldsContactgegevens", locatieModel.getEntiteitModel()));
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
				LocatieEditPage.this.locatieModel.save();
				LocatieEditPage.this.getReturnPage().modelChanged();
				EduArteRequestCycle.get().setResponsePage(LocatieEditPage.this.getReturnPage());
			}
		});

		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				LocatieEditPage.this.locatieModel.save();
				LocatieEditPage.this.getReturnPage().modelChanged();
				LocatieEditPage page = new LocatieEditPage(new LocatieZoekenPage());
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
				ActionKey action = CobraKeyAction.VOLGENDE;
				return action;
			}
		});
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return LocatieEditPage.this.getReturnPage();
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return LocatieEditPage.this.getReturnPageClass();
			}

		}));
		panel.addButton(new ProbeerTeVerwijderenButton(panel, locatieModel.getEntiteitModel(),
			"deze locatie", LocatieZoekenPage.class));
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		ComponentUtil.detachQuietly(locatieModel);
	}
}
