package nl.topicus.eduarte.web.components.panels.filter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.datapanel.CriteriaBean;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.FieldModifier;
import nl.topicus.cobra.web.pages.FeedbackComponent;
import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.filter.renderer.ZoekFilterMarkupRenderer;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;

public class AutoZoekFilterPanel<T extends DetachableZoekFilter< ? >> extends Panel
{
	private static final long serialVersionUID = 1L;

	private T zoekfilter;

	private Form<Void> form;

	private AutoFieldSet<T> fieldset;

	public AutoZoekFilterPanel(String id, T zoekfilter, final IPageable pageable)
	{
		super(id);
		this.zoekfilter = zoekfilter;
		setOutputMarkupId(true);

		add(form = new Form<Void>("form"));
		form.add(fieldset =
			new AutoFieldSet<T>("inputfields", new PropertyModel<T>(this, "zoekfilter")));

		fieldset.setMarkupRendererName(ZoekFilterMarkupRenderer.NAME);
		fieldset.setRenderMode(RenderMode.EDIT);

		AjaxSubmitLink submit = new AjaxSubmitLink("submit", form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form< ? > submitForm)
			{
				onZoek(pageable, target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form< ? > submitForm)
			{
				FeedbackComponent feedbackComponent = findParent(FeedbackComponent.class);
				if (feedbackComponent != null)
				{
					feedbackComponent.refreshFeedback(target);
				}
				target.addComponent(AutoZoekFilterPanel.this);
			}
		};
		submit.add(new AttributeModifier("src", new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				return getRequest().getRelativePathPrefixToContextRoot()
					+ "assets/img/icons/searchicon.gif";
			}
		}));
		form.setDefaultButton(submit);
		form.add(submit);

		if (pageable instanceof EduArteDataPanel< ? >)
		{
			EduArteDataPanel< ? > datapanel = (EduArteDataPanel< ? >) pageable;
			datapanel.setZoekFilterPanel(this);
		}
	}

	public void setPropertyNames(List<String> propertyNames)
	{
		fieldset.setPropertyNames(propertyNames);
		fieldset.setSortAccordingToPropertyNames(true);
	}

	public FormComponent< ? > getComponentProperty(String labelName)
	{
		return (FormComponent< ? >) fieldset.findFieldComponent(labelName);
	}

	public void addFieldModifier(FieldModifier modifier)
	{
		fieldset.addFieldModifier(modifier);
	}

	public T getZoekfilter()
	{
		return zoekfilter;
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		zoekfilter.detach();
	}

	protected void onZoek(final IPageable pageable, AjaxRequestTarget target)
	{
		if (pageable != null)
		{
			pageable.setCurrentPage(0);
			target.addComponent((Component) pageable);
		}
		// aanwijzing omtrent lege peildatum alleen tonen indien peildatum ingevoerd kan
		// worden
		if (getZoekfilter() instanceof AbstractZoekFilter< ? >
			&& ((AbstractZoekFilter< ? >) getZoekfilter()).getPeildatum() == null
			&& fieldset.getPropertyNames().contains("peildatum"))
		{
			info("Let op: geen datum ingevoerd. De peildatum is niet meegenomen bij de zoekopdracht.");
		}
		(findParent(FeedbackComponent.class)).refreshFeedback(target);
		target.addComponent(this);
	}

	public List<CriteriaBean> getZoekCriteria()
	{
		List<CriteriaBean> zoekCriteria = new ArrayList<CriteriaBean>();
		for (String propertyName : fieldset.getPropertyNames())
		{
			String value = null;
			String label = null;
			Object object = null;

			Property< ? , ? , ? > property =
				ReflectionUtil.findProperty(zoekfilter.getClass(), propertyName);
			if (property == null)
			{
				Component component = fieldset.findFieldComponent(propertyName);
				if (component != null)
				{
					try
					{
						object = component.getDefaultModelObject();
					}
					catch (Exception e)
					{
					}
				}
			}
			else
				object = property.getValueNull(zoekfilter);

			if (object != null)
			{
				if (object instanceof Date)
					value = TimeUtil.getInstance().formatDate((Date) object);
				else if (object instanceof Boolean)
				{
					Boolean bool = (Boolean) object;
					if (bool)
						value = "Ja";
					else
						value = "Nee";
				}
				else
					value = object.toString();
			}

			FieldProperties< ? , ? , ? > fieldProperties =
				fieldset.getFieldProperties(propertyName);
			if (fieldProperties != null)
				label = fieldProperties.getLabel();

			if (value != null && label != null)
			{
				CriteriaBean criteriaBean = new CriteriaBean();
				criteriaBean.setValue(label);
				criteriaBean.setExpression(value);
				zoekCriteria.add(criteriaBean);
			}
		}
		return zoekCriteria;
	}

	protected Form<Void> getForm()
	{
		return form;
	}
}
