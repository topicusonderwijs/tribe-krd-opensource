package nl.topicus.eduarte.web.pages.beheer.organisatie;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.bottomrow.BewerkenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.core.principals.beheer.organisatie.OrganisatiemodelPrincipal;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.LocatieAdres;
import nl.topicus.eduarte.entities.organisatie.LocatieContactgegeven;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.adres.AdressenPanel;
import nl.topicus.eduarte.web.components.panels.ContactgegevenEntiteitPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Locatie", menu = "Beheer > Locatie > [Locatie]")
@InPrincipal(OrganisatiemodelPrincipal.class)
public class LocatieKaartPage extends AbstractBeheerPage<Locatie>
{

	public LocatieKaartPage(Locatie locatie)
	{
		this(ModelFactory.getModel(locatie), new LocatieZoekenPage());
	}

	public LocatieKaartPage(IModel<Locatie> locatie, SecurePage returnPage)
	{
		super(locatie, BeheerMenuItem.Locaties);
		setReturnPage(returnPage);

		addAlgemeenFieldSet();
		addFieldsetAdres();
		addFieldsetContactGegevens();
		createComponents();
	}

	private void addAlgemeenFieldSet()
	{
		AutoFieldSet<Locatie> algemeenFieldSet =
			new AutoFieldSet<Locatie>("locatie", getLocatieModel(), "Locatie");
		algemeenFieldSet.setPropertyNames("brincode", "afkorting", "naam", "begindatum",
			"einddatum");
		algemeenFieldSet.setRenderMode(RenderMode.DISPLAY);
		algemeenFieldSet.setSortAccordingToPropertyNames(true);
		add(algemeenFieldSet);
	}

	private void addFieldsetAdres()
	{
		add(new AdressenPanel<LocatieAdres>("inputFieldsAdres", getLocatieModel()));
	}

	@SuppressWarnings("unchecked")
	private IModel<Locatie> getLocatieModel()
	{
		return (IModel<Locatie>) getDefaultModel();
	}

	private void addFieldsetContactGegevens()
	{
		add(new ContactgegevenEntiteitPanel<LocatieContactgegeven>("inputFieldsContactgegevens",
			new LoadableDetachableModel<List<LocatieContactgegeven>>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected List<LocatieContactgegeven> load()
				{
					return getLocatieModel().getObject().getContactgegevens();
				}
			}, false));
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new BewerkenButton<Locatie>(panel, LocatieEditPage.class,
			getContextModel(), LocatieKaartPage.this));

		panel.addButton(new TerugButton(panel, LocatieKaartPage.this.getReturnPage()));
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}

	/**
	 * @see nl.topicus.eduarte.web.pages.SecurePage#getBookmarkConstructorArguments(java.util.List,
	 *      java.util.List)
	 */
	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(Locatie.class);
		ctorArgValues.add(getDefaultModel());
	}
}
