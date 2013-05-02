package nl.topicus.eduarte.resultaten.jobs;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.quartz.TwoPartSegment;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.ResultaatstructuurDeelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.ResultaatstructuurMedewerker;
import nl.topicus.eduarte.entities.resultaatstructuur.Scoreschaalwaarde;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.ToetsVerwijzing;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.resultaten.entities.ResultaatstructurenExporterenJobRun;
import nl.topicus.eduarte.xml.JAXBContextFactory;
import nl.topicus.eduarte.xml.resultaatstructuur.v10.ResultaatstructuurExport;
import nl.topicus.eduarte.xml.resultaatstructuur.v10.XResultaatstructuur;
import nl.topicus.eduarte.xml.resultaatstructuur.v10.XResultaatstructuurCategorie;
import nl.topicus.eduarte.xml.resultaatstructuur.v10.XResultaatstructuurDeelnemer;
import nl.topicus.eduarte.xml.resultaatstructuur.v10.XScoreschaalwaarde;
import nl.topicus.eduarte.xml.resultaatstructuur.v10.XToets;
import nl.topicus.eduarte.xml.resultaatstructuur.v10.XToetsverwijzing;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@JobInfo(name = ResultaatstructurenExporterenJob.JOB_NAME)
@JobRunClass(ResultaatstructurenExporterenJobRun.class)
public class ResultaatstructurenExporterenJob extends EduArteJob
{
	public static final String JOB_GROUP = "KRD";

	public static final String JOB_NAME = "Resultaatstructuren exporteren";

	private int structuurCount = 0;

	@Override
	protected void executeJob(JobExecutionContext context) throws JobExecutionException,
			InterruptedException
	{
		ResultaatstructurenExporterenJobRun run = new ResultaatstructurenExporterenJobRun();
		run.setGestartDoor(getMedewerker());
		run.setRunStart(TimeUtil.getInstance().currentDateTime());
		run.setSamenvatting("Resultaatstructuren exporteren");
		run.save();

		Set<Onderwijsproduct> onderwijsproducten = getValue(context, "onderwijsproducten");
		run.setContents(export(onderwijsproducten));

		run.setRunEinde(TimeUtil.getInstance().currentDateTime());
		run.setSamenvatting(run.getSamenvatting() + ": Structuren: " + structuurCount
			+ " geëxporteerd.");
		run.update();
		run.commit();
	}

	private byte[] export(Set<Onderwijsproduct> onderwijsproducten) throws InterruptedException,
			JobExecutionException
	{
		ResultaatstructuurDataAccessHelper helper =
			DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class);
		List<Long> structuren = helper.getResultaatstructuurIds(onderwijsproducten, null);
		Map<Long, XToets> toetsen = new HashMap<Long, XToets>();
		int count = 0;
		ResultaatstructuurExport export = new ResultaatstructuurExport();
		for (Long curStructuurId : structuren)
		{
			Resultaatstructuur curStructuur = helper.get(curStructuurId);
			setProgress(count, structuren.size(), TwoPartSegment.FIRST_HALF);
			setStatus("Exporteren van toetsen voor " + curStructuur.getOnderwijsproduct().getCode()
				+ " " + curStructuur.getCode() + " (" + curStructuur.getType() + ")");
			export.getResultaatstructuren().add(exportStructuur(curStructuur, toetsen));
			structuurCount++;
			count = flushAndClearHibernateAndIncCount(count);
		}

		count = 0;
		for (Long curToetsId : toetsen.keySet())
		{
			Toets curToets =
				DataAccessRegistry.getHelper(ToetsDataAccessHelper.class).get(Toets.class,
					curToetsId);
			setProgress(count, toetsen.size(), TwoPartSegment.SECOND_HALF);
			setStatus("Exporteren van toetsverwijzingen voor " + curToets.toString());
			exportVerwijzingen(curToets, toetsen);
			count = flushAndClearHibernateAndIncCount(count);
		}
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

	private XResultaatstructuur exportStructuur(Resultaatstructuur structuur,
			Map<Long, XToets> toetsen)
	{
		XResultaatstructuurCategorie categorie = null;
		if (structuur.getCategorie() != null)
		{
			categorie = new XResultaatstructuurCategorie();
			categorie.setActief(structuur.getCategorie().isActief());
			categorie.setNaam(structuur.getCategorie().getNaam());
		}

		XResultaatstructuur ret = new XResultaatstructuur();
		ret.setActief(structuur.isActief());
		ret.setAuteur(structuur.getAuteur());
		ret.setCategorie(categorie);
		ret.setCode(structuur.getCode());
		ret.setCohort(structuur.getCohort());
		ret.setEindresultaat(exportToets(structuur.getEindresultaat(), toetsen));
		ret.setNaam(structuur.getNaam());
		ret.setOnderwijsproduct(structuur.getOnderwijsproduct());
		ret.setSpecifiek(structuur.isSpecifiek());
		ret.setStatus(structuur.getStatus());
		ret.setType(structuur.getType());
		for (ResultaatstructuurDeelnemer curDeelnemer : structuur.getDeelnemers())
		{
			XResultaatstructuurDeelnemer deelnemer = new XResultaatstructuurDeelnemer();
			deelnemer.setDeelnemer(curDeelnemer.getDeelnemer());
			deelnemer.setGroep(curDeelnemer.getGroep());
			ret.getDeelnemers().add(deelnemer);
		}
		for (ResultaatstructuurMedewerker curMedewerker : structuur.getMedewerkers())
			ret.getMedewerkers().add(curMedewerker.getMedewerker());

		return ret;
	}

