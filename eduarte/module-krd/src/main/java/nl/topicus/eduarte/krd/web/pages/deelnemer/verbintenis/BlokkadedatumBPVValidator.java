package nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.security.checks.DeelnemerSecurityCheck;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.settings.MutatieBlokkedatumSetting;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

public class BlokkadedatumBPVValidator<T> extends AbstractValidator<T> implements IDetachable
{
	private static final long serialVersionUID = 1L;

	public static final String WIJZIGEN_NA_MUTATIEBLOKKADE = "WIJZIGEN_NA_MUTATIEBLOKKADE";

	private IModel<BPVInschrijving> bpvInschrijvingModel;

	private Date initieleBegindatum;

	private BlokkadedatumValidatorMode mode;

	public BlokkadedatumBPVValidator(IModel<BPVInschrijving> bpvInschrijvingModel,
			BlokkadedatumValidatorMode mode)
	{
		this(bpvInschrijvingModel, null, mode);
	}

	public BlokkadedatumBPVValidator(IModel<BPVInschrijving> bpvInschrijvingModel,
			Date initieleBegindatum, BlokkadedatumValidatorMode mode)
	{
		this.bpvInschrijvingModel = bpvInschrijvingModel;
		this.initieleBegindatum = initieleBegindatum;
		this.mode = mode;
	}

	@Override
	protected void onValidate(IValidatable<T> validatable)
	{
		if (bpvInschrijvingModel != null && getMutatieBlokkadedatum() != null
			&& !heeftBlokkadeOverrideAutorisatie(getBpvInschrijving().getDeelnemer()))
		{
			controleerBlokkadedatum(validatable);
		}
	}

	private void controleerBlokkadedatum(IValidatable<T> validatable)
	{
		switch (mode)
		{
			case Aanmaken:
				controleerBegindatumBPVInschrijving(validatable);
				break;
			case Beeindigen:
				controleerEinddatumBPVInschrijving(validatable);
				break;
			case Bewerken:
				controleerInitieleBegindatumBPVInschrijving(validatable);
				break;
		}
	}

	private void controleerBegindatumBPVInschrijving(IValidatable<T> validatable)
	{
		if (!getBpvInschrijving().getBegindatum().after(getMutatieBlokkadedatum()))
		{
			Map<String, Object> vars = createKeyMap();
			vars.put("begindatum", formatDate(getBpvInschrijving().getBegindatum()));
			error(validatable, "BlokkadedatumBPVValidator.voorBlokkadedatum", vars);
		}
	}

	private void controleerEinddatumBPVInschrijving(IValidatable<T> validatable)
	{
		if (getBpvInschrijving().getEinddatum() != null
			&& !getBpvInschrijving().getEinddatum().after(getMutatieBlokkadedatum()))
		{
			Map<String, Object> vars = createKeyMap();
			vars.put("einddatum", formatDate(getBpvInschrijving().getEinddatum()));
			error(validatable, "BlokkadedatumBPVValidator.einddatum", vars);
		}
	}

	private void controleerInitieleBegindatumBPVInschrijving(IValidatable<T> validatable)
	{

		if (initieleBegindatum != null)
		{
			boolean begindatumGewijzigd =
				!initieleBegindatum.equals(getBpvInschrijving().getBegindatum());

			boolean initieleBegindatumNaBlokkadedatum =
				initieleBegindatum.after(getMutatieBlokkadedatum());

			boolean initieleBegindatumVoorBlokkadedatum =
				initieleBegindatum.before(getMutatieBlokkadedatum())
					|| initieleBegindatum.equals(getMutatieBlokkadedatum());

			boolean huidigeBegindatumVoorBlokkadedatum =
				getBpvInschrijving().getBegindatum().before(getMutatieBlokkadedatum())
					|| getBpvInschrijving().getBegindatum().equals(getMutatieBlokkadedatum());

			boolean huidigeBegindatumNaBlokkadedatum =
				getBpvInschrijving().getBegindatum().after(getMutatieBlokkadedatum());

			if (begindatumGewijzigd
				&& ((initieleBegindatumVoorBlokkadedatum && huidigeBegindatumNaBlokkadedatum) || (initieleBegindatumNaBlokkadedatum && huidigeBegindatumVoorBlokkadedatum)))
			{
				Map<String, Object> vars = createKeyMap();
				vars.put("initieleBegindatum", formatDate(initieleBegindatum));
				error(validatable, "BlokkadedatumBPVValidator.begindatumWijzigen", vars);

			}
		}
	}

	private static boolean heeftBlokkadeOverrideAutorisatie(Deelnemer deelnemer)
	{
		DataSecurityCheck dataCheck = new DataSecurityCheck(WIJZIGEN_NA_MUTATIEBLOKKADE);

		DeelnemerSecurityCheck deelnemerCheck = new DeelnemerSecurityCheck(dataCheck, deelnemer);

		return deelnemerCheck.isActionAuthorized(Enable.class);
	}

	private static Date getMutatieBlokkadedatum()
	{
		MutatieBlokkedatumSetting setting =
			DataAccessRegistry.getHelper(SettingsDataAccessHelper.class).getSetting(
				MutatieBlokkedatumSetting.class);

		if (setting != null && setting.getValue() != null)
			return setting.getValue().getBlokkadedatumBPV();
		else
			return null;
	}

	private Map<String, Object> createKeyMap()
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("blokkadedatum", formatDate(getMutatieBlokkadedatum()));
		return params;
	}

	private BPVInschrijving getBpvInschrijving()
	{
		return bpvInschrijvingModel.getObject();
	}

	private String formatDate(Date date)
	{
		return TimeUtil.getInstance().formatDate(date);
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(bpvInschrijvingModel);
	}

	/**
	 * Deze check kan alleen gebruikt worden als in de interface de begindatum van de
	 * BPV-inschrijving niet aangepast kan worden.
	 */
	public static boolean isWijzigenToegestaan(BPVInschrijving bpvInschrijving)
	{
		if (getMutatieBlokkadedatum() != null
			&& !heeftBlokkadeOverrideAutorisatie(bpvInschrijving.getDeelnemer()))
		{
			return bpvInschrijving.getBegindatum().after(getMutatieBlokkadedatum());
		}
		return true;
	}
}
