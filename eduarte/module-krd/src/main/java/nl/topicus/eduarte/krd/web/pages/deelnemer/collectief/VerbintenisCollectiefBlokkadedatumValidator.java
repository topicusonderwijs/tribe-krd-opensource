package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.security.checks.DeelnemerSecurityCheck;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.BlokkadedatumValidatorMode;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.BlokkadedatumVerbintenisValidator;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;

public class VerbintenisCollectiefBlokkadedatumValidator
{
	private List<String> errors = new ArrayList<String>();

	private Verbintenis verbintenis;

	private Date blokkadedatum;

	private BlokkadedatumValidatorMode mode;

	public VerbintenisCollectiefBlokkadedatumValidator(Verbintenis verbintenis, Date blokkadedatum,
			BlokkadedatumValidatorMode mode)
	{
		this.verbintenis = verbintenis;
		this.blokkadedatum = blokkadedatum;
		this.mode = mode;
	}

	public List<String> validate()
	{
		Asserts.assertNotNull("verbintenis", verbintenis);
		Asserts.assertNotNull("blokkadedatum", blokkadedatum);

		if (!heeftBlokkadeOverrideAutorisatie())
			doChecks();

		return errors;
	}

	private void doChecks()
	{
		switch (mode)
		{
			case Aanmaken:
				controleerBegindatum();
				break;
			case Beeindigen:
				controleerEinddatum();
				break;
			case Bewerken:
				// Geen checks, begindatum kan niet collectief aangepast worden.
				break;
		}
	}

	private void controleerEinddatum()
	{
		if (verbintenis.getEinddatum() != null && !verbintenis.getEinddatum().after(blokkadedatum))
			addValidationError("Einddatum ("
				+ TimeUtil.getInstance().formatDate(verbintenis.getEinddatum())
				+ ") ligt niet na blokkadedatum ("
				+ TimeUtil.getInstance().formatDate(blokkadedatum) + "). ");
	}

	private void controleerBegindatum()
	{
		if (!verbintenis.getBegindatum().after(blokkadedatum))
			addValidationError("Begindatum ("
				+ TimeUtil.getInstance().formatDate(verbintenis.getBegindatum())
				+ ") ligt niet na blokkadedatum ("
				+ TimeUtil.getInstance().formatDate(blokkadedatum) + "). ");
	}

	private void addValidationError(String msg)
	{
		errors.add(getDefaultMessage() + ". " + msg);
	}

	private boolean heeftBlokkadeOverrideAutorisatie()
	{
		DataSecurityCheck dataCheck =
			new DataSecurityCheck(BlokkadedatumVerbintenisValidator.WIJZIGEN_NA_MUTATIEBLOKKADE);

		DeelnemerSecurityCheck deelnemerCheck =
			new DeelnemerSecurityCheck(dataCheck, verbintenis.getDeelnemer());

		return deelnemerCheck.isActionAuthorized(Enable.class);
	}

	private String getDefaultMessage()
	{
		return createDeelnemerOmschrijving() + "Fout bij wijzigen verbintenis "
			+ verbintenis.getOmschrijving();
	}

	private String createDeelnemerOmschrijving()
	{
		Deelnemer deelnemer = verbintenis.getDeelnemer();

		String msg =
			"Deelnemer: " + deelnemer.getPersoon().getAchternaam() + ", "
				+ deelnemer.getPersoon().getVoornamen() + " (" + deelnemer.getDeelnemernummer()
				+ "). ";
		return msg;
	}
}
