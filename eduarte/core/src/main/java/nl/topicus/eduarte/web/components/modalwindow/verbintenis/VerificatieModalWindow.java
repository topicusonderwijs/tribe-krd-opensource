package nl.topicus.eduarte.web.components.modalwindow.verbintenis;

import java.util.List;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;

import org.apache.wicket.markup.html.WebMarkupContainer;

public class VerificatieModalWindow<T> extends CobraModalWindow<T>
{
	private static final long serialVersionUID = 1L;

	private AutoFieldSet<T> fieldset;

	private List<WebMarkupContainer> refreshComponents;

	public VerificatieModalWindow(String id, String title, AutoFieldSet<T> fieldset,
			List<WebMarkupContainer> refreshComponents)
	{
		super(id);
		this.fieldset = fieldset;
		this.refreshComponents = refreshComponents;
		fieldset.setRenderMode(RenderMode.EDIT);
		setInitialWidth(420);
		setInitialHeight(320);
		setTitle(title);
	}

	@Override
	protected CobraModalWindowBasePanel<T> createContents(String id)
	{
		return new InschrijfverzoekVerificatieEditPanel<T>(id, this, fieldset, refreshComponents);
	}

}