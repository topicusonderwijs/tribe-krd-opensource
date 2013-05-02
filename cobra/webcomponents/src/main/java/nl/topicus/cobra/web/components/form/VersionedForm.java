package nl.topicus.cobra.web.components.form;

import java.util.Arrays;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.modelsv2.IObjectAccess;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basis form component voor het verwerken van geversioneerde objecten.
 * 
 * @author Martijn Dashorst
 */
public class VersionedForm<T> extends Form<T>
{
	private static final long serialVersionUID = 1L;

	/** voor logging. */
	private static final Logger log = LoggerFactory.getLogger(VersionedForm.class);

	/**
	 * De versie van het model object zoals dat op constructie tijd bestond.
	 */
	private Long version;

	@SpringBean
	private IObjectAccess objectAccess;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            de identifier van het form.
	 * @param model
	 *            het model object dat een entiteit bevat.
	 */
	public VersionedForm(String id, IModel<T> model)
	{
		super(id, model);
		version = getVersion();
	}

	/**
	 * Bepaalt de versie van het model object van dit form.
	 * 
	 * @return de versie, Long.MIN_VALUE als de versie null is.
	 */
	private Long getVersion()
	{
		Object modelObject = getModelObject();
		Long newVersion = Long.MIN_VALUE;

		if (modelObject instanceof IdObject)
		{
			IdObject entiteit = (IdObject) modelObject;
			newVersion = entiteit.getVersion();
			if (newVersion == null)
			{
				newVersion = Long.MIN_VALUE;
			}
		}
		return newVersion;
	}

	/**
	 * @see org.apache.wicket.markup.html.form.Form#validate()
	 */
	@Override
	protected void onValidate()
	{
		Long newVersion = getVersion();

		if (log.isDebugEnabled())
		{
			log.debug("Controle op versies van model object: was " + version + " is nu "
				+ newVersion);
		}

		// controleer de versie van het model object, want als dat niet klopt, dan zijn de
		// overige ingevoerde velden niet relevant.
		if (!newVersion.equals(version))
		{
			// verwijder de input van de gebruiker uit de form componenten,
			// deze is niet meer geldig, en overschrijft in het standaard
			// error gedrag van het form de nieuwe waarden die we willen tonen.
			clearInput();
			reloadModel();

			// we lezen het object opnieuw in, dus we moeten het nieuwe
			// versienummer overnemen.
			version = newVersion;

			error("Deze gegevens zijn al door een ander aangepast, voer uw wijzigingen opnieuw door.");
		}
		else
		{
			// de versies zijn in overeenstemming, dus nu het normale proces door voor de
			// validatie van het formulier.
			super.onValidate();
		}
	}

	private void reloadModel()
	{
		Object object = getModelObject();
		if (getModel() != null)
			getModel().detach();
		if (object instanceof IdObject)
			objectAccess.evict(Arrays.asList((IdObject) object));
	}
}
