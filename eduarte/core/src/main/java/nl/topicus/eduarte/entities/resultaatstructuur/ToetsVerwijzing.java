package nl.topicus.eduarte.entities.resultaatstructuur;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.entities.FieldPersistance;
import nl.topicus.cobra.entities.FieldPersistenceMode;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
public class ToetsVerwijzing extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "lezenUit")
	@Index(name = "idx_Toets_lezenUit")
	@AutoForm(include = false)
	@FieldPersistance(FieldPersistenceMode.SAVE)
	private Toets lezenUit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "schrijvenIn")
	@Index(name = "idx_Toets_schrijvenIn")
	@AutoForm(include = false)
	@FieldPersistance(FieldPersistenceMode.SAVE)
	private Toets schrijvenIn;

	public ToetsVerwijzing()
	{
	}

	public Toets getLezenUit()
	{
		return lezenUit;
	}

	public void setLezenUit(Toets lezenUit)
	{
		this.lezenUit = lezenUit;
	}

	public Toets getSchrijvenIn()
	{
		return schrijvenIn;
	}

	public void setSchrijvenIn(Toets schrijvenIn)
	{
		this.schrijvenIn = schrijvenIn;
	}

	@Override
	public String toString()
	{
		return getLezenUit() + " -> " + getSchrijvenIn();
	}
}
