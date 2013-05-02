package nl.topicus.eduarte.web.pages.shared;

import java.util.List;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectieUpdateListener;
import nl.topicus.cobra.web.components.datapanel.selection.bottomrow.AbstractSelecterenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.app.security.actions.Begeleider;
import nl.topicus.eduarte.app.security.actions.Docent;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.app.security.actions.Uitvoerend;
import nl.topicus.eduarte.app.security.actions.Verantwoordelijk;
import nl.topicus.eduarte.web.pages.AbstractDynamicContextPage;
import nl.topicus.eduarte.web.pages.PageContext;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.SubpageContext;
import nl.topicus.eduarte.zoekfilters.IMentorDocentZoekFilter;
import nl.topicus.eduarte.zoekfilters.IOrganisatieEenheidLocatieZoekFilter;
import nl.topicus.eduarte.zoekfilters.IVerantwoordelijkeUitvoerendeZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;

@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class, Verantwoordelijk.class,
	Uitvoerend.class, Begeleider.class, Docent.class})
public abstract class AbstractSelectiePage<R, S extends IdObject, Z extends DetachableZoekFilter<S>>
		extends AbstractDynamicContextPage<Void>
{
	private Form<Void> form;

	private SecurePage returnPage;

	private Class< ? extends Page> returnPageClass;

	private SelectieTarget<R, S> target;

	private AbstractSelectiePanel<R, S, Z> selectiePanel;

	private Z filter;

	private Selection<R, S> selection;

	public AbstractSelectiePage(SecurePage returnPage, Z filter, Selection<R, S> selection,
			SelectieTarget<R, S> target)
	{
		super(new SubpageContext(returnPage));
		this.returnPage = returnPage;
		this.target = target;
		this.filter = filter;
		this.selection = selection;
		createForm();
	}

	public AbstractSelectiePage(Class< ? extends Page> returnPage, PageContext context, Z filter,
			Selection<R, S> selection, SelectieTarget<R, S> target)
	{
		super(context);
		this.returnPageClass = returnPage;
		this.target = target;
		this.filter = filter;
		this.selection = selection;
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		if (!target.getSecurityCheck().isActionAuthorized(getAction(Instelling.class)))
		{
			if (target.getSecurityCheck().isActionAuthorized(getAction(OrganisatieEenheid.class)))
			{
				if (!(filter instanceof IOrganisatieEenheidLocatieZoekFilter< ? >))
				{
					throw new IllegalArgumentException(
						"Kan geen authorisatie instellen op organisatie-eenheid-locatieniveau, "
							+ "zorg dat " + filter.getClass().getSimpleName()
							+ " IOrganisatieEenheidLocatieZoekFilter implementeert");
				}
			}
			else
			{
				boolean mentor =
					target.getSecurityCheck().isActionAuthorized(getAction(Begeleider.class));
				boolean docent =
					target.getSecurityCheck().isActionAuthorized(getAction(Docent.class));
				if ((mentor || docent) && !(filter instanceof IMentorDocentZoekFilter< ? >))
				{
					throw new IllegalArgumentException(
						"Kan geen authorisatie instellen op mentor- en/of docentniveau, "
							+ "zorg dat " + filter.getClass().getSimpleName()
							+ " IMentorDocentZoekFilter implementeert");
				}
				if (mentor && !docent)
					((IMentorDocentZoekFilter<S>) filter).setMentor(getIngelogdeMedewerker());
				if (!mentor && docent)
					((IMentorDocentZoekFilter<S>) filter).setDocent(getIngelogdeMedewerker());
				if (mentor && docent)
					((IMentorDocentZoekFilter<S>) filter)
						.setMentorOfDocent(getIngelogdeMedewerker());

				boolean verantwoordelijke =
					target.getSecurityCheck().isActionAuthorized(getAction(Verantwoordelijk.class));
				boolean uitvoerende =
					target.getSecurityCheck().isActionAuthorized(getAction(Uitvoerend.class));
				if ((verantwoordelijke || uitvoerende)
					&& !(filter instanceof IVerantwoordelijkeUitvoerendeZoekFilter< ? >))
				{
					throw new IllegalArgumentException(
						"Kan geen authorisatie instellen op verantwoordelijke- en/of uitvoerendeniveau, "
							+ "zorg dat " + filter.getClass().getSimpleName()
							+ " IVerantwoordelijkeUitvoerendeZoekFilter implementeert");
				}
				if (verantwoordelijke && !uitvoerende)
					((IVerantwoordelijkeUitvoerendeZoekFilter<S>) filter)
						.setVerantwoordelijke(getIngelogdeMedewerker());
				if (!verantwoordelijke && uitvoerende)
					((IVerantwoordelijkeUitvoerendeZoekFilter<S>) filter)
						.setUitvoerende(getIngelogdeMedewerker());
				if (verantwoordelijke && uitvoerende)
					((IVerantwoordelijkeUitvoerendeZoekFilter<S>) filter)
						.setVerantwoordelijkeOfUitvoerende(getIngelogdeMedewerker());
			}
		}
		if (filter instanceof IOrganisatieEenheidLocatieZoekFilter< ? >)
			((IOrganisatieEenheidLocatieZoekFilter<S>) filter)
				.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(target
					.getSecurityCheck()));
	}

	@Override
	protected void onBeforeRender()
	{
		if (!hasBeenRendered())
		{
			selectiePanel = createSelectiePanel("selectiePanel", filter, selection);
			selectiePanel.setMaxResults(getMaxResults());
			form.add(selectiePanel);
			selectiePanel.addSelectieUpdateListener(new ISelectieUpdateListener()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onSelectieUpdate(AjaxRequestTarget ajaxTarget)
				{
					ajaxTarget.addComponent(getBottomRow());
				}
			});
			createComponents();
		}
		super.onBeforeRender();
	}

	@SuppressWarnings("hiding")
	protected abstract AbstractSelectiePanel<R, S, Z> createSelectiePanel(String id, Z filter,
			Selection<R, S> selection);

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AbstractBottomRowButton(panel, target.getLinkLabel(),
			CobraKeyAction.OPSLAAN, ButtonAlignment.RIGHT)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected WebMarkupContainer getLink(String linkId)
			{
				return target.createLink(linkId, selectiePanel);
			}

			@Override
			public boolean isEnabled()
			{
				int size = getSelection().size();
				return (allowEmptySelection() || size > 0) && size <= getMaxResults();
			}
		});

		AbstractBottomRowButton terugButton = createTerugButton(panel);
		if (terugButton != null)
			panel.addButton(terugButton);

		AbstractSelecterenButton.addSelectieButtons(panel, selectiePanel);
	}

	protected AbstractBottomRowButton createTerugButton(BottomRowPanel panel)
	{
		if (returnPage != null)
			return new TerugButton(panel, returnPage);
		else if (returnPageClass != null)
			return new TerugButton(panel, returnPageClass);
		else
			return null;
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachFields(this);
	}

	public Z getFilter()
	{
		return selectiePanel.getFilter();
	}

	public void setFilter(Z filter)
	{
		selectiePanel.setFilter(filter);
	}

	public Selection<R, S> getSelection()
	{
		return selectiePanel.getSelection();
	}

	public List<R> getSelectedElements()
	{
		return selectiePanel.getSelectedElements();
	}

	public CustomDataPanel<S> getDataPanel()
	{
		return selectiePanel.getDataPanel();
	}

	public abstract int getMaxResults();

	public boolean allowEmptySelection()
	{
		return false;
	}

	public AbstractSelectiePanel<R, S, Z> getSelectiePanel()
	{
		return selectiePanel;
	}

}
