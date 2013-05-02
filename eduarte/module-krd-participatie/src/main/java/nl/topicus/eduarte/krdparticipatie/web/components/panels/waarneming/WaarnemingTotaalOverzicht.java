package nl.topicus.eduarte.krdparticipatie.web.components.panels.waarneming;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.entities.participatie.Waarneming;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

/**
 * Class voor het bijhouden van totaalOverzichten van Waarnemingen
 * 
 * @author vandekamp
 */
public class WaarnemingTotaalOverzicht implements Serializable, IDetachable
{
	private static final long serialVersionUID = 1L;

	private String type;

	private String collor;

	private IModel<List<Waarneming>> waarnemingenListModel;

	private static final NumberFormat FORMAT = NumberFormat.getNumberInstance();
	static
	{
		FORMAT.setMinimumFractionDigits(1);
		FORMAT.setMaximumFractionDigits(1);
	}

	public WaarnemingTotaalOverzicht(String type, IModel<List<Waarneming>> waarnemingListModel,
			String collor)
	{
		this.type = type;
		this.waarnemingenListModel = waarnemingListModel;
		this.collor = collor;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public IModel<List<Waarneming>> getWaarnemingenListModel()
	{
		return waarnemingenListModel;
	}

	public void setWaarnemingenListModel(IModel<List<Waarneming>> waarnemingenListModel)
	{
		this.waarnemingenListModel = waarnemingenListModel;
	}

	/**
	 * @return het aantal klokuren
	 */
	public String getKlokuren()
	{
		List<Waarneming> waarnemingen = waarnemingenListModel.getObject();
		long seconds = 0;
		for (Waarneming waarneming : waarnemingen)
		{
			seconds +=
				(waarneming.getEindDatumTijd().getTime() - waarneming.getBeginDatumTijd().getTime());
		}
		BigDecimal totaal = new BigDecimal(seconds);
		BigDecimal uur = new BigDecimal(3600000);
		BigDecimal klokuren = BigDecimal.ZERO;
		if (seconds > 0)
		{
			klokuren = (totaal.divide(uur, 2, RoundingMode.HALF_UP));
		}
		return "" + FORMAT.format(klokuren);
	}

	public String getLesuren()
	{
		List<Waarneming> waarnemingen = waarnemingenListModel.getObject();
		int lesuren = 0;
		for (Waarneming waarneming : waarnemingen)
		{
			if (waarneming.getBeginLesuur() != null && waarneming.getEindLesuur() != null)
				lesuren += (waarneming.getEindLesuur() - waarneming.getBeginLesuur()) + 1;
		}
		return "" + FORMAT.format(lesuren);
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(waarnemingenListModel);
	}

	public String getCollor()
	{
		return collor;
	}

	public void setCollor(String collor)
	{
		this.collor = collor;
	}
}
