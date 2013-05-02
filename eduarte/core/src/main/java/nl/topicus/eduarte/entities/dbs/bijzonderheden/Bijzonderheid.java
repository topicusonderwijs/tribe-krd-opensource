package nl.topicus.eduarte.entities.dbs.bijzonderheden;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.text.HtmlLabel;
import nl.topicus.cobra.web.components.wiquery.TextEditorPanel;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.dbs.ZorgvierkantObject;
import nl.topicus.eduarte.entities.dbs.bijlagen.BijzonderheidBijlage;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.components.choice.ZorglijnCombobox;
import nl.topicus.eduarte.web.components.panels.bijlage.BijlageEditField;
import nl.topicus.eduarte.web.components.panels.bijlage.BijlageLinkPanel;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class Bijzonderheid extends InstellingEntiteit implements ZorgvierkantObject,
		DeelnemerProvider, IBijlageKoppelEntiteit<BijzonderheidBijlage>
{
	private static final long serialVersionUID = 1L;

	public static final String BIJZONDERHEID = "BIJZONDERHEID";

	public static final String VERTROUWELIJKE_BIJZONDERHEID = "VERTROUWELIJKE_BIJZONDERHEID";

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = false)
	@Index(name = "idx_Bijzonderheid_deelnemer")
	private Deelnemer deelnemer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auteur", nullable = false)
	@Index(name = "idx_Bijzonderheid_auteur")
	@AutoForm(readOnly = true)
	private Medewerker auteur;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categorie", nullable = false)
	@Index(name = "idx_Bijzonderheid_categorie")
	@AutoForm(htmlClasses = "unit_max")
	private BijzonderheidCategorie categorie;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bijzonderheid")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<ToegekendHulpmiddel> toegekendHulpmiddel = new ArrayList<ToegekendHulpmiddel>();

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = false)
	@AutoForm(readOnly = true)
	private Date datumInvoer;

	@Column(length = 50, nullable = false)
	@AutoForm(htmlClasses = "unit_max")
	private String titel;

	@Lob
	@AutoForm(editorClass = TextEditorPanel.class, displayClass = HtmlLabel.class, required = true)
	private String omschrijving;

	@Column(nullable = false)
	private boolean tonenOpDeelnemerkaart;

	@Column(nullable = false)
	private boolean vertrouwelijk;

	@Column(nullable = true)
	@AutoForm(editorClass = ZorglijnCombobox.class)
	private Integer zorglijn;

	/**
	 * Bijzonderheid tonen in zoeklijsten e.d. als waarschuwingsicoontje.
	 */
	@Column(nullable = false)
	private boolean tonenAlsWaarschuwing;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "handelingsinstructies", nullable = true)
	@Index(name = "idx_Bijzonderhei_handelingsin")
	@AutoForm(editorClass = BijlageEditField.class, displayClass = BijlageLinkPanel.class)
	private Bijlage handelingsinstructies;

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bijzonderheid")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<BijzonderheidBijlage> bijlagen = new ArrayList<BijzonderheidBijlage>();

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bijzonderheid")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<BijzonderheidNietTonenInZorgvierkant> nietTonenInZorgvierkants =
		new ArrayList<BijzonderheidNietTonenInZorgvierkant>();

	public Bijzonderheid()
	{
	}

	public Bijzonderheid(Deelnemer deelnemer)
	{
		setDeelnemer(deelnemer);
	}

	/**
	 * Geeft een nieuwe bijzonderheid voor de gegeven deelnemer met de gegeven medewerker
	 * als auteur. De datum van invoer wordt op de huidige datum/tijd gezet.
	 * 
	 * @param deelnemer
	 *            De deelnemer
	 * @param auteur
	 *            De auteur
	 */
	public Bijzonderheid(Medewerker auteur, Deelnemer deelnemer)
	{
		setAuteur(auteur);
		setDeelnemer(deelnemer);
		setDatumInvoer(TimeUtil.getInstance().currentDateTime());
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	public BijzonderheidCategorie getCategorie()
	{
		return categorie;
	}

	public void setCategorie(BijzonderheidCategorie categorie)
	{
		this.categorie = categorie;
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

	public boolean isTonenOpDeelnemerkaart()
	{
		return tonenOpDeelnemerkaart;
	}

	public void setTonenOpDeelnemerkaart(boolean tonenOpDeelnemerkaart)
	{
		this.tonenOpDeelnemerkaart = tonenOpDeelnemerkaart;
	}

	public boolean isTonenAlsWaarschuwing()
	{
		return tonenAlsWaarschuwing;
	}

	public void setTonenAlsWaarschuwing(boolean tonenAlsWaarschuwing)
	{
		this.tonenAlsWaarschuwing = tonenAlsWaarschuwing;
	}

	public Bijlage getHandelingsinstructies()
	{
		return handelingsinstructies;
	}

	public void setHandelingsinstructies(Bijlage handelingsinstructies)
	{
		this.handelingsinstructies = handelingsinstructies;
	}

	public Medewerker getAuteur()
	{
		return auteur;
	}

	public void setAuteur(Medewerker auteur)
	{
		this.auteur = auteur;
	}

	public Date getDatumInvoer()
	{
		return datumInvoer;
	}

	public void setDatumInvoer(Date datumInvoer)
	{
		this.datumInvoer = datumInvoer;
	}

	public boolean isVertrouwelijk()
	{
		return vertrouwelijk;
	}

	public void setVertrouwelijk(boolean vertrouwelijk)
	{
		this.vertrouwelijk = vertrouwelijk;
	}

	public List<BijzonderheidBijlage> getBijlagen()
	{
		return bijlagen;
	}

	public void setBijlagen(List<BijzonderheidBijlage> bijlagen)
	{
		this.bijlagen = bijlagen;
	}

	public List<BijzonderheidBijlage> getBijlagenZonderInstructie()
	{
		List<BijzonderheidBijlage> lijst = new ArrayList<BijzonderheidBijlage>();
		lijst.addAll(getBijlagen());
		lijst.remove(getHandelingsinstructies());
		return lijst;
	}

	public Integer getZorglijn()
	{
		return zorglijn;
	}

	public void setZorglijn(Integer zorglijn)
	{
		this.zorglijn = zorglijn;
	}

	public List<ToegekendHulpmiddel> getToegekendHulpmiddel()
	{
		return toegekendHulpmiddel;
	}

	public void setToegekendHulpmiddel(List<ToegekendHulpmiddel> toegekendHulpmiddel)
	{
		this.toegekendHulpmiddel = toegekendHulpmiddel;
	}

	public List<Hulpmiddel> getToegekendHulpmiddelAsHulpmiddel()
	{
		List<Hulpmiddel> ret = new ArrayList<Hulpmiddel>();
		for (ToegekendHulpmiddel curHulpmiddel : getToegekendHulpmiddel())
		{
			ret.add(curHulpmiddel.getHulpmiddel());
		}
		return ret;
	}

	@Override
	public BijzonderheidBijlage addBijlage(Bijlage bijlage)
	{
		BijzonderheidBijlage newBijlage = new BijzonderheidBijlage();
		newBijlage.setBijlage(bijlage);
		newBijlage.setDeelnemer(getDeelnemer());
		newBijlage.setBijzonderheid(this);

		getBijlagen().add(newBijlage);

		return newBijlage;
	}

	@Override
	public boolean bestaatBijlage(Bijlage bijlage)
	{
		for (BijzonderheidBijlage deelbijlage : getBijlagen())
		{
			if (deelbijlage.getBijlage().equals(bijlage))
				return true;
		}
		return false;
	}

	@Override
	public String getSecurityId()
	{
		return BIJZONDERHEID;
	}

	@Override
	public String getVertrouwelijkSecurityId()
	{
		return VERTROUWELIJKE_BIJZONDERHEID;
	}

	private BijzonderheidNietTonenInZorgvierkant findNietTonen()
	{
		Medewerker medewerker = EduArteContext.get().getMedewerker();
		for (BijzonderheidNietTonenInZorgvierkant curTonen : getNietTonenInZorgvierkants())
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
		BijzonderheidNietTonenInZorgvierkant nietTonen = findNietTonen();
		if (nietTonen == null && !tonenInZorgvierkant)
		{
			BijzonderheidNietTonenInZorgvierkant newNietTonen =
				new BijzonderheidNietTonenInZorgvierkant(this, EduArteContext.get().getMedewerker());
			getNietTonenInZorgvierkants().add(newNietTonen);
		}
		else if (nietTonen != null && tonenInZorgvierkant)
		{
			getNietTonenInZorgvierkants().remove(nietTonen);
		}
	}

	public List<BijzonderheidNietTonenInZorgvierkant> getNietTonenInZorgvierkants()
	{
		return nietTonenInZorgvierkants;
	}

	public void setNietTonenInZorgvierkants(
			List<BijzonderheidNietTonenInZorgvierkant> nietTonenInZorgvierkants)
	{
		this.nietTonenInZorgvierkants = nietTonenInZorgvierkants;
	}

	public String getVolledigeTitel()
	{
		return getCategorie().getCode() + " - " + getTitel();
	}
}
