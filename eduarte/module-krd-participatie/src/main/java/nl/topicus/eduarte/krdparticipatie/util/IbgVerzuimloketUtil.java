package nl.topicus.eduarte.krdparticipatie.util;

import java.math.BigInteger;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import nl.ib_groep.vsv.webservice_v2.AanvraagGegevensResponseElement;
import nl.ib_groep.vsv.webservice_v2.AanvraagGewijzigdeMeldingRelatiefVerzuimResponseElement;
import nl.ib_groep.vsv.webservice_v2.MeldenRelatiefVerzuimRequestElement;
import nl.ib_groep.vsv.webservice_v2.MeldenRelatiefVerzuimResponseElement;
import nl.ib_groep.vsv.webservice_v2.MutatieMeldingRelatiefVerzuimResponseElement;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.NummerGeneratorDataAccessHelper;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.ibgverzuimloket.IbgVerzuimdag;
import nl.topicus.eduarte.entities.ibgverzuimloket.IbgVerzuimmelding;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Gemeente;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.PersoonContactgegeven;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.onderwijs.ibgverzuimloket.logic.AbstractIbgVerzuimModule;
import nl.topicus.onderwijs.ibgverzuimloket.logic.IbgClientFactory;
import nl.topicus.onderwijs.ibgverzuimloket.model.IbgEnums;
import ocw.gegevenswoordenboek.*;
import ocw.vsv.bevestiging.DC0001Bevestiging;
import ocw.vsv.leveringnummermeldingrelatiefverzuim.DC0018LeveringNummerMeldingRelatiefVerzuim;
import ocw.vsv.leveringverzuim.DC0011LeveringMeldingRelatiefVerzuim;
import ocw.vsv.leveringverzuim.Verzuimgeval;

public class IbgVerzuimloketUtil extends AbstractIbgVerzuimModule<IbgVerzuimmelding, IbgVerzuimdag>
{

	public IbgVerzuimloketUtil()
	{
		setIbgClientFactory(new IbgClientFactory());
	}

	public static void generateNextMeldingsnummer(IbgVerzuimmelding melding)
	{
		NummerGeneratorDataAccessHelper generator =
			DataAccessRegistry.getHelper(NummerGeneratorDataAccessHelper.class);
		melding.setMeldingsnummer(BigInteger.valueOf(generator.newVerzuimmeldingsnummer()));
	}

	public static void setDefaultStatus(IbgVerzuimmelding melding)
	{
		melding.setStatus(IbgEnums.StatusMeldingRelatiefVerzuim.W);
	}

	@Override
	protected AdresType createAfwijkendAdres(IbgVerzuimmelding melding)
	{
		PersoonAdres adres = melding.getVerbintenis().getDeelnemer().getPersoon().getFysiekAdres();
		if (adres != null)
		{
			Adres eindelijkhetadresgevonden = adres.getAdres();
			AdresType adrestype = new AdresType();
			BinnenlandsAdresType binnAdres = new BinnenlandsAdresType();
			binnAdres.setStraatnaam(eindelijkhetadresgevonden.getStraat());
			binnAdres.setHuisnummer(eindelijkhetadresgevonden.getHuisnummer());
			binnAdres.setHuisnummerToevoeging(eindelijkhetadresgevonden.getHuisnummerToevoeging());
			binnAdres.setPlaatsnaam(eindelijkhetadresgevonden.getPlaats());
			binnAdres.setPostcode(eindelijkhetadresgevonden.getPostcode());
			if (melding.getLocatie() != null)
			{
				binnAdres.setAanduidingLocatie(melding.getLocatie().getNaam());
			}
			adrestype.setBinnenlandsAdres(binnAdres);
			return adrestype;
		}
		return null;
	}

