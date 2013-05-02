package nl.topicus.eduarte.web.validators;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.util.JavaUtil;
import nl.topicus.eduarte.entities.opleiding.ITeamEntiteit;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelbaarEntiteit;

import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * This class is a validator for classes that implement the
 * IOrganisatieEenheidLocatieKoppelbaarEntiteit interface. This class will check if there
 * is an unique constraint violation. Each IOrganisatieEenheidLocatieKoppelbaarEntiteit is
 * allowed to have 1 relation with an organisation-eenheid in the same location (lokatie).
 * If there is more then one relation, this class will invoke an error, displayed on the
 * form (argument of validation method)
 * 
 * @author vanharen
 * @see IOrganisatieEenheidLocatieKoppelbaarEntiteit
 * @see IOrganisatieEenheidLocatieKoppelEntiteit
 * 
 */
public class OrganisatieEenheidLocatieKoppelEntiteitValidator<X, T extends IOrganisatieEenheidLocatieKoppelEntiteit<T>>
		extends AbstractValidator<X>
{
	private static final long serialVersionUID = 1L;

	private IModel< ? extends List<T>> organisatieEenheidLocatieModel;

	public OrganisatieEenheidLocatieKoppelEntiteitValidator(IModel< ? extends List<T>> model)
	{
		organisatieEenheidLocatieModel = model;
	}

	@Override
	public void onValidate(IValidatable<X> validatable)
	{
		List<T> listKoppelbaar = organisatieEenheidLocatieModel.getObject();
		List<T> koppelEntiteitenNew = new ArrayList<T>();
		boolean violation = false;

		violation = compareArraylist(listKoppelbaar, koppelEntiteitenNew, validatable);

		if (violation)
		{
			ValidationError error = new ValidationError();
			error
				.setMessage("Er kan slechts eenmaal met een organisatie op dezelfde locatie gekoppeld worden");
			validatable.error(error);
		}

	}

	/*
	 * this method will compare 2 arraylists using for loops. The standard "contains"
	 * method is invalid, since there is no id. this method will compare the locatie and
	 * organisatie-eenheid parameters for each Organisatiemedewerker object in arr1.
	 * @param arr1 : filled arraylist which might contain duplicates.
	 * @param arr2 : empty arraylist which will be filled during the process. This
	 * arraylist will not contain any duplicates.
	 */
	private boolean compareArraylist(List<T> arr1, List<T> arr2, IValidatable< ? > validatable)

	{
		boolean locatieDuplicate = false;

		for (T currentKoppelEntiteit : arr1)
		{
			if (currentKoppelEntiteit.getOrganisatieEenheid() == null)
				continue;

			for (T currentKoppelEntiteitNew : arr2)
			{
				if (currentKoppelEntiteitNew != null)
				{
					if (currentKoppelEntiteit.getOrganisatieEenheid().equals(
						currentKoppelEntiteitNew.getOrganisatieEenheid()))
						if (JavaUtil.equalsOrBothNull(currentKoppelEntiteitNew.getLocatie(),
							validatable.getValue()))
						{
							if (currentKoppelEntiteit instanceof ITeamEntiteit)
							{
								if (JavaUtil.equalsOrBothNull(
									((ITeamEntiteit) currentKoppelEntiteit).getTeam(),
									((ITeamEntiteit) currentKoppelEntiteitNew).getTeam()))
								{
									locatieDuplicate = true;
								}
							}
							else
							{
								locatieDuplicate = true;
							}
						}
				}
			}
			if (locatieDuplicate)
			{
				return true;
			}
			arr2.add(currentKoppelEntiteit);
		}
		return false;
	}
}
