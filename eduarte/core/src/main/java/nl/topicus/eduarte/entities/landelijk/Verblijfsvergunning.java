package nl.topicus.eduarte.entities.landelijk;

import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.dao.helpers.VerblijfsvergunningDataAccessHelper;
import nl.topicus.eduarte.entities.LandelijkEntiteit;
import nl.topicus.eduarte.entities.codenaamactief.ICodeNaamActiefEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Exportable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class Verblijfsvergunning extends LandelijkEntiteit implements ICodeNaamActiefEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(length = 10, nullable = false)
	private String code;

	@Column(length = 125, nullable = false)
	@AutoForm(htmlClasses = "unit_max")
	private String naam;

	@Column(nullable = false)
	private boolean actief = true;

	/**
	 * Default constructor voor Hibernate
	 */
	public Verblijfsvergunning()
	{
	}

	public static Verblijfsvergunning get(String code)
	{
		return DataAccessRegistry.getHelper(VerblijfsvergunningDataAccessHelper.class).get(code);
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
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

}
