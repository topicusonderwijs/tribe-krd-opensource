package nl.topicus.eduarte.entities.dbs.trajecten.templates;

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
import nl.topicus.eduarte.entities.dbs.trajecten.Taak;
import nl.topicus.eduarte.entities.dbs.trajecten.TaakSoort;
import nl.topicus.eduarte.entities.dbs.trajecten.Traject;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/**
 * @author maatman
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class TaakTemplate extends BegeleidingsHandelingTemplate
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "taakSoort", nullable = true)
	@Basic(optional = false)
	@ForeignKey(name = "FK_TaakTempl_soort")
	@Index(name = "idx_TaakTempl_soort")
	@AutoForm(htmlClasses = "unit_max")
	private TaakSoort taakSoort;

	public TaakTemplate()
	{
	}

	public TaakSoort getTaakSoort()
	{
		return taakSoort;
	}

	public void setTaakSoort(TaakSoort taakSoort)
	{
		this.taakSoort = taakSoort;
	}

	@Override
	public String getTypeOmschrijving()
	{
		return "Taak";
	}

	@Override
	public String getSoortOmschrijving()
	{
		return getTaakSoort() == null ? "" : getTaakSoort().getNaam();
	}

	@Override
	public String getAanwezigenOmschrijving()
	{
		return "n.v.t";
	}

	@Override
	public void createHandelingen(Traject traject)
	{
		List<Date> data = getData(traject.getBegindatum(), traject.getBeoogdeEinddatum());
		for (Date datum : data)
		{
			// Er mogen geen taken worden geplanned in het verleden
			if (!datum.before(TimeUtil.getInstance().currentDate()))
			{
				Taak taak = new Taak(traject.getVerantwoordelijke(), traject);
				addEigenaar(taak, traject);
				addToegekendAan(taak, traject);

				taak.setStatus(BegeleidingsHandelingsStatussoort.Inplannen);
				taak.setTaakSoort(((TaakTemplate) doUnproxy()).getTaakSoort());
				taak.setOmschrijving(getOmschrijving());
				taak.setTraject(traject);
				taak.setDeadlineStatusovergang(datum);
				taak.setDeadline(datum);

				if (taak.getEigenaar() == null)
				{
					taak.setEigenaar(traject.getVerantwoordelijke());
				}
				if (taak.getToegekendAan() == null)
				{
					taak.setToegekendAan(traject.getVerantwoordelijke());
				}

				BegeleidingsHandelingStatusovergang overgang =
					new BegeleidingsHandelingStatusovergang();
				overgang.setBegeleidingsHandeling(taak);
				overgang.setDatumTijd(TimeUtil.getInstance().currentDateTime());
				overgang.setMedewerker(traject.getVerantwoordelijke());
				overgang.setVanStatus(null);
				overgang.setNaarStatus(taak.getStatus());
				taak.getStatusOvergangen().add(overgang);

				traject.getBegeleidingsHandelingen().add(taak);
			}
		}
	}

}
