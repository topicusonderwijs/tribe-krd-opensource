package nl.topicus.eduarte.resultaten.web.components.structuur;

import java.util.List;

import nl.topicus.cobra.modelsv2.HibernateModel;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.ToetsVerwijzing;
import nl.topicus.eduarte.web.components.panels.filter.ResultaatstructuurZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.model.IModel;

public class ToetsKoppelenPanel extends TypedPanel<Toets>
{
	private static final long serialVersionUID = 1L;

	private IModel<Resultaatstructuur> selectedStructuur =
		new HibernateModel<Resultaatstructuur>(null);

	private ResultaatstructuurBoomPanel boomPanel;

	public ToetsKoppelenPanel(String id, IModel<Toets> model)
	{
		super(id, model);

		boomPanel = new ResultaatstructuurBoomPanel("boom", selectedStructuur)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && getModelObject() != null;
			}

			@Override
			protected List<Toets> getChildren(Toets toets)
			{
				if (isGekoppeldMetToets(toets))
					return ToetsKoppelenPanel.this.getModelObject().getChildren();
				return super.getChildren(toets);
			}

			@Override
			protected void addExtraClasses(StringBuilder sb, Toets toets)
			{
				if (isGekoppeldMetToets(toets))
					sb.append(" gekoppeld");
				if (toets.getResultaatstructuur().equals(
					ToetsKoppelenPanel.this.getModelObject().getResultaatstructuur()))
					sb.append(" added");
				if (isClickable(toets))
					sb.append(" hand");
				super.addExtraClasses(sb, toets);
			}

			@Override
			@SuppressWarnings("hiding")
			protected ToetsBoomPanel createToetsPanel(String id, final IModel<Toets> model)
			{
				ToetsBoomPanel ret = new ToetsBoomPanel(id, model)
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected boolean isSelectable()
					{
						return isGekoppeldMetToets(model.getObject())
							|| isClickable(model.getObject());
					}

					@Override
					protected boolean isSelected()
					{
						return isGekoppeldMetToets(model.getObject());
					}
				};
				if (isClickable(model.getObject()))
				{
					ret.add(new AjaxEventBehavior("onclick")
					{
						private static final long serialVersionUID = 1L;

						@Override
						protected void onEvent(AjaxRequestTarget target)
						{
							koppelAanToets(target, model.getObject());
						}
					});
				}
				return ret;
			}

			private boolean isClickable(Toets toets)
			{
				return toets.isVerwijsbaar()
					&& toets.getResultaatstructuur().equals(selectedStructuur.getObject())
					&& !isGekoppeldMetToets(toets);
			}

			private boolean isGekoppeldMetToets(Toets toets)
			{
				Toets thisToets = ToetsKoppelenPanel.this.getModelObject();
				for (ToetsVerwijzing curVerwijzing : thisToets.getUitgaandeVerwijzingen())
					if (curVerwijzing.getSchrijvenIn().equals(toets))
						return true;
				return false;
			}
		};
		boomPanel.setOutputMarkupPlaceholderTag(true);
		add(boomPanel);

		ResultaatstructuurZoekFilter filter = new ResultaatstructuurZoekFilter();
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		filter.setCohortModel(EduArteSession.get().getSelectedCohortModel());
		filter.setVerwijsbareToets(true);

		selectedStructuur.setObject(getStartStructuur(filter));

		StructuurListPanel structuurPanel =
			new StructuurListPanel("structuren", selectedStructuur, filter)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onClick(AjaxRequestTarget target, Resultaatstructuur structuur)
				{
					target.addComponent(boomPanel);
				}
			};
		add(structuurPanel);

		add(new ResultaatstructuurZoekFilterPanel("filter", filter, structuurPanel, true));
		setOutputMarkupPlaceholderTag(true);

		add(CSSPackageResource.getHeaderContribution(ToetsKoppelenPanel.class,
			"resources/structuurboom.css"));
	}

	protected void koppelAanToets(AjaxRequestTarget target, Toets toets)
	{
		for (ToetsVerwijzing curVerwijzing : getModelObject().getUitgaandeVerwijzingen())
			curVerwijzing.getSchrijvenIn().getInkomendeVerwijzingen().remove(curVerwijzing);
		getModelObject().getUitgaandeVerwijzingen().clear();

		ToetsVerwijzing nieuweVerwijzing = new ToetsVerwijzing();
		nieuweVerwijzing.setLezenUit(getModelObject());
		nieuweVerwijzing.setSchrijvenIn(toets);
		getModelObject().getUitgaandeVerwijzingen().add(nieuweVerwijzing);

		boomPanel.detach();
		target.addComponent(boomPanel);
	}

	private Resultaatstructuur getStartStructuur(ResultaatstructuurZoekFilter filter)
	{
		if (getModelObject().getUitgaandeVerwijzingen().size() != 1)
		{
			filter.setCohort(getModelObject().getResultaatstructuur().getCohort());
			filter.setOnderwijsproduct(getModelObject().getResultaatstructuur()
				.getOnderwijsproduct());
			filter.setType(Resultaatstructuur.Type.SUMMATIEF);
			return null;
		}
		Toets targetToets = getModelObject().getUitgaandeVerwijzingen().get(0).getSchrijvenIn();
		Resultaatstructuur ret = targetToets.getResultaatstructuur();
		filter.setCohort(ret.getCohort());
		filter.setOnderwijsproduct(ret.getOnderwijsproduct());
		filter.setType(ret.getType());
		filter.setCategorie(ret.getCategorie());
		return ret;
	}

	@Override
	public boolean isVisible()
	{
		Toets toets = getModelObject();
		return super.isVisible() && toets.getUitgaandeVerwijzingen().size() < 2;
	}
}
