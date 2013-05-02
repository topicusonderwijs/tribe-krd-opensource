package nl.topicus.eduarte.web.components.modalwindow.maatregeltoekenning;

import java.util.List;

import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.participatie.MaatregelToekenning;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.model.IModel;

public class MaatregelToekenningEditModalWindow extends CobraModalWindow<List<MaatregelToekenning>>
{
	private static final long serialVersionUID = 1L;

	public MaatregelToekenningEditModalWindow(String id,
			IModel<List<MaatregelToekenning>> maatregelToekenningModel)
	{
		super(id, maatregelToekenningModel);
		setInitialHeight(200);
		setInitialWidth(900);
		setTitle("Maatregelen");

		setWindowClosedCallback(new WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				MaatregelToekenningEditModalWindow.this.onWindowClose(target);
			}
		});
	}

	@Override
	protected CobraModalWindowBasePanel<List<MaatregelToekenning>> createContents(String id)
	{
		return new MaatregelToekenningEditPanel(id, this);
	}

	@SuppressWarnings("unused")
	public void onWindowClose(AjaxRequestTarget target)
	{
	}

	public IModel<List<MaatregelToekenning>> getMaatregelToekenningenModel()
	{
		return getModel();
	}
}