	private XToets exportToets(Toets toets, Map<Long, XToets> toetsen)
	{
		XToets ret = new XToets();
		ret.setAantalHerkansingen(toets.getAantalHerkansingen());
		ret.setAlternatiefCombinerenMetHoofd(toets.isAlternatiefCombinerenMetHoofd());
		ret.setAlternatiefResultaatMogelijk(toets.isAlternatiefResultaatMogelijk());
		ret.setCode(toets.getCode());
		ret.setCompenseerbaarVanaf(toets.getCompenseerbaarVanaf());
		ret.setFormule(toets.getFormule());
		ret.setHandmatigInleveren(toets.isHandmatigInleveren());
		ret.setId("ref-" + toets.getId());
		ret.setMaxAantalIngevuld(toets.getMaxAantalIngevuld());
		ret.setMaxAantalNietBehaald(toets.getMaxAantalNietBehaald());
		ret.setMinAantalIngevuld(toets.getMinAantalIngevuld());
		ret.setMinStudiepuntenVoorBehaald(toets.getMinStudiepuntenVoorBehaald());
		ret.setNaam(toets.getNaam());
		ret.setOverschrijfbaar(toets.isOverschrijfbaar());
		ret.setReferentieCode(toets.getReferentieCode());
		ret.setReferentieVersie(toets.getReferentieVersie());
		ret.setRekenregel(toets.getRekenregel());
		ret.setSamengesteld(toets.isSamengesteld());
		ret.setSamengesteldMetHerkansing(toets.isSamengesteldMetHerkansing());
		ret.setSamengesteldMetVarianten(toets.isSamengesteldMetVarianten());
		ret.setSchaal(toets.getSchaal());
		ret.setScoreBijHerkansing(toets.getScoreBijHerkansing());
		ret.setScoreschaal(toets.getScoreschaal());
		ret.setScoreschaalLengteTijdvak1(toets.getScoreschaalLengteTijdvak1());
		ret.setScoreschaalLengteTijdvak2(toets.getScoreschaalLengteTijdvak2());
		ret.setScoreschaalLengteTijdvak3(toets.getScoreschaalLengteTijdvak3());
		ret.setScoreschaalNormeringTijdvak1(toets.getScoreschaalNormeringTijdvak1());
		ret.setScoreschaalNormeringTijdvak2(toets.getScoreschaalNormeringTijdvak2());
		ret.setScoreschaalNormeringTijdvak3(toets.getScoreschaalNormeringTijdvak3());
		ret.setSoort(toets.getSoort());
		ret.setStudiepunten(toets.getStudiepunten());
		ret.setVariantVoorPoging(toets.getVariantVoorPoging());
		ret.setVerplicht(toets.isVerplicht());
		ret.setVerwijsbaar(toets.isVerwijsbaar());
		ret.setVolgnummer(toets.getVolgnummer());
		ret.setWeging(toets.getWeging());

		for (Scoreschaalwaarde curScoreschaalwaarde : toets.getScoreschaalwaarden())
			ret.getScoreschaalwaarden().add(exportScoreschaalwaarde(curScoreschaalwaarde));

		for (Toets curDeeltoets : toets.getChildren())
			ret.getDeeltoetsen().add(exportToets(curDeeltoets, toetsen));

		toetsen.put(toets.getId(), ret);
		return ret;
	}

	private XScoreschaalwaarde exportScoreschaalwaarde(Scoreschaalwaarde scoreschaalwaarde)
	{
		XScoreschaalwaarde ret = new XScoreschaalwaarde();
		ret.setAdvies(scoreschaalwaarde.getAdvies());
		ret.setTotScore(scoreschaalwaarde.getTotScore());
		ret.setVanafScore(scoreschaalwaarde.getVanafScore());
		ret.setWaarde(scoreschaalwaarde.getWaarde());
		return ret;
	}

	private void exportVerwijzingen(Toets toets, Map<Long, XToets> toetsen)
	{
		XToets xtoets = toetsen.get(toets.getId());
		for (ToetsVerwijzing curToetsVerwijzing : toets.getUitgaandeVerwijzingen())
			xtoets.getVerwijzingen().add(exportVerwijzing(curToetsVerwijzing, toetsen));
	}

	private XToetsverwijzing exportVerwijzing(ToetsVerwijzing toetsVerwijzing,
			Map<Long, XToets> toetsen)
	{
		XToetsverwijzing ret = new XToetsverwijzing();
		if (toetsen.containsKey(toetsVerwijzing.getSchrijvenIn().getId()))
			ret.setToets(toetsen.get(toetsVerwijzing.getSchrijvenIn().getId()));
		else
			ret.setToetsref(toetsVerwijzing.getSchrijvenIn());
		return ret;
	}
}
