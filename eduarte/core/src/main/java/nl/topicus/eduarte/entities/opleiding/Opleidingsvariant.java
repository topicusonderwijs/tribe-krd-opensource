package nl.topicus.eduarte.entities.opleiding;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.reflection.copy.CopyManager;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.HibernateObjectCopyToSubclassManager;
import nl.topicus.eduarte.entities.vrijevelden.OpleidingVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldOptieKeuze;
import nl.topicus.eduarte.providers.OpleidingProvider;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Een opleidingsvariant is een variant van een andere opleiding. Dit is bijvoorbeeld het
 * geval wanneer een student zich in eerste instantie voor een opleiding aanmeldt en zich
 * in een later stadium specialiseert in een bepaalde richting. Er kunnen ook
 * instroomvarianten zijn voor bijv. verschillende vooropleidingen.
 * 
 * <p>
 * Opleidingsvariant is een subclass van opleiding. Hierdoor functioneert een
 * opleidingsvariant als een gewone opleiding, in de zin dat er studenten op kunnen worden
 * ingeschreven, onderwijsproducten voor kunnen worden afgenomen en diploma’s kunnen
 * worden uitgegeven. Een opleidingsvariant hoort daarnaast ook bij een opleiding (de
 * parent). De criteriumbank past de regels van zowel de opleidingsvariant als zijn
 * bovenliggende opleiding toe. Wanneer studenten op opleiding worden geselecteerd, worden
 * ook studenten geselecteerd die zijn ingeschreven op de onderliggende
 * opleidingsvarianten.
 * </p>
 * 
 * <p>
 * De parent van deze opleidingsvariant mag niet zelf een opleidingsvariant zijn: de
 * hiërarchie is max. 2 lagen diep. De setParent-methode dwingt dit af.
 * </p>
 * 
 * @author hop
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@BatchSize(size = 20)
@Exportable
public class Opleidingsvariant extends Opleiding implements OpleidingProvider
{
	private static final long serialVersionUID = 1L;

	private boolean instroomvariant = true;

	private boolean uitstroomvariant = true;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "parent")
	@Index(name = "idx_Opleidingsvariant_parent")
	private Opleiding parent;

	public boolean isInstroomvariant()
	{
		return instroomvariant;
	}

	public void setInstroomvariant(boolean instroomvariant)
	{
		this.instroomvariant = instroomvariant;
	}

	public boolean isUitstroomvariant()
	{
		return uitstroomvariant;
	}

	public void setUitstroomvariant(boolean uitstroomvariant)
	{
		this.uitstroomvariant = uitstroomvariant;
	}

	public Opleiding getParent()
	{
		return parent;
	}

	/**
	 * @param parent
	 *            bovenliggende opleiding. Mag niet zelf een Opleidingsvariant zijn.
	 */
	public void setParent(Opleiding parent)
	{
		Asserts.assertFalse("parent mag geen Opleidingsvariant zijn",
			parent instanceof Opleidingsvariant);

		this.parent = parent;
	}

	public Opleidingsvariant()
	{
		super();
	}

	/**
	 * Maakt een opleidingsvariant van de gegeven opleiding. Alle properties, het aanbod
	 * en de vrije velden worden gekopieerd van de parent, met uitzondering van de code en
	 * naam.
	 */
	public static Opleidingsvariant maakVariantVan(Opleiding parent)
	{
		CopyManager manager =
			new HibernateObjectCopyToSubclassManager(Opleidingsvariant.class, Opleiding.class,
				OpleidingAanbod.class, OpleidingVrijVeld.class, VrijVeldOptieKeuze.class);
		Opleidingsvariant variant = (Opleidingsvariant) manager.copyObject(parent);
		variant.setParent(parent);

		variant.setCode(null);
		variant.setNaam(null);
		return variant;
	}
}
