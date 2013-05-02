package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.rapportage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import nl.topicus.cobra.util.DecimalUtil;

/**
 * @author vanderkamp
 */
public abstract class AbstractActiviteitTotaal implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String omschrijving;

	private BigDecimal aanbod;

	private BigDecimal aanwezig;

	private BigDecimal afwezig;

	private BigDecimal geoorloofdAfwezig;

	private BigDecimal ongeoorloofdAfwezig;

	private BigDecimal geenReden;

	private BigDecimal procentAanwezig;

	private BigDecimal procentAfwezig;

	private BigDecimal procentGeoorloofdAfwezig;

	private BigDecimal procentOngeoorloofdAfwezig;

	private BigDecimal budget;

	private BigDecimal resterend;

	private int aanbodLesuren;

	private int aanwezigLesuren;

	private int afwezigLesuren;

	private int geoorloofdAfwezigLesuren;

	private int ongeoorloofdAfwezigLesuren;

	private int geenRedenLesuren;

	Map<String, BigDecimal> totalenPerReden = new HashMap<String, BigDecimal>();

	Map<String, Integer> totalenPerRedenLesuren = new HashMap<String, Integer>();

	private static final NumberFormat FORMAT = NumberFormat.getNumberInstance();
	static
	{
		FORMAT.setMinimumFractionDigits(1);
		FORMAT.setMaximumFractionDigits(1);
	}

	public AbstractActiviteitTotaal(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public BigDecimal getAanbod()
	{
		return aanbod;
	}

	public void setAanbod(BigDecimal aanbod)
	{
		this.aanbod = aanbod;
	}

	public BigDecimal getAanwezig()
	{
		return aanwezig;
	}

	public void setAanwezig(BigDecimal aanwezig)
	{
		this.aanwezig = aanwezig;
	}

	public BigDecimal getGeoorloofdAfwezig()
	{
		return geoorloofdAfwezig;
	}

	public void setGeoorloofdAfwezig(BigDecimal geoorloofdAfwezig)
	{
		this.geoorloofdAfwezig = geoorloofdAfwezig;
	}

	public BigDecimal getOngeoorloofdAfwezig()
	{
		return ongeoorloofdAfwezig;
	}

	public void setOngeoorloofdAfwezig(BigDecimal ongeoorloofdAfwezig)
	{
		this.ongeoorloofdAfwezig = ongeoorloofdAfwezig;
	}

	public BigDecimal getProcentAanwezig()
	{
		return procentAanwezig;
	}

	public void setProcentAanwezig(BigDecimal procentAanwezig)
	{
		this.procentAanwezig = procentAanwezig;
	}

	public int getAanbodLesuren()
	{
		return aanbodLesuren;
	}

	public void setAanbodLesuren(int aanbodLesuren)
	{
		this.aanbodLesuren = aanbodLesuren;
	}

	public int getAanwezigLesuren()
	{
		return aanwezigLesuren;
	}

	public void setAanwezigLesuren(int aanwezigLesuren)
	{
		this.aanwezigLesuren = aanwezigLesuren;
	}

	public int getGeoorloofdAfwezigLesuren()
	{
		return geoorloofdAfwezigLesuren;
	}

	public void setGeoorloofdAfwezigLesuren(int geoorloofdAfwezigLesuren)
	{
		this.geoorloofdAfwezigLesuren = geoorloofdAfwezigLesuren;
	}

	public int getOngeoorloofdAfwezigLesuren()
	{
		return ongeoorloofdAfwezigLesuren;
	}

	public void setOngeoorloofdAfwezigLesuren(int ongeoorloofdAfwezigLesuren)
	{
		this.ongeoorloofdAfwezigLesuren = ongeoorloofdAfwezigLesuren;
	}

	public Map<String, BigDecimal> getTotalenPerReden()
	{
		return totalenPerReden;
	}

	public void setTotalenPerReden(Map<String, BigDecimal> totalenPerReden)
	{
		this.totalenPerReden = totalenPerReden;
	}

	/**
	 * @param afkorting
	 * @return Bigdecimal met het totaal voor deze reden
	 */
	public BigDecimal getTotaalVoorReden(String afkorting)
	{
		if (getTotalenPerReden().containsKey(afkorting))
			return getTotalenPerReden().get(afkorting);
		return BigDecimal.ZERO;
	}

	/**
	 * @param afkorting
	 * @return Bigdecimal met het totaal voor deze reden
	 */
	public Integer getTotaalVoorRedenLesuren(String afkorting)
	{
		if (getTotalenPerRedenLesuren().containsKey(afkorting))
			return getTotalenPerRedenLesuren().get(afkorting);
		return 0;
	}

	public Map<String, Integer> getTotalenPerRedenLesuren()
	{
		return totalenPerRedenLesuren;
	}

	public void setTotalenPerRedenLesuren(Map<String, Integer> totalenPerRedenLesuren)
	{
		this.totalenPerRedenLesuren = totalenPerRedenLesuren;
	}

	public BigDecimal getGeenReden()
	{
		return geenReden;
	}

	public void setGeenReden(BigDecimal geenReden)
	{
		this.geenReden = geenReden;
	}

	public int getGeenRedenLesuren()
	{
		return geenRedenLesuren;
	}

	public void setGeenRedenLesuren(int geenRedenLesuren)
	{
		this.geenRedenLesuren = geenRedenLesuren;
	}

	protected BigDecimal getKlokUren(long seconds)
	{
		BigDecimal totaal = new BigDecimal(seconds);
		BigDecimal uur = new BigDecimal(3600);
		BigDecimal aantalUren = BigDecimal.ZERO;
		if (seconds > 0)
		{
			aantalUren = (totaal.divide(uur, 2, RoundingMode.HALF_UP));
		}
		return aantalUren;
	}

	protected static BigDecimal getProcentAanwezig(long totaalSeconden, long aanwezigSeconden)
	{
		if (totaalSeconden == 0)
			return BigDecimal.valueOf(100);
		if (aanwezigSeconden == 0)
			return BigDecimal.ZERO;
		BigDecimal aanwezigBigd = BigDecimal.valueOf(aanwezigSeconden);
		BigDecimal totaalBidg = BigDecimal.valueOf(totaalSeconden);
		return aanwezigBigd.multiply(DecimalUtil.HUNDRED).divide(totaalBidg, RoundingMode.HALF_UP);
	}

	protected static BigDecimal getProcentGeoorloofdAfwezig(long totaalSeconden,
			long geoorloofdAfwezigSeconden)
	{
		if (totaalSeconden == 0)
			return BigDecimal.ZERO;
		if (geoorloofdAfwezigSeconden == 0)
			return BigDecimal.ZERO;
		BigDecimal afwezigBidg = BigDecimal.valueOf(geoorloofdAfwezigSeconden);
		BigDecimal totaalBidg = BigDecimal.valueOf(totaalSeconden);
		return afwezigBidg.multiply(DecimalUtil.HUNDRED).divide(totaalBidg, RoundingMode.HALF_UP);
	}

	protected static BigDecimal getProcentOngeoorloofdAfwezig(long totaalSeconden,
			long ongeoorloofdAfwezigSeconden)
	{
		if (totaalSeconden == 0)
			return BigDecimal.ZERO;
		if (ongeoorloofdAfwezigSeconden == 0)
			return BigDecimal.ZERO;
		BigDecimal afwezigBidg = BigDecimal.valueOf(ongeoorloofdAfwezigSeconden);
		BigDecimal totaalBidg = BigDecimal.valueOf(totaalSeconden);
		return afwezigBidg.multiply(DecimalUtil.HUNDRED).divide(totaalBidg, RoundingMode.HALF_UP);
	}

	public BigDecimal getResterend()
	{
		return resterend;
	}

	public void setResterend(BigDecimal resterend)
	{
		this.resterend = resterend;
	}

	public BigDecimal getBudget()
	{
		return budget;
	}

	public void setBudget(BigDecimal budget)
	{
		this.budget = budget;
	}

	public BigDecimal getProcentGeoorloofdAfwezig()
	{
		return procentGeoorloofdAfwezig;
	}

	public void setProcentGeoorloofdAfwezig(BigDecimal procentGeoorloofdAfwezig)
	{
		this.procentGeoorloofdAfwezig = procentGeoorloofdAfwezig;
	}

	public BigDecimal getProcentOngeoorloofdAfwezig()
	{
		return procentOngeoorloofdAfwezig;
	}

	public void setProcentOngeoorloofdAfwezig(BigDecimal procentOngeoorloofdAfwezig)
	{
		this.procentOngeoorloofdAfwezig = procentOngeoorloofdAfwezig;
	}

	public BigDecimal getProcentAfwezig()
	{
		return procentAfwezig;
	}

	public void setProcentAfwezig(BigDecimal procentAfwezig)
	{
		this.procentAfwezig = procentAfwezig;
	}

	public BigDecimal getAfwezig()
	{
		return afwezig;
	}

	public void setAfwezig(BigDecimal afwezig)
	{
		this.afwezig = afwezig;
	}

	public int getAfwezigLesuren()
	{
		return afwezigLesuren;
	}

	public void setAfwezigLesuren(int afwezigLesuren)
	{
		this.afwezigLesuren = afwezigLesuren;
	}
}
