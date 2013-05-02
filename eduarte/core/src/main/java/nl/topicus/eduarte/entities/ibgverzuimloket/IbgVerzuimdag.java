package nl.topicus.eduarte.entities.ibgverzuimloket;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.onderwijs.ibgverzuimloket.model.IIbgVerzuimdag;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class IbgVerzuimdag extends InstellingEntiteit implements IIbgVerzuimdag
{

	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private Date datum;

	@Column(nullable = false)
	private boolean heledag = true;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "verzuimmelding", nullable = false)
	@Index(name = "idx_verzuimdag_melding")
	private IbgVerzuimmelding verzuimmelding;

	@Column(nullable = true)
	private boolean lesuur1;

	@Column(nullable = true)
	private boolean lesuur2;

	@Column(nullable = true)
	private boolean lesuur3;

	@Column(nullable = true)
	private boolean lesuur4;

	@Column(nullable = true)
	private boolean lesuur5;

	@Column(nullable = true)
	private boolean lesuur6;

	@Column(nullable = true)
	private boolean lesuur7;

	@Column(nullable = true)
	private boolean lesuur8;

	@Column(nullable = true)
	private boolean lesuur9;

	@Column(nullable = true)
	private boolean lesuur10;

	@Column(nullable = true)
	private boolean lesuur11;

	@Column(nullable = true)
	private boolean lesuur12;

	public IbgVerzuimdag()
	{
		setDatum(new Date());
	}

	@Override
	public Date getDatum()
	{
		return datum;
	}

	@Override
	public boolean isHeledag()
	{
		return heledag;
	}

	public IbgVerzuimmelding getVerzuimmelding()
	{
		return verzuimmelding;
	}

	@Override
	public void setDatum(Date value)
	{
		datum = value;

	}

	@Override
	public void setHeledag(boolean value)
	{
		heledag = value;
	}

	public void setVerzuimmelding(IbgVerzuimmelding melding)
	{
		verzuimmelding = melding;
	}

	@Override
	public List<String> getVerzuimLesuren()
	{
		List<String> lesuren = new ArrayList<String>();
		if (getLesuur1())
		{
			lesuren.add("1");
		}
		if (getLesuur2())
		{
			lesuren.add("2");
		}
		if (getLesuur3())
		{
			lesuren.add("3");
		}
		if (getLesuur4())
		{
			lesuren.add("4");
		}
		if (getLesuur5())
		{
			lesuren.add("5");
		}
		if (getLesuur6())
		{
			lesuren.add("6");
		}
		if (getLesuur7())
		{
			lesuren.add("7");
		}
		if (getLesuur8())
		{
			lesuren.add("8");
		}
		if (getLesuur9())
		{
			lesuren.add("9");
		}
		if (getLesuur10())
		{
			lesuren.add("10");
		}
		if (getLesuur11())
		{
			lesuren.add("11");
		}
		if (getLesuur12())
		{
			lesuren.add("12");
		}

		return lesuren;
	}

	public void setLesuur1(boolean value)
	{
		lesuur1 = value;
	}

	public void setLesuur2(boolean value)
	{
		lesuur2 = value;
	}

	public void setLesuur3(boolean value)
	{
		lesuur3 = value;
	}

	public void setLesuur4(boolean value)
	{
		lesuur4 = value;
	}

	public void setLesuur5(boolean value)
	{
		lesuur5 = value;
	}

	public void setLesuur6(boolean value)
	{
		lesuur6 = value;
	}

	public void setLesuur7(boolean value)
	{
		lesuur7 = value;
	}

	public void setLesuur8(boolean value)
	{
		lesuur8 = value;
	}

	public void setLesuur9(boolean value)
	{
		lesuur9 = value;
	}

	public void setLesuur10(boolean value)
	{
		lesuur10 = value;
	}

	public void setLesuur11(boolean value)
	{
		lesuur11 = value;
	}

	public void setLesuur12(boolean value)
	{
		lesuur12 = value;
	}

	public boolean getLesuur1()
	{
		return lesuur1;
	}

	public boolean getLesuur2()
	{
		return lesuur2;
	}

	public boolean getLesuur3()
	{
		return lesuur3;
	}

	public boolean getLesuur4()
	{
		return lesuur4;
	}

	public boolean getLesuur5()
	{
		return lesuur5;
	}

	public boolean getLesuur6()
	{
		return lesuur6;
	}

	public boolean getLesuur7()
	{
		return lesuur7;
	}

	public boolean getLesuur8()
	{
		return lesuur8;
	}

	public boolean getLesuur9()
	{
		return lesuur9;
	}

	public boolean getLesuur10()
	{
		return lesuur10;
	}

	public boolean getLesuur11()
	{
		return lesuur11;
	}

	public boolean getLesuur12()
	{
		return lesuur12;
	}
}
