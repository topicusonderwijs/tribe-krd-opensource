package nl.topicus.cobra.web.components.datapanel.selection;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

public interface IUpdateListener<T> extends Serializable
{
	public void onUpdate(IModel<Boolean> checkboxModel, IModel<T> rowModel, AjaxRequestTarget target);
}
