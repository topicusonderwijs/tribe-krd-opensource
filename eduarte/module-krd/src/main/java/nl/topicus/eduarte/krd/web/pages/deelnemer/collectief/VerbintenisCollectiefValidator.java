package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.web.validators.BronValidator;
import nl.topicus.eduarte.providers.VerbintenisProvider;

import org.apache.wicket.validation.IErrorMessageSource;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.Validatable;

/*
 * Validator die checkt of alle verplichte velden voor de gewenste eindstatus zijn ingevuld. 
 * Voor het wijzigingen van de status van 1 verbintenis wordt het invullen van de gegevens 
 * normaal gesproken via de interface afgedwongen.  
 * 
 * @author idserda
 */
public class VerbintenisCollectiefValidator
{
	private List<String> errors = new ArrayList<String>();

	private Verbintenis verbintenis;

	private VerbintenisStatus beginstatus;

	private VerbintenisStatus eindstatus;

	public VerbintenisCollectiefValidator(Verbintenis verbintenis, VerbintenisStatus beginstatus,
			VerbintenisStatus eindstatus)
	{
		this.verbintenis = verbintenis;
		this.beginstatus = beginstatus;
		this.eindstatus = eindstatus;
	}

	public VerbintenisCollectiefValidator(Verbintenis verbintenis, VerbintenisStatus beginstatus)
	{
		// Constructor om deze validator te kunnen gebruiken als er geen statusovergang
		// is.
		this.verbintenis = verbintenis;
		this.beginstatus = beginstatus;
		this.eindstatus = beginstatus;
	}

	public List<String> validate()
	{
		Asserts.assertNotNull("verbintenis", verbintenis);
		Asserts.assertNotNull("beginstatus", beginstatus);
		Asserts.assertNotNull("eindstatus", eindstatus);

		if (VerbintenisStatus.Voorlopig.equals(eindstatus))
			checkVerplichteVeldenEindstatusVoorlopig();
		else if (VerbintenisStatus.Volledig.equals(eindstatus))
			checkVerplichteVeldenEindstatusVolledig();

		checkStatusovergangBron();

		return errors;
	}

	private void checkVerplichteVeldenEindstatusVoorlopig()
	{
		checkVerplichteVeldenEindstatusVoorlopigAlgemeen();

		if (verbintenis.getTaxonomie() != null)
		{
			if (verbintenis.getTaxonomie().isVO() && verbintenis.isVOVerbintenis())
				checkVerplichteVeldenEindstatusVoorlopigVO();
			else if (verbintenis.getTaxonomie().isVO() && verbintenis.isVAVOVerbintenis())
				checkVerplichteVeldenEindstatusVoorlopigVAVO();
			else if (verbintenis.isEducatieVerbintenis())
				checkVerplichteVeldenEindstatusVoorlopigED();
			else if (verbintenis.isBOVerbintenis())
				checkVerplichteVeldenEindstatusVoorlopigBO();
			else if (verbintenis.isInburgeringVerbintenis())
				checkVerplichteVeldenEindstatusVoorlopigInburgering();
		}
	}

	private void checkVerplichteVeldenEindstatusVoorlopigAlgemeen()
	{
		if (verbintenis.getBegindatum() == null)
			addDefaultValidationError("einddatum");
		if (verbintenis.getOrganisatieEenheid() == null)
			addDefaultValidationError("Organisatie-eenheid");
	}

	private void checkVerplichteVeldenEindstatusVoorlopigVO()
	{
	}

	private void checkVerplichteVeldenEindstatusVoorlopigVAVO()
	{
		if (verbintenis.getContacturenPerWeek() == null)
			addDefaultValidationError("contacturen per week");
	}

	private void checkVerplichteVeldenEindstatusVoorlopigED()
	{
		if (verbintenis.getContacturenPerWeek() == null)
			addDefaultValidationError("contacturen per week");
	}

	private void checkVerplichteVeldenEindstatusVoorlopigBO()
	{
		// Zou eigenlijk niet moeten kunnen, maar toch maar even checken.
		if (verbintenis.getBekostigd() == null)
			addDefaultValidationError("bekostigd");
	}

