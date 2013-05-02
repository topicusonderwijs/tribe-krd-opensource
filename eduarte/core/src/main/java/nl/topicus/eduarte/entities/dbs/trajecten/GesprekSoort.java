package nl.topicus.eduarte.entities.dbs.trajecten;

import javax.persistence.*;

import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.wiquery.TextEditorPanel;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.AfspraakType;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class GesprekSoort extends InstellingEntiteit implements IActiefEntiteit,
		OrganisatieEenheidLocatieProvider
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatieEenheid", nullable = false)
	@Index(name = "idx_gesprekS_organisatieE")
	@AutoForm(label = "Organisatie-eenheid", htmlClasses = "unit_max")
	private OrganisatieEenheid organisatieEenheid;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "locatie", nullable = true)
	@Index(name = "idx_gesprekS_Locatie")
	@AutoForm(label = "Locatie", htmlClasses = "unit_max")
	private Locatie locatie;

	@Column(nullable = false, length = 41)
	@AutoForm(htmlClasses = "unit_max")
	private String naam;

	@Column(nullable = false)
	private boolean actief = true;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "afspraakType", nullable = false)
	@Index(name = "idx_gespreksoort_afspraakT")
	@AutoForm(htmlClasses = "unit_max")
	private AfspraakType afspraakType;

	@Column(nullable = false)
	@AutoForm(label = "Verslag versturen")
	private boolean standaardVerslagVersturen;

	@Column(nullable = false)
	private boolean oudersUitnodigen;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private BegeleidingsHandelingsStatussoort standaardStatus;

	@Lob
	@Column(nullable = true)
	@AutoForm(editorClass = TextEditorPanel.class)
	private String verslagTemplate;

	public GesprekSoort()
	{
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;
	}

	public Locatie getLocatie()
	{
		return locatie;
	}

	public void setLocatie(Locatie locatie)
	{
		this.locatie = locatie;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public boolean isStandaardVerslagVersturen()
	{
		return standaardVerslagVersturen;
	}

	public void setStandaardVerslagVersturen(boolean standaardVerslagVersturen)
	{
		this.standaardVerslagVersturen = standaardVerslagVersturen;
	}

	public boolean isOudersUitnodigen()
	{
		return oudersUitnodigen;
	}

	public void setOudersUitnodigen(boolean oudersUitnodigen)
	{
		this.oudersUitnodigen = oudersUitnodigen;
	}

	public String getVerslagTemplate()
	{
		return verslagTemplate;
	}

	public void setVerslagTemplate(String verslagTemplate)
	{
		this.verslagTemplate = verslagTemplate;
	}

	public BegeleidingsHandelingsStatussoort getStandaardStatus()
	{
		return standaardStatus;
	}

	public void setStandaardStatus(BegeleidingsHandelingsStatussoort standaardStatus)
	{
		this.standaardStatus = standaardStatus;
	}

	public boolean isActief()
	{
		return actief;
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}

	public AfspraakType getAfspraakType()
	{
		return afspraakType;
	}

	public void setAfspraakType(AfspraakType afspraakType)
	{
		this.afspraakType = afspraakType;
	}

	@Override
	public String toString()
	{
		return getNaam();
	}
}
