package nl.topicus.eduarte.web.components.modalwindow.adres;

import java.util.List;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.adres.AdresEntiteit;
import nl.topicus.eduarte.entities.adres.Adresseerbaar;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.AdresEntiteitTable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class ListAdressenEditPanel<T extends AdresEntiteit<T>, A extends Adresseerbaar<T>> extends
		AbstractEduArteToevoegenBewerkenPanel<T>
{
	private static final long serialVersionUID = 1L;

	private IModel< ? extends Adresseerbaar<T>> entiteitModel;

	private AdressenSubmitProcessor<T, A> submitProcessor;

	ListAdressenEditPanel(String id, IModel<A> entiteitModel, ModelManager manager)
	{
		super(id, new PropertyModel<List<T>>(entiteitModel, "adressen"), manager,
			new AdresEntiteitTable<T>());
		setInsertAtEnd(false);
		this.entiteitModel = entiteitModel;
		getModalWindow().setInitialHeight(320);
		getModalWindow().setInitialWidth(500);
		getModalWindow().setResizable(false);

		add(submitProcessor = new AdressenSubmitProcessor<T, A>("submitprocessor", entiteitModel));
	}

	public void setAdressenVerplicht(boolean adressenVerplicht)
	{
		submitProcessor.setAdressenVerplicht(adressenVerplicht);
	}

	@Override
	public AbstractToevoegenBewerkenModalWindowPanel<T> createModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<T> modalWindow)
	{
		return new ModalWindowAdresEditPanel<T>(id, modalWindow, this);
	}

	@Override
	public T createNewT()
	{
		T ret = entiteitModel.getObject().newAdres();
		ret.setFysiekadres(true);
		ret.setPostadres(true);
		ret.setFactuuradres(true);
		return ret;
	}

	@Override
	protected List<IdObject> getEditableObjects(T base)
	{
		List<IdObject> ret = super.getEditableObjects(base);
		ret.add(base.getAdres());
		return ret;
	}

	@Override
	public String getModalWindowTitle()
	{
		return "Adres";
	}

	@Override
	public String getToevoegenLabel()
	{
		return "Adres toevoegen";
	}

	@Override
	protected void onSaveCurrent(AjaxRequestTarget target, T object)
	{
	}

	@Override
	protected void onSaveNew(AjaxRequestTarget target, T object)
	{
	}
}