	private void checkVerplichteVeldenEindstatusVoorlopigInburgering()
	{
		if (verbintenis.getRedenInburgering() == null)
			addDefaultValidationError("reden inburgering");
		if (verbintenis.getProfielInburgering() == null)
			addDefaultValidationError("profiel inburgering");
		if (verbintenis.getLeerprofiel() == null)
			addDefaultValidationError("leerprofiel");
		if (verbintenis.getDeelcursus() == null)
			addDefaultValidationError("deelcursus");
		if (verbintenis.getSoortPraktijkexamen() == null)
			addDefaultValidationError("soort praktijkexamen");
	}

	private void checkVerplichteVeldenEindstatusVolledig()
	{
		checkVerplichteVeldenEindstatusVolledigAlgemeen();

		if (verbintenis.getTaxonomie() != null)
		{
			if (verbintenis.getTaxonomie().isVO() && verbintenis.isVOVerbintenis())
				checkVerplichteVeldenEindstatusVolledigVO();
			else if (verbintenis.getTaxonomie().isVO() && verbintenis.isVAVOVerbintenis())
				checkVerplichteVeldenEindstatusVolledigVAVO();
			else if (verbintenis.isEducatieVerbintenis())
				checkVerplichteVeldenEindstatusVolledigED();
			else if (verbintenis.isBOVerbintenis())
				checkVerplichteVeldenEindstatusVolledigBO();
			else if (verbintenis.isInburgeringVerbintenis())
				checkVerplichteVeldenEindstatusVolledigInburgering();
		}
	}

	private void checkVerplichteVeldenEindstatusVolledigAlgemeen()
	{
		if (verbintenis.getOpleiding() == null)
			addDefaultValidationError("opleiding");
		if (verbintenis.getLocatie() == null)
			addDefaultValidationError("locatie");
	}

	private void checkVerplichteVeldenEindstatusVolledigVO()
	{
		// Relevante vooropleiding is verplicht maar wordt door BRON validator gecheckt.
	}

	private void checkVerplichteVeldenEindstatusVolledigVAVO()
	{
		// Relevante vooropleiding is verplicht maar wordt door BRON validator gecheckt.
		if (verbintenis.getGeplandeEinddatum() == null)
			addDefaultValidationError("geplande einddatum");
	}

	private void checkVerplichteVeldenEindstatusVolledigED()
	{
		// Relevante vooropleiding is verplicht maar wordt door BRON validator gecheckt.
		if (verbintenis.getGeplandeEinddatum() == null)
			addDefaultValidationError("geplande einddatum");
	}

	private void checkVerplichteVeldenEindstatusVolledigBO()
	{
		// Relevante vooropleiding is verplicht maar wordt door BRON validator gecheckt.
		if (verbintenis.getGeplandeEinddatum() == null)
			addDefaultValidationError("geplande einddatum");
		if (verbintenis.getIntensiteit() == null)
			addDefaultValidationError("intensiteit");
	}

	private void checkVerplichteVeldenEindstatusVolledigInburgering()
	{
		// Geen extra verplichte velden ten opzichte van vorige status.
	}

	private void checkStatusovergangBron()
	{
		if (eindstatus.isBronCommuniceerbaar())
		{
			verbintenis.setStatus(eindstatus);

			final BronValidator<Void> validator = new BronValidator<Void>(new VerbintenisProvider()
			{
				@Override
				public Verbintenis getVerbintenis()
				{
					return null;
				}
			});

			validator.onValidate(new Validatable<Void>()
			{
				@Override
				public void error(IValidationError error)
				{
					addValidationError(error.getErrorMessage(new IErrorMessageSource()
					{
						@Override
						public String getMessage(String key)
						{
							// TODO: Hier via locale de 'key' als foutmelding gebruiken.
							String msg = getDefaultMessage() + " (" + key + ")";
							return msg;
						}

						@SuppressWarnings("unchecked")
						@Override
						public String substitute(String string, Map vars)
								throws IllegalStateException
						{
							return string;
						}
					}));
				}
			}, verbintenis);

			verbintenis.setStatus(beginstatus);
		}
	}

	private void addDefaultValidationError(String msg)
	{
		addValidationError(getDefaultMessage() + ". Veld " + msg + " is niet ingevuld.");
	}

	private void addValidationError(String msg)
	{
		errors.add(msg);
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
