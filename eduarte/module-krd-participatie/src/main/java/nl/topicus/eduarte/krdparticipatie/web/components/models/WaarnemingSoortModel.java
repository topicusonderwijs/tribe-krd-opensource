package nl.topicus.eduarte.krdparticipatie.web.components.models;

import java.io.Serializable;

import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.eduarte.entities.participatie.AbsentieReden;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingSoort;

import org.apache.wicket.model.IModel;

/**
 * @author vanderkamp
 */
public class WaarnemingSoortModel implements IModel<Serializable>
{
	/** Voor serializatie. */
	private static final long serialVersionUID = 1L;

	IModel<AbsentieReden> absentieRedenModel;

	WaarnemingSoort waarnemingSoort;

	String waarnemingString;

	/**
	 * @param object
	 */
	public WaarnemingSoortModel(Object object)
	{
		if (object instanceof AbsentieReden)
		{
			AbsentieReden reden = (AbsentieReden) object;
			absentieRedenModel =
				ModelFactory.getModel(reden, new DefaultModelManager(AbsentieReden.class));
		}
		else if (object instanceof WaarnemingSoort)
		{
			waarnemingSoort = (WaarnemingSoort) object;
		}
		else if (object instanceof String)
		{
			waarnemingString = (String) object;
		}
	}

	public void detach()
	{
		if (absentieRedenModel != null)
			absentieRedenModel.detach();
	}

	public Serializable getObject()
	{
		if (absentieRedenModel != null)
			return absentieRedenModel.getObject();
		if (waarnemingSoort != null)
			return waarnemingSoort;
		return waarnemingString;
	}

	public void setObject(Serializable object)
	{
		if (object instanceof AbsentieReden)
		{
			AbsentieReden reden = (AbsentieReden) object;
			absentieRedenModel =
				ModelFactory.getModel(reden, new DefaultModelManager(AbsentieReden.class));
			waarnemingSoort = null;
			waarnemingString = null;
		}
		else if (object instanceof WaarnemingSoort)
		{
			waarnemingSoort = (WaarnemingSoort) object;
			detach();
			absentieRedenModel = null;
			waarnemingString = null;

		}
		else if (object instanceof String)
		{
			waarnemingString = (String) object;
			detach();
			absentieRedenModel = null;
			waarnemingSoort = null;
		}
	}

}
