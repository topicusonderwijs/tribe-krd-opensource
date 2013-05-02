package nl.topicus.eduarte.web.components.panels.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.datapanel.CriteriaBean;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.wiquery.CollapsablePanel;
import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldEntiteit;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.panels.AbstractVrijVeldEntiteitPanel;
import nl.topicus.eduarte.web.components.panels.filter.uitgebreid.DeelnemerUitgebreidZoekFilterPanel;
import nl.topicus.eduarte.web.pages.deelnemer.DeelnemerUitgebreidZoekenPage;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class DeelnemerPeildatumZoekFilterPanel extends AutoZoekFilterPanel<VerbintenisZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public DeelnemerPeildatumZoekFilterPanel(String id, VerbintenisZoekFilter filter,
			final IPageable pageable, boolean peildatumVastzetten)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("peildatum", "peilEindDatum"));
		addFieldModifier(new EduArteAjaxRefreshModifier("peildatum", "peilEindDatum")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				getZoekfilter().setPeilEindDatum(getZoekfilter().getPeildatum());
			}
		});
		addFieldModifier(new EduArteAjaxRefreshModifier("peilEindDatum", "peildatum")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				Date newValue = getZoekfilter().getPeilEindDatum();
				if (newValue != null
					&& (getZoekfilter().getPeildatum() == null || newValue.before(getZoekfilter()
						.getPeildatum())))
				{
					getZoekfilter().setPeildatum(newValue);
				}
			}
		});
		addFieldModifier(new EnableModifier(!peildatumVastzetten, "*"));
	}

	@Override
	public List<CriteriaBean> getZoekCriteria()
	{
		DeelnemerUitgebreidZoekenPage page = new DeelnemerUitgebreidZoekenPage(getZoekfilter());
		DeelnemerUitgebreidZoekFilterPanel zoekfilterPanel = page.getFilterPanel();
		final List<CriteriaBean> zoekCriteria = new ArrayList<CriteriaBean>();
		// Probeer de zoekcriteria te bepalen.
		zoekfilterPanel.visitChildren(CollapsablePanel.class, new IVisitor<CollapsablePanel< ? >>()
		{
			@Override
			public Object component(CollapsablePanel< ? > panelComponent)
			{
				final CollapsablePanel< ? > panel = panelComponent;
				panel.visitChildren(FormComponent.class, new IVisitor<FormComponent< ? >>()
				{
					@Override
					public Object component(FormComponent< ? > inputComponent)
					{
						if (inputComponent instanceof AbstractVrijVeldEntiteitPanel< ? >)
						{
							VrijVeldable< ? > vrijveldable =
								(VrijVeldable< ? >) inputComponent.getDefaultModelObject();
							for (VrijVeldEntiteit curEntiteit : vrijveldable.getVrijVelden())
							{
								if (curEntiteit.isIngevuld())
								{
									String expression = curEntiteit.getVrijVeld().getNaam();
									String value = curEntiteit.getOmschrijving();
									if (expression != null && value != null)
									{
										CriteriaBean criteriaBean = new CriteriaBean();
										criteriaBean.setValue(value);
										criteriaBean.setExpression(expression);
										zoekCriteria.add(criteriaBean);
									}
								}
							}
							return CONTINUE_TRAVERSAL_BUT_DONT_GO_DEEPER;
						}
						// else if (inputComponent instanceof
						// UitgebreidZoekMultipleChoice)
						// {
						// UitgebreidZoekMultipleChoice uitgebreidZoekMC =
						// (UitgebreidZoekMultipleChoice) inputComponent;
						// if (uitgebreidZoekMC.isActive())
						// {
						// panel.open();
						// return STOP_TRAVERSAL;
						// }
						// return CONTINUE_TRAVERSAL_BUT_DONT_GO_DEEPER;
						//
						// }
						else if (inputComponent.getDefaultModelObject() != null)
						{
							String expression = null;
							String value = null;
							Property< ? extends VerbintenisZoekFilter, ? , ? > prop =
								ReflectionUtil.findProperty(getZoekfilter().getClass(),
									inputComponent.getId());
							if (prop != null)
							{
								AutoForm af = prop.getAnnotation(AutoForm.class);
								if (af == null)
								{
									value = StringUtil.convertCamelCase(prop.getName());
								}
								else
								{
									value = af.label();
									if (AutoForm.DEFAULT.equals(value))
									{
										value = StringUtil.convertCamelCase(prop.getName());
									}
								}
							}
							Object object = inputComponent.getDefaultModelObject();
							if (object != null)
							{
								if (object instanceof Date)
									expression = TimeUtil.getInstance().formatDate((Date) object);
								else if (object instanceof Boolean)
								{
									Boolean bool = (Boolean) object;
									if (bool)
										expression = "Ja";
									else
										expression = "Nee";
								}
								else if (object instanceof Collection< ? >)
								{
									if (!((Collection< ? >) object).isEmpty())
									{
										expression =
											StringUtil.toString((Collection< ? >) object, ",", "");
									}
								}
								else
								{
									expression = object.toString();
								}
							}
							if (expression != null && value != null)
							{
								CriteriaBean criteriaBean = new CriteriaBean();
								criteriaBean.setValue(value);
								criteriaBean.setExpression(expression);
								zoekCriteria.add(criteriaBean);
							}
						}
						return CONTINUE_TRAVERSAL;
					}
				});
				return CONTINUE_TRAVERSAL_BUT_DONT_GO_DEEPER;
			}
		});
		return zoekCriteria;
	}

}
