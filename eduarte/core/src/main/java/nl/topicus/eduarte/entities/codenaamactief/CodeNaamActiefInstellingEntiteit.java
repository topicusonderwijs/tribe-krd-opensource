package nl.topicus.eduarte.entities.codenaamactief;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.zoekfilters.ICodeNaamActiefZoekFilter;

/**
 * Basisentiteit voor alle instellingsentiteiten die code, naam en actief kolom hebben
 * 
 * @author loite
 */
@MappedSuperclass()
public abstract class CodeNaamActiefInstellingEntiteit extends InstellingEntiteit implements
		ICodeNaamActiefEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(length = 10, nullable = false)
	@AutoForm(label = "Code", htmlClasses = "unit_max")
	private String code;

	@Column(length = 100, nullable = false)
	@AutoForm(label = "Naam", htmlClasses = "unit_max")
	private String naam;

	@Column(nullable = false)
	private boolean actief = true;

	public CodeNaamActiefInstellingEntiteit()
	{
	}

	@Override
	@Exportable
	public String getCode()
	{
		return code;
	}

	@Override
	public void setCode(String code)
	{
		this.code = code;
	}

	@Override
	@Exportable
	public String getNaam()
	{
		return naam;
	}

	@Override
	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	@Override
	public boolean isActief()
	{
		return actief;
	}

	@Override
	public void setActief(boolean actief)
	{
		this.actief = actief;
	}

	@SuppressWarnings("unchecked")
	public static <T extends ICodeNaamActiefEntiteit> CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper<T, ? extends ICodeNaamActiefZoekFilter<T>> getHelper()
	{
		return DataAccessRegistry
			.getHelper(CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);
	}

	@Override
	public String toString()
	{
		return getNaam();
	}
}
