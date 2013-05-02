package nl.topicus.eduarte.krd.web.pages.deelnemer.examens.correctiestaat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.dao.helpers.ResultaatDataAccessHelper;
import nl.topicus.eduarte.dao.hibernate.ResultaatHibernateDataAccessHelper.TypeResultaat;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

public class VerbintenisOnderwijsproductResultaten implements Serializable, IDetachable
{
	private static final long serialVersionUID = 1L;

	private IModel<Verbintenis> verbintenis;

	private IModel<Onderwijsproduct> onderwijsproduct;

	private BigDecimal seCijfer;

	private Integer cseScore1;

	private BigDecimal cseCijfer1;

	private BigDecimal speEindcijfer1;

	private BigDecimal ceCijfer1;

	private BigDecimal eindCijfer1;

	private Integer cseScore2;

	private BigDecimal cseCijfer2;

	private BigDecimal speEindcijfer2;

	private BigDecimal ceCijfer2;

	private BigDecimal eindCijfer2;

	private BigDecimal definitiefEindcijfer;

	private Integer tijdvak;

	public VerbintenisOnderwijsproductResultaten(IModel<Verbintenis> verbintenis,
			IModel<Onderwijsproduct> onderwijsproduct, Integer tijdvak)
	{
		this.verbintenis = verbintenis;
		this.onderwijsproduct = onderwijsproduct;
		this.tijdvak = tijdvak;
		setAlleResultaten();
	}

	private void setAlleResultaten()
	{
		Verbintenis verb = getVerbintenis();
		Deelnemer deelnemer = verb.getDeelnemer();
		ResultaatDataAccessHelper helper =
			DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class);
		List<OnderwijsproductAfname> afnames = getAfnames();