	@Override
	protected OnderwijsdeelnameType createOnderwijsdeelname(IbgVerzuimmelding melding)
	{
		Verbintenis verbintenis = melding.getVerbintenis();
		verbintenis.getLeerprofiel();
		OnderwijsdeelnameType deelname = new OnderwijsdeelnameType();

		if (verbintenis.getPlaatsingOpPeildatum() != null
			&& verbintenis.getPlaatsingOpPeildatum().getLeerjaar() != null)
		{
			deelname.setLeerjaarVO(BigInteger.valueOf((verbintenis.getPlaatsingOpPeildatum()
				.getLeerjaar())));
		}
		if (verbintenis.getOpleiding().getVerbintenisgebied() != null)
		{
			deelname.setNiveauOpleiding(convertNiveauForXml(verbintenis.getOpleiding()
				.getVerbintenisgebied().getNiveauNaam()));
		}

		String naamopleiding = verbintenis.getOpleiding().getNaam();
		if (naamopleiding.length() > 40)
		{
			naamopleiding = naamopleiding.substring(0, 40);
		}
		deelname.setNaamOpleiding(naamopleiding);

		deelname.setLeerweg(mapLeerweg(verbintenis.getOpleiding().getLeerweg()));

		if (verbintenis.getTaxonomie().isVO())
		{
			deelname.setSoortOpleiding(IbgEnums.SoortOpleiding.VO.name());
		}
		else
		{
			deelname.setSoortOpleiding(IbgEnums.SoortOpleiding.BVE.name());
		}
		if (verbintenis.getIntensiteit() != null)
		{
			deelname.setVormOpleiding(convertVormForXml(verbintenis.getIntensiteit().name()));
		}
		return deelname;
	}

	private String convertVormForXml(String name)
	{
		String vorm = null;
		if (name == IbgEnums.VormOpleiding.VT.toString())
		{
			vorm = IbgEnums.VormOpleiding.VT.name();
		}
		else if (name == IbgEnums.VormOpleiding.DT.toString())
		{
			vorm = IbgEnums.VormOpleiding.DT.name();
		}
		return vorm;
	}

	private String convertNiveauForXml(String niveauNaam)
	{
		String niveau = null;
		if (niveauNaam == "Niveau 1")
		{
			niveau = IbgEnums.NiveauOpleiding.niveau1.toString();
		}
		else if (niveauNaam == "Niveau 2")
		{
			niveau = IbgEnums.NiveauOpleiding.niveau2.toString();
		}
		else if (niveauNaam == "Niveau 3")
		{
			niveau = IbgEnums.NiveauOpleiding.niveau3.toString();
		}
		else if (niveauNaam == "Niveau 4")
		{
			niveau = IbgEnums.NiveauOpleiding.niveau4.toString();
		}
		return niveau;
	}

	@Override
	protected OnderwijslocatieType createOnderwijslocatie(IbgVerzuimmelding melding)
	{
		Brin brin = melding.getVerbintenis().getInstellingBrincode();
		OnderwijslocatieType onderwijslocatie = new OnderwijslocatieType();
		onderwijslocatie.setOnderwijslocatienaam(brin.getNaam());
		return onderwijslocatie;
	}

	@Override
	protected String getOntvangendeInstantie(IbgVerzuimmelding melding) // throws
																		// IbgVerzuimException
	{
		// FIXME ontvangende instantie overschreven voor testdoeleinden, uncomment voor
		// gewenste functionaliteit
		return "935";
		/*
		 * PersoonAdres persoonAdres =
		 * melding.getVerbintenis().getDeelnemer().getPersoon().getFysiekAdres(); if
		 * (persoonAdres != null) { Gemeente gemeente =
		 * persoonAdres.getAdres().getGemeente(); if (gemeente != null) { return
		 * gemeente.getCode(); } } throw new
		 * IbgVerzuimException("Geen woonplaats gevonden bij deelnemer");
		 */
	}

	@Override
	protected String getVerzendendeInstantie(IbgVerzuimmelding melding)
	{
		Brin brin = melding.getVerbintenis().getInstellingBrincode();
		// TODO BRIN is waarschijnlijk niet hetzelfde als identificatiecode
		return brin.getCode();
	}

