package nl.topicus.eduarte.entities.dbs.trajecten;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.*;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.labels.JaNeeObjectLabel;
import nl.topicus.cobra.web.components.text.HtmlLabel;
import nl.topicus.cobra.web.components.wiquery.TextEditorPanel;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.security.checks.ZorgvierkantObjectSecurityCheck;
import nl.topicus.eduarte.dao.helpers.dbs.TrajectDataAccessHelper;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.dbs.ZorgvierkantObject;
import nl.topicus.eduarte.entities.dbs.bijlagen.TrajectBijlage;
import nl.topicus.eduarte.entities.dbs.trajecten.templates.BegeleidingsHandelingTemplate;
import nl.topicus.eduarte.entities.dbs.trajecten.templates.TrajectTemplate;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.web.components.choice.VerbintenisCombobox;
import nl.topicus.eduarte.web.components.choice.ZorglijnCombobox;

import org.apache.wicket.security.actions.Render;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity()
@Exportable
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class Traject extends BeginEinddatumInstellingEntiteit implements ZorgvierkantObject,
		IBijlageKoppelEntiteit<TrajectBijlage>
{
	private static final long serialVersionUID = 1L;

	public static final String TRAJECT = "TRAJECT";

	public static final String VERTROUWELIJK_TRAJECT = "VERTROUWELIJK_TRAJECT";

	@Temporal(value = TemporalType.DATE)
	@Column(columnDefinition = "date", nullable = true)
	@AutoForm(htmlClasses = "unit_80")
	private Date beoogdeEinddatum;

	@Column(nullable = false, length = 255)
	@AutoForm(htmlClasses = "unit_max")
	private String titel;

	@Column(nullable = false, length = 1024)
	@AutoForm(htmlClasses = "unit_max")
	private String omschrijving;

	@Column(nullable = false)
	private boolean vertrouwelijk;

	@Column(nullable = true, length = 2)
	@AutoForm(editorClass = ZorglijnCombobox.class)
	private Integer zorglijn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "trajectSoort", nullable = false)
	@ForeignKey(name = "FK_Traject_soort")
	@Index(name = "idx_Traject_soort")
	@AutoForm(label = "Trajectsoort", htmlClasses = "unit_max")
	private TrajectSoort trajectSoort;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "trajectStatusSoort", nullable = false)
	@ForeignKey(name = "FK_Traject_trajSSrt")
	@Index(name = "idx_Traject_trajSSrt")
	@AutoForm(label = "Status", htmlClasses = "unit_max")
	private TrajectStatusSoort trajectStatusSoort;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = false)
	@ForeignKey(name = "FK_Traject_deelnemer")
	@Index(name = "idx_Traject_deelnemer")
	private Deelnemer deelnemer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "verbintenis", nullable = true)
	@ForeignKey(name = "FK_Traject_verbintenis")
	@Index(name = "idx_Traject_verbintenis")
	@AutoForm(editorClass = VerbintenisCombobox.class, htmlClasses = "unit_max")
	private Verbintenis verbintenis;

	/**
	 * Tekstuele aanleiding van het traject.
	 */
	@Column(nullable = true)
	@AutoForm(htmlClasses = "unit_max")
	@Lob
	private String aanleiding;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "traject")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<Aanleiding> aanleidingen = new ArrayList<Aanleiding>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "traject")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<BegeleidingsHandeling> begeleidingsHandelingen =
		new ArrayList<BegeleidingsHandeling>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "eindHandelingTemplate")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@Index(name = "idx_Traject_eindHandeling")
	@AutoForm(label = "Eindhandeling gekoppeld", displayClass = JaNeeObjectLabel.class)
	private BegeleidingsHandelingTemplate eindHandelingTemplate;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "traject")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<TrajectStatusovergang> trajectStatusovergangen =
		new ArrayList<TrajectStatusovergang>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "traject")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<TrajectUitvoerder> uitvoerders = new ArrayList<TrajectUitvoerder>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Verantwoordelijke", nullable = false)
	@ForeignKey(name = "FK_Traject_Verantw")
	@Index(name = "idx_Traject_Verantw")
	private Medewerker verantwoordelijke;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "traject")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<TrajectBijlage> bijlagen = new ArrayList<TrajectBijlage>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Template", nullable = true)
	@ForeignKey(name = "FK_Traject_Template")
	@Index(name = "idx_Traject_Template")
	@AutoForm(label = "Template", htmlClasses = "unit_max", description = "Met trajecttemplate "
		+ "kunnen de meeste velden van een traject automatisch gevuld worden. Let op: bij het "
		+ "wijzigen van dit veld zullen waardes overschreven worden. Koppelingen naar "
		+ "aanleidingen zullen alleen gelegd worden indien de benodigde rechten aanwezig zijn.")
	private TrajectTemplate trajectTemplate;

	@Lob
	@Basic(optional = true)
	@AutoForm(editorClass = TextEditorPanel.class, displayClass = HtmlLabel.class)
	private String doelen;

	@Lob
	@Basic(optional = true)
	@AutoForm(editorClass = TextEditorPanel.class, displayClass = HtmlLabel.class)
	private String beginSituatie;

	@Lob
	@Basic(optional = true)
	@AutoForm(editorClass = TextEditorPanel.class, displayClass = HtmlLabel.class)
	private String handelingen;

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "traject")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<TrajectNietTonenInZorgvierkant> nietTonenInZorgvierkants =
		new ArrayList<TrajectNietTonenInZorgvierkant>();

	public Traject()
	{
	}

	public Traject(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		setDeelnemer(deelnemer);
		setVerbintenis(verbintenis);
	}

	public Date getBeoogdeEinddatum()
	{
		return beoogdeEinddatum;
	}

	public void setBeoogdeEinddatum(Date beoogdeEinddatum)
	{
		this.beoogdeEinddatum = beoogdeEinddatum;
	}

	public String getTitel()
	{
		return titel;
	}

	public void setTitel(String titel)
	{
		this.titel = titel;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public TrajectSoort getTrajectSoort()
	{
		return trajectSoort;
	}

	public void setTrajectSoort(TrajectSoort trajectSoort)
	{
		this.trajectSoort = trajectSoort;
	}

	public TrajectStatusSoort getTrajectStatusSoort()
	{
		return trajectStatusSoort;
	}

	public void setTrajectStatusSoort(TrajectStatusSoort trajectStatusSoort)
	{
		this.trajectStatusSoort = trajectStatusSoort;
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	public Verbintenis getVerbintenis()
	{
		return verbintenis;
	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = verbintenis;
	}

	public List<Aanleiding> getAanleidingen()
	{
		return aanleidingen;
	}

	public void setAanleidingen(List<Aanleiding> aanleidingen)
	{
		this.aanleidingen = aanleidingen;
	}

	public List<BegeleidingsHandeling> getBegeleidingsHandelingen()
	{
		return begeleidingsHandelingen;
	}

	public void setBegeleidingsHandelingen(List<BegeleidingsHandeling> begeleidingsHandelingen)
	{
		this.begeleidingsHandelingen = begeleidingsHandelingen;
	}

	public BegeleidingsHandelingTemplate getEindHandelingTemplate()
	{
		return eindHandelingTemplate;
	}

	public void setEindHandelingTemplate(BegeleidingsHandelingTemplate eindHandelingTemplate)
	{
		this.eindHandelingTemplate = eindHandelingTemplate;
	}

	public List<TrajectStatusovergang> getTrajectStatusovergangen()
	{
		return trajectStatusovergangen;
	}

	public void setTrajectStatusovergangen(List<TrajectStatusovergang> trajectStatusovergangen)
	{
		this.trajectStatusovergangen = trajectStatusovergangen;
	}

	public List<TrajectUitvoerder> getUitvoerders()
	{
		return uitvoerders;
	}

	public void setUitvoerders(List<TrajectUitvoerder> uitvoerders)
	{
		this.uitvoerders = uitvoerders;
	}

	public Medewerker getVerantwoordelijke()
	{
		return verantwoordelijke;
	}

	public void setVerantwoordelijke(Medewerker verantwoordelijke)
	{
		this.verantwoordelijke = verantwoordelijke;
	}

	public void setVertrouwelijk(boolean vertrouwelijk)
	{
		this.vertrouwelijk = vertrouwelijk;
	}

	@Override
	public boolean isVertrouwelijk()
	{
		return vertrouwelijk;
	}

	public List<TrajectBijlage> getBijlagen()
	{
		return bijlagen;
	}

	public void setBijlagen(List<TrajectBijlage> bijlagen)
	{
		this.bijlagen = bijlagen;
	}

	public Integer getZorglijn()
	{
		return zorglijn;
	}

	public void setZorglijn(Integer zorglijn)
	{
		this.zorglijn = zorglijn;
	}

	public TrajectTemplate getTrajectTemplate()
	{
		return trajectTemplate;
	}

	public void setTrajectTemplate(TrajectTemplate trajectTemplate)
	{
		this.trajectTemplate = trajectTemplate;
	}

	public String getAanleiding()
	{
		return aanleiding;
	}

	public void setAanleiding(String aanleiding)
	{
		this.aanleiding = aanleiding;
	}

	public String getDoelen()
	{
		return doelen;
	}

	public void setDoelen(String doelen)
	{
		this.doelen = doelen;
	}

	public String getBeginSituatie()
	{
		return beginSituatie;
	}

	public void setBeginSituatie(String beginSituatie)
	{
		this.beginSituatie = beginSituatie;
	}

	public String getHandelingen()
	{
		return handelingen;
	}

	public void setHandelingen(String handelingen)
	{
		this.handelingen = handelingen;
	}

	@Override
	public TrajectBijlage addBijlage(Bijlage bijlage)
	{
		TrajectBijlage newBijlage = new TrajectBijlage();
		newBijlage.setBijlage(bijlage);
		newBijlage.setDeelnemer(getDeelnemer());
		newBijlage.setTraject(this);

		getBijlagen().add(newBijlage);

		return newBijlage;
	}

	@Override
	public boolean bestaatBijlage(Bijlage bijlage)
	{
		for (TrajectBijlage deelbijlage : getBijlagen())
		{
			if (deelbijlage.getBijlage().equals(bijlage))
				return true;
		}
		return false;
	}

	@Override
	public String getSecurityId()
	{
		return TRAJECT;
	}

	@Override
	public String getVertrouwelijkSecurityId()
	{
		return VERTROUWELIJK_TRAJECT;
	}

	private TrajectNietTonenInZorgvierkant findNietTonen()
	{
		Medewerker medewerker = EduArteContext.get().getMedewerker();
		for (TrajectNietTonenInZorgvierkant curTonen : getNietTonenInZorgvierkants())
		{
			if (curTonen.getMedewerker().equals(medewerker))
				return curTonen;
		}
		return null;
	}

	@Override
	public boolean isTonenInZorgvierkant()
	{
		return findNietTonen() == null;
	}

	public void setTonenInZorgvierkant(boolean tonenInZorgvierkant)
	{
		TrajectNietTonenInZorgvierkant nietTonen = findNietTonen();
		if (nietTonen == null && !tonenInZorgvierkant)
		{
			TrajectNietTonenInZorgvierkant newNietTonen =
				new TrajectNietTonenInZorgvierkant(this, EduArteContext.get().getMedewerker());
			getNietTonenInZorgvierkants().add(newNietTonen);
		}
		else if (nietTonen != null && tonenInZorgvierkant)
		{
			getNietTonenInZorgvierkants().remove(nietTonen);
		}
	}

	public List<TrajectNietTonenInZorgvierkant> getNietTonenInZorgvierkants()
	{
		return nietTonenInZorgvierkants;
	}

	public void setNietTonenInZorgvierkants(
			List<TrajectNietTonenInZorgvierkant> nietTonenInZorgvierkants)
	{
		this.nietTonenInZorgvierkants = nietTonenInZorgvierkants;
	}

	public boolean isUitvoerende(Medewerker medewerker)
	{
		for (TrajectUitvoerder curUitvoerder : getUitvoerders())
			if (curUitvoerder.getMedewerker().equals(medewerker))
				return true;
		return false;
	}

	public boolean isEindHandelingAanmaken()
	{
		if (getEindHandelingTemplate() == null)
			return false;

		for (BegeleidingsHandeling bh : getBegeleidingsHandelingen())
		{
			if (bh.getStatus() == null || !bh.getStatus().isEindStatus() || bh.isEindHandeling())
				return false;
		}

		return true;
	}

	public void maakEindHandelingAan()
	{
		if (isEindHandelingAanmaken())
			getEindHandelingTemplate().createHandelingen(this);
	}

	public List<Gesprek> getGesprekken()
	{
		List<Gesprek> gesprekken = new ArrayList<Gesprek>();
		for (BegeleidingsHandeling handeling : getBegeleidingsHandelingen())
		{
			if (handeling.getClass().isAssignableFrom(Gesprek.class))
			{
				gesprekken.add((Gesprek) handeling);
			}
		}
		return gesprekken;
	}

	public List<Taak> getTaken()
	{
		List<Taak> taken = new ArrayList<Taak>();
		for (BegeleidingsHandeling handeling : getBegeleidingsHandelingen())
		{
			if (handeling.getClass().isAssignableFrom(Taak.class))
			{
				taken.add((Taak) handeling);
			}
		}
		return taken;
	}

	public List<TestAfname> getTestAfnames()
	{
		List<TestAfname> testafnames = new ArrayList<TestAfname>();
		for (BegeleidingsHandeling handeling : getBegeleidingsHandelingen())
		{
			if (handeling.getClass().isAssignableFrom(TestAfname.class))
			{
				testafnames.add((TestAfname) handeling);
			}
		}
		return testafnames;
	}

	public List<GeplandeBegeleidingsHandeling> getRecenteHandelingen()
	{
		TrajectDataAccessHelper helper =
			DataAccessRegistry.getHelper(TrajectDataAccessHelper.class);

		List<GeplandeBegeleidingsHandeling> alle = helper.getRecenteHandelingen(this, 2);

		return alle;
	}

	/**************************************************************************************************************************
	 *** Sectie met getters voor samenvoeg velden
	 ************************************************************************************************************************** 
	 */

	private boolean doVertrouwlijkeSercurityCheck()
	{
		return ZorgvierkantObjectSecurityCheck.isAllowed(Render.class, this);
	}

	@Exportable
	public Date getExportableBeoogdeEinddatum()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getBeoogdeEinddatum();
		return null;
	}

	@Exportable
	public String getExportableBeoogdeEinddatumString()
	{
		if (getExportableBeoogdeEinddatum() != null)
		{
			return new SimpleDateFormat("dd MMMMM yyyy", new Locale("nl", "NL"))
				.format(getExportableBeoogdeEinddatum());
		}
		return "";
	}

	@Exportable
	public String geExportableTitel()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getTitel();
		return null;
	}

	@Exportable
	public String getExportableOmschrijving()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getOmschrijving();
		return null;
	}

	@Exportable
	public TrajectSoort getExportableTrajectSoort()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getTrajectSoort();
		return null;
	}

	@Exportable
	public Deelnemer getExportableDeelnemer()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getDeelnemer();
		return null;
	}

	@Exportable
	public List<Aanleiding> getExportableAanleidingen()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getAanleidingen();
		return null;
	}

	@Exportable
	public Medewerker getExportableVerantwoordelijke()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getVerantwoordelijke();
		return null;
	}

	@Exportable
	public boolean isExportableVertrouwelijk()
	{
		// security check niet nodig
		return isVertrouwelijk();
	}

	@Exportable
	public Integer getExportableZorglijn()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getZorglijn();
		return null;
	}

	@Exportable
	public String getExportableAanleiding()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getAanleiding();
		return null;
	}

	@Exportable
	public String getExportableDoelen()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getDoelen();
		return null;
	}

	@Exportable
	public String getExportableBeginSituatie()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getBeginSituatie();
		return null;
	}

	@Exportable
	public String getExportableHandelingen()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getHandelingen();
		return null;
	}

	@Exportable
	public List<Gesprek> getExportableGesprekken()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getGesprekken();
		return null;
	}

	@Exportable
	public List<Taak> getExportableTaken()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getTaken();
		return null;
	}

	@Exportable
	public List<TestAfname> getExportableTestAfnames()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getTestAfnames();
		return null;
	}
}
