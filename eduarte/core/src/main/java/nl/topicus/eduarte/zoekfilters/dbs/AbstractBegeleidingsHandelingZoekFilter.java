package nl.topicus.eduarte.zoekfilters.dbs;

import java.util.Date;

import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.eduarte.entities.dbs.trajecten.BegeleidingsHandeling;
import nl.topicus.eduarte.entities.dbs.trajecten.Traject;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.providers.DeelnemerProvider;

import org.apache.wicket.model.IModel;

public class AbstractBegeleidingsHandelingZoekFilter<T extends BegeleidingsHandeling> extends
		AbstractZorgvierkantObjectZoekFilter<T>
{
	private static final long serialVersionUID = 1L;

	private IModel<Traject> traject;

	private IModel<Medewerker> eigenaar;

	private IModel<Medewerker> toegekendAan;

	private IModel<Medewerker> toegekendAanOngelijkAan;

	private IModel<Medewerker> eigenaarOfToegekendAan;

	private Boolean toegekendAanNotNull;

	private Date datumDeadlineVanaf;

	private Date datumDeadlineTot;

	private Boolean geweigerd;

	private Boolean mijnActiesPage;

	private Boolean actief;

	public AbstractBegeleidingsHandelingZoekFilter()
	{
	}

	public AbstractBegeleidingsHandelingZoekFilter(Account account, DeelnemerProvider deelnemer)
	{
		super(account, deelnemer, Traject.VERTROUWELIJK_TRAJECT);
	}

	public AbstractBegeleidingsHandelingZoekFilter(Account account, Traject traject)
	{
		super(account, traject, Traject.VERTROUWELIJK_TRAJECT);

		setTraject(traject);
	}

	@Override
	public void addCriteria(CriteriaBuilder builder)
	{
		if (isMaxZorglijnSet())
		{
			if (getMaxZorglijn() == null)
				builder.addIsNull("traject.zorglijn", true);
			else
				builder.addNullOrLessOrEquals("traject.zorglijn", getMaxZorglijn());
		}
		if (!isVertrouwelijkAllowed())
			builder.addEquals("traject.vertrouwelijk", false);
	}

	public Traject getTraject()
	{
		return getModelObject(traject);
	}

	public void setTraject(Traject traject)
	{
		this.traject = makeModelFor(traject);
	}

	public Medewerker getEigenaar()
	{
		return getModelObject(eigenaar);
	}

	public void setEigenaar(Medewerker eigenaar)
	{
		this.eigenaar = makeModelFor(eigenaar);
	}

	public Medewerker getEigenaarOfToegekendAan()
	{
		return getModelObject(eigenaarOfToegekendAan);
	}

	public void setEigenaarOfToegekendAan(Medewerker eigenaarOfToegekendAan)
	{
		this.eigenaarOfToegekendAan = makeModelFor(eigenaarOfToegekendAan);
	}

	public Medewerker getToegekendAan()
	{
		return getModelObject(toegekendAan);
	}

	public void setToegekendAan(Medewerker toegekendAan)
	{
		this.toegekendAan = makeModelFor(toegekendAan);
	}

	public Medewerker getToegekendAanOngelijkAan()
	{
		return getModelObject(toegekendAanOngelijkAan);
	}

	public void setToegekendAanOngelijkAan(Medewerker toegekendAanOngelijkAan)
	{
		this.toegekendAanOngelijkAan = makeModelFor(toegekendAanOngelijkAan);
	}

	public Boolean getToegekendAanNotNull()
	{
		return toegekendAanNotNull;
	}

	public void setToegekendAanNotNull(Boolean toegekendAanNotNull)
	{
		this.toegekendAanNotNull = toegekendAanNotNull;
	}

	public Date getDatumDeadlineVanaf()
	{
		return datumDeadlineVanaf;
	}

	public void setDatumDeadlineVanaf(Date datumDeadlineVanaf)
	{
		this.datumDeadlineVanaf = datumDeadlineVanaf;
	}

	public Date getDatumDeadlineTot()
	{
		return datumDeadlineTot;
	}

	public void setDatumDeadlineTot(Date datumDeadlineTot)
	{
		this.datumDeadlineTot = datumDeadlineTot;
	}

	public Boolean getGeweigerd()
	{
		return geweigerd;
	}

	public void setGeweigerd(Boolean geweigerd)
	{
		this.geweigerd = geweigerd;
	}

	public Boolean getMijnActiesPage()
	{
		return mijnActiesPage;
	}

	public void setMijnActiesPage(Boolean mijnActiesPage)
	{
		this.mijnActiesPage = mijnActiesPage;
	}

	public void setActief(Boolean actief)
	{
		this.actief = actief;
	}

	public Boolean getActief()
	{
		return actief;
	}
}
