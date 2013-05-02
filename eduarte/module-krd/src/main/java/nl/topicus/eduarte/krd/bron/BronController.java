package nl.topicus.eduarte.krd.bron;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.BronCommuniceerbaar;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.util.EduArteUtil;
import nl.topicus.onderwijs.duo.bron.BronException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BronController
{
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(BronController.class);

	private static final BronWatchList watchList = new BronWatchList();

	private final BronChanges recordedChanges = new BronChanges();

	private final BronEduArteModel model = new BronEduArteModel();

	private final BronControllerBve controllerBve;

	private final BronControllerVo controllerVo;

	public BronController()
	{
		this.controllerBve = new BronControllerBve();
		this.controllerVo = new BronControllerVo();
	}

	/**
	 * @return true als er wijzigingen zijn die naar BRON gestuurd moeten worden.
	 */
	public boolean hasChanges()
	{
		return !getRecordedChanges().isEmpty();
	}

	/**
	 * Geeft de vastgelegde wijzigingen terug die naar BRON gestuurd moeten worden. Deze
	 * methode niet direct gebruiken (anders dan in tests).
	 */
	public BronChanges getRecordedChanges()
	{
		return recordedChanges;
	}

	/**
	 * Controleert of er wijzigingen zijn op eventuele velden die ten behoeve van BRON
	 * gemonitord moeten worden.
	 * 
	 * @throws BronException
	 *             als de wijziging gevetod wordt omdat er een mutatiestop ingesteld is
	 */
	public void controleerOpWijzigingenOpBronVelden(Entiteit entiteit, Object[] currentState,
			Object[] previousState, String[] propertyNames) throws BronException
	{
		if (!watchList.isWatched(entiteit))
		{
			return;
		}
		if (previousState == null)
		{
			return;
		}
		for (int i = 0; i < currentState.length; i++)
		{
			String property = propertyNames[i];
			Object currentValue = currentState[i];
			Object previousValue = previousState[i];
			boolean hasMonitoredChange =
				watchList.isWatched(entiteit, property)
					&& heeftWijzigingenInProperty(previousValue, currentValue);
			if (hasMonitoredChange)
			{
				recordChange(entiteit, property, previousValue, currentValue);
			}
		}
	}

	private boolean heeftWijzigingenInProperty(Object previousState, Object currentState)
	{
		return !equals(previousState, currentState);
	}

	private boolean equals(Object o1, Object o2)
	{
		if (o1 == o2)
			return true;
		if (o1 instanceof Collection< ? >)
		{
			return equals((Collection< ? >) o1, (Collection< ? >) o2);
		}
		if (o1 instanceof BronCommuniceerbaar && o2 instanceof BronCommuniceerbaar)
		{
			return ((BronCommuniceerbaar) o1).isBronCommuniceerbaar()
				&& ((BronCommuniceerbaar) o2).isBronCommuniceerbaar();
		}
		return o1 == null ? o2 == null : o1.equals(o2);
	}

	private boolean equals(Collection< ? > o1, Collection< ? > o2)
	{
		if (o1 == null)
			return (o2 == null);
		else if (o2 == null)
			return false;

		else if (o1.size() != o2.size())
			return false;

		Iterator< ? > iter1 = o1.iterator();
		Iterator< ? > iter2 = o2.iterator();
		while (iter1.hasNext() && iter2.hasNext())
		{
			Object element1 = iter1.next();
			Object element2 = iter2.next();

			if (element1 == element2)
				continue;
			if (element1 != null && !element1.equals(element2))
				return false;
		}
		return true;
	}

	private void recordChange(Object entity, String propertyName, Object previousState,
			Object currentState)
	{
		// een wijziging op een entiteit kan betrekking hebben op meerdere verbintenissen,
		// die allemaal naar BRON toe moeten (bijvoorbeeld een wijziging op een
		// vooropleiding).
		Collection<Verbintenis> verbintenissen = model.getVerbintenissen(entity);
		if (!verbintenissen.isEmpty())
		{
			for (Verbintenis verbintenis : verbintenissen)
			{
				BronStateChange change =
					new BronStateChange((IdObject) entity, propertyName, previousState,
						currentState);
				recordedChanges.addChange(verbintenis, change);
				recordedChanges.addChange(verbintenis.getDeelnemer(), change);
			}
		}
		else
		{
			Deelnemer deelnemer = EduArteUtil.getDeelnemer(entity);
			if (deelnemer != null)
			{
				BronStateChange change =
					new BronStateChange((IdObject) entity, propertyName, previousState,
						currentState);
				recordedChanges.addChange(deelnemer, change);
			}
		}
	}

	public void save() throws BronException
	{
		for (Verbintenis verbintenis : recordedChanges.getGewijzigdeVerbintenissen())
		{
			BronVerbintenisChanges changes = recordedChanges.getWijzigingen(verbintenis);

			if (verbintenis.isBVEVerbintenis())
			{
				controllerBve.verwerkVerbintenisWijzigingen(changes);
			}
			else if (verbintenis.isVOVerbintenis())
			{
				controllerVo.verwerkVerbintenisWijzigingen(changes);
			}
			verwerkExamendeelnamesVoEnVavo(changes);
		}
		for (Deelnemer deelnemer : recordedChanges.getGewijzigdeDeelnemers())
		{
			BronDeelnemerChanges changes = recordedChanges.getWijzigingen(deelnemer);
			controllerBve.verwerkPersonaliaWijzigingen(changes);
			controllerVo.verwerkPersonaliaWijzigingen(changes);
		}
	}

	private void verwerkExamendeelnamesVoEnVavo(BronVerbintenisChanges changes)
	{
		Examendeelname deelname = getGewijzigdeExamendeelname(changes);
		if (deelname != null
			&& (deelname.getVerbintenis().isVOVerbintenis() || deelname.getVerbintenis()
				.isVAVOVerbintenis()))
		{
			deelname.setGewijzigd(true);
			deelname.saveOrUpdate();
		}
	}

	private Examendeelname getGewijzigdeExamendeelname(BronVerbintenisChanges changes)
	{
		if (changes.heeftWijzigingenVoor(Examendeelname.class))
		{
			return changes.getGewijzigde(Examendeelname.class).get(0);
		}
		if (changes.heeftWijzigingenVoor(OnderwijsproductAfnameContext.class))
		{
			OnderwijsproductAfnameContext afnameContext =
				changes.getGewijzigde(OnderwijsproductAfnameContext.class).get(0);
			return afnameContext.getVerbintenis().getLaatsteExamendeelname();
		}
		if (changes.heeftWijzigingenVoor(OnderwijsproductAfname.class))
		{
			List<OnderwijsproductAfname> afnames =
				changes.getGewijzigde(OnderwijsproductAfname.class);
			for (OnderwijsproductAfname afname : afnames)
			{
				for (OnderwijsproductAfnameContext context : afname.getAfnameContexten())
				{
					if (context.getVerbintenis().getLaatsteExamendeelname() != null)
						return context.getVerbintenis().getLaatsteExamendeelname();
				}
			}
		}

		if (changes.heeftWijzigingenVoor(Resultaat.class))
		{
			List<Resultaat> resultaten = changes.getGewijzigde(Resultaat.class);
			for (OnderwijsproductAfname afname : changes.getDeelnemer()
				.getOnderwijsproductAfnames())
			{
				for (Resultaat resultaat : resultaten)
				{
					Onderwijsproduct product =
						resultaat.getToets().getResultaatstructuur().getOnderwijsproduct();
					if (afname.getOnderwijsproduct().equals(product))
					{
						for (OnderwijsproductAfnameContext context : afname.getAfnameContexten())
						{
							if (context.getVerbintenis().getLaatsteExamendeelname() != null)
								return context.getVerbintenis().getLaatsteExamendeelname();
						}
					}
					return null;
				}
			}
		}
		return null;
	}
}
