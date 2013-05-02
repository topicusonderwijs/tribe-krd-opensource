package nl.topicus.eduarte.entities.onderwijsproduct;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Samenstelling van onderwijsproducten, dwz dat het parent onderwijsproduct het child
 * onderwijsproduct bevat.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"parent", "child"})})
public class OnderwijsproductSamenstelling extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "parent")
	@Index(name = "idx_ProdSamenstelling_parent")
	private Onderwijsproduct parent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "child")
	@Index(name = "idx_ProdSamenstelling_child")
	private Onderwijsproduct child;

	public OnderwijsproductSamenstelling()
	{
	}

	public OnderwijsproductSamenstelling(Onderwijsproduct parent, Onderwijsproduct child)
	{
		setParent(parent);
		setChild(child);
	}

	public Onderwijsproduct getParent()
	{
		return parent;
	}

	public void setParent(Onderwijsproduct parent)
	{
		this.parent = parent;
	}

	public Onderwijsproduct getChild()
	{
		return child;
	}

	public void setChild(Onderwijsproduct child)
	{
		this.child = child;
	}

}
