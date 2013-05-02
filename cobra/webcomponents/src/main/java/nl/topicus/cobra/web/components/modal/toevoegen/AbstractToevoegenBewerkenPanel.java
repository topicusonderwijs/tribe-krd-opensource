package nl.topicus.cobra.web.components.modal.toevoegen;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.modelsv2.ReadOnlyListPropertyModel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.TypedPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Abstract class voor het maken van een toevoegen & bewerken modal window. Deze panel
 * heeft een datapanel, modalwindow en toevoegen knop.
 * 
 * @author hoeve
 */
public abstract class AbstractToevoegenBewerkenPanel<T extends IdObject> extends
		TypedPanel<List<T>>
{
	private static final long serialVersionUID = 1L;

	private AbstractToevoegenBewerkenModalWindow<T> modalWindow;

	private final Panel datapanel;

	private boolean insertAtEnd = true;

	/**
	 * Enum welke aangeeft of men een toevoegen-en-opslaan knop, een opslaan knop of beide
	 * moet tonen.
	 * 
	 * @author hoeve
	 */
	public static enum OpslaanButtonType
	{
		Opslaan(1),
		OpslaanEnToevoegen(2),
		OpslaanEnKopie(5),
		Allen(10);

		private int number;

		private OpslaanButtonType(int number)
		{
			this.number = number;
		}

		public int getNumber()
		{
			return number;
		}

		public boolean contains(OpslaanButtonType other)
		{
			return getNumber() % other.getNumber() == 0;
		}
	}

	public AbstractToevoegenBewerkenPanel(String id, IModel<List<T>> model, ModelManager manager,
			IDetachable table)
	{
		super(id, model);
		setOutputMarkupPlaceholderTag(true);

		add(prepareModalWindow(model, manager));

		ListModelDataProvider<T> provider = new ListModelDataProvider<T>(model);
		datapanel = createCustomDataPanel("datapanel", provider, table, modalWindow);
		add(datapanel);

		AjaxLink<T> objectToevoegen = new AjaxLink<T>("objectToevoegen")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				modalWindow.setDefaultModelObject(addT());
				modalWindow.setAddEntity(true);
				modalWindow.show(target);
			}

			@Override
			public boolean isVisible()
			{
				return isAddable() && isEditable();
			}
		};

		add(objectToevoegen);
		objectToevoegen.add(new Label("objectToevoegenLabel",
			new Model<String>(getToevoegenLabel())));
	}

	protected abstract Panel createCustomDataPanel(String string,
			ListModelDataProvider<T> provider, IDetachable table,
			AbstractToevoegenBewerkenModalWindow<T> modalWindow2);

	protected boolean isAddable()
	{
		return true;
	}

	protected boolean isClickable()
	{
		return true;
	}

	protected boolean isEditable()
	{
		return true;
	}

	protected boolean isDeletable()
	{
		return true;
	}

	/**
	 * Override deze functie wanneer je een opslaan-en-nieuwe knop of een opslaan-en-kopie
	 * knop wilt tonen ipv een opslaan knop.
	 */
	public OpslaanButtonType getOpslaanButtonType()
	{
		return OpslaanButtonType.Opslaan;
	}

	private AbstractToevoegenBewerkenModalWindow<T> prepareModalWindow(
			IModel< ? extends List<T>> model, ModelManager manager)
	{
		modalWindow =
			new AbstractToevoegenBewerkenModalWindow<T>("modalWindow",
				new ReadOnlyListPropertyModel<T>(model), manager, getModalWindowTitle())
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected CobraModalWindowBasePanel<T> createContents(String id)
				{
					return createModalWindowPanel(id, AbstractToevoegenBewerkenPanel.this
						.getModalWindow());
				}

				@Override
				protected List<IdObject> getEditableObjects()
				{
					return AbstractToevoegenBewerkenPanel.this.getEditableObjects(getModelObject());
				}
			};
		modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				T object = modalWindow.getModelObject();
				if (modalWindow.isDeleteOnClose() && modalWindow.isAddEntity())
					// toevoegen knop + annuleren
					deleteT(object);
				else if (modalWindow.isDeleteOnClose() && !modalWindow.isAddEntity())
					// 'edit knop' + annuleren
					modalWindow.revertData();
				else if (modalWindow.isAddEntity())
					// toevoegen knop + opslaan
					AbstractToevoegenBewerkenPanel.this.onSaveNew(target, object);
				else
					// 'edit knop' + opslaan.
					AbstractToevoegenBewerkenPanel.this.onSaveCurrent(target, object);
				target.addComponent(AbstractToevoegenBewerkenPanel.this);
			}
		});
		modalWindow.setCloseButtonCallback(new ModalWindow.CloseButtonCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean onCloseButtonClicked(AjaxRequestTarget target)
			{
				if (modalWindow.isAddEntity())
					deleteT(modalWindow.getModelObject());
				target.addComponent(AbstractToevoegenBewerkenPanel.this);
				return true;
			}
		});
		return modalWindow;
	}

	/**
	 * Hook functie om te implementeren wanneer men iets wilt doen wanneer een nieuw
	 * object wordt toegevoegd & opgeslagen.
	 */
	@SuppressWarnings("unused")
	protected void onSaveNew(AjaxRequestTarget target, T object)
	{
	}

	/**
	 * Hook functie om te implementeren wanneer men iets wilt doen wanneer een bestaand
	 * object wordt bewerkt & opgeslagen.
	 */
	@SuppressWarnings("unused")
	protected void onSaveCurrent(AjaxRequestTarget target, T object)
	{
	}

	protected List<IdObject> getEditableObjects(T base)
	{
		List<IdObject> ret = new ArrayList<IdObject>();
		ret.add(base);
		return ret;
	}

	public Panel getCustomDataPanel()
	{
		return datapanel;
	}

	protected T addT()
	{
		List<T> modelObjectList = getModelObject();
		T modelObject = createNewT();
		if (!modelObjectList.contains(modelObject))
		{
			if (insertAtEnd)
				modelObjectList.add(modelObject);
			else
				modelObjectList.add(0, modelObject);
		}

		return modelObject;
	}

	protected void deleteT(T modelObject)
	{
		List<T> modelObjectList = getModelObject();
		modelObjectList.remove(modelObject);
		modalWindow.getModel().setObject(null);
	}

	public void setInsertAtEnd(boolean insertAtEnd)
	{
		this.insertAtEnd = insertAtEnd;
	}

	protected AbstractToevoegenBewerkenModalWindow<T> getModalWindow()
	{
		return modalWindow;
	}

	/**
	 * @return creert een nieuwe T zodat deze direct in de
	 *         {@link AbstractToevoegenBewerkenModalWindow} kan worden gebruikt.
	 */
	public abstract T createNewT();

	/**
	 * @param id
	 * @param modalWindow
	 * @return een {@link ModalWindowBasePanel} welke de content van de
	 *         {@link CobraModalWindow} bevat.
	 */
	@SuppressWarnings("hiding")
	public abstract AbstractToevoegenBewerkenModalWindowPanel<T> createModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<T> modalWindow);

	/**
	 * @return de naam van de knop om een object toe te voegen.
	 */
	public abstract String getToevoegenLabel();

	/**
	 * 
	 * @return de title van het modal window.
	 */
	public abstract String getModalWindowTitle();

	/**
	 * @param title
	 *            de titel die in de popup wordt gebruikt.
	 */
	public void setModalWindowTitle(String title)
	{
		modalWindow.setTitle(title);
	}
}