		Map<Onderwijsproduct, List<Resultaat>> ceResultaten =
			helper.getDefinitieveCentraalExamenResultaten(deelnemer, afnames);
		Map<Onderwijsproduct, Resultaat> seResultaten =
			helper.getDefinitieveSchoolexamenResultaten(deelnemer, afnames, TypeResultaat.Hoogste);
		for (OnderwijsproductAfname afname : afnames)
		{
			Onderwijsproduct product = afname.getOnderwijsproduct();
			List<Resultaat> eindcijfers = null;
			if (seResultaten.containsKey(product))
			{
				Resultaat seResultaat = seResultaten.get(product);
				if (seResultaat.getCijfer() != null)
				{
					Resultaat SE = seResultaten.get(product);
					setSeCijfer(SE.getCijfer());
					eindcijfers = helper.getActueleResultaten(SE.getToets().getParent(), deelnemer);
				}
			}
			if (ceResultaten.containsKey(product))
			{
				List<Resultaat> ceCijfers = ceResultaten.get(product);
				Resultaat CE1 = getHerkansing(ceCijfers, 0);
				Resultaat CE2 = getHerkansing(ceCijfers, 1);

				if (CE1 != null)
				{
					eindcijfers =
						helper.getActueleResultaten(CE1.getToets().getParent(), deelnemer);
					setCeCijfer1(CE1.getCijfer());
					if (CE1.getToets().isSamengesteld())
					{
						Toets cseToets = null;
						Toets cspeToets = null;
						for (Toets toets : CE1.getToets().getChildren())
						{
							if (toets.getCode().equalsIgnoreCase("cspe"))
								cspeToets = toets;
							if (toets.getCode().equalsIgnoreCase("cse"))
								cseToets = toets;
						}
						if (cseToets != null)
						{
							List<Resultaat> results =
								helper.getActueleResultaten(cseToets, deelnemer);
							Resultaat res = getHerkansing(results, 0);
							if (res != null)
							{
								setCseCijfer1(res.getCijfer());
								setCseScore1(res.getScore());
							}

						}
						if (cspeToets != null)
						{
							List<Resultaat> results =
								helper.getActueleResultaten(cspeToets, deelnemer);
							Resultaat res = getHerkansing(results, 0);
							if (res != null)
								setSpeEindcijfer1(res.getCijfer());

						}
					}
					else
					{
						setCseCijfer1(CE1.getCijfer());
						setCseScore1(CE1.getScore());
					}
				}

				if (CE2 != null)
				{
					setCeCijfer2(CE2.getCijfer());
					if (CE2.getToets().isSamengesteld())
					{
						Toets cseToets = null;
						Toets cspeToets = null;
						for (Toets toets : CE2.getToets().getChildren())
						{
							if (toets.getCode().equalsIgnoreCase("cspe"))
								cspeToets = toets;
							if (toets.getCode().equalsIgnoreCase("cse"))
								cseToets = toets;
						}
						if (cseToets != null)
						{
							List<Resultaat> results =
								helper.getActueleResultaten(cseToets, deelnemer);
							Resultaat res = getHerkansing(results, 1);
							if (res != null)
							{
								setCseCijfer2(res.getCijfer());
								setCseScore2(res.getScore());
							}

						}
						if (cspeToets != null)
						{
							List<Resultaat> results =
								helper.getActueleResultaten(cspeToets, deelnemer);
							Resultaat res = getHerkansing(results, 1);
							if (res != null)
								setSpeEindcijfer2(res.getCijfer());

						}

					}
					else
					{
						setCseCijfer2(CE2.getCijfer());
						setCseScore2(CE2.getScore());
					}
				}
			}

			if (eindcijfers != null)
			{
				Resultaat eindRes1 = getHerkansing(eindcijfers, 0);
				Resultaat eindRes2 = getHerkansing(eindcijfers, 1);
				if (eindRes1 != null)
					setEindCijfer1(eindRes1.getCijfer());
				if (eindRes2 != null)
					setEindCijfer2(eindRes2.getCijfer());

				if (getGeldendResultaat(eindcijfers) != null)
					setDefinitiefEindcijfer(getGeldendResultaat(eindcijfers).getCijfer());
			}
		}
	}

	private List<OnderwijsproductAfname> getAfnames()
	{
		List<OnderwijsproductAfname> ret = new ArrayList<OnderwijsproductAfname>();
		for (OnderwijsproductAfname afname : getVerbintenis().getOnderwijsproductAfnames())
		{
			if (afname.getOnderwijsproduct().equals(getOnderwijsproduct()))
				ret.add(afname);
		}
		return ret;
	}

	private Resultaat getGeldendResultaat(List<Resultaat> eindcijfers)
	{
		for (Resultaat resultaat : eindcijfers)
		{
			if (resultaat.isGeldend())
				return resultaat;
		}
		return null;
	}

	private Resultaat getHerkansing(List<Resultaat> resultaten, int herkansingNr)
	{
		for (Resultaat resultaat : resultaten)
		{
			if (resultaat.getHerkansingsnummer() == herkansingNr)
				return resultaat;
		}
		return null;
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(verbintenis);
		ComponentUtil.detachQuietly(onderwijsproduct);
	}

	public Verbintenis getVerbintenis()
	{
		return getModelObject(verbintenis);
	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = makeModelFor(verbintenis);
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return getModelObject(onderwijsproduct);
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = makeModelFor(onderwijsproduct);
	}

	/**
	 * Wraps a null check around getting the object from the model.
	 * 
	 * @param model
	 * @return the object from the model or null if the model is null.
	 */
	protected <Y> Y getModelObject(IModel<Y> model)
	{
		if (model != null)
			return model.getObject();
		return null;
	}

	/**
	 * Wraps a null check around building a model for a value.
	 * 
	 * @param object
	 * @return a new IModel or null if the object is null.
	 */
	protected <T> IModel<T> makeModelFor(T object)
	{
		if (object != null)
			return ModelFactory.getModel(object);
		return null;
	}

	public BigDecimal getSeCijfer()
	{
		return seCijfer;
	}

	public void setSeCijfer(BigDecimal seCijfer)
	{
		this.seCijfer = seCijfer;
	}

	public Integer getCseScore1()
	{
		return tijdvak == 2 ? cseScore1 : null;
	}

	public void setCseScore1(Integer cseScore1)
	{
		this.cseScore1 = cseScore1;
	}

	public BigDecimal getCseCijfer1()
	{
		return tijdvak == 2 ? cseCijfer1 : null;
	}

	public void setCseCijfer1(BigDecimal cseCijfer1)
	{
		this.cseCijfer1 = cseCijfer1;
	}

	public BigDecimal getCeCijfer1()
	{
		return tijdvak == 2 ? ceCijfer1 : null;
	}

	public void setCeCijfer1(BigDecimal ceCijfer1)
	{
		this.ceCijfer1 = ceCijfer1;
	}

	public BigDecimal getEindCijfer1()
	{
		return tijdvak == 2 ? eindCijfer1 : null;
	}

	public void setEindCijfer1(BigDecimal eindCijfer1)
	{
		this.eindCijfer1 = eindCijfer1;
	}

	public Integer getCseScore2()
	{
		return cseScore2;
	}

	public void setCseScore2(Integer cseScore2)
	{
		this.cseScore2 = cseScore2;
	}

	public BigDecimal getCseCijfer2()
	{
		return cseCijfer2;
	}

	public void setCseCijfer2(BigDecimal cseCijfer2)
	{
		this.cseCijfer2 = cseCijfer2;
	}

	public BigDecimal getCeCijfer2()
	{
		return ceCijfer2;
	}

	public void setCeCijfer2(BigDecimal ceCijfer2)
	{
		this.ceCijfer2 = ceCijfer2;
	}

	public BigDecimal getEindCijfer2()
	{
		return eindCijfer2;
	}

	public void setEindCijfer2(BigDecimal eindCijfer2)
	{
		this.eindCijfer2 = eindCijfer2;
	}

	public void setVerbintenis(IModel<Verbintenis> verbintenis)
	{
		this.verbintenis = verbintenis;
	}

	public void setOnderwijsproduct(IModel<Onderwijsproduct> onderwijsproduct)
	{
		this.onderwijsproduct = onderwijsproduct;
	}

	public BigDecimal getSpeEindcijfer1()
	{
		return tijdvak == 2 ? speEindcijfer1 : null;
	}

	public void setSpeEindcijfer1(BigDecimal speEindcijfer1)
	{
		this.speEindcijfer1 = speEindcijfer1;
	}

	public BigDecimal getSpeEindcijfer2()
	{
		return speEindcijfer2;
	}

	public void setSpeEindcijfer2(BigDecimal speEindcijfer2)
	{
		this.speEindcijfer2 = speEindcijfer2;
	}

	public BigDecimal getDefinitiefEindcijfer()
	{
		return definitiefEindcijfer;
	}

	public void setDefinitiefEindcijfer(BigDecimal definitiefEindcijfer)
	{
		this.definitiefEindcijfer = definitiefEindcijfer;
	}
}