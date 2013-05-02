package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.bpv;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.security.checks.DeelnemerSecurityCheck;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.BlokkadedatumBPVValidator;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.BlokkadedatumValidatorMode;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;

public class BPVInschrijvingCollectiefBlokkadedatumValidator
{
	private List<String> errors = new ArrayList<String>();

	private BPVInschrijving bpvInschrijving;

	private Date blokkadedatum;

	private BlokkadedatumValidatorMode mode;

	public BPVInschrijvingCollectiefBlokkadedatumValidator(BPVInschrijving bpvInschrijving,
			Date blokkadedatum, BlokkadedatumValidatorMode mode)
	{
		this.bpvInschrijving = bpvInschrijving;
		this.blokkadedatum = blokkadedatum;
		this.mode = mode;
	}

	public List<String> validate()
	{
		Asserts.assertNotNull("verbintenis", bpvInschrijving);
		Asserts.assertNotNull("blokkadedatum", blokkadedatum);

		if (!heeftBlokkadeOverrideAutorisatie())
			doCheck();

		return errors;
	}

	private void doCheck()
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
				break;
		}
	}

	private void controleerEinddatum()
	{
		if (bpvInschrijving.getEinddatum() != null
			&& !bpvInschrijving.getEinddatum().after(blokkadedatum))
			addValidationError("Einddatum ("
				+ TimeUtil.getInstance().formatDate(bpvInschrijving.getEinddatum())
				+ ") ligt voor blokkadedatum (" + TimeUtil.getInstance().formatDate(blokkadedatum)
				+ "). ");
	}

	private void controleerBegindatum()
	{
		if (!bpvInschrijving.getBegindatum().after(blokkadedatum))
			addValidationError("Begindatum ("
				+ TimeUtil.getInstance().formatDate(bpvInschrijving.getBegindatum())
				+ ") ligt voor blokkadedatum (" + TimeUtil.getInstance().formatDate(blokkadedatum)
				+ "). ");
	}

	private boolean heeftBlokkadeOverrideAutorisatie()
	{
		DataSecurityCheck dataCheck =
			new DataSecurityCheck(BlokkadedatumBPVValidator.WIJZIGEN_NA_MUTATIEBLOKKADE);

		DeelnemerSecurityCheck deelnemerCheck =
			new DeelnemerSecurityCheck(dataCheck, bpvInschrijving.getDeelnemer());

		return deelnemerCheck.isActionAuthorized(Enable.class);
	}

	private void addValidationError(String msg)
	{
		errors.add(getDefaultMessage() + ". " + msg);
	}

	private String getDefaultMessage()
	{
		return createDeelnemerOmschrijving() + "Fout bij statusovergang BPV inschrijving "
			+ bpvInschrijving.toString();
	}

	private String createDeelnemerOmschrijving()
	{
		Deelnemer deelnemer = bpvInschrijving.getDeelnemer();

		String msg =
			"Deelnemer: " + deelnemer.getPersoon().getAchternaam() + ", "
				+ deelnemer.getPersoon().getVoornamen() + " (" + deelnemer.getDeelnemernummer()
				+ "). ";
		return msg;
	}
}