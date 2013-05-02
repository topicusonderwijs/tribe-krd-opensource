package nl.topicus.eduarte.jobs.csvexport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.templates.documents.csv.CSVWorkbook;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.jobs.LvsCsvExportJobRun;
import nl.topicus.eduarte.entities.jobs.logging.RapportageJobRun;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieAdres;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieContactgegeven;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
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

@JobInfo(name = LvsCsvExportJob.JOB_NAME)
@JobRunClass(LvsCsvExportJobRun.class)
public class LvsCsvExportJob extends EduArteJob
{
	public static final String JOB_NAME = "LVS CSV export";

	private int currentRow = 0;

	private int currentCell = 0;

	private Sheet sheet;

	public static enum RecordType
	{
		Deelnemer,
		Vooropleiding,
		Verbintenis,
		BPV;
	}

	@Override
	protected void executeJob(JobExecutionContext context) throws InterruptedException
	{
		LvsCsvExportJobRun run = new LvsCsvExportJobRun();
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
		sheet = workbook.createSheet();
		int count = 0;
		for (Long id : ids)
		{
			Verbintenis verbintenis = helper.getVerbintenisById(id);
			if (verbintenis != null)
				writeToCsv(verbintenis);
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
		workbook.setCharset(Charset.forName("UTF-8"));
		return workbook;
	}

	private void createRapportageJobRun(LvsCsvExportJobRun run, CSVWorkbook workbook)
	{
		RapportageJobRun rapRun = new RapportageJobRun();
		rapRun.setRunStart(run.getRunStart());
		rapRun.setRunEinde(TimeUtil.getInstance().currentDate());
		rapRun.setGestartDoor(run.getGestartDoor());
		rapRun.setGestartDoorAccount(run.getGestartDoor().getAccount());
		rapRun.setSamenvatting("Lvs CSV export");
		rapRun.setDocumentType(DocumentTemplateType.CSV);
		rapRun.setBestandsNaam("Lvs_CSV_Export.csv");
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

	private void writeToCsv(Verbintenis verbintenis)
	{
		Deelnemer deelnemer = verbintenis.getDeelnemer();
		writeDeelnemer(deelnemer);
		for (Vooropleiding vooropleiding : deelnemer.getVooropleidingen())
		{
			writeVooropleiding(vooropleiding);
		}
		for (Verbintenis verb : deelnemer.getVerbintenissen())
		{
			writeVerbintenis(verb);
			for (BPVInschrijving bpv : verb.getBpvInschrijvingen())
			{
				writeBPV(bpv);
			}
		}

	}

	private void writeBPV(BPVInschrijving bpv)
	{
		Row row = getNextRow(RecordType.BPV);
		writeCellValue(row, bpv.getDagenPerWeek());
		writeCellValue(row, bpv.getUrenPerWeek());
		writeCellValue(row, formatDatum(bpv.getBegindatum()));
		writeCellValue(row, formatDatum(bpv.getVerwachteEinddatum()));
		writeCellValue(row, formatDatum(bpv.getEinddatum()));
		writeCellValue(row, bpv.getStatus().toString());
		BPVBedrijfsgegeven bpvBedrijfsgegeven = bpv.getBedrijfsgegeven();
		if (bpvBedrijfsgegeven != null)
		{
			writeCellValue(row, bpvBedrijfsgegeven.getCodeLeerbedrijf());
			writeCellValue(row, bpvBedrijfsgegeven.getRelatienummer());
		}
		else
			currentCell += 2;

		ExterneOrganisatie bpvBedrijf = bpv.getBpvBedrijf();
		if (bpvBedrijf != null)
		{
			writeCellValue(row, bpvBedrijf.getNaam());
			writeCellValue(row, bpvBedrijf.getVerkorteNaam());

			ExterneOrganisatieAdres fysiekAdres = bpvBedrijf.getFysiekAdres();
			if (fysiekAdres != null && !fysiekAdres.getAdres().isGeheim())
				writeAdres(row, fysiekAdres.getAdres());
			else
				currentCell += 8;

			ExterneOrganisatieAdres postAdres = bpvBedrijf.getPostAdres();
			if (postAdres != null && !postAdres.getAdres().isGeheim())
				writeAdres(row, postAdres.getAdres());
			else
				currentCell += 8;

			// write contactgegevens
			StringBuilder contact = new StringBuilder();
			StringBuilder type = new StringBuilder();
			for (ExterneOrganisatieContactgegeven extOrgCont : bpvBedrijf.getContactgegevens())
			{
				contact.append(extOrgCont.getContactgegeven() + ",");
				type.append(extOrgCont.getSoortContactgegeven().getTypeContactgegeven() + ",");
			}
			if (contact.length() > 0)
				contact.deleteCharAt(contact.lastIndexOf(","));
			if (type.length() > 0)
				type.deleteCharAt(type.lastIndexOf(","));
			writeCellValue(row, contact.toString());
			writeCellValue(row, type.toString());

		}
		currentCell += 20;

	}

	private void writeVerbintenis(Verbintenis verbintenis)
	{
		Row row = getNextRow(RecordType.Verbintenis);
		writeCellValue(row, verbintenis.getStatus().toString());
		if (verbintenis.getIntensiteit() != null)
			writeCellValue(row, verbintenis.getIntensiteit().toString());
		else
			currentCell++;
		writeCellValue(row, verbintenis.getExterneCode());
		Opleiding opleiding = verbintenis.getOpleiding();
		if (opleiding != null)
		{
			writeCellValue(row, opleiding.getVerbintenisgebied().getNaamKenniscentrum());
			writeCellValue(row, opleiding.getVerbintenisgebied().getSectornamen());
			writeCellValue(row, opleiding.getNaam());
			writeCellValue(row, opleiding.getCode());
		}
		else
			currentCell += 4;
		if (verbintenis.getCohort() != null)
			writeCellValue(row, verbintenis.getCohort().getNaam());
		else
			currentCell++;
		if (opleiding != null)
		{
			writeCellValue(row, verbintenis.getOpleiding().getVerbintenisgebied().getNiveauNaam());
			writeCellValue(row, verbintenis.getOpleiding().getLeerweg() == null ? "" : verbintenis
				.getOpleiding().getLeerweg().toString());
		}
		else
			currentCell += 2;

		if (verbintenis.getIntensiteit() != null)
			writeCellValue(row, verbintenis.getIntensiteit().toString());
		else
			currentCell++;
		writeCellValue(row, verbintenis.getOrganisatieEenheid().getAfkorting());
		writeCellValue(row, verbintenis.getOrganisatieEenheid().getNaam());
		writeCellValue(row, verbintenis.getOrganisatieEenheid().getOfficieleNaam());
		writeCellValue(row, verbintenis.getLocatie() == null ? "" : verbintenis.getLocatie()
			.getAfkorting());
		writeCellValue(row, verbintenis.getLocatie() == null ? "" : verbintenis.getLocatie()
			.getNaam());
		writeCellValue(row, formatDatum(verbintenis.getBegindatum()));
		writeCellValue(row, formatDatum(verbintenis.getEinddatum()));

	}

	private void writeVooropleiding(Vooropleiding vooropleiding)
	{
		Row row = getNextRow(RecordType.Vooropleiding);
		writeCellValue(row, vooropleiding.getNaam());
		if (vooropleiding.getSoortVooropleiding() != null)
			writeCellValue(row, vooropleiding.getSoortVooropleiding().getNaam());
		else
			currentCell++;
		writeCellValue(row, vooropleiding.getPlaats());
		if (vooropleiding.getSchooladvies() != null)
			writeCellValue(row, vooropleiding.getSchooladvies().getNaam());
		else
			currentCell++;
		writeCellValue(row, vooropleiding.getCitoscore());
		writeCellValue(row, vooropleiding.isDiplomaBehaald());
	}

	private void writeDeelnemer(Deelnemer deelnemer)
	{
		Persoon persoon = deelnemer.getPersoon();
		Row row = getNextRow(RecordType.Deelnemer);
		writeCellValue(row, persoon.getAchternaam());
		writeCellValue(row, persoon.getVoorvoegsel());
		writeCellValue(row, persoon.getOfficieleAchternaam());
		writeCellValue(row, persoon.getOfficieleVoorvoegsel());
		writeCellValue(row, persoon.getVoorletters());
		writeCellValue(row, persoon.getRoepnaam());
		writeCellValue(row, formatDatum(persoon.getGeboortedatum()));

		PersoonAdres woonadres = persoon.getFysiekAdres();
		if (woonadres != null && !woonadres.getAdres().isGeheim())
			writeAdres(row, woonadres.getAdres());
		else
			currentCell += 8;

		PersoonAdres postAdres = persoon.getPostAdres();
		if (postAdres != null && !postAdres.getAdres().isGeheim())
			writeAdres(row, postAdres.getAdres());
		else
			currentCell += 8;

		writeCellValue(row, deelnemer.getDeelnemernummer());
		writeCellValue(row, String.valueOf(persoon.getGeslachtEersteLetter()));
		writeCellValue(row, persoon.getBsn());
		writeCellValue(row, deelnemer.getOnderwijsnummer());
		writeCellValue(row, persoon.getVoornamen());
		writeCellValue(row, persoon.getGeboorteplaats());
		if (persoon.getGeboorteGemeente() != null)
			writeCellValue(row, persoon.getGeboorteGemeente().getNaam());
		else
			currentCell++;

		if (persoon.getGeboorteland() != null)
			writeCellValue(row, persoon.getGeboorteland().getNaam());
		else
			currentCell++;

		// write contactgegevens
		StringBuilder contact = new StringBuilder();
		StringBuilder type = new StringBuilder();
		StringBuilder soort = new StringBuilder();
		for (PersoonContactgegeven persCont : persoon.getContactgegevens())
		{
			contact.append(persCont.getContactgegeven() + ",");
			type.append(persCont.getSoortContactgegeven().getTypeContactgegeven() + ",");
			soort.append(persCont.getSoortContactgegeven().getCode() + ",");
		}
		if (contact.length() > 0)
			contact.deleteCharAt(contact.lastIndexOf(","));
		if (type.length() > 0)
			type.deleteCharAt(type.lastIndexOf(","));
		if (soort.length() > 0)
			soort.deleteCharAt(soort.lastIndexOf(","));
		writeCellValue(row, contact.toString());
		writeCellValue(row, type.toString());
		writeCellValue(row, soort.toString());

		// write groepsdeelnames
		StringBuilder plaatsingsGroepen = new StringBuilder();
		StringBuilder alleGroepen = new StringBuilder();
		StringBuilder typeGroepen = new StringBuilder();
		for (Groepsdeelname deelame : deelnemer.getGroepsdeelnamesOpPeildatum())
		{
			if (deelame.getGroep() != null)
			{
				if (deelame.getGroep().getGroepstype().isPlaatsingsgroep())
					plaatsingsGroepen.append(deelame.getGroep().getCode() + ",");
				alleGroepen.append(deelame.getGroep().getCode() + ",");
				typeGroepen.append(deelame.getGroep().getGroepstype().getCode() + ",");
			}
		}
		if (plaatsingsGroepen.length() > 0)
			plaatsingsGroepen.deleteCharAt(plaatsingsGroepen.lastIndexOf(","));
		if (alleGroepen.length() > 0)
			alleGroepen.deleteCharAt(alleGroepen.lastIndexOf(","));
		if (typeGroepen.length() > 0)
			typeGroepen.deleteCharAt(typeGroepen.lastIndexOf(","));
		writeCellValue(row, plaatsingsGroepen.toString());
		writeCellValue(row, alleGroepen.toString());
		writeCellValue(row, typeGroepen.toString());

	}

	private void writeAdres(Row row, Adres adres)
	{
		writeCellValue(row, adres.getStraat());
		writeCellValue(row, adres.getHuisnummer());
		writeCellValue(row, adres.getPostcode());
		writeCellValue(row, adres.getHuisnummerToevoeging());
		writeCellValue(row, adres.getPlaats());
		writeCellValue(row, adres.getLand().getCode());
		writeCellValue(row, adres.getLand().getNaam());
		writeCellValue(row, adres.getGeheimOmschrijving());
	}

	private void writeCellValue(Row row, boolean arg0)
	{
		row.createCell(currentCell).setCellValue(arg0);
		currentCell++;
	}

	private void writeCellValue(Row row, Integer arg0)
	{
		if (arg0 != null)
			row.createCell(currentCell).setCellValue(arg0);
		currentCell++;
	}

	private void writeCellValue(Row row, Long arg0)
	{
		if (arg0 != null)
			row.createCell(currentCell).setCellValue(arg0);
		currentCell++;
	}

	private void writeCellValue(Row row, BigDecimal arg0)
	{
		if (arg0 != null)
			row.createCell(currentCell).setCellValue(arg0.doubleValue());
		currentCell++;
	}

	private void writeCellValue(Row row, String string)
	{
		if (string != null)
			row.createCell(currentCell).setCellValue(string);
		currentCell++;
	}

	private Row getNextRow(RecordType type)
	{
		Row row = sheet.createRow(currentRow++);
		row.createCell(0).setCellValue(type.name());
		currentCell = 1;
		return row;
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
		return filter;
	}
}
