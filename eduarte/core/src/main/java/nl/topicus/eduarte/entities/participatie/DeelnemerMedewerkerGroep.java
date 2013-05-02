package nl.topicus.eduarte.entities.participatie;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.ViewEntiteit;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.participatie.enums.DeelnemerMedewerkerGroepEnum;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Index;

@Entity
@Immutable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "Instelling")
@Table(name = "DeelnemerMedewerkerGroepView")
public class DeelnemerMedewerkerGroep extends ViewEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 100)
	@AutoForm(htmlClasses = "unit_max")
	@Index(name = "idx_DeMeGr_omschrijving")
	private String omschrijving;

	@Column(nullable = false, length = 100)
	@AutoForm(htmlClasses = "unit_max")
	@Index(name = "idx_DeMeGr_code")
	private String code;

	@Column(nullable = false, length = 100)
	@AutoForm(htmlClasses = "unit_max")
	@Index(name = "idx_DeMeGr_voorletters")
	private String voorletters;

	@Column(nullable = false, length = 100)
	@AutoForm(htmlClasses = "unit_max")
	@Index(name = "idx_DeMeGr_voornamen")
	private String voornamen;

	@Column(nullable = false, length = 100)
	@AutoForm(htmlClasses = "unit_max")
	@Index(name = "idx_DeMeGr_voorvoegsel")
	private String voorvoegsel;

	@Column(nullable = false, length = 100)
	@AutoForm(htmlClasses = "unit_max")
	@Index(name = "idx_DeMeGr_naam")
	private String naam;

	@Column(nullable = false, length = 100)
	@AutoForm(editorClass = EnumCombobox.class, htmlClasses = "unit_max")
	@Index(name = "idx_DeMeGr_type")
	@Enumerated(EnumType.STRING)
	private DeelnemerMedewerkerGroepEnum type;

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setType(DeelnemerMedewerkerGroepEnum type)
	{
		this.type = type;
	}

	public DeelnemerMedewerkerGroepEnum getType()
	{
		return type;
	}

	@SuppressWarnings("unchecked")
	public IdObject getEntiteit()
	{
		long id = Long.parseLong(getId().substring(2));
		Class< ? extends IdObject> entityClass;

		// if ((getType().toString()).equals("deelnemer"))
		// entityClass = Deelnemer.class;
		// else if (getType().equals("groep"))
		// entityClass = Groep.class;
		// else if (getType().equals("medewerker"))
		// entityClass = Medewerker.class;

		if (getType() == DeelnemerMedewerkerGroepEnum.deelnemer)
			entityClass = Deelnemer.class;
		else if (getType() == DeelnemerMedewerkerGroepEnum.groep)
			entityClass = Groep.class;
		else if (getType() == DeelnemerMedewerkerGroepEnum.medewerker)
			entityClass = Medewerker.class;
		else
			throw new IllegalStateException("Ongeldig type " + getType());
		BatchDataAccessHelper<IdObject> helper =
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class);
		return helper.get(entityClass, id);
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getCode()
	{
		return code;
	}

	public void setVoorletters(String voorletters)
	{
		this.voorletters = voorletters;
	}

	public String getVoorletters()
	{
		return voorletters;
	}

	public void setVoornamen(String voornamen)
	{
		this.voornamen = voornamen;
	}

	public String getVoornamen()
	{
		return voornamen;
	}

	public void setVoorvoegsel(String voorvoegsel)
	{
		this.voorvoegsel = voorvoegsel;
	}

	public String getVoorvoegsel()
	{
		return voorvoegsel;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public String getNaam()
	{
		return naam;
	}

	@Override
	public String toString()
	{
		String str = "";

		if (getCode() != null)
			str = getCode() + " - ";

		if (getType().equals(DeelnemerMedewerkerGroepEnum.deelnemer) && voornamen != null)
			str += getVoornamen() + " ";

		str += getOmschrijving();

		str += " (" + type + ") ";

		return str;
	}

}
