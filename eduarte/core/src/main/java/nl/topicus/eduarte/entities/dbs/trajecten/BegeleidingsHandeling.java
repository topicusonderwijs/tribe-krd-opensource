package nl.topicus.eduarte.entities.dbs.trajecten;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.*;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.app.security.checks.ZorgvierkantObjectSecurityCheck;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.dbs.ZorgvierkantObject;
import nl.topicus.eduarte.entities.dbs.bijlagen.BegeleidingsBijlage;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.web.components.choice.DeelnemerTrajectCombobox;

import org.apache.wicket.security.actions.Render;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity()
@Exportable
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public abstract class BegeleidingsHandeling extends InstellingEntiteit implements
		ZorgvierkantObject, IBijlageKoppelEntiteit<BegeleidingsBijlage>
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true, length = 255)
	private String omschrijving;

	@Column(columnDefinition = "date", nullable = true)
	private Date deadlineStatusovergang;

	@Column(nullable = false)
	private boolean gelezen;

	@Column(nullable = false)
	private boolean geweigerd;

	@Column(nullable = true, length = 512)
	private String aanleiding;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "voorafgaand")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<VervolgHandeling> vervolgHandelingen = new ArrayList<VervolgHandeling>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vervolg")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<VervolgHandeling> voorgaandeHandelingen = new ArrayList<VervolgHandeling>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Traject", nullable = false)
	@ForeignKey(name = "FK_BegHand_traject")
	@Index(name = "idx_BegHand_traject")
	@AutoForm(editorClass = DeelnemerTrajectCombobox.class, htmlClasses = "unit_max")
	private Traject traject;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Eigenaar", nullable = false)
	@ForeignKey(name = "FK_BegHand_Eig")
	@Index(name = "idx_BegHand_Eig")
	private Medewerker eigenaar;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Verantwoordelijke", nullable = true)
	@ForeignKey(name = "FK_BegHand_toeg")
	@Index(name = "idx_BegHand_toeg")
	private Medewerker toegekendAan;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "begeleidingsHandeling")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<BegeleidingsBijlage> bijlagen = new ArrayList<BegeleidingsBijlage>();

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BegeleidingsHandelingsStatussoort status;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "begeleidingsHandeling")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@OrderBy("datumTijd desc")
	private List<BegeleidingsHandelingStatusovergang> statusOvergangen =
		new ArrayList<BegeleidingsHandelingStatusovergang>();

	// // 3=Voltooid, 4=Geannuleerd
	// @Formula(value =
	// "CASE WHEN STATUS IN ('Voltooid','Geannuleerd') THEN NULL ELSE DEADLINESTATUSOVERGANG END")
	// private Date deadline;

	/**
	 * De lijst met aanleidingen die naar deze handeling verwijzen. Dit zijn niet de
	 * aanleidingen van deze handeling, maar juist objecten die aangeven dat deze
	 * handeling de aanleiding is van een traject.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "begeleidingsHandeling")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<Aanleiding> trajectAanleidingen;

	@Column(nullable = true)
	private boolean eindHandeling;

	// overnemen soort handeling omdat hierop gesorteerd moet worden in de database.
	@Column(nullable = false, length = 32)
	protected String soort;

	public BegeleidingsHandeling()
	{
	}

	public String getSoortHandeling()
	{
		return soort;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public Date getDeadlineStatusovergang()
	{
		return deadlineStatusovergang;
	}

	public void setDeadlineStatusovergang(Date deadlineStatusovergang)
	{
		this.deadlineStatusovergang = deadlineStatusovergang;
	}

	public boolean isGelezen()
	{
		return gelezen;
	}

	public void setGelezen(boolean gelezen)
	{
		this.gelezen = gelezen;
	}

	public boolean isGeweigerd()
	{
		return geweigerd;
	}

	public void setGeweigerd(boolean geweigerd)
	{
		this.geweigerd = geweigerd;
	}

	public String getAanleiding()
	{
		return aanleiding;
	}

	public void setAanleiding(String aanleiding)
	{
		this.aanleiding = aanleiding;
	}

	public Traject getTraject()
	{
		return traject;
	}

	public void setTraject(Traject traject)
	{
		this.traject = traject;
	}

	public List<BegeleidingsBijlage> getBijlagen()
	{
		return bijlagen;
	}

	public void setBijlagen(List<BegeleidingsBijlage> bijlagen)
	{
		this.bijlagen = bijlagen;
	}

	public Medewerker getEigenaar()
	{
		return eigenaar;
	}

	public void setEigenaar(Medewerker eigenaar)
	{
		this.eigenaar = eigenaar;
	}

	public Medewerker getToegekendAan()
	{
		return toegekendAan;
	}

	public void setToegekendAan(Medewerker toegekendAan)
	{
		this.toegekendAan = toegekendAan;
	}

	public BegeleidingsHandelingsStatussoort getStatus()
	{
		return status;
	}

	public void setStatus(BegeleidingsHandelingsStatussoort status)
	{
		this.status = status;
	}

	public List<VervolgHandeling> getVervolgHandelingen()
	{
		return vervolgHandelingen;
	}

	public void setVervolgHandelingen(List<VervolgHandeling> vervolgHandelingen)
	{
		this.vervolgHandelingen = vervolgHandelingen;
	}

	public List<VervolgHandeling> getVoorgaandeHandelingen()
	{
		return voorgaandeHandelingen;
	}

	public void setVoorgaandeHandelingen(List<VervolgHandeling> voorgaandeHandelingen)
	{
		this.voorgaandeHandelingen = voorgaandeHandelingen;
	}

	public List<BegeleidingsHandelingStatusovergang> getStatusOvergangen()
	{
		return statusOvergangen;
	}

	public void setStatusOvergangen(List<BegeleidingsHandelingStatusovergang> statusOvergangen)
	{
		this.statusOvergangen = statusOvergangen;
	}

	@Override
	public String toString()
	{
		return getOmschrijving();
	}

	/**
	 * @return De deadline voor de volgende statusovergang als de status van deze
	 *         handeling niet al een eindstatus bereikt heeft. Als de handeling al een
	 *         eindstatus bereikt heeft, wordt null teruggegeven.
	 */
	public Date getDeadline()
	{

		if (BegeleidingsHandelingsStatussoort.getEindStatussen().contains(getStatus()))
			return null;
		else
			return deadlineStatusovergang;
	}

	public void setDeadline(Date deadline)
	{
		setDeadlineStatusovergang(deadline);
	}

	public Date getSorteerDatum()
	{
		return getDeadline();
	}

	public List<Aanleiding> getTrajectAanleidingen()
	{
		return trajectAanleidingen;
	}

	public void setTrajectAanleidingen(List<Aanleiding> trajectAanleidingen)
	{
		this.trajectAanleidingen = trajectAanleidingen;
	}

	public void setEindHandeling(boolean eindHandeling)
	{
		this.eindHandeling = eindHandeling;
	}

	public boolean isEindHandeling()
	{
		return eindHandeling;
	}

	public Deelnemer getDeelnemer()
	{
		return getTraject().getDeelnemer();
	}

	@Override
	public boolean bestaatBijlage(Bijlage bijlage)
	{
		for (BegeleidingsBijlage bijlageEntiteit : getBijlagen())
		{
			if (bijlageEntiteit.getBijlage().equals(bijlage))
				return true;
		}
		return false;
	}

	@Override
	public BegeleidingsBijlage addBijlage(Bijlage bijlage)
	{
		BegeleidingsBijlage newBijlage = new BegeleidingsBijlage();
		newBijlage.setBijlage(bijlage);
		newBijlage.setDeelnemer(getDeelnemer());
		newBijlage.setBegeleidingsHandeling(this);

		getBijlagen().add(newBijlage);

		return newBijlage;
	}

	@Override
	public Integer getZorglijn()
	{
		return getTraject().getZorglijn();
	}

	@Override
	public boolean isVertrouwelijk()
	{
		return getTraject().isVertrouwelijk();
	}

	@Override
	public String getSecurityId()
	{
		return getTraject().getSecurityId();
	}

	@Override
	public String getVertrouwelijkSecurityId()
	{
		return getTraject().getVertrouwelijkSecurityId();
	}

	@Override
	public boolean isTonenInZorgvierkant()
	{
		return getTraject().isTonenInZorgvierkant();
	}

	public boolean isToekenningAanVisible()
	{
		if (getStatus() != null)
		{
			if (getStatus().equals(BegeleidingsHandelingsStatussoort.Inplannen))
				return true;
			if (getStatus().equals(BegeleidingsHandelingsStatussoort.Uitvoeren))
				return true;
		}
		return false;
	}

	public String getSoort()
	{
		return soort;
	}

	public void setSoort(@SuppressWarnings("unused") String soort)
	{
		// dit veld kan niet aangepast worden.
	}

	public abstract String handelingsSoort();

	/**************************************************************************************************************************
	 *** Sectie met getters voor samenvoeg velden
	 ************************************************************************************************************************** 
	 */

	private boolean doVertrouwlijkeSercurityCheck()
	{
		return ZorgvierkantObjectSecurityCheck.isAllowed(Render.class, this);
	}

	@Exportable
	public boolean isExportableGeweigerd()
	{
		return isGeweigerd();
	}

	@Exportable
	public String getExportableAanleiding()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getAanleiding();
		return null;
	}

	@Exportable
	public List<BegeleidingsBijlage> getExportableBijlagen()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getBijlagen();
		return null;
	}

	@Exportable
	public Medewerker getExportableEigenaar()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getEigenaar();
		return null;
	}

	@Exportable
	public Medewerker getExportableToegekendAan()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getToegekendAan();
		return null;
	}

	@Exportable
	public BegeleidingsHandelingsStatussoort getExportableStatus()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getStatus();
		return null;
	}

	@Exportable
	public List<VervolgHandeling> getExportableVervolgHandelingen()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getVervolgHandelingen();
		return null;
	}

	@Exportable
	public List<VervolgHandeling> getExportableVoorgaandeHandelingen()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getVoorgaandeHandelingen();
		return null;
	}

	@Exportable
	public List<BegeleidingsHandelingStatusovergang> getExportableStatusOvergangen()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getStatusOvergangen();
		return null;
	}

	@Exportable
	public Date getExportableDeadline()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getDeadline();
		return null;
	}

	public String getExportableDeadlineString()
	{
		if (getExportableDeadline() != null)
		{
			return new SimpleDateFormat("dd MMMMM yyyy", new Locale("nl", "NL"))
				.format(deadlineStatusovergang);
		}
		return "";
	}

	@Exportable
	public Date getExportableSorteerDatum()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getSorteerDatum();
		return null;
	}

	@Exportable
	public List<Aanleiding> getExportableTrajectAanleidingen()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getTrajectAanleidingen();
		return null;
	}

	@Exportable
	public boolean isExportableEindHandeling()
	{
		return isEindHandeling();
	}

	@Exportable
	public Deelnemer getExportableDeelnemer()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getDeelnemer();
		return null;
	}

	@Exportable
	public Integer getExportableZorglijn()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getZorglijn();
		return null;
	}

	@Exportable
	public boolean isExportableVertrouwelijk()
	{
		return isVertrouwelijk();
	}

	@Exportable
	public String getExportableSoort()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getSoort();
		return null;
	}
}
