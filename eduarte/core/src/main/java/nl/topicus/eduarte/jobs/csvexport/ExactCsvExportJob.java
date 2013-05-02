package nl.topicus.eduarte.jobs.csvexport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.templates.documents.csv.CSVWorkbook;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.Debiteur;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.adres.AdresEntiteit;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.jobs.ExactCsvExportJobRun;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.jobs.logging.RapportageJobRun;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.personen.AbstractRelatie;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.PersoonContactgegeven;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;
import org.quartz.JobExecutionContext;

@JobInfo(name = ExactCsvExportJob.JOB_NAME)
@JobRunClass(ExactCsvExportJobRun.class)
public class ExactCsvExportJob extends EduArteJob
{
	public static final String JOB_NAME = "Exact CSV export";

	@Override
	protected void executeJob(JobExecutionContext context) throws InterruptedException
	{
		ExactCsvExportJobRun run = new ExactCsvExportJobRun();
		run.setGestartDoor(getMedewerker());
		run.setRunStart(getDatumTijdOpgestart());
		run.setSamenvatting("Genereren van csv export gestart");
		run.saveOrUpdate();

		VerbintenisZoekFilter zoekfilter = createZoekFilter(context);
		zoekfilter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			new AlwaysGrantedSecurityCheck()));
		VerbintenisDataAccessHelper helper =
			DataAccessRegistry.getHelper(VerbintenisDataAccessHelper.class);
		List<Long> ids = helper.getIds(zoekfilter);
		CSVWorkbook workbook = createWorkbook();
		Sheet sheet = workbook.createSheet();
		int count = 0;
		int row = 0;
		for (Long id : ids)
		{
			Verbintenis verbintenis = helper.getVerbintenisById(id);
			if (verbintenis != null)
				writeLine(sheet.createRow(row++), verbintenis);
			setProgress(count, ids.size());
			count++;
			if (count % 100 == 0)
			{
				flushAndClearHibernate();
			}
		}
		createRapportageJobRun(run, workbook);

		run.setSamenvatting("Genereren van csv export voltooid");
		run.setRunEinde(TimeUtil.getInstance().currentDateTime());
		run.saveOrUpdate();
		run.commit();
	}

	private CSVWorkbook createWorkbook()
	{
		CSVWorkbook workbook = new CSVWorkbook();
		workbook.setSeparator(';');
		// workbook.setCharset(Charset.defaultCharset());
		workbook.setCharset(Charset.forName("UTF-8"));
		return workbook;
	}

	private void createRapportageJobRun(ExactCsvExportJobRun run, CSVWorkbook workbook)
	{
		RapportageJobRun rapRun = new RapportageJobRun();
		rapRun.setRunStart(run.getRunStart());
		rapRun.setRunEinde(TimeUtil.getInstance().currentDate());
		rapRun.setGestartDoor(run.getGestartDoor());
		rapRun.setGestartDoorAccount(run.getGestartDoor().getAccount());
		rapRun.setSamenvatting("Exact CSV export");
		rapRun.setDocumentType(DocumentTemplateType.CSV);
		rapRun.setBestandsNaam("Exact_CSV_Export.csv");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			// Onderstaande is UTF-8 identifier bytes die ervoor zorgen dat Excel het
			// herkend als een UTF-8 bestand.
			baos.write(new byte[] {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
			workbook.write(baos);
		}
		catch (IOException e)
		{
			log.error(e.getMessage());
		}
		rapRun.setResultaat(baos.toByteArray());
		rapRun.save();

	}

	private void writeLine(Row row, Verbintenis verbintenis)
	{
		Deelnemer deelnemer = verbintenis.getDeelnemer();
		Persoon persoon = deelnemer.getPersoon();

		// 0 OV-nummer Unieke identificatie deelnemer verplicht numeriek
		row.createCell(0).setCellValue(deelnemer.getDeelnemernummer());
		// 1 Debiteurnummer Unieke identificatie in factureringssysteem verplicht numeriek
		if (persoon.getDebiteurennummer() != null)
			row.createCell(1).setCellValue(persoon.getDebiteurennummer());
		// 2 Achternaam Vermelding op factuur verplicht alfanumeriek
		row.createCell(2).setCellValue(persoon.getAchternaam());
		// 3 Voorvoegsels Vermelding op factuur verplicht alfanumeriek
		row.createCell(3).setCellValue(persoon.getVoorvoegsel());
		// 4 Voorletters Vermelding op factuur verplicht alfanumeriek
		row.createCell(4).setCellValue(persoon.getVoorletters());
		// 5 Roepnaam Vermelding op factuur optioneel alfanumeriek
		row.createCell(5).setCellValue(persoon.getRoepnaam());
		// 6 Geboortedatum deelnemer Leeftijdbepaling voor wettelijk verplicht cursusgeld
		// verplicht alfanumerieke datum 8 cijfers (ddmmjjjj)
		row.createCell(6).setCellValue(formatDatum(persoon.getGeboortedatum()));
		// 7 Geslacht deelnemer Vermelding aanhef (mevrouw, meneer) op factuur verplicht
		// numeriek integer getal (1 positie) 1 = Man, 2 = Vrouw en 3 = onbekend
		row.createCell(7).setCellValue(
			persoon.getGeslacht() == null ? 3 : persoon.getGeslacht().getNummer());

		PersoonAdres persAdres = persoon.getPostAdres();
		if (persAdres == null)
			persAdres = persoon.getFysiekAdres();
		if (persAdres != null && persAdres.getAdres() != null)
		{
			Adres adres = persAdres.getAdres();
			// 8 Straatnaam deelnemer Vermelding op factuur verplicht alfanumeriek
			// Postadres
			// indien aanwezig en anders woonadres
			row.createCell(8).setCellValue(adres.getStraat());
			// 9 Huisnummer deelnemer Vermelding op factuur verplicht numeriek Postadres
			// indien aanwezig en anders woonadres
			row.createCell(9).setCellValue(adres.getHuisnummer());
			// 10 Toevoeging huisnummer deelnemer Vermelding op factuur optioneel
			// alfanumeriek
			// Postadres indien aanwezig en anders woonadres
			row.createCell(10).setCellValue(adres.getHuisnummerToevoeging());
			// 11 Postcode deelnemer Vermelding op factuur verplicht alfanumeriek 9999 xx
			// Postadres indien aanwezig en anders woonadres
			row.createCell(11).setCellValue(adres.getPostcodeMetSpatie());
			// 12 Woonplaats deelnemer Vermelding op factuur verplicht alfanumeriek
			// Postadres
			// indien aanwezig en anders woonadres
			row.createCell(12).setCellValue(adres.getPlaats());
			// 13 Landcode deelnemer identificatie binnen systemen verplicht indien anders
			// dan
			// NL alfanumeriek standaard NL
			row.createCell(13).setCellValue(adres.getLand().getIsoCode());
			// 14 Land deelnemer Vermelding op factuur verplicht indien anders dan
			// Nederland
			// alfanumeriek standaard Nederland
			row.createCell(14).setCellValue(adres.getLand().getNaam());
		}
		PersoonContactgegeven tel = persoon.getEersteTelefoon();
		if (tel != null)
		{
			// 15 Telefoonnummer deelnemer Contactgegeven optioneel alfanumeriek Het
			// nummer
			// met kenmerk in KRD 'overal standaard tonen'
			row.createCell(15).setCellValue(tel.getContactgegevenInclGeheim());
		}
		PersoonContactgegeven email = persoon.getEersteEmailAdres();
		if (email != null)
		{
			// 16 E-mail adres deelnemer Contactgegeven optioneel alfanumeriek Het e-mail
			// adres met kenmerk in KRD 'standaard tonen bij personen'
			row.createCell(16).setCellValue(email.getContactgegevenInclGeheim());
		}

		// 17 Crebonummer verbintenis Bepaling productprijs en vermelding op factuur
		// verplicht alfanumeriek in huidige systematiek: het crebonummer /
		// kwalificatiecode
		row.createCell(17).setCellValue(
			verbintenis.getExterneCode() == null ? "" : verbintenis.getExterneCode());

		// 18 Omschrijving opleiding verbintenis Vermelding op factuur verplicht
		// alfanumeriek in huidige systematiek: de naam van de kwalificatie
		if (verbintenis.getOpleiding() != null)
			row.createCell(18).setCellValue(verbintenis.getOpleiding().getNaam());
		// 19 Organisatie eenheid verbintenis Bepaling kostenplaats en vermelding op
		// factuur verplicht alfanumeriek Een code die de organieke eenheid van de interne
		// organisatie aanduidt.
		row.createCell(19).setCellValue(verbintenis.getOrganisatieEenheid().getAfkorting());
		// 20 Locatie verbintenis Bepaling kostenplaats en vermelding op factuur
		// verplicht.
		if (verbintenis.getLocatie() != null)
			row.createCell(20).setCellValue(verbintenis.getLocatie().getAfkorting());
		// 21 Opleidingsniveau verbintenis Niveaubepaling voor wettelijk verplicht
		// cursusgeld en vermelding op factuur verplicht alfanumeriek in huidige
		// systematiek: kwalificatieniveau
		if (verbintenis.getOpleiding() != null
			&& verbintenis.getOpleiding().getVerbintenisgebied().getNiveauNaam() != null)
			row.createCell(21).setCellValue(
				verbintenis.getOpleiding().getVerbintenisgebied().getNiveauNaam());
		// 22 Leerweg verbintenis Leerwegbepaling voor wettelijk verplicht cursusgeld en
		// vermelding op factuur verplicht alfanumeriek BBL of BOL
		if (verbintenis.getOpleiding() != null && verbintenis.getOpleiding().getLeerweg() != null)
			row.createCell(22).setCellValue(verbintenis.getOpleiding().getLeerweg().toString());
		// 23 Intensiteit verbintenis Intensiteitbepaling voor wettelijk verplicht
		// cursusgeld en vermelding op factuur verplicht alfanumeriek Voltijd',
		// 'Deeltijd', 'Examendeelnemer', 'n.v.t.'
		if (verbintenis.getIntensiteit() != null)
			row.createCell(23).setCellValue(verbintenis.getIntensiteit().toString());
		// 24 Status inschrijving Bepalen geldigheid/actualiteit van onderwijsovereenkomst
		// (factureren J/N) verplicht alfanumeriek
		row.createCell(24).setCellValue(
			verbintenis.isActief(TimeUtil.getInstance().currentDate())
				&& verbintenis.getStatus().tussen(VerbintenisStatus.Volledig,
					VerbintenisStatus.Definitief) ? "J" : "N");
		// 25 Datum inschrijving Bepalen moment van facturering en bepalen prijs (indien
		// afhankelijk van datum) verplicht alfanumerieke datum 8 cijfers (ddmmjjjj)
		row.createCell(25).setCellValue(formatDatum(verbintenis.getBegindatum()));
		// 26 Datum uitschrijving Bepalen eventuele restitutie van gefactureerde bedragen
		// optioneel alfanumerieke datum 8 cijfers (ddmmjjjj)
		if (verbintenis.getEinddatum() != null)
			row.createCell(26).setCellValue(formatDatum(verbintenis.getEinddatum()));

		Plaatsing plaatsing = verbintenis.getPlaatsingOpPeildatum();
		if (plaatsing != null)
		{
			// 27 Leer-/verblijfsjaar of cohortaanduiding Bepalen product/prijs en
			// vermelding
			// op factuur verplicht alfanumeriek Kan wellicht ook gerealiseerd worden
			// vanuit
			// groeperingsmechanisme
			if (plaatsing.getLeerjaar() != null)
				row.createCell(27).setCellValue(plaatsing.getLeerjaar());
			// 28 Groep Basisgroep Wellantcollege Groep met plaatsingsgroep "J" en als dat
			// er
			// meer dan alleen de eerste
			Groep basisgroep = null;
			if (verbintenis.getTaxonomie() != null && verbintenis.getTaxonomie().isBO())
			{
				for (Groepsdeelname deelname : deelnemer.getGroepsdeelnamesOpPeildatum())
				{
					if (deelname.getGroep() != null
						&& deelname.getGroep().getGroepstype().getCode().equals("MBO")
						&& deelname.getGroep().getOrganisatieEenheid() != null
						&& verbintenis.getOrganisatieEenheid() != null
						&& deelname.getGroep().getOrganisatieEenheid().equals(
							verbintenis.getOrganisatieEenheid()))
					{
						basisgroep = deelname.getGroep();
						break;
					}
				}
			}
			else if (verbintenis.getTaxonomie() != null && verbintenis.getTaxonomie().isVO())
			{
				for (Groepsdeelname deelname : deelnemer.getGroepsdeelnamesOpPeildatum())
				{
					if (deelname.getGroep() != null
						&& deelname.getGroep().getGroepstype().getCode().equals("BGR")
						&& deelname.getGroep().getOrganisatieEenheid() != null
						&& verbintenis.getOrganisatieEenheid() != null
						&& deelname.getGroep().getOrganisatieEenheid().equals(
							verbintenis.getOrganisatieEenheid()))
					{
						basisgroep = deelname.getGroep();
						break;
					}
				}
			}
			if (basisgroep != null)
				row.createCell(28).setCellValue(basisgroep.getCode());
		}
		// 29 Aantal lesuren per week Bepalen lesgeld VAVO en vermelding op factuur
		// optioneel numeriek integer getal (2 posities)
		row.createCell(29).setCellValue("");
		Debiteur debiteur = persoon.getBetalingsplichtige();
		if (debiteur != null)
		{
			// 30 Debiteurnummer betalingsplichtige Identificatie binnen systemen en
			// vermelding op factuur verplicht numeriek
			if (debiteur.getDebiteurennummer() != null)
				row.createCell(30).setCellValue(debiteur.getDebiteurennummer());
			// 31 Type relatie ouder/verzorger/voogd OF externe organisatie
			AbstractRelatie relatie = persoon.getBetalingsplichtigeRelatie();
			if (relatie != null && relatie.getRelatieSoort() != null)
				row.createCell(31).setCellValue(relatie.getRelatieSoort().getNaam());
			if (debiteur instanceof Persoon)
			{
				Persoon debPers = (Persoon) debiteur;

				// 32 Aanspreeknaam (gebruikte achternaam) ouder/verzorger/voogd
				// Vermelding op
				// factuur bij minderjarige deelnemer optioneel alfanumeriek
				row.createCell(32).setCellValue(debPers.getAchternaam());
				// 33 Voorvoegsel(s) ouder/verzorger/voogd Vermelding op factuur bij
				// minderjarige
				// deelnemer optioneel alfanumeriek
				row.createCell(33).setCellValue(debPers.getVoorvoegsel());
				// 34 Voorletters ouder/verzorger/voogd Vermelding op factuur bij
				// minderjarige
				// deelnemer optioneel alfanumeriek
				row.createCell(34).setCellValue(debPers.getVoorletters());
				// 35 Geslacht ouder/verzorger/voogd Vermelding aanhef (mevrouw, meneer)
				// op
				// factuur optioneel numeriek integer getal (1 positie) (standaard
				// minderjarige
				// deelnemers) 1 = Man, 2 = Vrouw en 3 = onbekend
				row.createCell(35).setCellValue(
					debPers.getGeslacht() == null ? 3 : debPers.getGeslacht().getNummer());
			}
			if (debiteur instanceof ExterneOrganisatie)
			{
				ExterneOrganisatie debExtOrg = (ExterneOrganisatie) debiteur;
				// 36 Naam bedrijf Vermelding op factuur bij minderjarige deelnemer
				// optioneel
				// alfanumeriek
				row.createCell(36).setCellValue(debExtOrg.getNaam());
			}
			AdresEntiteit< ? > debAdres = debiteur.getPostAdres();
			if (debAdres == null)
				debAdres = debiteur.getFysiekAdres();
			if (debAdres != null && debAdres.getAdres() != null)
			{
				Adres adres = debAdres.getAdres();
				// 37 Straatnaam betalingsplichtige Vermelding op factuur verplicht
				// alfanumeriek
				// max. 30 karakters Postadres indien aanwezig en anders woonadres
				row.createCell(37).setCellValue(adres.getStraat());
				// 38 Huisnummer betalingsplichtige Vermelding op factuur verplicht
				// numeriek
				// integer getal Postadres indien aanwezig en anders woonadres
				row.createCell(38).setCellValue(adres.getHuisnummer());
				// 39 Toevoeging huisnummer betalingsplichtige Vermelding op factuur
				// optioneel
				// alfanumeriek max. 8 karakters Postadres indien aanwezig en anders
				// woonadres
				row.createCell(39).setCellValue(adres.getHuisnummerToevoeging());
				// 40 Postcode betalingsplichtige Vermelding op factuur verplicht
				// alfanumeriek
				// max. 8 karakters Postadres indien aanwezig en anders woonadres
				row.createCell(40).setCellValue(adres.getPostcodeMetSpatie());
				// 41 Woonplaats betalingsplichtige Vermelding op factuur verplicht
				// alfanumeriek
				// max. 30 karakters Postadres indien aanwezig en anders woonadres
				row.createCell(41).setCellValue(adres.getPlaats());
				// 42 Landcode betalingsplichtige identificatie binnen systemen verplicht
				// indien
				// anders dan NL alfanumeriek max. 3 karakters standaard NL
				row.createCell(42).setCellValue(adres.getLand().getIsoCode());
				// 43 Land betalingsplichtige Vermelding op factuur verplicht indien
				// anders
				// dan
				// Nederland alfanumeriek max 30 karakters standaard Nederland
				row.createCell(43).setCellValue(adres.getLand().getNaam());
			}
			// 44 Telefoonnummer betalingsplichtige Contactgegeven optioneel alfanumeriek
			// max.
			// 15 karakters (standaard minderjarige deelnemers) Het 1e of algemene
			// telefoonnummer
			if (debiteur.getEersteTelefoon() != null)
				row.createCell(44).setCellValue(debiteur.getEersteTelefoon().getContactgegeven());
			// 45 E-mail adres betalingsplichtige Contactgegeven optioneel alfanumeriek
			// max.
			// 60 karakters (standaard minderjarige deelnemers) Het 1e of algemene e-mail
			// adres
			if (debiteur.getEersteEmailAdres() != null)
				row.createCell(45).setCellValue(debiteur.getEersteEmailAdres().getContactgegeven());
		}

	}

	private String formatDatum(Date datum)
	{
		if (datum == null)
			return null;
		Locale loc = new Locale("nl", "NL");
		DateFormat format = new SimpleDateFormat("ddMMyyyy", loc);
		return format.format(datum);
	}

	private VerbintenisZoekFilter createZoekFilter(JobExecutionContext context)
	{
		VerbintenisZoekFilter filter = new VerbintenisZoekFilter();
		filter.setOrganisatieEenheid((OrganisatieEenheid) context.getMergedJobDataMap().get(
			"organisatieEenheid"));
		filter.setLocatie((Locatie) context.getMergedJobDataMap().get("locatie"));
		filter.setOpleiding((Opleiding) context.getMergedJobDataMap().get("opleiding"));
		filter.setVerbintenisStatusOngelijkAan(Arrays.asList(VerbintenisStatus.Aangemeld,
			VerbintenisStatus.Intake));
		return filter;
	}
}
