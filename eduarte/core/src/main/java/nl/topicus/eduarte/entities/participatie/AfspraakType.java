package nl.topicus.eduarte.entities.participatie;

import java.awt.Color;

import javax.persistence.*;

import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.wiquery.Colordisplay;
import nl.topicus.cobra.web.components.wiquery.Colorpicker;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakTypeCategory;
import nl.topicus.eduarte.entities.participatie.enums.DeelnemerPresentieRegistratie;
import nl.topicus.eduarte.entities.participatie.enums.OnderwijsproductGebruik;
import nl.topicus.eduarte.entities.participatie.enums.StandaardUitnodigingenVersturen;
import nl.topicus.eduarte.participatie.web.components.choice.combobox.AfspraakTypeCategoryComboBox;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;

import org.hibernate.annotations.Index;

/**
 * Afspraken kunnen getypeerd worden om verschillend default gedrag aan verschillende
 * soorten afspraken te kunnen koppelen.
 * 
 * @author loite
 */
@Entity()
@javax.persistence.Table(uniqueConstraints = @UniqueConstraint(columnNames = {"naam",
	"organisatieEenheid"}))
public class AfspraakType extends InstellingEntiteit implements OrganisatieEenheidLocatieProvider,
		IActiefEntiteit
{
	private static final long serialVersionUID = 1L;

	public static final String SECURITY = "SECURITY";

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatieEenheid", nullable = false)
	@Index(name = "idx_afspraakT_organisatieE")
	@AutoForm(label = "Organisatie-eenheid", htmlClasses = "unit_max")
	private OrganisatieEenheid organisatieEenheid;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "locatie", nullable = true)
	@Index(name = "idx_afspraakT_Locatie")
	@AutoForm(label = "Locatie", htmlClasses = "unit_max")
	private Locatie locatie;

	@Column(nullable = false, length = 30)
	@AutoForm(htmlClasses = "unit_max")
	private String naam;

	@Column(nullable = false, length = 1000)
	@AutoForm(htmlClasses = "unit_max")
	private String omschrijving;

	@Column(nullable = false)
	@AutoForm(include = false)
	private int standaardKleur;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@AutoForm(label = "Categorie", editorClass = AfspraakTypeCategoryComboBox.class)
	private AfspraakTypeCategory category;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private StandaardUitnodigingenVersturen uitnodigingenVersturen =
		StandaardUitnodigingenVersturen.ALLE;

	@Column(nullable = false)
	@AutoForm(label = "Alleen voor medewerkers")
	private boolean medewerkerOnly;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OnderwijsproductGebruik onderwijsproductGebruik;

	@Column(nullable = false)
	@AutoForm(label = "Percentage IIVO", htmlClasses = "unit_40")
	private int percentageIIVO;

	@Column(nullable = false)
	@AutoForm(label = "Presentieregistratie")
	private boolean presentieRegistratieDefault;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@AutoForm(label = "Presentieregistratie door deelnemer")
	private DeelnemerPresentieRegistratie presentieRegistratie;

	@Column(nullable = false)
	@AutoForm(order = 100)
	private boolean actief;

	public AfspraakType()
	{
	}

	public boolean isActief()
	{
		return actief;
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;
	}

	public int getStandaardKleur()
	{
		return standaardKleur;
	}

	public void setStandaardKleur(int standaardKleur)
	{
		this.standaardKleur = standaardKleur;
	}

	@AutoForm(editorClass = Colorpicker.class, displayClass = Colordisplay.class, label = "Standaard kleur")
	public Color getStandaardKleurObj()
	{
		return new Color(standaardKleur);
	}

	public void setStandaardKleurObj(Color standaardKleur)
	{
		this.standaardKleur = standaardKleur.getRGB();
	}

	public AfspraakTypeCategory getCategory()
	{
		return category;
	}

	public void setCategory(AfspraakTypeCategory category)
	{
		this.category = category;
	}

	public StandaardUitnodigingenVersturen getUitnodigingenVersturen()
	{
		return uitnodigingenVersturen;
	}

	public void setUitnodigingenVersturen(StandaardUitnodigingenVersturen uitnodigingenVersturen)
	{
		this.uitnodigingenVersturen = uitnodigingenVersturen;
	}

	@Override
	public String toString()
	{
		return getNaam();
	}

	public void setMedewerkerOnly(boolean medewerkerOnly)
	{
		this.medewerkerOnly = medewerkerOnly;
	}

	public boolean isMedewerkerOnly()
	{
		return medewerkerOnly;
	}

	public void setOnderwijsproductGebruik(OnderwijsproductGebruik onderwijsproductGebruik)
	{
		this.onderwijsproductGebruik = onderwijsproductGebruik;
	}

	public OnderwijsproductGebruik getOnderwijsproductGebruik()
	{
		return onderwijsproductGebruik;
	}

	public void setPercentageIIVO(int percentageIIVO)
	{
		this.percentageIIVO = percentageIIVO;
	}

	public int getPercentageIIVO()
	{
		return percentageIIVO;
	}

	public void setPresentieRegistratie(DeelnemerPresentieRegistratie presentieRegistratie)
	{
		this.presentieRegistratie = presentieRegistratie;
	}

	public DeelnemerPresentieRegistratie getPresentieRegistratie()
	{
		return presentieRegistratie;
	}

	public void setPresentieRegistratieDefault(boolean presentieRegistratieDefault)
	{
		this.presentieRegistratieDefault = presentieRegistratieDefault;
	}

	public boolean isPresentieRegistratieDefault()
	{
		return presentieRegistratieDefault;
	}

	public void setLocatie(Locatie locatie)
	{
		this.locatie = locatie;
	}

	public Locatie getLocatie()
	{
		return locatie;
	}
}
