package nl.topicus.eduarte.web.components.modalwindow;

import java.util.Date;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public abstract class AbstractZoekenModalWindow<T> extends CobraModalWindow<T>
{
	private static final long serialVersionUID = 1L;

	public AbstractZoekenModalWindow(String id, IModel<T> model, AbstractZoekFilter<T> filter)
	{
		super(id, model);
		filter.setCustomPeildatumModel(new Model<Date>(TimeUtil.getInstance().currentDate()));
		setInitialHeight(500);
		setInitialWidth(970);
		setCookieName("AbstractZoekenModalWindow");
	}

	@Override
	public MarkupContainer setDefaultModel(IModel< ? > model)
	{
		throw new UnsupportedOperationException(
			"Gebruik niet setDefaultModel op een ModalWindow, dat gaat nooit goed!");
	}
}
