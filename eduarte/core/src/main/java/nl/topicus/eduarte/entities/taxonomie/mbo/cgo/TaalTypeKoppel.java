package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author vandenbrink
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class TaalTypeKoppel extends LandelijkOfInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false)
	@JoinColumn(name = "type", nullable = false)
	@Index(name = "idx_taaltypeK_type")
	private TaalType type;

	@ManyToOne(optional = false)
	@JoinColumn(name = "taal", nullable = false)
	@Index(name = "idx_taaltypeK_taal")
	private ModerneTaal taal;

	protected TaalTypeKoppel()
	{
	}

	public TaalTypeKoppel(EntiteitContext context)
	{
		super(context);
	}

	public TaalType getType()
	{
		return type;
	}

	public void setType(TaalType type)
	{
		this.type = type;
	}

	public ModerneTaal getTaal()
	{
		return taal;
	}

	public void setTaal(ModerneTaal taal)
	{
		this.taal = taal;
	}
}
