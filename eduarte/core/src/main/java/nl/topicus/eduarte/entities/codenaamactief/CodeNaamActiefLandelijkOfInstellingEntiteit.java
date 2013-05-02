package nl.topicus.eduarte.entities.codenaamactief;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;

/**
 * @author idserda
 */
@MappedSuperclass()
public abstract class CodeNaamActiefLandelijkOfInstellingEntiteit extends
		LandelijkOfInstellingEntiteit implements ICodeNaamActiefEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(length = 10, nullable = false)
	private String code;

	@Column(length = 50, nullable = false)
	@AutoForm(htmlClasses = "unit_max")
	private String naam;

	@Column(nullable = false)
	private boolean actief = true;

	public CodeNaamActiefLandelijkOfInstellingEntiteit()
	{
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

	@SuppressWarnings("unchecked")
	public static CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper getHelper()
	{
		return DataAccessRegistry
			.getHelper(CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);
	}
}
