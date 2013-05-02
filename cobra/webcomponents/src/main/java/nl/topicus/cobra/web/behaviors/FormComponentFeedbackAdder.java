package nl.topicus.cobra.web.behaviors;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.application.IComponentOnBeforeRenderListener;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.model.AbstractReadOnlyModel;

public class FormComponentFeedbackAdder implements IComponentOnBeforeRenderListener
{
	private static final class ClassModel extends AbstractReadOnlyModel<String>
	{
		private final FormComponent< ? > component;

		private static final long serialVersionUID = 1L;

		private ClassModel(FormComponent< ? > component)
		{
			this.component = component;
		}

		@Override
		public String getObject()
		{
			List<String> classnames = new ArrayList<String>();

			addRequiredness(classnames);
			addValidness(classnames);

			return constructClassAttribute(classnames);
		}

		private void addValidness(List<String> classnames)
		{
			if (!component.isValid())
			{
				classnames.add("error");
			}
		}

		private void addRequiredness(List<String> classnames)
		{
			if (component.isRequired() && component.isEnabled()
				&& !(component instanceof RadioGroup< ? >)
				&& !(component instanceof CheckGroup< ? >))
			{
				classnames.add("required");
			}
		}

		private String constructClassAttribute(List<String> classnames)
		{
			StringBuilder sb = new StringBuilder("");
			String spacer = "";
			for (String classname : classnames)
			{
				sb.append(spacer);
				sb.append(classname);
				spacer = " ";
			}
			return sb.toString();
		}
	}

	private static final class TitleModel extends AbstractReadOnlyModel<String>
	{
		private final FormComponent< ? > component;

		private static final long serialVersionUID = 1L;

		private TitleModel(FormComponent< ? > component)
		{
			this.component = component;
		}

		@Override
		public String getObject()
		{
			if (component.hasFeedbackMessage())
			{
				return "Invoerfout - " + component.getFeedbackMessage().getMessage().toString();
			}
			return null;
		}
	}

	@Override
	public void onBeforeRender(Component component)
	{
		if (!component.hasBeenRendered() && component instanceof FormComponent< ? >)
			addFormcomponentAttributes((FormComponent< ? >) component);
	}

	private void addFormcomponentAttributes(FormComponent< ? > component)
	{
		AddAttributes add = component.getClass().getAnnotation(AddAttributes.class);
		if ((add != null && add.value()) || !(component instanceof FormComponentPanel< ? >))
		{
			component.add(new AttributeAppender("class", new ClassModel(component), " "));
			component.add(new AttributeAppender("title", new TitleModel(component), " "));
		}
	}
}
