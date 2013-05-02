package nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.VolgendeButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VorigeButton;
import nl.topicus.eduarte.app.security.checks.DeelnemerSecurityCheck;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.settings.MutatieBlokkedatumSetting;
import nl.topicus.eduarte.providers.VerbintenisProvider;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IFormSubmittingComponent;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

public class BlokkadedatumVerbintenisValidator<T> extends AbstractValidator<T>
{
	private static final long serialVersionUID = 1L;

	public static final String WIJZIGEN_NA_MUTATIEBLOKKADE = "WIJZIGEN_NA_MUTATIEBLOKKADE";

	private final VerbintenisProvider provider;

	private Form< ? > form;

	private Date initieleBegindatum;

	private BlokkadedatumValidatorMode mode;

	public BlokkadedatumVerbintenisValidator(VerbintenisProvider provider)
	{
		this(provider, null, null, BlokkadedatumValidatorMode.Bewerken);
	}

	public BlokkadedatumVerbintenisValidator(VerbintenisProvider provider, Form< ? > form,
			Date initieleBegindatum, BlokkadedatumValidatorMode mode)
	{
		Asserts.assertNotNull("provider", provider);
		this.provider = provider;
		this.form = form;
		this.initieleBegindatum = initieleBegindatum;
		this.mode = mode;
	}

	@Override
	protected void onValidate(IValidatable<T> validatable)
	{
		if (provider.getVerbintenis() != null)
			onValidate(validatable, provider.getVerbintenis());
	}

	protected void onValidate(IValidatable<T> validatable, Verbintenis verbintenis)
	{
		if (form != null)
		{
			IFormSubmittingComponent submitButton = form.findSubmittingButton();
			if (submitButton instanceof SubmitLink)
			{
				SubmitLink link = (SubmitLink) submitButton;
				// Op deze manier gaat de validator niet af op de vorigen en volgende
				// button,maar alleen bij de voltooien
				if (link.findParent(VorigeButton.class) != null
					|| link.findParent(VolgendeButton.class) != null)
					return;
			}
		}

		doChecks(validatable, verbintenis);
	}

	private void doChecks(IValidatable<T> validatable, Verbintenis verbintenis)
	{
		if (getMutatieBlokkadedatum() != null
			&& !heeftBlokkadeOverrideAutorisatie(verbintenis.getDeelnemer()))
		{
			switch (mode)
			{
				case Beeindigen:
					valideerEinddatumVerbintenisBlokkadedatum(validatable, verbintenis);
					break;
				case Bewerken:
					valideerInitieleBegindatumVerbintenisBlokkadedatum(validatable, verbintenis);
					break;
				case Aanmaken:
					valideerHuidigeBegindatumVerbintenisBlokkadedatum(validatable, verbintenis);
					break;
			}
		}
	}

	// Verbintenis mag alleen beeindigd worden als de einddatum na de blokkadedatum ligt
	private void valideerEinddatumVerbintenisBlokkadedatum(IValidatable<T> validatable,
			Verbintenis verbintenis)
	{
		if (verbintenis.getEinddatum() != null
			&& !verbintenis.getEinddatum().after(getMutatieBlokkadedatum()))
		{
			Map<String, Object> keymap = createKeyMap();
			keymap.put("einddatum", formatDate(verbintenis.getEinddatum()));
			error(validatable, "BlokkadedatumVerbintenisValidator.einddatum", keymap);
		}
	}

	private void valideerInitieleBegindatumVerbintenisBlokkadedatum(IValidatable<T> validatable,
			Verbintenis verbintenis)
	{
		if (initieleBegindatum != null)
		{
			boolean begindatumGewijzigd = !initieleBegindatum.equals(verbintenis.getBegindatum());

			boolean initieleBegindatumNaBlokkadedatum =
				initieleBegindatum.after(getMutatieBlokkadedatum());

			boolean initieleBegindatumVoorBlokkadedatum =
				initieleBegindatum.before(getMutatieBlokkadedatum())
					|| initieleBegindatum.equals(getMutatieBlokkadedatum());

			boolean huidigeBegindatumVoorBlokkadedatum =
				verbintenis.getBegindatum().before(getMutatieBlokkadedatum())
					|| verbintenis.getBegindatum().equals(getMutatieBlokkadedatum());

			boolean huidigeBegindatumNaBlokkadedatum =
				verbintenis.getBegindatum().after(getMutatieBlokkadedatum());

			if (begindatumGewijzigd
				&& ((initieleBegindatumVoorBlokkadedatum && huidigeBegindatumNaBlokkadedatum) || (initieleBegindatumNaBlokkadedatum && huidigeBegindatumVoorBlokkadedatum)))
			{
				Map<String, Object> keymap = createKeyMap();
				keymap.put("initieleBegindatum", formatDate(initieleBegindatum));
				error(validatable, "BlokkadedatumVerbintenisValidator.begindatumWijzigen", keymap);
			}
		}
	}

	// Een nieuwe verbintenis mag alleen aangemaakt worden als de begindatum na de
	// blokkadedatum ligt
	private void valideerHuidigeBegindatumVerbintenisBlokkadedatum(IValidatable<T> validatable,
			Verbintenis verbintenis)
	{
		if (!verbintenis.getBegindatum().after(getMutatieBlokkadedatum()))
		{
			Map<String, Object> keymap = createKeyMap();
			keymap.put("begindatum", formatDate(verbintenis.getBegindatum()));
			error(validatable, "BlokkadedatumVerbintenisValidator.voorBlokkadedatum", keymap);
		}
	}

	private Map<String, Object> createKeyMap()
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("blokkadedatum", formatDate(getMutatieBlokkadedatum()));
		return params;
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
			return setting.getValue().getBlokkadedatumVerbintenis();
		else
			return null;
	}

	private String formatDate(Date date)
	{
		return TimeUtil.getInstance().formatDate(date);
	}

	/**
	 * Deze check kan alleen gebruikt worden als in de interface de begindatum van de
	 * verbintenis niet aangepast kan worden.
	 */
	public static boolean isWijzigenToegestaan(Verbintenis verbintenis)
	{
		if (getMutatieBlokkadedatum() != null
			&& !heeftBlokkadeOverrideAutorisatie(verbintenis.getDeelnemer()))
		{
			return verbintenis.getBegindatum().after(getMutatieBlokkadedatum());
		}
		return true;
	}
}
