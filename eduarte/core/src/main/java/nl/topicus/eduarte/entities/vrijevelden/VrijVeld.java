package nl.topicus.eduarte.entities.vrijevelden;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.web.components.choice.TaxonomieCombobox;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(name = "VrijVeld", uniqueConstraints = {@UniqueConstraint(columnNames = {
	"naam", "organisatie"})})
public class VrijVeld extends InstellingEntiteit implements IActiefEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	@AutoForm(htmlClasses = "unit_max")
	private String naam;

	@Column(nullable = false)
	private boolean actief = true;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@Index(name = "idx_VrijVeld_VrijVeldType")
	@AutoForm(description = "Het type bepaalt het soort waarde dat dit vrije veld kan hebben")
	private VrijVeldType type;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@Index(name = "idx_VrijVeld_VrijVeldCategorie")
	@AutoForm(description = "De categorie bepaalt op welke type pagina's het vrije veld wordt getoond en/of een waarde gegeven kan worden. De onderstaande vinkjes geven aan op welk soort pagina's het getoond zal worden.")
	private VrijVeldCategorie categorie;

	@Column(nullable = false)
	@AutoForm(description = "Dit veld geeft aan of het veld getoond moet worden op intake schermen")
	private boolean intakescherm;

	@Column(nullable = false)
	@AutoForm(description = "Dit veld geeft aan of het veld getoond moet worden op de bijbehorende schermen")
	private boolean dossierscherm = true;

	@Column(nullable = false)
	@AutoForm(description = "Dit veld geeft aan of het veld gebruikt moet worden voor uitgebreid zoeken", label = "Uitgebreid zoeken")
	private boolean uitgebreidzoeken;

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vrijVeld")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<VrijVeldKeuzeOptie> vrijVeldKeuzeOpties = new ArrayList<VrijVeldKeuzeOptie>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "taxonomie", nullable = true)
	@Index(name = "idx_VrijVeld_Taxonomie")
	@AutoForm(required = false, editorClass = TaxonomieCombobox.class)
	private Taxonomie taxonomie;

	public VrijVeld()
	{
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public boolean isActief()
	{
		return actief;
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}

	public VrijVeldType getType()
	{
		return type;
	}

	public void setType(VrijVeldType type)
	{
		this.type = type;
	}

	public VrijVeldCategorie getCategorie()
	{
		return categorie;
	}

	public void setCategorie(VrijVeldCategorie categorie)
	{
		this.categorie = categorie;
	}

	public boolean isIntakescherm()
	{
		return intakescherm;
	}

	public void setIntakescherm(boolean intakescherm)
	{
		this.intakescherm = intakescherm;
	}

	public boolean isDossierscherm()
	{
		return dossierscherm;
	}

	public void setDossierscherm(boolean dossierscherm)
	{
		this.dossierscherm = dossierscherm;
	}

	public boolean isUitgebreidzoeken()
	{
		return uitgebreidzoeken;
	}

	public void setUitgebreidzoeken(boolean uitgebreidzoeken)
	{
		this.uitgebreidzoeken = uitgebreidzoeken;
	}

	public List<VrijVeldKeuzeOptie> getVrijVeldKeuzeOpties()
	{
		if (vrijVeldKeuzeOpties == null)
			vrijVeldKeuzeOpties = new ArrayList<VrijVeldKeuzeOptie>();
		return vrijVeldKeuzeOpties;
	}

	public void setVrijVeldKeuzeOpties(List<VrijVeldKeuzeOptie> vrijVeldKeuzeOpties)
	{
		this.vrijVeldKeuzeOpties = vrijVeldKeuzeOpties;
	}

	public List<VrijVeldKeuzeOptie> getBeschikbareKeuzeOpties()
	{
		List<VrijVeldKeuzeOptie> ret = new ArrayList<VrijVeldKeuzeOptie>();
		for (VrijVeldKeuzeOptie curOptie : getVrijVeldKeuzeOpties())
			if (curOptie.isActief())
				ret.add(curOptie);
		return ret;
	}

	public void setTaxonomie(Taxonomie taxonomie)
	{
		this.taxonomie = taxonomie;
	}

	public Taxonomie getTaxonomie()
	{
		return taxonomie;
	}
}