	@Override
	protected IdentificatieOnderwijsinstellingType createIdentificatieOnderwijsInstelling(
			IbgVerzuimmelding melding)
	{
		Brin brin = melding.getVerbintenis().getInstellingBrincode();
		IdentificatieOnderwijsinstellingType idOnderwijsinstelling =
			new IdentificatieOnderwijsinstellingType();
		idOnderwijsinstelling.setBRIN(brin.getCode());
		// FIXME BRIN overschreven voor testdoeleinden
		idOnderwijsinstelling.setBRIN("25PL");
		return idOnderwijsinstelling;
	}

	@Override
	protected String getIdentificatiecodeBedrijfsdocument(IbgVerzuimmelding melding)
	{
		return melding.getMeldingsnummer().toString();
	}

	@Override
	protected List<String> handleAanvraagGegevensOnderwijsOntvangendeResponse(
			AanvraagGegevensResponseElement response)
	{
		if (response.getDC0002Foutmelding() != null)
		{
			return handleFoutmelding(response.getDC0002Foutmelding());
		}

		return Collections.emptyList();
	}

	@Override
	protected TelefoonnummerType createTelefoonnummerOnderwijsontvanger(IbgVerzuimmelding melding)
	{
		PersoonContactgegeven gegeven =
			melding.getVerbintenis().getDeelnemer().getPersoon().getEersteTelefoon();
		if (gegeven != null && !gegeven.isGeheim())
		{
			String compleetNummer = gegeven.getContactgegeven();
			String[] nummersplit = compleetNummer.split("-");
			TelefoonnummerType nummer = new TelefoonnummerType();
			nummer.setNetnummer(nummersplit[0]);
			nummer.setAbonneenummer(nummersplit[1]);
			return nummer;
		}
		return null;
	}

	@Override
	protected IdentificatieType createIdentificatieOnderwijsOntvanger(IbgVerzuimmelding melding)
	// throws IbgVerzuimException
	{
		// Deelnemer deelnemer = melding.getVerbintenis().getDeelnemer();
		IdentificatieType onderwijsontvanger = new IdentificatieType();
		// FIXME voor testen bsn overschreven
		onderwijsontvanger.setBSN("208782552");
		// if (deelnemer.getPersoon().getBsn() != null)
		// {
		//			
		// onderwijsontvanger.setBSN(Long.toString(deelnemer.getPersoon().getBsn()));
		//			
		// }
		// else if (deelnemer.getOnderwijsnummer() != null)
		// {
		// onderwijsontvanger.setOnderwijsnummer(Long.toString(deelnemer.getOnderwijsnummer()));
		// /
		// onderwijsontvanger.setOnderwijsnummer(null);
		// }
		// else
		// {
		// throw new IbgVerzuimException(
		// "Leerling heeft geen BSN en geen onderwijsnummer, minimaal één van deze velden is verplicht");
		// }

		return onderwijsontvanger;
	}

	private String mapLeerweg(MBOLeerweg leerweg)
	{
		if (leerweg == null)
		{
			return null;
		}
		return leerweg.name();
	}

	@Override
	protected List<String> handleRelatiefVerzuimResponse(IbgVerzuimmelding melding,
			MeldenRelatiefVerzuimRequestElement request,
			MeldenRelatiefVerzuimResponseElement response)
	{
		if (response.getDC0002Foutmelding() != null)
		{
			return handleFoutmelding(response.getDC0002Foutmelding());
		}
		DC0018LeveringNummerMeldingRelatiefVerzuim levering =
			response.getDC0018LeveringNummerMeldingRelatiefVerzuim();
		melding.setVerzonden(true);
		melding.setStatus(IbgEnums.StatusMeldingRelatiefVerzuim.R);
		// laatste mutatiedatum moet worden opgevraagd adhv DC0010
		melding.setMeldingsnummer(levering.getIdentificatieMeldingRelatiefVerzuim()
			.getNummerMeldingRelatiefVerzuim());
		return Collections.emptyList();
	}

