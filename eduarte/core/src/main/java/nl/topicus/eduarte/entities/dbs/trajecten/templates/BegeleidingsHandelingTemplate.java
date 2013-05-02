package nl.topicus.eduarte.entities.dbs.trajecten.templates;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.HibernateObjectCopyManager;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.eduarte.dao.helpers.MedewerkerDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.trajecten.BegeleidingsHandeling;
import nl.topicus.eduarte.entities.dbs.trajecten.Traject;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/**
 * @author maatman
 */
@Entity()
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public abstract class BegeleidingsHandelingTemplate extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	@AutoForm(htmlClasses = "unit_max")
	private String omschrijving;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "planning", nullable = false)
	@ForeignKey(name = "FK_BegHandTemp_planning")
	@Index(name = "idx_FK_BegHandTemp_planning")
	private PlanningTemplate planning;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "eigenaar", nullable = false)
	@ForeignKey(name = "FK_EigeTempl_eigenaar")
	@Index(name = "idx_EigeTempl_eigenaar")
	@AutoFormEmbedded
	private EigenaarTemplate eigenaar;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "toegekendAan", nullable = false)
	@ForeignKey(name = "FK_EigeTempl_toegekendAan")
	@Index(name = "idx_EigeTempl_toegekendAan")
	@AutoFormEmbedded
	private EigenaarTemplate toegekendAan;

	public BegeleidingsHandelingTemplate()
	{
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public PlanningTemplate getPlanning()
	{
		return planning;
	}

	public void setPlanning(PlanningTemplate planning)
	{
		this.planning = planning;
	}

	public EigenaarTemplate getToegekendAan()
	{
		return toegekendAan;
	}

	public void setToegekendAan(EigenaarTemplate toegekendAan)
	{
		this.toegekendAan = toegekendAan;
	}

	public EigenaarTemplate getEigenaar()
	{
		return eigenaar;
	}

	public void setEigenaar(EigenaarTemplate eigenaar)
	{
		this.eigenaar = eigenaar;
	}

	public abstract String getTypeOmschrijving();

	public abstract String getSoortOmschrijving();

	public abstract String getAanwezigenOmschrijving();

	public abstract void createHandelingen(Traject traject);

	public BegeleidingsHandelingTemplate copy()
	{
		HibernateObjectCopyManager copyManager =
			new HibernateObjectCopyManager(AanwezigenTemplate.class,
				BegeleidingsHandelingTemplate.class, GeplandeBegeleidingsHandelingTemplate.class,
				GesprekTemplate.class, TaakTemplate.class, TestAfnameTemplate.class,
				EigenaarTemplate.class, PlanningTemplate.class);
		return copyManager.copyObject(this);
	}

	protected void addEigenaar(BegeleidingsHandeling handeling, Traject traject)
	{
		addEigenaarOfToegekendAan(handeling, traject, true);
	}

	protected void addToegekendAan(BegeleidingsHandeling handeling, Traject traject)
	{
		addEigenaarOfToegekendAan(handeling, traject, false);
	}

	protected void addEigenaarOfToegekendAan(BegeleidingsHandeling handeling, Traject traject,
			boolean setEigenaar)
	{
		MedewerkerDataAccessHelper helper =
			DataAccessRegistry.getHelper(MedewerkerDataAccessHelper.class);
		Medewerker res = null;
		if (getEigenaar() != null)
		{
			EigenaarTemplate eigenaarTemplate;
			if (setEigenaar)
			{
				eigenaarTemplate = getEigenaar();
			}
			else
			{
				eigenaarTemplate = getToegekendAan();
			}
			if (eigenaarTemplate.getPersoon() != null)
			{
				res = helper.get(eigenaarTemplate.getPersoon());
			}
			else
			{
				if (eigenaarTemplate.getRol().equals(
					BegeleidingsHandelingTemplateRol.EersteUitvoerende))
				{
					if (traject.getUitvoerders().size() > 0)
					{
						res = traject.getUitvoerders().get(0).getMedewerker();
					}
				}
				else if (eigenaarTemplate.getRol().equals(BegeleidingsHandelingTemplateRol.Mentor))
				{
					if (traject.getDeelnemer().getBegeleidersOpDatum(traject.getBegindatum()) != null
						&& traject.getDeelnemer().getBegeleidersOpDatum(traject.getBegindatum())
							.size() > 0)
					{
						res =
							traject.getDeelnemer().getBegeleidersOpDatum(traject.getBegindatum())
								.get(0);
					}
				}
				else if (eigenaarTemplate.getRol().equals(
					BegeleidingsHandelingTemplateRol.Verantwoordelijke))
				{
					res = traject.getVerantwoordelijke();
				}
			}
		}
		if (setEigenaar)
			handeling.setEigenaar(res);
		else
			handeling.setToegekendAan(res);
	}

	protected List<Date> getData(final Date beginDatumTraject, final Date eindDatumTraject)
	{
		List<Date> data = new ArrayList<Date>();

		final Date vandaag = TimeUtil.getInstance().currentDate();
		Date eersteDatum = beginDatumTraject;

		int aantal = getPlanning().getAantalEenhedenNaAanvang();
		switch (getPlanning().getTijdEenheid())
		{
			case Dagen:
				eersteDatum = TimeUtil.getInstance().addDays(beginDatumTraject, aantal);
				break;
			case Weken:
				eersteDatum = TimeUtil.getInstance().addWeeks(beginDatumTraject, aantal);
				break;
			case Maanden:
				eersteDatum = TimeUtil.getInstance().addMonths(beginDatumTraject, aantal);
				break;
			case Jaren:
				eersteDatum = TimeUtil.getInstance().addYears(beginDatumTraject, aantal);
				break;
		}

		if (!eersteDatum.before(vandaag))
			data.add(eersteDatum);

		// als het een eenmalige datum betreft
		if (getPlanning().getEenheidTussenHerhaling() == null)
		{
			return data;
		}

		Date datum = TimeUtil.getInstance().asDate(beginDatumTraject);

		int aantalKeer = 99;
		if (getPlanning().getStoptNaAantalKeer() > 0)
			aantalKeer = getPlanning().getStoptNaAantalKeer();

		Date eindDatumHerhaling = null;
		if (getPlanning().getStoptNaAantalEenheden() != null)
		{
			switch (getPlanning().getStoptNaEenheid())
			{
				case Dagen:
					eindDatumHerhaling =
						TimeUtil.getInstance().addDays(eersteDatum,
							getPlanning().getStoptNaAantalEenheden());
					break;
				case Weken:
					eindDatumHerhaling =
						TimeUtil.getInstance().addWeeks(eersteDatum,
							getPlanning().getStoptNaAantalEenheden());
					break;
				case Maanden:
					eindDatumHerhaling =
						TimeUtil.getInstance().addMonths(eersteDatum,
							getPlanning().getStoptNaAantalEenheden());
					break;
				case Jaren:
					eindDatumHerhaling =
						TimeUtil.getInstance().addYears(eersteDatum,
							getPlanning().getStoptNaAantalEenheden());
					break;
			}
			if (eindDatumTraject.before(eindDatumHerhaling))
			{
				eindDatumHerhaling = eindDatumTraject;
			}
		}
		else
		{
			eindDatumHerhaling = eindDatumTraject;
		}

		aantal = getPlanning().getAantalEenhedenTussenHerhaling();

		datum = TimeUtil.getInstance().asDate(eersteDatum);

		// zo niet, gaat het dus om een herhalende planning
		while (datum.before(eindDatumHerhaling) && data.size() < aantalKeer)
		{
			switch (getPlanning().getEenheidTussenHerhaling())
			{
				case Dagen:
					datum = TimeUtil.getInstance().addDays(datum, aantal);
					break;
				case Weken:
					datum = TimeUtil.getInstance().addWeeks(datum, aantal);
					break;
				case Maanden:
					datum = TimeUtil.getInstance().addMonths(datum, aantal);
					break;
				case Jaren:
					datum = TimeUtil.getInstance().addYears(datum, aantal);
					break;
			}
			if (datum.before(eindDatumHerhaling) && !datum.before(vandaag))
			{
				data.add(datum);
			}
		}

		return data;
	}
}
