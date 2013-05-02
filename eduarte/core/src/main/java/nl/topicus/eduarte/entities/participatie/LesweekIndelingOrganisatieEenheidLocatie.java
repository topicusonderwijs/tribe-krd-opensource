package nl.topicus.eduarte.entities.participatie;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import nl.topicus.cobra.util.JavaUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Table(name = "LesweekIndelingOrgLoc")
public class LesweekIndelingOrganisatieEenheidLocatie extends InstellingEntiteit implements
		IOrganisatieEenheidLocatieKoppelEntiteit<LesweekIndelingOrganisatieEenheidLocatie>,
		Comparable<LesweekIndelingOrganisatieEenheidLocatie>
{

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatieEenheid", nullable = false)
	@Index(name = "idx_LiOrgLoc_orgEhd")
	@AutoForm(label = "Organisatie-eenheid", htmlClasses = "unit_max")
	private OrganisatieEenheid organisatieEenheid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "locatie", nullable = true)
	@Index(name = "idx_LiOrgLoc_locatie")
	@AutoForm(htmlClasses = "unit_max")
	private Locatie locatie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lesweekIndeling", nullable = false)
	@Index(name = "idx_LiOrgLoc_lesweekindeling")
	public LesweekIndeling lesweekIndeling;

	public LesweekIndelingOrganisatieEenheidLocatie()
	{
	}

	public LesweekIndelingOrganisatieEenheidLocatie(OrganisatieEenheid organisatieEenheid,
			Locatie locatie)
	{
		setOrganisatieEenheid(organisatieEenheid);
		setLocatie(locatie);
	}

	public LesweekIndelingOrganisatieEenheidLocatie(LesweekIndeling lesweekindeling)
	{
		setLesweekIndeling(lesweekindeling);
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

	/**
	 * geeft de lesweek indeling weer van dit object als deze null is zal het object bij
	 * zijn parent gaan vragen
	 */
	public LesweekIndeling getLesweekIndeling()
	{
		LesweekIndeling returnLesweek = lesweekIndeling;
		if (returnLesweek == null)
		{
			// if (organisatieEenheid != null)
			// {
			// returnLesweek = organisatieEenheid.getLesweekindeling();
			// }
			// else
			return null;
		}
		return returnLesweek;
	}

	/**
	 * geeft de lesweek indeling weer van dit object weer zonder bij zijn parent te vragen
	 */
	public LesweekIndeling getLesweekndelingVanObject()
	{
		return lesweekIndeling;
	}

	public void setLesweekIndeling(LesweekIndeling lesweekIndeling)
	{
		this.lesweekIndeling = lesweekIndeling;
	}

	@Override
	public LesweekIndeling getEntiteit()
	{
		return getLesweekIndeling();
	}

	@Override
	public int compareTo(LesweekIndelingOrganisatieEenheidLocatie o)
	{
		if (o.getOrganisatieEenheid().equals(getOrganisatieEenheid()))
		{
			if (JavaUtil.equalsOrBothNull(o.getLocatie(), getLocatie()))
			{
				return 0;
			}
		}
		else if (o.getOrganisatieEenheid().isParentOf(getOrganisatieEenheid()))
		{
			return -1;
		}
		return 1;
	}

	@Override
	public boolean isActief(Date peildatum)
	{
		return true;
	}
}
