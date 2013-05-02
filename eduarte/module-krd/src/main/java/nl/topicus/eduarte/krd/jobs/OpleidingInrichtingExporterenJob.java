package nl.topicus.eduarte.krd.jobs;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.CriteriumDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.OpleidingDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ProductregelDataAccessHelper;
import nl.topicus.eduarte.entities.criteriumbank.Criterium;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.productregel.ToegestaanOnderwijsproduct;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krd.entities.OpleidingInrichtingExporterenJobRun;
import nl.topicus.eduarte.xml.JAXBContextFactory;
import nl.topicus.eduarte.xml.onderwijscatalogus.v10.OpleidingInrichtingExport;
import nl.topicus.eduarte.xml.onderwijscatalogus.v10.XCriterium;
import nl.topicus.eduarte.xml.onderwijscatalogus.v10.XOpleidingInrichting;
import nl.topicus.eduarte.xml.onderwijscatalogus.v10.XProductregel;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@JobInfo(name = OpleidingInrichtingExporterenJob.JOB_NAME)
@JobRunClass(OpleidingInrichtingExporterenJobRun.class)
public class OpleidingInrichtingExporterenJob extends EduArteJob
{
	public static final String JOB_NAME = "Opleidingsinrichting exporteren";

	private int exportCount = 0;

	@Override
	protected void executeJob(JobExecutionContext context) throws JobExecutionException,
			InterruptedException
	{
		OpleidingInrichtingExporterenJobRun run = new OpleidingInrichtingExporterenJobRun();
		run.setGestartDoor(getMedewerker());
		run.setRunStart(TimeUtil.getInstance().currentDateTime());
		run.setSamenvatting("Opleidingsinrichting exporteren");
		run.save();

		Set<Opleiding> opleidingen = getValue(context, "opleidingen");
		List<Long> opleidingIds = new ArrayList<Long>();
		for (Opleiding curOpleiding : opleidingen)
			opleidingIds.add(curOpleiding.getId());

		run.setContents(exportOpleidingen(opleidingIds));

		run.setRunEinde(TimeUtil.getInstance().currentDateTime());
		run.setSamenvatting(run.getSamenvatting() + ": " + exportCount + " geëxporteerd.");
		run.update();
		run.commit();
	}

	private byte[] exportOpleidingen(List<Long> opleidingIds) throws InterruptedException,
			JobExecutionException
	{
		OpleidingDataAccessHelper opleidingHelper =
			DataAccessRegistry.getHelper(OpleidingDataAccessHelper.class);
		int count = 0;
		OpleidingInrichtingExport export = new OpleidingInrichtingExport();
		for (Long curOpleidingId : opleidingIds)
		{
			Opleiding curOpleiding = opleidingHelper.get(Opleiding.class, curOpleidingId);
			setProgress(count, opleidingIds.size());
			setStatus("Inrichting exporteren voor " + curOpleiding.getCode());
			XOpleidingInrichting inrichting = exportOpleiding(curOpleiding);
			if (!inrichting.getCriteria().isEmpty() || !inrichting.getProductregels().isEmpty())
			{
				export.getOpleidingInrichtingen().add(inrichting);
				exportCount++;
			}
			count = flushAndClearHibernateAndIncCount(count);
		}

		setStatus("Genereren van exportbestand");
		StringWriter output = new StringWriter();
		try
		{
			JAXBContextFactory.createMarshaller().marshal(export, output);
			return output.toString().getBytes("UTF-8");
		}
		catch (JAXBException e)
		{
			throw new JobExecutionException(e);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new JobExecutionException(e);
		}
	}

	private XOpleidingInrichting exportOpleiding(Opleiding opleiding)
	{
		XOpleidingInrichting ret = new XOpleidingInrichting();
		ret.setOpleiding(opleiding);
		for (Criterium curCriterium : getCriteria(opleiding))
			ret.getCriteria().add(exportCriterium(curCriterium));
		for (Productregel curProductregel : getProductregels(opleiding))
			ret.getProductregels().add(exportProductregel(curProductregel));

		return ret;
	}

	private XCriterium exportCriterium(Criterium criterium)
	{
		XCriterium ret = new XCriterium();
		ret.setCohort(criterium.getCohort());
		ret.setFormule(criterium.getFormule());
		ret.setMelding(criterium.getMelding());
		ret.setNaam(criterium.getNaam());
		ret.setOpleiding(criterium.getOpleiding());
		ret.setVerbintenisgebied(criterium.getVerbintenisgebied());
		ret.setVolgnummer(criterium.getVolgnummer());
		return ret;
	}

	private XProductregel exportProductregel(Productregel productregel)
	{
		XProductregel ret = new XProductregel();
		ret.setAantalDecimalen(productregel.getAantalDecimalen());
		ret.setAfkorting(productregel.getAfkorting());
		ret.setCohort(productregel.getCohort());
		ret.setMinimaleWaarde(productregel.getMinimaleWaarde());
		ret.setMinimaleWaardeTekst(productregel.getMinimaleWaardeTekst());
		ret.setNaam(productregel.getNaam());
		ret.setOpleiding(productregel.getOpleiding());
		ret.setSoortProductregel(productregel.getSoortProductregel());
		ret.setTypeProductregel(productregel.getTypeProductregel());
		ret.setVerbintenisgebied(productregel.getVerbintenisgebied());
		ret.setVerplicht(productregel.isVerplicht());
		ret.setVolgnummer(productregel.getVolgnummer());
		for (ToegestaanOnderwijsproduct curOnderwijsproduct : productregel
			.getToegestaneOnderwijsproducten())
		{
			ret.getToegestaneOnderwijsproducten().add(curOnderwijsproduct.getOnderwijsproduct());
		}
		return ret;
	}

	public List<Criterium> getCriteria(Opleiding opleiding)
	{
		return DataAccessRegistry.getHelper(CriteriumDataAccessHelper.class).getCriteria(opleiding);
	}

	public List<Productregel> getProductregels(Opleiding opleiding)
	{
		return DataAccessRegistry.getHelper(ProductregelDataAccessHelper.class).getProductRegels(
			opleiding);
	}
}
