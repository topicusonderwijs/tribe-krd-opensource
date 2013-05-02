package nl.topicus.cobra.web.components.text;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

public class ReadonlyTextField<T> extends TextField<T>
{
	private static final long serialVersionUID = 1L;

	private boolean readonly = true;

	public ReadonlyTextField(String id)
	{
		super(id);
	}

	public ReadonlyTextField(String id, Class<T> type)
	{
		super(id, type);
	}

	public ReadonlyTextField(String id, IModel<T> object)
	{
		super(id, object);
	}

	public ReadonlyTextField(String id, IModel<T> model, Class<T> type)
	{
		super(id, model, type);
	}

	@Override
	protected void onDisabled(ComponentTag tag)
	{
		if (isReadonly())
			tag.put("readonly", "readonly");
		else
			super.onDisabled(tag);
	}

	@Override
	public boolean isEnabled()
	{
		return super.isEnabled() && !isReadonly();
	}

	public boolean isReadonly()
	{
		return readonly;
	}

	public void setReadonly(boolean readonly)
	{
		this.readonly = readonly;
	}
}
