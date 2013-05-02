package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BewerkenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.entities.criteriumbank.Criterium;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.krd.principals.onderwijs.OpleidingWrite;
import nl.topicus.eduarte.providers.CriteriumProvider;
import nl.topicus.eduarte.web.behavior.NullableInstellingEntiteitEditLinkVisibleBehavior;
import nl.topicus.eduarte.web.components.menu.OpleidingMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.AbstractOpleidingPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;

/**
 * Detailpagina van een criterium. Dit kan zowel een landelijke als een
 * opleidingsspecifiek criterium zijn.
 * 
 * @author loite
 */
@PageInfo(title = "Criterium", menu = {})
@InPrincipal(OpleidingWrite.class)
public class CriteriumPage extends AbstractOpleidingPage implements CriteriumProvider
{
	private static final long serialVersionUID = 1L;

	private final SecurePage returnToPage;

	public CriteriumPage(Opleiding opleiding, Criterium criterium)
	{
		this(opleiding, criterium, null);
	}

	public CriteriumPage(Opleiding opleiding, Criterium criterium, SecurePage returnToPage)
	{
		super(OpleidingMenuItem.Criteria, opleiding);
		this.returnToPage = returnToPage;
		setDefaultModel(ModelFactory.getCompoundModel(criterium));
		add(ComponentFactory.getDataLabel("volgnummer"));
		add(ComponentFactory.getDataLabel("naam"));
		add(ComponentFactory.getDataLabel("melding"));
		add(ComponentFactory.getDataLabel("cohort.naam"));
		add(ComponentFactory.getDataLabel("formule"));
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.setDefaultModel(getContextOpleidingModel());
		BewerkenButton<Void> bewerken = new BewerkenButton<Void>(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new EditCriteriumPage(getCriterium(), CriteriumPage.this);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return EditCriteriumPage.class;
			}

		});
		// als het criterium niet hoort bij deze opleiding(svariant) maar bij de parent,
		// dan kan het criterium niet worden bewerkt
		bewerken.setVisible(getContextOpleiding().equals(getCriterium().getOpleiding()));
		bewerken.add(new NullableInstellingEntiteitEditLinkVisibleBehavior(getCriteriumModel()));
		panel.addButton(bewerken);
		if (returnToPage != null)
			panel.addButton(new TerugButton(panel, returnToPage));
		panel.addButton(new CriteriumTestenButton(panel));
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(Criterium.class);
		ctorArgValues.add(getDefaultModel());
	}

	public Criterium getCriterium()
	{
		return (Criterium) getDefaultModelObject();
	}

	@SuppressWarnings("unchecked")
	public IModel<Criterium> getCriteriumModel()
	{
		return (IModel<Criterium>) getDefaultModel();
	}
}
