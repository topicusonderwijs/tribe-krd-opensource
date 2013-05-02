package nl.topicus.eduarte.entities.dbs.trajecten.templates;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.eduarte.entities.kenmerk.Kenmerk;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

@Entity(name = "trajTemplAutoKopp")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class TrajectTemplateAutomatischeKoppeling extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "trajectTemplateAutomatischeKoppeling", targetEntity = TrajectTemplateKoppeling.class)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@Where(clause = "DTYPE='TTKoppelingOpleiding'")
	private List<TrajectTemplateKoppelingOpleiding> opleidingKoppelingen =
		new ArrayList<TrajectTemplateKoppelingOpleiding>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "trajectTemplateAutomatischeKoppeling", targetEntity = TrajectTemplateKoppeling.class)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@Where(clause = "DTYPE='TTKoppelingOrganisatie'")
	private List<TrajectTemplateKoppelingOrganisatie> organisatieKoppelingen =
		new ArrayList<TrajectTemplateKoppelingOrganisatie>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "trajectTemplateAutomatischeKoppeling", targetEntity = TrajectTemplateKoppeling.class)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@Where(clause = "DTYPE='TTKoppelingKenmerk'")
	private List<TrajectTemplateKoppelingKenmerk> kenmerkKoppelingen =
		new ArrayList<TrajectTemplateKoppelingKenmerk>();

	@Column(nullable = true)
	private Boolean indicatieLWOO;

	@Column(nullable = true)
	private Boolean indicatieGehandicapt;

	@Column(nullable = true)
	private Boolean alleenNieuweDeelnemers;

	@Column(nullable = true)
	private Boolean alleenIntakeStatussen;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date datumBeschikbaar;

	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	private Date datumEindeBeschikbaar;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "automatischeKoppeling")
	@IgnoreInGebruik
	private TrajectTemplate trajectTemplate;

	public TrajectTemplateAutomatischeKoppeling()
	{
	}

	public List<TrajectTemplateKoppelingOpleiding> getOpleidingKoppelingen()
	{
		return opleidingKoppelingen;
	}

	public void setOpleidingKoppelingen(List<TrajectTemplateKoppelingOpleiding> opleidingKoppelingen)
	{
		this.opleidingKoppelingen = opleidingKoppelingen;
	}

	public List<TrajectTemplateKoppelingOrganisatie> getOrganisatieKoppelingen()
	{
		return organisatieKoppelingen;
	}

	public void setOrganisatieKoppelingen(
			List<TrajectTemplateKoppelingOrganisatie> organisatieKoppelingen)
	{
		this.organisatieKoppelingen = organisatieKoppelingen;
	}

	public List<TrajectTemplateKoppelingKenmerk> getKenmerkKoppelingen()
	{
		return kenmerkKoppelingen;
	}

	public void setKenmerkKoppelingen(List<TrajectTemplateKoppelingKenmerk> kenmerkKoppelingen)
	{
		this.kenmerkKoppelingen = kenmerkKoppelingen;
	}

	public Boolean getIndicatieLWOO()
	{
		return indicatieLWOO;
	}

	public void setIndicatieLWOO(Boolean indicatieLWOO)
	{
		this.indicatieLWOO = indicatieLWOO;
	}

	public Boolean getIndicatieGehandicapt()
	{
		return indicatieGehandicapt;
	}

	public void setIndicatieGehandicapt(Boolean indicatieGehandicapt)
	{
		this.indicatieGehandicapt = indicatieGehandicapt;
	}

	public Boolean getAlleenNieuweDeelnemers()
	{
		return alleenNieuweDeelnemers;
	}

	public void setAlleenNieuweDeelnemers(Boolean alleenNieuweDeelnemers)
	{
		this.alleenNieuweDeelnemers = alleenNieuweDeelnemers;
	}

	public Boolean getAlleenIntakeStatussen()
	{
		return alleenIntakeStatussen;
	}

	public void setAlleenIntakeStatussen(Boolean alleenIntakeStatussen)
	{
		this.alleenIntakeStatussen = alleenIntakeStatussen;
	}

	public Date getDatumBeschikbaar()
	{
		return datumBeschikbaar;
	}

	public void setDatumBeschikbaar(Date datumBeschikbaar)
	{
		this.datumBeschikbaar = datumBeschikbaar;
	}

	public Date getDatumEindeBeschikbaar()
	{
		return datumEindeBeschikbaar;
	}

	public void setDatumEindeBeschikbaar(Date datumEindeBeschikbaar)
	{
		this.datumEindeBeschikbaar = datumEindeBeschikbaar;
	}

	public String getOrganisatieEenhedenAlsString()
	{
		String str = "";
		boolean seperator = false;

		for (TrajectTemplateKoppelingOrganisatie org : getOrganisatieKoppelingen())
		{
			if (seperator)
				str += ", ";
			seperator = true;
			str += org.getOrganisatieEenheid().getAfkorting();
		}
		return str;
	}

	public String getKenmerkenAlsString()
	{
		String str = "";
		boolean seperator = false;

		for (TrajectTemplateKoppelingKenmerk kenmerk : getKenmerkKoppelingen())
		{
			if (seperator)
				str += ", ";
			seperator = true;
			str += kenmerk.getKenmerk().getCategorieNaam() + " - " + kenmerk.getKenmerk().getNaam();
		}
		return str;
	}

	public String getOpleidingenAlsString()
	{
		String str = "";
		boolean seperator = false;

		for (TrajectTemplateKoppelingOpleiding opl : getOpleidingKoppelingen())
		{
			if (seperator)
				str += ", ";
			seperator = true;
			str += "" + opl.getOpleiding().getNaam();
		}
		return str;
	}

	public List<Opleiding> getOpleidingen()
	{
		List<Opleiding> opleidingen = new ArrayList<Opleiding>();

		for (TrajectTemplateKoppelingOpleiding opl : getOpleidingKoppelingen())
		{
			opleidingen.add(opl.getOpleiding());
		}
		return opleidingen;
	}

	public List<OrganisatieEenheid> getOrganisatieEenheden()
	{
		List<OrganisatieEenheid> organisaties = new ArrayList<OrganisatieEenheid>();

		for (TrajectTemplateKoppelingOrganisatie opl : getOrganisatieKoppelingen())
		{
			organisaties.add(opl.getOrganisatieEenheid());
		}
		return organisaties;
	}

	public List<Kenmerk> getKenmerken()
	{
		List<Kenmerk> kenmerken = new ArrayList<Kenmerk>();

		for (TrajectTemplateKoppelingKenmerk opl : getKenmerkKoppelingen())
		{
			kenmerken.add(opl.getKenmerk());
		}
		return kenmerken;
	}

	public boolean heeftVerbintenisKenmerken()
	{
		if (getIndicatieGehandicapt() != null && getIndicatieGehandicapt().booleanValue())
			return true;
		if (getIndicatieLWOO() != null && getIndicatieLWOO().booleanValue())
			return true;
		if (getAlleenIntakeStatussen() != null && getAlleenIntakeStatussen().booleanValue())
			return true;
		if (getAlleenNieuweDeelnemers() != null && getAlleenNieuweDeelnemers().booleanValue())
			return true;
		if (getOpleidingKoppelingen() != null && !getOpleidingKoppelingen().isEmpty())
			return true;

		return false;
	}

	public void setTrajectTemplate(TrajectTemplate trajectTemplate)
	{
		this.trajectTemplate = trajectTemplate;
	}

	public TrajectTemplate getTrajectTemplate()
	{
		return trajectTemplate;
	}
}
