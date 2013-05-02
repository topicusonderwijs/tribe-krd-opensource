package nl.topicus.eduarte.web.models.secure;

import java.util.Date;

import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.eduarte.entities.Entiteit;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.springframework.util.Assert;

/**
 * Abstract edit model om het maken van een page model makkelijk te maken. Soms is het
 * nodig om een object wat standaard waarden te geven om op een pagina te tonen. Dit wilt
 * men niet in de pagina doen dus maken we een model dat dit regelt, inclusief het opslaan
 * en detachen.
 * 
 * @author hoeve
 * @param <T>
 *            elke database entiteit mogelijk
 */
public abstract class AbstractEntiteitEditPageModel<T extends Entiteit> implements IModel<T>,
		IDetachable
{
	private static final long serialVersionUID = 1L;

	private Date registratieDatum;

	protected IChangeRecordingModel<T> entiteitModel;

	protected ModelManager entiteitManager;

	/**
	 * Controleert de data op not null en maakt de EntiteitManager aan.
	 * 
	 * @param registratieDatum
	 */
	public AbstractEntiteitEditPageModel(Date registratieDatum)
	{
		Assert.notNull(registratieDatum);

		this.registratieDatum = registratieDatum;

		setEntiteitManager();
	}

	protected abstract void setEntiteitManager();

	protected abstract T createDefaultT();

	@Override
	public void detach()
	{
		entiteitModel.detach();
		entiteitManager.detach();
	}

	public Date getRegistratieDatum()
	{
		return registratieDatum;
	}

	public IChangeRecordingModel<T> getEntiteitModel()
	{
		return entiteitModel;
	}

	public ModelManager getEntiteitManager()
	{
		return entiteitManager;
	}

	/**
	 * Controleert het model en sub objecten voordat alles wordt opgeslagen.
	 */
	public abstract void save();

	@Override
	public T getObject()
	{
		return entiteitModel.getObject();
	}

	@Override
	public void setObject(T object)
	{
		// TODO Auto-generated method stub
	}
}
