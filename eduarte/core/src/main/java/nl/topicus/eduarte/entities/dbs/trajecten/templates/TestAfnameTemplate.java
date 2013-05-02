package nl.topicus.eduarte.entities.dbs.trajecten.templates;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.dbs.testen.TestDefinitie;
import nl.topicus.eduarte.entities.dbs.trajecten.BegeleidingsHandelingStatusovergang;
import nl.topicus.eduarte.entities.dbs.trajecten.BegeleidingsHandelingsStatussoort;
import nl.topicus.eduarte.entities.dbs.trajecten.TestAfname;
import nl.topicus.eduarte.entities.dbs.trajecten.Traject;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.AfspraakParticipant;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/**
 * @author maatman
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class TestAfnameTemplate extends GeplandeBegeleidingsHandelingTemplate
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testDefinitie", nullable = true)
	@Basic(optional = false)
	@ForeignKey(name = "FK_TestAfnTempl_soort")
	@Index(name = "idx_TestAfnTempl_soort")
	@AutoForm(htmlClasses = "unit_max")
	private TestDefinitie testDefinitie;

	public TestAfnameTemplate()
	{
	}

	public TestDefinitie getTestDefinitie()
	{
		return testDefinitie;
	}

	public void setTestDefinitie(TestDefinitie testDefinitie)
	{
		this.testDefinitie = testDefinitie;
	}

	@Override
	public String getTypeOmschrijving()
	{
		return "Testafname";
	}

	@Override
	public String getSoortOmschrijving()
	{
		return getTestDefinitie() == null ? "" : getTestDefinitie().getNaam();
	}

	@Override
	public void createHandelingen(Traject traject)
	{
		List<Date> data = getData(traject.getBegindatum(), traject.getBeoogdeEinddatum());
		for (Date datum : data)
		{
			// Er mogen geen testafname worden geplanned in het verleden
			if (!datum.before(TimeUtil.getInstance().currentDate()))
			{
				TestAfname testAfname = new TestAfname(traject.getVerantwoordelijke(), traject);

				Afspraak afspraak = new Afspraak();
				afspraak.setAfspraakType(getTestDefinitie().getAfspraakType());
				afspraak
					.setOrganisatieEenheid(traject.getTrajectTemplate().getOrganisatieEenheid());
				afspraak.setLocatie(traject.getTrajectTemplate().getLocatie());
				afspraak.setAuteur(traject.getVerantwoordelijke().getPersoon());
				afspraak.setTitel(traject.getTitel());

				List<AfspraakParticipant> participanten = new ArrayList<AfspraakParticipant>();

				createParticipanten(traject, afspraak, participanten);
				addEigenaar(testAfname, traject);
				addToegekendAan(testAfname, traject);

				testAfname.setStatus(BegeleidingsHandelingsStatussoort.Inplannen);
				testAfname.setTestDefinitie(getTestDefinitie());
				testAfname.setOmschrijving(getOmschrijving());
				testAfname.setTraject(traject);
				testAfname.setDeadlineStatusovergang(datum);
				testAfname.setDeadline(datum);
				testAfname.setAfspraak(afspraak);

				if (testAfname.getEigenaar() == null)
				{
					testAfname.setEigenaar(traject.getVerantwoordelijke());
				}
				if (testAfname.getToegekendAan() == null)
				{
					testAfname.setToegekendAan(traject.getVerantwoordelijke());
				}

				BegeleidingsHandelingStatusovergang overgang =
					new BegeleidingsHandelingStatusovergang();
				overgang.setBegeleidingsHandeling(testAfname);
				overgang.setDatumTijd(TimeUtil.getInstance().currentDateTime());
				overgang.setMedewerker(traject.getVerantwoordelijke());
				overgang.setVanStatus(null);
				overgang.setNaarStatus(testAfname.getStatus());
				testAfname.getStatusOvergangen().add(overgang);

				traject.getBegeleidingsHandelingen().add(testAfname);
			}
		}
	}
}
