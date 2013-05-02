package nl.topicus.cobra.web.components.datapanel.selection;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

public interface ISelectieUpdateListener extends Serializable
{
	public void onSelectieUpdate(AjaxRequestTarget target);
}
