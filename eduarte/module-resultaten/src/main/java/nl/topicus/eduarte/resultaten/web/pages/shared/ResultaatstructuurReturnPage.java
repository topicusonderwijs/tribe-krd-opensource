package nl.topicus.eduarte.resultaten.web.pages.shared;

import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.model.IDetachable;

public interface ResultaatstructuurReturnPage extends IDetachable
{
	public SecurePage getReturnPage();

	public SecurePage getReturnPageAfterDelete();
}
