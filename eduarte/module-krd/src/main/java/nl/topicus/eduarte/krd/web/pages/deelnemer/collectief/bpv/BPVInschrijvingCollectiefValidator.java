package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.bpv;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.personen.Deelnemer;

/*
 * @author idserda
 */
public class BPVInschrijvingCollectiefValidator
{
	private List<String> errors = new ArrayList<String>();

	private BPVInschrijving inschrijving;

	private BPVStatus eindstatus;

	public BPVInschrijvingCollectiefValidator(BPVInschrijving inschrijving, BPVStatus eindstatus)
	{
		this.inschrijving = inschrijving;
		this.eindstatus = eindstatus;
	}

	public List<String> validate()
	{
		Asserts.assertNotNull("inschrijving", inschrijving);
		Asserts.assertNotNull("eindstatus", eindstatus);

		if (BPVStatus.Voorlopig.equals(eindstatus))
			checkVerplichteVeldenEindstatusVoorlopig();
		else if (BPVStatus.Volledig.equals(eindstatus))
			checkVerplichteVeldenEindstatusVolledig();

		return errors;
	}

	private void checkVerplichteVeldenEindstatusVoorlopig()
	{
		checkVerplichteVeldenEindstatusVoorlopigAlgemeen();
	}

	private void checkVerplichteVeldenEindstatusVoorlopigAlgemeen()
	{
		if (inschrijving.getBegindatum() == null)
			addValidationError("einddatum");
		if (inschrijving.getBpvBedrijf() == null)
			addValidationError("bpv bedrijf");
	}

	private void checkVerplichteVeldenEindstatusVolledig()
	{
		checkVerplichteVeldenEindstatusVolledigAlgemeen();
	}

	private void checkVerplichteVeldenEindstatusVolledigAlgemeen()
	{
		if (inschrijving.getVerwachteEinddatum() == null)
			addValidationError("verwachte einddatum");
		if (inschrijving.getTotaleOmvang() == null)
			addValidationError("totale omvang");
		if (inschrijving.getAfsluitdatum() == null)
			addValidationError("afsluitdatum");
	}

	private void addValidationError(String msg)
	{
		errors.add(getDefaultMessage() + ". Veld " + msg + " is niet ingevuld.");
	}

	private String getDefaultMessage()
	{
		return createDeelnemerOmschrijving() + "Fout bij statusovergang BPV inschrijving "
			+ inschrijving.toString();
	}

	private String createDeelnemerOmschrijving()
	{
		Deelnemer deelnemer = inschrijving.getDeelnemer();

		String msg =
			"Deelnemer: " + deelnemer.getPersoon().getAchternaam() + ", "
				+ deelnemer.getPersoon().getVoornamen() + " (" + deelnemer.getDeelnemernummer()
				+ "). ";
		return msg;
	}

}
