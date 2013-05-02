package nl.topicus.eduarte.entities;

import java.io.Serializable;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEntiteit;
import nl.topicus.eduarte.entities.organisatie.Instelling;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Immutable;

/**
 * Class voor het mappen van views naar entiteiten. Een view kan bijvoorbeeld
 * geaggregeerde gegevens bevatten.
 * 
 * @author loite
 */
@MappedSuperclass
@Immutable
public abstract class ViewEntiteit implements IOrganisatieEntiteit, IdObject
{
	private static final long serialVersionUID = 1L;

	@Id
	@AccessType("property")
	private String id;

	/**
	 * De instelling waarbij deze entiteit hoort.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatie", nullable = false)
	private Instelling organisatie;

	public ViewEntiteit()
	{
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	@Override
	public Serializable getIdAsSerializable()
	{
		return getId();
	}

	@Override
	public Long getVersion()
	{
		return 0l;
	}

	@Override
	public boolean isSaved()
	{
		return true;
	}

	@Override
	public void setVersion(Long version)
	{
		throw new UnsupportedOperationException("View heeft geen version");
	}

	@Override
	public Instelling getOrganisatie()
	{
		return organisatie;
	}

	/**
	 * <strong>Let op:</strong> Sets de INSTELLING
	 * 
	 * @param instelling
	 *            The instelling to set.
	 */
	public void setOrganisatie(Instelling instelling)
	{
		this.organisatie = instelling;
	}
}
