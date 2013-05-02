package nl.topicus.eduarte.web.components.panels.afspraak;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.dao.participatie.helpers.AfspraakParticpantDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.AfspraakParticipant;
import nl.topicus.eduarte.entities.participatie.PersoonlijkeGroepDeelnemer;

import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * Image die middels een icoontje aangeeft wat voor type entiteit iets is. De ondersteunde
 * entiteiten zijn: * Deelnemer * Medewerker * Groep * Persoonlijke groep
 * 
 * @author loite
 */
public class EntiteitTypeImage extends ContextImage
{
	private static final long serialVersionUID = 1L;

	private IModel< ? extends IdObject> entiteitModel;

	public EntiteitTypeImage(String id, final IModel< ? extends IdObject> model)
	{
		super(id, new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				IdObject value = model.getObject();
				IdObject entiteit = null;
				if (value instanceof AfspraakParticipant)
					entiteit = ((AfspraakParticipant) value).getParticipantEntiteit();
				else if (value instanceof PersoonlijkeGroepDeelnemer)
					entiteit = ((PersoonlijkeGroepDeelnemer) value).getDeelnemerEntiteit();
				else
					entiteit = value;
				return DataAccessRegistry.getHelper(AfspraakParticpantDataAccessHelper.class)
					.getImageName(entiteit);
			}
		});
		this.entiteitModel = model;
	}

	@Override
	public boolean isVisible()
	{
		return super.isVisible() && entiteitModel.getObject() != null;
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		entiteitModel.detach();
	}
}
