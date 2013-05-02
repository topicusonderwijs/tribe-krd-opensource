package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.rapportage;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.participatie.zoekfilters.DeelnemerActiviteitTotalenZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.model.IModel;
import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;

public class DeelnemerActiviteitTotalenRapportageUtil
{
	private DeelnemerActiviteitTotalenZoekFilter filter;

	public DeelnemerActiviteitTotalenRapportageUtil(DeelnemerActiviteitTotalenZoekFilter filter)
	{
		if (filter != null)
			this.filter = filter;
		else
			this.filter = new DeelnemerActiviteitTotalenZoekFilter();

	}

	@SuppressWarnings("unchecked")
	public JasperPrint generateDocuments(List<Verbintenis> verbintenissen, byte[] bestand)
			throws JRException
	{
		List<JRPrintPage> pages = new ArrayList<JRPrintPage>();
		JasperPrint firstPrint = null;
		boolean isFirstPrint = true;

		for (Verbintenis verbintenis : verbintenissen)
		{
			StringWriter writer = new StringWriter(1000);
			Deelnemer deelnemer = verbintenis.getDeelnemer();
			filter.setDeelnemer(deelnemer);
			filter.setOrganisatieEenheid(verbintenis.getOrganisatieEenheid());
			filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
				new AlwaysGrantedSecurityCheck()));

			IModel<List<ActiviteitTotaal>> model =
				new ActiviteitTotaalRapportModel(verbintenis, filter);
			List<ActiviteitTotaal> totalenList = model.getObject();

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("nummer", deelnemer.getDeelnemernummer());
			parameters.put("voornaam", deelnemer.getPersoon().getRoepnaam());
			parameters.put("achternaam", deelnemer.getPersoon().getVoorvoegselAchternaam());
			parameters.put("leeftijd", deelnemer.getPersoon().getLeeftijd());
			parameters.put("totalen", totalenList);
			parameters.put("verbintenis", verbintenis.getOmschrijving());
			parameters.put("rapporttitel", "Onderwijsproduct Totalen");

			ByteArrayInputStream is = new ByteArrayInputStream(bestand);
			JasperDesign design = JRXmlLoader.load(is);
			String export = writer.toString();

			JasperReport report = JasperCompileManager.compileReport(design);
			JRCsvDataSource source = new JRCsvDataSource(new StringReader(export));
			source.setFieldDelimiter(';');
			source.setRecordDelimiter("\r\n");
			source.setUseFirstRowAsHeader(false);
			if (isFirstPrint)
			{
				firstPrint = JasperFillManager.fillReport(report, parameters, source);
				isFirstPrint = false;
			}
			else
			{
				JasperPrint print = JasperFillManager.fillReport(report, parameters, source);
				pages.addAll(print.getPages());
			}
		}

		for (JRPrintPage page : pages)
		{
			if (firstPrint != null)
				firstPrint.addPage(page);
		}
		return firstPrint;
	}
}
