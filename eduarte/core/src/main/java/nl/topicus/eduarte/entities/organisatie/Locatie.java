package nl.topicus.eduarte.entities.organisatie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.eduarte.entities.Contacteerbaar;
import nl.topicus.eduarte.entities.ContacteerbaarUtil;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.adres.Adresseerbaar;
import nl.topicus.eduarte.entities.adres.AdresseerbaarUtil;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.web.components.choice.BrinLocatieCombobox;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * Locatie van een onderwijsinstelling. Dit is iets anders dan een organisatie-eenheid.
 * Een locatie is een fysiek gebouw, een organisatie-eenheid is een administratieve
 * eenheid binnen een instelling.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
@BatchSize(size = 100)
@Table(appliesTo = "Locatie", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
@IsViewWhenOnNoise
@Exportable
public class Locatie extends BeginEinddatumInstellingEntiteit implements
		Adresseerbaar<LocatieAdres>, Contacteerbaar<LocatieContactgegeven>
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code", nullable = true)
	@Index(name = "idx_Locatie_brincode")
	@AutoForm(editorClass = BrinLocatieCombobox.class)
	private Brin brincode;

	@Column(nullable = false, length = 10)
	private String afkorting;

	@Column(nullable = false, length = 100)
	@AutoForm(label = "Locatie", htmlClasses = "unit_max")
	private String naam;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "locatie")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
	@OrderBy(value = "volgorde")
	private List<LocatieContactgegeven> contactgegevens;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "locatie")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
	@OrderBy(value = "begindatum DESC")
	private List<LocatieAdres> adressen = new ArrayList<LocatieAdres>();

	public Locatie()
	{
	}

	public Locatie(Date beginDatum)
	{
		setBegindatum(beginDatum);
	}

	public void setBrincode(Brin brincode)
	{
		this.brincode = brincode;
	}

	@Exportable
	public Brin getBrincode()
	{
		return brincode;
	}

	@Exportable
	public String getAfkorting()
	{
		return afkorting;
	}

	public void setAfkorting(String afkorting)
	{
		this.afkorting = afkorting;
	}

	@Exportable
	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	@Override
	public String toString()
	{
		return getNaam();
	}

	public Brin getBrincodeInstelling()
	{
		return getOrganisatie().getBrincode();
	}

	@Override
	public List<LocatieAdres> getAdressen()
	{
		return adressen;
	}

	@Override
	public void setAdressen(List<LocatieAdres> adressen)
	{
		this.adressen = adressen;
	}

	@Override
	public List<LocatieAdres> getAdressenOpPeildatum()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil.createPeildatumFilter());
	}

	@Override
	public List<LocatieAdres> getFysiekAdressen()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil.createFysiekadresFilter());
	}

	@Override
	@Exportable
	public List<LocatieAdres> getFysiekAdressenOpPeildatum()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil
			.createFysiekadresOpPeildatumFilter());
	}

	@Override
	public List<LocatieAdres> getPostAdressen()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil.createPostadresFilter());
	}

	@Override
	public List<LocatieAdres> getPostAdressenOpPeildatum()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil
			.createPostadresOpPeildatumFilter());
	}

	@Override
	public List<LocatieAdres> getFactuurAdressen()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil.createFactuuradresFilter());
	}

	@Override
	public List<LocatieAdres> getFactuurAdressenOpPeildatum()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil
			.createFactuuradresOpPeildatumFilter());
	}

	@Override
	public LocatieAdres getFysiekAdres()
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createFysiekadresOpPeildatumFilter());
	}

	@Override
	public LocatieAdres getFysiekAdres(Date peildatum)
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createFysiekadresOpPeildatumFilter(peildatum));
	}

	@Override
	public LocatieAdres getPostAdres()
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createPostadresOpPeildatumFilter());
	}

	@Override
	public LocatieAdres getPostAdres(Date peildatum)
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createPostadresOpPeildatumFilter(peildatum));
	}

	@Override
	public LocatieAdres getFactuurAdres()
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createFactuuradresOpPeildatumFilter());
	}

	@Override
	public LocatieAdres getFactuurAdres(Date peildatum)
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createFactuuradresOpPeildatumFilter(peildatum));
	}

	@Override
	public LocatieAdres newAdres()
	{
		LocatieAdres nieuwAdres = new LocatieAdres();
		nieuwAdres.setBegindatum(TimeUtil.getInstance().currentDate());
		nieuwAdres.setLocatie(this);
		return nieuwAdres;
	}

	@Override
	public String getFysiekAdresOmschrijving()
	{
		return "Bezoekadres";
	}

	@Override
	public String getPostAdresOmschrijving()
	{
		return "Postadres";
	}

	@Override
	public String getFactuurAdresOmschrijving()
	{
		return "Factuuradres";
	}

	@Override
	public List<LocatieContactgegeven> getContactgegevens()
	{
		if (contactgegevens == null)
			contactgegevens = new ArrayList<LocatieContactgegeven>();

		return contactgegevens;
	}

	@Override
	public void setContactgegevens(List<LocatieContactgegeven> contactgegevens)
	{
		this.contactgegevens = contactgegevens;
	}

	@Override
	public LocatieContactgegeven newContactgegeven()
	{
		LocatieContactgegeven gegeven = new LocatieContactgegeven();
		gegeven.setLocatie(this);
		gegeven.setVolgorde(getContactgegevens().size());

		return gegeven;
	}

	public List<LocatieContactgegeven> getContactgegevens(SoortContactgegeven soort)
	{
		return ContacteerbaarUtil.getContactgegevens(this, soort);
	}

	@AutoForm(label = "Eerste e-mailadres")
	@AutoFormEmbedded
	public LocatieContactgegeven getEersteEmailAdres()
	{
		return ContacteerbaarUtil.getEersteEmailAdres(this);
	}

	@AutoForm
	@AutoFormEmbedded
	public LocatieContactgegeven getEersteHomepage()
	{
		return ContacteerbaarUtil.getEersteHomepage(this);
	}

	@AutoForm
	@AutoFormEmbedded
	public LocatieContactgegeven getEersteMobieltelefoon()
	{
		return ContacteerbaarUtil.getEersteMobieltelefoon(this);
	}

	@AutoForm
	@AutoFormEmbedded
	public LocatieContactgegeven getEersteOverig()
	{
		return ContacteerbaarUtil.getEersteOverig(this);
	}

	@AutoForm
	@AutoFormEmbedded
	public LocatieContactgegeven getEersteTelefoon()
	{
		return ContacteerbaarUtil.getEersteTelefoon(this);
	}
}