	@Override
	protected List<String> handleAanvraagGewijzigdeMeldingRelatiefVerzuimResponseElement(
			IbgVerzuimmelding melding,
			AanvraagGewijzigdeMeldingRelatiefVerzuimResponseElement response)
	{
		if (response.getDC0002Foutmelding() != null)
		{
			return handleFoutmelding(response.getDC0002Foutmelding());
		}
		DC0011LeveringMeldingRelatiefVerzuim levering =
			response.getDC0011LeveringMeldingRelatiefVerzuim();
		Verzuimgeval verzuimgevalMelding = null;
		for (Verzuimgeval verzuimgeval : levering.getVerzuimgeval())
		{
			if (verzuimgeval.getMeldingRelatiefVerzuim().getIdentificatieMeldingRelatiefVerzuim()
				.getNummerMeldingRelatiefVerzuim().equals(melding.getMeldingsnummer()))
			{
				verzuimgevalMelding = verzuimgeval;
			}
			if (verzuimgevalMelding != null)
			{
				melding.setLaatsteMutatieDatum(verzuimgevalMelding.getProcesverloop()
					.getDatumLaatsteMutatie());
				SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
				melding.setLaatsteMutatieTijd(formatter.parse(verzuimgevalMelding
					.getProcesverloop().getTijdstipLaatsteMutatie(), new ParsePosition(0)));

				melding.setStatus(IbgEnums.StatusMeldingRelatiefVerzuim.valueOf(verzuimgevalMelding
					.getMeldingRelatiefVerzuim().getStatusMeldingRelatiefVerzuim()));
				// verzuimgevalMelding.getMeldingRelatiefVerzuim().getRedenMeldingAfgesloten();

			}
			else
			{
				// TODO: melding is niet gevonden, nog een aanvraag doen?
				// gebruik dan volgende selectieperiode
				// Date nieuweSelectieDatum =
				// levering.getSelectieperiode().getDatumBeginSelectieperiode();
				// String nieuweSelectieTijd =
				// levering.getSelectieperiode().getTijdstipBeginSelectieperiode();

			}
		}
		return Collections.emptyList();
	}

	@Override
	protected List<String> handleMutatieMeldingRelatiefVerzuimResponseElement(
			IbgVerzuimmelding melding, MutatieMeldingRelatiefVerzuimResponseElement response)
	{
		if (response.getDC0002Foutmelding() != null)
		{
			return handleFoutmelding(response.getDC0002Foutmelding());
		}

		DC0001Bevestiging levering = response.getDC0001Bevestiging();
		melding.setLaatsteMutatieDatum(levering.getDatumBedrijfsdocument());
		melding.setLaatsteMutatieTijd(levering.getDatumBedrijfsdocument());

		return Collections.emptyList();
	}

	@Override
	protected Date createHuidigeDatum()
	{
		Date datetime = TimeUtil.getInstance().currentDateTime();
		// Date datetime = TimeUtil.getInstance().currentDate();
		// datetime = TimeUtil.getInstance().setTimeOnDate(datetime,
		// Time.getHuidigeTijd());
		return datetime;
	}

	@Override
	protected IdentificatieUitvoerderVSVType createIdentificatieAanvragendeUitvoerderVsv(
			IbgVerzuimmelding melding)
	{
		IdentificatieUitvoerderVSVType uitvoerder = new IdentificatieUitvoerderVSVType();
		// FIXME onduidelijk welke code gebruikt dient te worden, tijdelijk fix...

		PersoonAdres persoonAdres =
			melding.getVerbintenis().getDeelnemer().getPersoon().getFysiekAdres();
		if (persoonAdres != null)
		{
			Gemeente gemeente = persoonAdres.getAdres().getGemeente();
			if (gemeente != null)
			{
				uitvoerder.setCodeUitvoerderVSV("00" + gemeente.getCode());
				return uitvoerder;
			}
		}
		return null;

	}

}
