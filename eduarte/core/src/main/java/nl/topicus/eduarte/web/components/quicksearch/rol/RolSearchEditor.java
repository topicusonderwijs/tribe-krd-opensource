package nl.topicus.eduarte.web.components.quicksearch.rol;

import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.rol.RolSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.RolZoekFilter;

import org.apache.wicket.model.IModel;

public class RolSearchEditor extends AbstractSearchEditor<Rol>
{
	private static final long serialVersionUID = 1L;

	private static final RolZoekFilter getDefaultFilter()
	{
		RolZoekFilter filter = new RolZoekFilter();
		filter.addOrderByProperty("naam");
		return filter;
	}

	private RolZoekFilter filter;

	public RolSearchEditor(String id)
	{
		this(id, null, getDefaultFilter());
	}

	public RolSearchEditor(String id, IModel<Rol> model)
	{
		this(id, model, getDefaultFilter());
	}

	public RolSearchEditor(String id, IModel<Rol> model, RolZoekFilter filter)
	{
		super(id, model);
		this.filter = filter;
	}

	@Override
	public AbstractZoekenModalWindow<Rol> createModelWindow(String id, IModel<Rol> model)
	{
		return new RolSelectieModalWindow(id, model, filter);
	}

	@Override
	public QuickSearchField<Rol> createSearchField(String id, IModel<Rol> model)
	{
		return new RolQuickSearchField(id, model, new ZoekFilterCopyManager().copyObject(filter));
	}
}
