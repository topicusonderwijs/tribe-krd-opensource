package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.aanmaken;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.bron.BronVerbintenisWijzigingToegestaanCheck;

/*
 * @author idserda
 */
public class CollectiefAanmakenValidator
{
	private List<String> errors = new ArrayList<String>();

	private Plaatsing plaatsing;

	private Verbintenis verbintenis;

	public CollectiefAanmakenValidator(Plaatsing plaatsing)
	{
		this.plaatsing = plaatsing;
	}

	public CollectiefAanmakenValidator(Verbintenis verbintenis)
	{
		this.verbintenis = verbintenis;
	}

	public List<String> validatePlaatsing()
	{
		Asserts.assertNotNull("plaatsing", plaatsing);

		if (plaatsing.getBegindatum() == null)
			addValidationError("begindatum");

		if (plaatsingIsVO())
			controleerVeldenVO();

		controleerBeginEinddatumVerbintenis();
		controleerBron();

		return errors;
	}

	private void controleerBron()
	{
		BronVerbintenisWijzigingToegestaanCheck check =
			new BronVerbintenisWijzigingToegestaanCheck(plaatsing.getBegindatum(), plaatsing
				.getVerbintenis());

		if (!check.isWijzigingToegestaan())
			addError(getDefaultMessage() + getVerbintenisOmschrijving()
				+ " Aanmaken plaatsing is niet toegestaan voor BRON.");
	}

	public List<String> validateVerbintenis()
	{
		Asserts.assertNotNull("verbintenis", verbintenis);

		// TODO: implement

		return errors;
	}

	private void controleerBeginEinddatumVerbintenis()
	{
		if (plaatsing.getBegindatum().before(plaatsing.getVerbintenis().getBegindatum()))
			addError(getDefaultMessageVerbintenis() + " Begindatum plaatsing ("
				+ plaatsing.getBegindatumFormatted() + ") ligt voor begindatum verbintenis ("
				+ getVerbintenis().getBegindatumFormatted() + ").");

		if (plaatsing.getEinddatum() != null && plaatsing.getVerbintenis().getEinddatum() != null
			&& plaatsing.getEinddatum().after(verbintenis.getEinddatum()))
			addError(getDefaultMessageVerbintenis() + " Begindatum plaatsing ("
				+ plaatsing.getEinddatumFormatted() + ") ligt voor begindatum verbintenis ("
				+ getVerbintenis().getEinddatumFormatted() + ").");
	}

	private String getVerbintenisOmschrijving()
	{
		return " Verbintenis: " + getVerbintenis().getContextInfoOmschrijving() + ".";
	}

	private boolean plaatsingIsVO()
	{
		Opleiding opleiding = plaatsing.getVerbintenis().getOpleiding();

		if (opleiding != null)
		{
			return opleiding.getVerbintenisgebied().getTaxonomie().isVO();
		}
		else
		{
			return false;
		}
	}

	private void controleerVeldenVO()
	{
		boolean praktijkopleiding =
			"0090".equals(plaatsing.getVerbintenis().getOpleiding().getVerbintenisgebied()
				.getExterneCode());

		if (praktijkopleiding)
		{
			if (plaatsing.getJarenPraktijkonderwijs() == null)
				addValidationError("jarenPraktijkonderwijs");
		}
		else
		{
			if (plaatsing.getLeerjaar() == null)
				addValidationError("leerjaar");
		}
	}

	private void addValidationError(String msg)
	{
		addError(getDefaultMessage() + " Veld " + msg + " is niet ingevuld.");
	}

	private void addError(String msg)
	{
		errors.add(msg);
	}

	private String getDefaultMessageVerbintenis()
	{
		return getDefaultMessage() + getVerbintenisOmschrijving();
	}

	private String getDefaultMessage()
	{
		return createDeelnemerOmschrijving() + "Fout bij aanmaken " + getValidatorSoort() + ".";
	}

	private String getValidatorSoort()
	{
		if (plaatsing != null)
			return "plaatsing";
		else if (verbintenis != null)
			return "verbintenis";
		else
			return "";
	}

	private String createDeelnemerOmschrijving()
	{
		Deelnemer deelnemer = getDeelnemer();

		String msg =
			"Deelnemer: " + deelnemer.getPersoon().getAchternaam() + ", "
				+ deelnemer.getPersoon().getVoornamen() + " (" + deelnemer.getDeelnemernummer()
				+ "). ";
		return msg;
	}

	private Deelnemer getDeelnemer()
	{
		if (plaatsing != null)
			return plaatsing.getDeelnemer();
		else if (verbintenis != null)
			return verbintenis.getDeelnemer();
		else
			return null;
	}

	private Verbintenis getVerbintenis()
	{
		if (verbintenis != null)
			return verbintenis;
		return plaatsing.getVerbintenis();
	}

}
