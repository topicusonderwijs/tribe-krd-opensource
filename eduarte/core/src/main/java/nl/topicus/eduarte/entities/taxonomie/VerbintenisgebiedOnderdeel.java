package nl.topicus.eduarte.entities.taxonomie;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Een verbintenisgebied kan onderdeel zijn van een ander verbintenisgebied, zoals LLB een
 * onderdeel is van elke uitstroom.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"parent", "child"})})
public class VerbintenisgebiedOnderdeel extends LandelijkOfInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent", nullable = false)
	@Index(name = "idx_verbintenisonderdeel_prnt")
	private Verbintenisgebied parent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "child", nullable = false)
	@Index(name = "idx_verbintenisonderdeel_child")
	private Verbintenisgebied child;

	/**
	 * Default constructor voor Hibernate.
	 */
	protected VerbintenisgebiedOnderdeel()
	{
	}

	public VerbintenisgebiedOnderdeel(EntiteitContext context)
	{
		super(context);
	}

	/**
	 * Constructor
	 * 
	 * @param parent
	 * @param child
	 */
	public VerbintenisgebiedOnderdeel(Verbintenisgebied parent, Verbintenisgebied child)
	{
		super(parent.isLandelijk());
		setParent(parent);
		setChild(child);
	}

	/**
	 * @return Returns the parent.
	 */
	public Verbintenisgebied getParent()
	{
		return parent;
	}

	/**
	 * @param parent
	 *            The parent to set.
	 */
	public void setParent(Verbintenisgebied parent)
	{
		this.parent = parent;
	}

	/**
	 * @return Returns the child.
	 */
	public Verbintenisgebied getChild()
	{
		return child;
	}

	/**
	 * @param child
	 *            The child to set.
	 */
	public void setChild(Verbintenisgebied child)
	{
		this.child = child;
	}

}
