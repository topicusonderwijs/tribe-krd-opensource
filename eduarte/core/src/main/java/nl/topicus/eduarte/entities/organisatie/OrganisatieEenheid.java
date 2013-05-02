/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
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

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.FieldPersistance;
import nl.topicus.cobra.entities.FieldPersistenceMode;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.util.StringUtil.StringConverter;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.OrganisatieEenheidDataAccessHelper;
import nl.topicus.eduarte.entities.Contacteerbaar;
import nl.topicus.eduarte.entities.ContacteerbaarUtil;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.adres.Adresseerbaar;
import nl.topicus.eduarte.entities.adres.AdresseerbaarUtil;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.personen.OrganisatieEenheidContactPersoon;
import nl.topicus.eduarte.web.components.choice.SoortOrganisatieEenheidCombobox;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * Een organisatie-eenheid van een onderwijsinstelling. Dit is geen fysieke locatie (of
 * hoeft dat niet te zijn), maar is eerder een organisatorische eenheid, bijvoorbeeld een
 * bepaalde afdeling.
 * 
 * @author loite
 */
@Entity()
@Exportable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
@BatchSize(size = 100)
@Table(appliesTo = "OrganisatieEenheid", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
@IsViewWhenOnNoise
public class OrganisatieEenheid extends BeginEinddatumInstellingEntiteit implements
		Comparable<OrganisatieEenheid>, Adresseerbaar<OrganisatieEenheidAdres>,
		Contacteerbaar<OrganisatieEenheidContactgegeven>
{
	private static final long serialVersionUID = 1L;

	@Column(length = 10, nullable = false)
	@Index(name = "idx_orgEhd_afkorting")
	private String afkorting;

	@Column(length = 100, nullable = false)
	@AutoForm(label = "Naam", htmlClasses = "unit_max")
	private String naam;

	@Column(length = 150, nullable = true)
	private String officieleNaam;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent", nullable = true)
	@Index(name = "idx_OrgEhd_parent")
	@AutoForm(label = "Overkoepelende eenheid")
	@FieldPersistance(FieldPersistenceMode.SAVE)
	private OrganisatieEenheid parent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "soortOrganisatieEenheid", nullable = false)
	@Index(name = "idx_OrgEhd_soortOrgEhd")
	@AutoForm(label = "Soort organisatie-eenheid", editorClass = SoortOrganisatieEenheidCombobox.class)
	@FieldPersistance(FieldPersistenceMode.SAVE)
	private SoortOrganisatieEenheid soortOrganisatieEenheid;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organisatieEenheid")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
	@OrderBy(value = "begindatum DESC")
	private List<OrganisatieEenheidAdres> adressen = new ArrayList<OrganisatieEenheidAdres>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organisatieEenheid")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
	@OrderBy(value = "volgorde")
	private List<OrganisatieEenheidContactgegeven> contactgegevens;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organisatieEenheid")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
	@BatchSize(size = 100)
	private List<OrganisatieEenheidLocatie> locaties;

	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "lesweekindeling", nullable = true)
	// @Index(name = "idx_OrgEhdL_lesweekindeling")
	// private LesweekIndeling lesweekindeling;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organisatieEenheid")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
	@BatchSize(size = 100)
	private List<OrganisatieEenheidContactPersoon> contactpersonen;

	@Column(length = 11, nullable = true)
	@AutoForm(label = "Rekeningnummer")
	private String bankrekeningnummer;

	@Column(nullable = false)
	@AutoForm(description = "Indicatie die aangeeft of stap-3 van de intake wizard overgeslagen moet worden als deze organisatie-eenheid gekozen is", label = "Intake wizard stap-3 overslaan")
	private boolean intakeWizardStap3Overslaan;

	@Column(nullable = false)
	@AutoForm(description = "Indicatie die aangeeft of de organisatie-eenheid getoont moet worden als keuze bij digitaal aanmelden")
	private boolean tonenBijDigitaalAanmelden;

	public OrganisatieEenheid()
	{
	}

	public OrganisatieEenheid(Date beginDatum)
	{
		setBegindatum(beginDatum);
	}

	public OrganisatieEenheid getParent()
	{
		return parent;
	}

	public void setParent(OrganisatieEenheid parent)
	{
		this.parent = parent;
	}

	/**
	 * 
	 * @param soortNaam
	 * @return De organisatie-eenheid die ergens als parent voorkomt van deze
	 *         organisatie-eenheid waarbij de naam van de soort organisatie-eenheid
	 *         overeenkomt met de gegeven soortnaam.
	 */
	@Exportable
	public OrganisatieEenheid getParentVanSoort(String soortNaam)
	{
		if (getSoortOrganisatieEenheid().getNaam().equals(soortNaam))
			return this;
		if (getParent() == null)
			return null;
		return getParent().getParentVanSoort(soortNaam);
	}

	public SoortOrganisatieEenheid getSoortOrganisatieEenheid()
	{
		return soortOrganisatieEenheid;
	}

	public void setSoortOrganisatieEenheid(SoortOrganisatieEenheid soortOrganisatieEenheid)
	{
		this.soortOrganisatieEenheid = soortOrganisatieEenheid;
	}

	/**
	 * @return Een lijst met alle organisatieeenheden die hoger in de hierarchie staan dan
	 *         deze, plus deze organisatieeenheid.
	 */
	public List<OrganisatieEenheid> getParents()
	{
		List<OrganisatieEenheid> res = new ArrayList<OrganisatieEenheid>(10);
		res.add(this);
		internalGetParents(res);

		return res;
	}

	private void internalGetParents(List<OrganisatieEenheid> res)
	{
		if (getParent() == null)
			return;
		res.add(getParent());
		getParent().internalGetParents(res);
	}

	/**
	 * Kijkt of deze organisatieeenheid een parent is van de ander, of ze gelijk zijn.
	 * 
	 * @param other
	 * @return true als dit een parent is van de ander
	 */
	public boolean isParentOf(OrganisatieEenheid other)
	{
		if (other.equals(this))
			return true;
		return other.getParent() != null && isParentOf(other.getParent());
	}

	/**
	 * Lijst met alle organisatie-eenheden die lager in de hierarchy staan, inclusief
	 * deze.
	 * 
	 * @return lijst (volgorde niet gespecificeerd)
	 */
	public List<OrganisatieEenheid> getActieveChildren(Date peildatum)
	{
		return getBatchDataAccessHelper().getChildren(this, peildatum);
	}

	/**
	 * Lijst met organisatie-eenheden die child zijn van deze en die actief zijn op
	 * peildatum.
	 * 
	 * @return lijst (volgorde niet gespecificeerd)
	 */
	public List<OrganisatieEenheid> getActieveDirectChildren(Date peildatum)
	{
		return getBatchDataAccessHelper().getDirectChildren(this, peildatum);
	}

	/**
	 * Lijst met alle organisatie-eenheden die lager in de hierarchie staan, inclusief
	 * deze, alsmede alle parents van deze organisatie-eenheid
	 * 
	 * @return lijst met organisatie-eenheden
	 */
	public List<OrganisatieEenheid> getParentsEnChildren(Date peildatum)
	{
		List<OrganisatieEenheid> res = getBatchDataAccessHelper().getChildren(this, peildatum);
		OrganisatieEenheid currentParent = getParent();
		while (currentParent != null)
		{
			res.add(currentParent);
			currentParent = currentParent.getParent();
		}
		return res;
	}

	@Override
	protected OrganisatieEenheidDataAccessHelper getBatchDataAccessHelper()
	{
		return DataAccessRegistry.getHelper(OrganisatieEenheidDataAccessHelper.class);
	}

	@Override
	public int compareTo(OrganisatieEenheid o)
	{
		return getPad().compareTo(o.getPad());
	}

	@Override
	public String toString()
	{
		return getNaam();
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

	@Exportable
	public String getOfficieleNaam()
	{
		return officieleNaam;
	}

	public void setOfficieleNaam(String officieleNaam)
	{
		this.officieleNaam = officieleNaam;
	}

	public List<OrganisatieEenheidLocatie> getLocaties()
	{
		if (locaties == null)
			locaties = new ArrayList<OrganisatieEenheidLocatie>();
		return locaties;
	}

	public List<OrganisatieEenheidLocatie> getActieveLocaties(Date peildatum)
	{
		List<OrganisatieEenheidLocatie> actieveLocaties =
			new ArrayList<OrganisatieEenheidLocatie>();
		for (OrganisatieEenheidLocatie locatie : getAlleLocaties())
		{
			if (locatie.isActief(peildatum) && locatie.getLocatie().isActief(peildatum))
				actieveLocaties.add(locatie);
		}

		return actieveLocaties;
	}

	public void setLocaties(List<OrganisatieEenheidLocatie> locaties)
	{
		this.locaties = locaties;
	}

	public String getLocatieNamen()
	{
		return StringUtil.toString(getLocaties(), "",
			new StringConverter<OrganisatieEenheidLocatie>()
			{

				@Override
				public String getSeparator(int listIndex)
				{
					return ", ";
				}

				@Override
				public String toString(OrganisatieEenheidLocatie object, int listIndex)
				{
					return object.getLocatie().getNaam();
				}

			});
	}

	/**
	 * @param locatie
	 * @return true indien deze organisatie-eenheid verbonden is aan de gegeven locatie,
	 *         of direct of door een van de parents.
	 */
	public boolean isVerbondenAanLocatie(Locatie locatie, Date peildatum)
	{
		for (OrganisatieEenheid orgEhd : getParents())
		{
			for (OrganisatieEenheidLocatie loc : orgEhd.getLocaties())
			{
				if (loc.isActief(peildatum) && loc.getLocatie() != null)
				{
					if (loc.getLocatie().equals(locatie))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * @param locatie
	 * @return true indien deze organisatie-eenheid verbonden is aan de gegeven locatie,
	 *         parents en childs worden dus buiten beschouwing gelaten.
	 */
	public boolean isAlleenVerbondenAanHuidigeEenheid(Locatie locatie)
	{
		for (OrganisatieEenheidLocatie loc : getLocaties())
		{
			if (loc.getLocatie() != null)
			{
				if (loc.getLocatie().equals(locatie))
				{
					return true;
				}

			}
		}
		return false;
	}

	@Exportable
	@Override
	public List<OrganisatieEenheidAdres> getAdressen()
	{
		return adressen;
	}

	@Override
	public void setAdressen(List<OrganisatieEenheidAdres> adressen)
	{
		this.adressen = adressen;
	}

	@Exportable
	@Override
	public List<OrganisatieEenheidAdres> getAdressenOpPeildatum()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil.createPeildatumFilter());
	}

	@Exportable
	@Override
	public List<OrganisatieEenheidAdres> getFysiekAdressen()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil.createFysiekadresFilter());
	}

	@Exportable
	@Override
	public List<OrganisatieEenheidAdres> getFysiekAdressenOpPeildatum()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil
			.createFysiekadresOpPeildatumFilter());
	}

	@Exportable
	@Override
	public List<OrganisatieEenheidAdres> getPostAdressen()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil.createPostadresFilter());
	}

	@Exportable
	@Override
	public List<OrganisatieEenheidAdres> getPostAdressenOpPeildatum()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil
			.createPostadresOpPeildatumFilter());
	}

	@Exportable
	@Override
	public List<OrganisatieEenheidAdres> getFactuurAdressen()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil.createFactuuradresFilter());
	}

	@Exportable
	@Override
	public List<OrganisatieEenheidAdres> getFactuurAdressenOpPeildatum()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil
			.createFactuuradresOpPeildatumFilter());
	}

	@Exportable
	@Override
	public OrganisatieEenheidAdres getFysiekAdres()
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createFysiekadresOpPeildatumFilter());
	}

	@Override
	public OrganisatieEenheidAdres getFysiekAdres(Date peildatum)
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createFysiekadresOpPeildatumFilter(peildatum));
	}

	@Exportable
	@Override
	public OrganisatieEenheidAdres getPostAdres()
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createPostadresOpPeildatumFilter());
	}

	@Override
	public OrganisatieEenheidAdres getPostAdres(Date peildatum)
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createPostadresOpPeildatumFilter(peildatum));
	}

	@Exportable
	@Override
	public OrganisatieEenheidAdres getFactuurAdres()
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createFactuuradresOpPeildatumFilter());
	}

	@Override
	public OrganisatieEenheidAdres getFactuurAdres(Date peildatum)
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createFactuuradresOpPeildatumFilter(peildatum));
	}

	@Override
	public OrganisatieEenheidAdres newAdres()
	{
		OrganisatieEenheidAdres nieuwAdres = new OrganisatieEenheidAdres();
		nieuwAdres.setBegindatum(TimeUtil.getInstance().currentDate());
		nieuwAdres.setOrganisatieEenheid(this);
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
	public List<OrganisatieEenheidContactgegeven> getContactgegevens()
	{
		if (contactgegevens == null)
			contactgegevens = new ArrayList<OrganisatieEenheidContactgegeven>();

		return contactgegevens;
	}

	@Override
	public void setContactgegevens(List<OrganisatieEenheidContactgegeven> contactgegevens)
	{
		this.contactgegevens = contactgegevens;
	}

	@Override
	public OrganisatieEenheidContactgegeven newContactgegeven()
	{
		OrganisatieEenheidContactgegeven gegeven = new OrganisatieEenheidContactgegeven();
		gegeven.setOrganisatieEenheid(this);
		gegeven.setVolgorde(getContactgegevens().size());

		return gegeven;
	}

	public List<OrganisatieEenheidContactgegeven> getContactgegevens(SoortContactgegeven soort)
	{
		return ContacteerbaarUtil.getContactgegevens(this, soort);
	}

	@AutoForm(label = "Eerste e-mailadres")
	@AutoFormEmbedded
	public OrganisatieEenheidContactgegeven getEersteEmailAdres()
	{
		return ContacteerbaarUtil.getEersteEmailAdres(this);
	}

	@AutoForm
	@AutoFormEmbedded
	public OrganisatieEenheidContactgegeven getEersteHomepage()
	{
		return ContacteerbaarUtil.getEersteHomepage(this);
	}

	@AutoForm
	@AutoFormEmbedded
	public OrganisatieEenheidContactgegeven getEersteMobieltelefoon()
	{
		return ContacteerbaarUtil.getEersteMobieltelefoon(this);
	}

	@AutoForm
	@AutoFormEmbedded
	public OrganisatieEenheidContactgegeven getEersteOverig()
	{
		return ContacteerbaarUtil.getEersteOverig(this);
	}

	@AutoForm
	@AutoFormEmbedded
	@Exportable
	public OrganisatieEenheidContactgegeven getEersteTelefoon()
	{
		return ContacteerbaarUtil.getEersteTelefoon(this);
	}

	/**
	 * geeft de lesweek indeling weer van dit object als deze null is zal het object bij
	 * zijn parent gaan vragen
	 */
	// public LesweekIndeling getLesweekindeling()
	// {
	// LesweekIndeling returnLesweek = lesweekindeling;
	// if (returnLesweek == null)
	// {
	// if (getParent() != null)
	// {
	// return getParent().getLesweekindeling();
	// }
	// else
	// return null;
	// }
	// return returnLesweek;
	// }
	/**
	 * // * geeft de lesweek indeling weer van dit object weer zonder bij zijn parent te
	 * vragen //
	 */
	// public LesweekIndeling getLesweekndelingVanObject()
	// {
	// return lesweekindeling;
	// }
	//
	// public void setLesweekindeling(LesweekIndeling lesweekindeling)
	// {
	// this.lesweekindeling = lesweekindeling;
	// }
	public List<OrganisatieEenheidContactPersoon> getContactpersonen()
	{
		if (contactpersonen == null)
			contactpersonen = new ArrayList<OrganisatieEenheidContactPersoon>();
		return contactpersonen;
	}

	public void setContactpersonen(List<OrganisatieEenheidContactPersoon> contactpersonen)
	{
		this.contactpersonen = contactpersonen;
	}

	@Exportable(omschrijving = "Geeft de eerste contactpersoon bij deze organisatie-eenheid met de gegeven rol")
	public OrganisatieEenheidContactPersoon getContactpersoonMetRol(String rol)
	{
		for (OrganisatieEenheidContactPersoon cp : getContactpersonen())
		{
			if (cp.getRol() != null && cp.getRol().getNaam().equalsIgnoreCase(rol))
			{
				return cp;
			}
		}
		return null;
	}

	public void setBankrekeningnummer(String bankrekeningnummer)
	{
		this.bankrekeningnummer = bankrekeningnummer;
	}

	@Exportable
	public String getBankrekeningnummer()
	{
		return bankrekeningnummer;
	}

	public void setIntakeWizardStap3Overslaan(boolean intakeWizardStap3Overslaan)
	{
		this.intakeWizardStap3Overslaan = intakeWizardStap3Overslaan;
	}

	public boolean isIntakeWizardStap3Overslaan()
	{
		return intakeWizardStap3Overslaan;
	}

	public List<OrganisatieEenheidLocatie> getAlleLocaties()
	{
		List<OrganisatieEenheidLocatie> ret = new ArrayList<OrganisatieEenheidLocatie>();
		for (OrganisatieEenheid curEenheid : getParents())
		{
			ret.addAll(curEenheid.getLocaties());
		}
		return ret;
	}

	public List<OrganisatieEenheidLocatie> getActieveLocatiesVanParentsEnChildren(Date peildatum)
	{
		List<OrganisatieEenheidLocatie> ret = new ArrayList<OrganisatieEenheidLocatie>();
		for (OrganisatieEenheid curEenheid : getParentsEnChildren(peildatum))
		{
			for (OrganisatieEenheidLocatie locatie : curEenheid.getLocaties())
				if (locatie.isActief(peildatum) && locatie.getLocatie().isActief(peildatum))
					ret.add(locatie);
		}
		return ret;
	}

	public OrganisatieEenheidLocatie getLocatie(Locatie locatie)
	{
		return getLocatie(locatie, EduArteContext.get().getPeildatumOfVandaag());
	}

	public OrganisatieEenheidLocatie getLocatie(Locatie locatie, Date peildatum)
	{
		for (OrganisatieEenheidLocatie curLocatie : getAlleLocaties())
		{
			if (curLocatie.isActief(peildatum) && curLocatie.getLocatie().equals(locatie))
				return curLocatie;
		}
		return null;
	}

	public void setTonenBijDigitaalAanmelden(boolean tonenBijDigitaalAanmelden)
	{
		this.tonenBijDigitaalAanmelden = tonenBijDigitaalAanmelden;
	}

	public boolean isTonenBijDigitaalAanmelden()
	{
		return tonenBijDigitaalAanmelden;
	}

	public Brin getBrincode()
	{
		// TODO: MultiBRIN-support: OrganisatieEenheid krijgt zijn eigen brincode
		return getInstellingBrincode();
	}

	public String getPad()
	{
		return getParent() != null ? getParent().getPad() + "/" + getAfkorting() : getAfkorting();
	}
}
