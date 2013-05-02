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
import nl.topicus.eduarte.entities.dbs.trajecten.BegeleidingsHandelingStatusovergang;
import nl.topicus.eduarte.entities.dbs.trajecten.BegeleidingsHandelingsStatussoort;
import nl.topicus.eduarte.entities.dbs.trajecten.Gesprek;
import nl.topicus.eduarte.entities.dbs.trajecten.GesprekSoort;
import nl.topicus.eduarte.entities.dbs.trajecten.Traject;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.AfspraakParticipant;
import nl.topicus.eduarte.entities.participatie.AfspraakType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/**
 * @author maatman
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class GesprekTemplate extends GeplandeBegeleidingsHandelingTemplate
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gesprekSoort", nullable = true)
	@Basic(optional = false)
	@ForeignKey(name = "FK_GesprekTempl_soort")
	@Index(name = "idx_GesprekTempl_soort")
	@AutoForm(htmlClasses = "unit_max")
	private GesprekSoort gesprekSoort;

	public GesprekTemplate()
	{
	}

	public GesprekSoort getGesprekSoort()
	{
		return gesprekSoort;
	}

	public void setGesprekSoort(GesprekSoort gesprekSoort)
	{
		this.gesprekSoort = gesprekSoort;
	}

	@Override
	public String getTypeOmschrijving()
	{
		return "Gesprek";
	}

	@Override
	public String getSoortOmschrijving()
	{
		return getGesprekSoort() == null ? "" : getGesprekSoort().getNaam();
	}

	@Override
	public void createHandelingen(Traject traject)
	{
		List<Date> data = getData(traject.getBegindatum(), traject.getBeoogdeEinddatum());
		for (Date datum : data)
		{
			if (!datum.before(TimeUtil.getInstance().currentDate()))
			{
				Gesprek gesprek = new Gesprek(traject.getVerantwoordelijke(), traject);

				Afspraak afspraak = new Afspraak();
				afspraak.setAuteur(traject.getVerantwoordelijke().getPersoon());

				List<AfspraakParticipant> participanten = new ArrayList<AfspraakParticipant>();

				createParticipanten(traject, afspraak, participanten);
				afspraak.setParticipanten(participanten);

				addEigenaar(gesprek, traject);
				addToegekendAan(gesprek, traject);

				gesprek.setStatus(BegeleidingsHandelingsStatussoort.Inplannen);
				gesprek
					.setGesprekSoortUitgebreid(((GesprekTemplate) doUnproxy()).getGesprekSoort());
				gesprek.setOmschrijving(getOmschrijving());
				gesprek.setTraject(traject);
				gesprek.setDeadlineStatusovergang(datum);
				gesprek.setDeadline(datum);
				gesprek.setAfspraak(afspraak);

				updateGesprekAfspraak(gesprek);

				if (gesprek.getEigenaar() == null)
				{
					gesprek.setEigenaar(traject.getVerantwoordelijke());
				}
				if (gesprek.getToegekendAan() == null)
				{
					gesprek.setToegekendAan(traject.getVerantwoordelijke());
				}

				BegeleidingsHandelingStatusovergang overgang =
					new BegeleidingsHandelingStatusovergang();
				overgang.setBegeleidingsHandeling(gesprek);
				overgang.setDatumTijd(TimeUtil.getInstance().currentDateTime());
				overgang.setMedewerker(traject.getVerantwoordelijke());
				overgang.setVanStatus(null);
				overgang.setNaarStatus(gesprek.getStatus());
				gesprek.getStatusOvergangen().add(overgang);

				traject.getBegeleidingsHandelingen().add(gesprek);
			}
		}
	}

	private static void updateGesprekAfspraak(Gesprek currentGesprek)
	{
		if (currentGesprek.getGesprekSoort() == null)
			return;

		AfspraakType afspraakType = currentGesprek.getGesprekSoort().getAfspraakType();
		Afspraak afspraak = currentGesprek.getAfspraak();
		afspraak.setAfspraakType(afspraakType);
		afspraak.setOrganisatieEenheid(afspraakType.getOrganisatieEenheid());
		afspraak.setTitel(currentGesprek.getOmschrijving());
		afspraak.setPresentieRegistratieVerplicht(afspraakType.isPresentieRegistratieDefault());
		if (afspraak.getEindDatumTijd() != null && afspraak.getBeginDatumTijd() != null)
		{
			double minutes =
				TimeUtil.getInstance().getDifferenceInMinutes(afspraak.getEindDatumTijd(),
					afspraak.getBeginDatumTijd());
			minutes /= 100;
			minutes *= afspraakType.getPercentageIIVO();
			afspraak.setMinutenIIVO((int) minutes);
		}
	}
}
