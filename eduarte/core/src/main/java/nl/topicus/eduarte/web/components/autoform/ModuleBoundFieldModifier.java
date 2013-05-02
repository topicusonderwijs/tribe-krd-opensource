package nl.topicus.eduarte.web.components.autoform;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;
import nl.topicus.cobra.web.components.form.modifier.MultiFieldAdapter;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public class ModuleBoundFieldModifier extends MultiFieldAdapter
{
	private static final long serialVersionUID = 1L;

	private EduArteModuleKey module;

	public ModuleBoundFieldModifier(EduArteModuleKey module, String... propertyNames)
	{
		super(Action.VISIBILITY, propertyNames);
		this.module = module;
	}

	@Override
	public <T> IModel<Boolean> getVisibility(AutoFieldSet<T> fieldSet,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties)
	{
		return new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return isVisible();
			}
		};
	}

	protected boolean isVisible()
	{
		return EduArteApp.get().isModuleActive(module);
	}
}
