package nl.topicus.eduarte.web.components.text;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.eduarte.web.components.modalwindow.EnumSelectModalWindow;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.util.string.AppendingStringBuffer;

public class EnumSelectField<T extends Enum<T>> extends FormComponentPanel<T>
{
	private static final long serialVersionUID = 1L;

	private EnumCombobox<T> enumCombobox;

	private EnumSelectModalWindow<T> modalWindow;

	public EnumSelectField(String id, IObjectClassAwareModel<T> model)
	{
		super(id);

		addEnumComboBox(model);
		addModalWindow(model);
		addSelectLink();
	}

	private void addEnumComboBox(final IObjectClassAwareModel<T> model)
	{
		enumCombobox = new EnumCombobox<T>("combobox", model)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected CharSequence getDefaultChoice(Object selected)
			{
				String option =
					StringUtil.convertCamelCase(getEnumClass(model).getSimpleName()) + "..";

				AppendingStringBuffer buffer = new AppendingStringBuffer(32 + option.length());

				buffer.append("\n<option");

				if (selected == null)
				{
					buffer.append(" selected=\"selected\"");
				}

				buffer.append(" value=\"\">").append(option).append("</option>");
				return buffer;
			}
		};
		enumCombobox.setNullValid(true);
		add(enumCombobox);
	}

	private void addModalWindow(IObjectClassAwareModel<T> model)
	{
		modalWindow = new EnumSelectModalWindow<T>("modalWindow", model)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target, Item<T> item)
			{
				enumCombobox.setDefaultModelObject(item.getModelObject());
				target.addComponent(enumCombobox);
				modalWindow.close(target);
			}
		};
		add(modalWindow);
	}

	private void addSelectLink()
	{
		AjaxLink<Void> link = new AjaxLink<Void>("link")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				modalWindow.show(target);
			}
		};
		link.add(new AttributeModifier("src", new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				return getRequest().getRelativePathPrefixToContextRoot()
					+ "assets/img/icons/select.png";
			}
		}));
		add(link);
	}

	@SuppressWarnings("unchecked")
	private Class<T> getEnumClass(IObjectClassAwareModel model)
	{
		return model.getObjectClass();
	}

	@Override
	protected void convertInput()
	{
		setConvertedInput(enumCombobox.getConvertedInput());
	}

	@Override
	public void updateModel()
	{
		enumCombobox.updateModel();
	}
}
