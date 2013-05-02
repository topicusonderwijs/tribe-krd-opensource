package nl.topicus.eduarte.web.components.modalwindow.adres;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.adres.AdresEntiteit;
import nl.topicus.eduarte.entities.adres.AdresType;
import nl.topicus.eduarte.entities.adres.AdresTypePerDatum;
import nl.topicus.eduarte.entities.adres.Adresseerbaar;
import nl.topicus.eduarte.entities.adres.AdresseerbaarUtil;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * @author jutten
 */
public class AdressenSubmitProcessor<T extends AdresEntiteit<T>, A extends Adresseerbaar<T>>
		extends FormComponent<A>
{
	private static final long serialVersionUID = 1L;

	private boolean adressenVerplicht = true;

	private boolean waarschuwingVoorOntbrekendAdresGegeven = false;

	private List<AdresType<T>> adressenType;

	private List<AdresTypePerDatum> adresTypesPerBegindatum;

	private final int fysiekadres = 1;

	private final int postadres = 2;

	private final int factuuradres = 4;

	public AdressenSubmitProcessor(String id, IModel<A> model)
	{
		super(id, model);

		add(new AbstractValidator<A>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean validateOnNullValue()
			{
				return true;
			}

			@Override
			protected void onValidate(IValidatable<A> validatable)
			{
				initAdresLists();

				List<AdresTypePerDatum> dubbeleAdresTypesPerBegindatum =
					findDubbeleAdresTypesPerBegindatum();

				if (!dubbeleAdresTypesPerBegindatum.isEmpty())
				{
					TimeUtil timeUtil = TimeUtil.getInstance();

					for (AdresTypePerDatum dubbelAdresTypePerBegindatum : dubbeleAdresTypesPerBegindatum)
					{
						String datumStr =
							timeUtil.formatDate(dubbelAdresTypePerBegindatum.getDatum());

						if ((dubbelAdresTypePerBegindatum.getType() & fysiekadres) == fysiekadres)
						{
							ValidationError error = new ValidationError();
							error.setMessage("Er zijn meerdere woonadressen opgegeven op "
								+ datumStr + ".");
							validatable.error(error);
						}
						if ((dubbelAdresTypePerBegindatum.getType() & postadres) == postadres)
						{
							ValidationError error = new ValidationError();
							error.setMessage("Er zijn meerdere postadressen opgegeven op "
								+ datumStr + ".");
							validatable.error(error);
						}
						if ((dubbelAdresTypePerBegindatum.getType() & factuuradres) == factuuradres)
						{
							ValidationError error = new ValidationError();
							error.setMessage("Er zijn meerdere factuuradressen opgegeven op "
								+ datumStr + ".");
							validatable.error(error);
						}
					}
				}

				if (!validatable.isValid())
					return;

				if (adressenVerplicht)
				{
					if (getAdressen().isEmpty())
					{
						ValidationError error = new ValidationError();
						error.setMessage("Er is geen adres opgegeven.");
						validatable.error(error);
					}

					if (!waarschuwingVoorOntbrekendAdresGegeven)
					{
						List<AdresTypePerDatum> missendeAdresTypesPerBegindatum =
							findMissendeAdresTypesPerBegindatum(false);

						if (!missendeAdresTypesPerBegindatum.isEmpty())
						{
							TimeUtil timeUtil = TimeUtil.getInstance();

							for (AdresTypePerDatum missendAdresTypePerBegindatum : missendeAdresTypesPerBegindatum)
							{
								String datumStr =
									timeUtil.formatDate(missendAdresTypePerBegindatum.getDatum());

								if ((missendAdresTypePerBegindatum.getType() & fysiekadres) == fysiekadres)
								{
									ValidationError error = new ValidationError();
									error.setMessage("Er is geen woonadres opgegeven op "
										+ datumStr
										+ ". Klik nogmaals op opslaan om alsnog op te slaan.");
									validatable.error(error);
									waarschuwingVoorOntbrekendAdresGegeven = true;
								}
								if ((missendAdresTypePerBegindatum.getType() & postadres) == postadres)
								{
									ValidationError error = new ValidationError();
									error.setMessage("Er is geen postadres opgegeven op "
										+ datumStr
										+ ". Klik nogmaals op opslaan om alsnog op te slaan.");
									validatable.error(error);
									waarschuwingVoorOntbrekendAdresGegeven = true;
								}
								if ((missendAdresTypePerBegindatum.getType() & factuuradres) == factuuradres)
								{
									ValidationError error = new ValidationError();
									error.setMessage("Er is geen factuuradres opgegeven op "
										+ datumStr
										+ ". Klik nogmaals op opslaan om alsnog op te slaan.");
									validatable.error(error);
									waarschuwingVoorOntbrekendAdresGegeven = true;
								}
							}
						}
					}
				}
			}
		});
	}

	/**
	 * Initialiseert een lijst van aanwezige adressen met een bijbehorend adrestype, en
	 * een lijst van per begindatum aanwezige adrestypes.
	 */
	private void initAdresLists()
	{
		// Lijst van aanwezige adressen met een bijbehorend adrestype.
		adressenType = new ArrayList<AdresType<T>>();

		// Lijst van per begindatum aanwezige adrestypes.
		adresTypesPerBegindatum = new ArrayList<AdresTypePerDatum>();

		setAdressenType();
		setAdresTypesPerBegindatum();
	}

	/**
	 * Maak een lijst van aanwezige adressen met een bijbehorend adrestype.
	 * <p>
	 * 1 = fysiekadres<br>
	 * 2 = postadres<br>
	 * 3 = fysiekadres + postadres<br>
	 * 4 = factuuradres<br>
	 * 5 = fysiekadres + factuuradres<br>
	 * 6 = postadres + factuuradres<br>
	 * 7 = fysiekadres + postadres + factuuradres
	 * <p>
	 * AdresEntiteit | Type<br>
	 * AE1 ----------| 3 (fysiekadres + postadres)<br>
	 * AE2 ----------| 7 (fysiekadres + postadres + factuuradres)
	 */
	private void setAdressenType()
	{
		for (T adresEntiteit : getAdressen())
		{
			int type = 0;
			if (adresEntiteit.isFysiekadres())
			{
				type |= fysiekadres;
			}
			if (adresEntiteit.isPostadres())
			{
				type |= postadres;
			}
			if (adresEntiteit.isFactuuradres())
			{
				type |= factuuradres;
			}

			AdresType<T> adresType = new AdresType<T>();
			adresType.setAdresEntiteit(adresEntiteit);
			adresType.setType(type);

			adressenType.add(adresType);
		}
	}

	/**
	 * Maak een lijst van per begindatum aanwezige adrestype. Er wordt niet gekeken naar
	 * adrestypes, die al gestart zijn op een eerdere begindatum, maar nog wel van
	 * toepassing zijn op de huidige begindatum.
	 * <p>
	 * Begindatum | Type<br>
	 * 20-07-2010 | 3 (fysiekadres + postadres)<br>
	 * 22-07-2010 | 7 (fysiekadres + postadres + factuuradres)
	 */
	private void setAdresTypesPerBegindatum()
	{
		for (AdresType<T> adresType : adressenType)
		{
			boolean isFound = false;
			for (AdresTypePerDatum adresTypePerBegindatum : adresTypesPerBegindatum)
			{
				if (adresType.getAdresEntiteit().getBegindatum().equals(
					adresTypePerBegindatum.getDatum()))
				{
					// Verander eventueel het adrestype middels bitwise OR
					adresTypePerBegindatum.setType(adresTypePerBegindatum.getType()
						| adresType.getType());

					isFound = true;
				}
			}
			if (!isFound)
			{
				AdresTypePerDatum adresTypePerBegindatum = new AdresTypePerDatum();
				adresTypePerBegindatum.setDatum(adresType.getAdresEntiteit().getBegindatum());
				adresTypePerBegindatum.setType(adresType.getType());

				adresTypesPerBegindatum.add(adresTypePerBegindatum);
			}
		}
	}

	/**
	 * Vindt een lijst van per begindatum dubbele adrestypes.
	 */
	private List<AdresTypePerDatum> findDubbeleAdresTypesPerBegindatum()
	{
		List<AdresTypePerDatum> dubbeleAdresTypesPerBegindatum = new ArrayList<AdresTypePerDatum>();

		for (AdresType<T> adresType : adressenType)
		{
			for (AdresType<T> compareAdresType : adressenType)
			{
				// Vindt overeenkomstige adrestype middels bitwise AND
				int duplicatedAdresTypes = adresType.getType() & compareAdresType.getType();

				if (adresType != compareAdresType
					&& adresType.getAdresEntiteit().getBegindatum().equals(
						compareAdresType.getAdresEntiteit().getBegindatum())
					&& duplicatedAdresTypes != 0)
				{
					boolean isFound = false;
					for (AdresTypePerDatum dubbelAdresTypePerBegindatum : dubbeleAdresTypesPerBegindatum)
					{
						if (dubbelAdresTypePerBegindatum.getDatum().equals(
							compareAdresType.getAdresEntiteit().getBegindatum()))
						{
							// Verander eventueel het adrestype middels bitwise OR
							dubbelAdresTypePerBegindatum.setType(dubbelAdresTypePerBegindatum
								.getType()
								| duplicatedAdresTypes);

							isFound = true;
						}
					}
					if (!isFound)
					{
						AdresTypePerDatum adresTypePerBegindatum = new AdresTypePerDatum();
						adresTypePerBegindatum.setDatum(compareAdresType.getAdresEntiteit()
							.getBegindatum());
						adresTypePerBegindatum.setType(duplicatedAdresTypes);

						dubbeleAdresTypesPerBegindatum.add(adresTypePerBegindatum);
					}
				}
			}
		}
		return dubbeleAdresTypesPerBegindatum;
	}

	/**
	 * Vindt een lijst van per begindatum missende adrestypes. Adrestypes, die al gestart
	 * zijn op een eerdere begindatum, maar nog wel van toepassing zijn op de huidige
	 * begindatum, worden niet in deze lijst meegenomen. De gevonden missende adrestypes
	 * kunnen worden toegevoegd, door de boolean
	 * addGevondenMissendeAdresTypesPerBegindatum op true te zetten.
	 */
	private List<AdresTypePerDatum> findMissendeAdresTypesPerBegindatum(
			boolean addGevondenMissendeAdresTypesPerBegindatum)
	{
		List<AdresTypePerDatum> missendeAdresTypesPerBegindatum =
			new ArrayList<AdresTypePerDatum>();

		for (AdresTypePerDatum adresTypePerBegindatum : adresTypesPerBegindatum)
		{
			int requiredAdresType = getRequiredAdresType();

			// Controleer of per begindatum alle adrestypes aanwezig zijn
			if ((adresTypePerBegindatum.getType() & requiredAdresType) != requiredAdresType)
			{
				// Vindt missende adrestype middels bitwise AND en bitwise NOT
				int missingAdresType = requiredAdresType & ~adresTypePerBegindatum.getType();

				Date nearestDate =
					getDateNearest(adresTypesPerBegindatum, adresTypePerBegindatum.getDatum());

				List<AdresType<T>> foundMissingAdressenWithType =
					findAdressenType(adressenType, adresTypesPerBegindatum, missingAdresType,
						adresTypePerBegindatum.getDatum(), nearestDate);

				for (AdresType<T> foundMissingAdresWithType : foundMissingAdressenWithType)
				{
					if (addGevondenMissendeAdresTypesPerBegindatum)
					{
						addGevondenMissendeAdresTypesPerBegindatum(foundMissingAdresWithType,
							missingAdresType, adresTypePerBegindatum.getDatum());
					}

					// Vindt overeenkomstige adrestype middels bitwise AND
					int foundAdresType = missingAdresType & foundMissingAdresWithType.getType();

					// Vindt overgebleven missende adrestype middels bitwise AND en
					// bitwise NOT
					missingAdresType &= ~foundAdresType;
				}

				if (missingAdresType != 0)
				{
					boolean isFound = false;
					for (AdresTypePerDatum missendAdresTypePerBegindatum : missendeAdresTypesPerBegindatum)
					{
						if (adresTypePerBegindatum.getDatum().equals(
							missendAdresTypePerBegindatum.getDatum()))
						{
							// Verander eventueel het adrestype middels bitwise OR
							missendAdresTypePerBegindatum.setType(missendAdresTypePerBegindatum
								.getType()
								| missingAdresType);

							isFound = true;
						}
					}
					if (!isFound)
					{
						AdresTypePerDatum missingAdresTypePerBegindatum = new AdresTypePerDatum();
						missingAdresTypePerBegindatum.setDatum(adresTypePerBegindatum.getDatum());
						missingAdresTypePerBegindatum.setType(missingAdresType);

						missendeAdresTypesPerBegindatum.add(missingAdresTypePerBegindatum);
					}
				}
			}
		}
		return missendeAdresTypesPerBegindatum;
	}

	/**
	 * Vindt recursief een lijst van adressen met adrestype, aan de hand van een gegeven
	 * adrestype en een datum.
	 */
	private List<AdresType<T>> findAdressenType(List<AdresType<T>> adresWithTypeList,
			List<AdresTypePerDatum> adresTypePerBegindatumList, int targetAdresType,
			Date currentBegindate, Date date)
	{
		List<AdresType<T>> foundAdressenWithType = new ArrayList<AdresType<T>>();

		if (adresWithTypeList != null && adresTypePerBegindatumList != null
			&& currentBegindate != null && date != null)
		{
			int missingAdresType = targetAdresType;

			for (AdresType<T> adresWithType : adresWithTypeList)
			{
				// Vindt overeenkomstig missende adrestype middels bitwise AND
				int foundAdresType = missingAdresType & adresWithType.getType();

				if (adresWithType.getAdresEntiteit().getBegindatum().equals(date)
					&& (adresWithType.getAdresEntiteit().getEinddatum() == null || adresWithType
						.getAdresEntiteit().getEinddatum().compareTo(currentBegindate) >= 0)
					&& foundAdresType != 0)
				{
					missingAdresType &= ~foundAdresType;

					foundAdressenWithType.add(adresWithType);
				}
			}

			if (foundAdressenWithType.isEmpty() || missingAdresType != 0)
			{
				Date nearestDate = getDateNearest(adresTypePerBegindatumList, date);

				List<AdresType<T>> foundMissingAdressenWithType =
					findAdressenType(adresWithTypeList, adresTypePerBegindatumList,
						missingAdresType, currentBegindate, nearestDate);

				for (AdresType<T> foundMissingAdresWithType : foundMissingAdressenWithType)
				{
					foundAdressenWithType.add(foundMissingAdresWithType);
				}
			}
		}
		return foundAdressenWithType;
	}

	/**
	 * Vindt de datum in de lijst, die het dichtst voor de gegeven datum ligt.
	 */
	private Date getDateNearest(List<AdresTypePerDatum> adresTypesPerDatum, Date targetDate)
	{
		Date returnDate = null;
		if (adresTypesPerDatum != null && targetDate != null)
		{
			for (AdresTypePerDatum adresTypePerDatum : adresTypesPerDatum)
			{
				Date date = adresTypePerDatum.getDatum();
				if (date.compareTo(targetDate) < 0)
				{
					if (returnDate == null || date.compareTo(returnDate) > 0)
					{
						returnDate = date;
					}
				}
			}
		}
		return returnDate;
	}

	private void addGevondenMissendeAdresTypesPerBegindatum(AdresType<T> foundMissingAdresWithType,
			int missingAdresType, Date begindatum)
	{
		// Als het adrestype verschillend is, en als het gaat om een
		// gecombineerd adres, dan het adres opsplitsen
		if (foundMissingAdresWithType.getType() != missingAdresType
			&& foundMissingAdresWithType.getType() != fysiekadres
			&& foundMissingAdresWithType.getType() != postadres
			&& foundMissingAdresWithType.getType() != factuuradres)
		{
			T oldAdresEntiteit = null;
			T newAdresEntiteit = getEntiteit().newAdres();
			List<T> adresEntiteiten = getEntiteit().getAdressen();

			// TODO overbodig volgens mij, kan ook oldAdresEntiteit =
			// foundMissingAdresWithType.getAdresEntiteit()
			for (T adresEntiteit : adresEntiteiten)
			{
				if (adresEntiteit.equals(foundMissingAdresWithType.getAdresEntiteit()))
				{
					oldAdresEntiteit = adresEntiteit;
				}
			}

			if (oldAdresEntiteit != null)
			{
				// Vindt overeenkomstige adrestype middels bitwise AND
				int foundAdresType = missingAdresType & foundMissingAdresWithType.getType();

				newAdresEntiteit.getAdres().setGearchiveerd(
					oldAdresEntiteit.getAdres().isGearchiveerd());
				newAdresEntiteit.getAdres().setGeheim(oldAdresEntiteit.getAdres().isGeheim());
				newAdresEntiteit.getAdres().setGemeente(oldAdresEntiteit.getAdres().getGemeente());
				newAdresEntiteit.getAdres().setHuisnummer(
					oldAdresEntiteit.getAdres().getHuisnummer());
				newAdresEntiteit.getAdres().setHuisnummerToevoeging(
					oldAdresEntiteit.getAdres().getHuisnummerToevoeging());
				newAdresEntiteit.getAdres().setLand(oldAdresEntiteit.getAdres().getLand());
				newAdresEntiteit.getAdres().setLocatie(oldAdresEntiteit.getAdres().getLocatie());
				newAdresEntiteit.getAdres().setPlaats(oldAdresEntiteit.getAdres().getPlaats());
				newAdresEntiteit.getAdres().setPostcode(oldAdresEntiteit.getAdres().getPostcode());
				newAdresEntiteit.getAdres()
					.setProvincie(oldAdresEntiteit.getAdres().getProvincie());
				newAdresEntiteit.getAdres().setStraat(oldAdresEntiteit.getAdres().getStraat());

				newAdresEntiteit.setBegindatum(begindatum);

				newAdresEntiteit.setFysiekadres((foundAdresType & fysiekadres) != 0);
				newAdresEntiteit.setPostadres((foundAdresType & postadres) != 0);
				newAdresEntiteit.setFactuuradres((foundAdresType & factuuradres) != 0);

				getEntiteit().getAdressen().add(0, newAdresEntiteit);

				AdresType<T> adresType = new AdresType<T>();
				adresType.setAdresEntiteit(newAdresEntiteit);
				adresType.setType(foundAdresType);

				adressenType.add(adresType);

				oldAdresEntiteit.setEinddatum(TimeUtil.getInstance().addDays(begindatum, -1));
			}
		}
	}

	private void updateOverlappendeAdressen()
	{
		for (AdresTypePerDatum adresTypePerBegindatum : adresTypesPerBegindatum)
		{
			Date nearestDate =
				getDateNearest(adresTypesPerBegindatum, adresTypePerBegindatum.getDatum());

			List<AdresType<T>> overlappingAdressenWithType =
				findAdressenType(adressenType, adresTypesPerBegindatum, adresTypePerBegindatum
					.getType(), adresTypePerBegindatum.getDatum(), nearestDate);

			for (AdresType<T> overlappingAdresWithType : overlappingAdressenWithType)
			{
				T oldAdresEntiteit = null;
				List<T> adresEntiteiten = getEntiteit().getAdressen();

				// TODO overbodig volgens mij, kan ook oldAdresEntiteit =
				// overlappingAdresWithType.getAdresEntiteit()
				for (T adresEntiteit : adresEntiteiten)
				{
					if (adresEntiteit.equals(overlappingAdresWithType.getAdresEntiteit()))
					{
						oldAdresEntiteit = adresEntiteit;
					}
				}

				if (oldAdresEntiteit != null)
				{
					oldAdresEntiteit.setEinddatum(TimeUtil.getInstance().addDays(
						adresTypePerBegindatum.getDatum(), -1));
				}
			}
		}
	}

	private int getRequiredAdresType()
	{
		return (fysiekadres | postadres | factuuradres);
	}

	private Adresseerbaar<T> getEntiteit()
	{
		return getModelObject();
	}

	private List<T> getAdressen()
	{
		return AdresseerbaarUtil.sortAdressenOpBegindatum(getEntiteit().getAdressen());
	}

	@Override
	public void updateModel()
	{
		findMissendeAdresTypesPerBegindatum(true);

		updateOverlappendeAdressen();
	}

	public void setAdressenVerplicht(boolean adressenVerplicht)
	{
		this.adressenVerplicht = adressenVerplicht;
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(adressenType);
		super.onDetach();
	}
}