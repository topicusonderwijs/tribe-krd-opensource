package nl.topicus.eduarte.util;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumLandelijkOfInstellingEntiteit;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.VerbintenisContract;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.personen.AbstractRelatie;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.PersoonAdres;

import org.apache.wicket.model.IModel;
import org.hibernate.FlushMode;
import org.hibernate.Session;

/**
 * Util die bij overlijden of reincarnatie van een deelnemer de relevante gegevens
 * aanpast.
 * 
 * @author hoeve
 */
public class OverlijdenUtil
{
	private Session currentSession;

	private FlushMode oldFlushMode;

	private boolean actief;

	private IModel<Deelnemer> deelnemerModel;

	private Date datumVanOverlijden;

	public OverlijdenUtil(IModel<Deelnemer> deelnemerModel)
	{
		currentSession =
			DataAccessRegistry.getHelper(SessionDataAccessHelper.class)
				.getHibernateSessionProvider().getSession();
		this.deelnemerModel = deelnemerModel;
	}

	@SuppressWarnings("hiding")
	public boolean updateDeelnemer(Date datumVanOverlijden, boolean actief)
	{
		if (getRedenoverlijden() == null)
			return false;

		Asserts.assertNotNull("datumVanOverlijden", datumVanOverlijden);

		oldFlushMode = currentSession.getFlushMode();
		currentSession.setFlushMode(FlushMode.COMMIT);

		this.actief = actief;
		this.datumVanOverlijden = datumVanOverlijden;

		updateDatumOverlijden();
		updateGroepsdeelnames();
		updatePersoonAdressen();
		updateRelaties();
		updateVerbintenissen();

		getDeelnemer().update();
		getDeelnemer().commit();
		getDeelnemer().flush();

		currentSession.setFlushMode(oldFlushMode);

		return true;
	}

	private void updateDatumOverlijden()
	{
		if (actief)
			getDeelnemer().getPersoon().setDatumOverlijden(null);
		else
			getDeelnemer().getPersoon().setDatumOverlijden(datumVanOverlijden);

		getDeelnemer().getPersoon().update();
	}

	private void updateGroepsdeelnames()
	{
		List<Groepsdeelname> groepsdeelnames = getDeelnemer().getGroepsdeelnames();

		for (Groepsdeelname groepsdeelname : groepsdeelnames)
		{
			if (actief)
				checkEindDatumAndRevert(groepsdeelname);
			else
				checkEindDatumAndUpdate(groepsdeelname);
		}
	}

	private void updatePersoonAdressen()
	{
		for (PersoonAdres persoonadres : getDeelnemer().getPersoon().getFysiekAdressen())
		{
			if (actief)
				checkEindDatumAndRevert(persoonadres);
			else
				checkEindDatumAndUpdate(persoonadres);
		}
	}

	private void updateRelaties()
	{
		for (AbstractRelatie relatie : getDeelnemer().getPersoon().getRelaties())
		{
			if (actief)
				checkEindDatumAndRevert(relatie);
			else
				checkEindDatumAndUpdate(relatie);
		}

	}

	private void updateVerbintenissen()
	{
		for (Verbintenis verbintenis : getDeelnemer().getVerbintenissen())
		{
			if (actief)
				updateVerbintenissenNaarLevend(verbintenis);
			else
				updateVerbintenissenNaarOverleden(verbintenis);
		}
	}

	private void updateVerbintenissenNaarOverleden(Verbintenis verbintenis)
	{
		if (verbintenis.getEinddatum() == null)
		{
			verbintenis.setRedenUitschrijving(RedenUitschrijving.getOverledenReden());
			setVerbintenisStatusOverleden(verbintenis);

			checkEindDatumAndUpdate(verbintenis);

			for (Bekostigingsperiode bekostigingsperiode : verbintenis.getBekostigingsperiodes())
				checkEindDatumAndUpdate(bekostigingsperiode);

			for (BPVInschrijving bpvinschrijving : verbintenis.getBpvInschrijvingen())
			{
				if (bpvinschrijving.getEinddatum() == null)
				{
					setBPVInschrijvingStatusOverleden(bpvinschrijving);
					bpvinschrijving.setRedenUitschrijving(getRedenoverlijden());

					checkEindDatumAndUpdate(bpvinschrijving);
				}
			}

			for (VerbintenisContract contract : verbintenis.getContracten())
				checkEindDatumAndUpdate(contract);

			for (Plaatsing plaatsing : verbintenis.getPlaatsingen())
				checkEindDatumAndUpdate(plaatsing);
		}
	}

	private void updateVerbintenissenNaarLevend(Verbintenis verbintenis)
	{
		if (matchVerbintenis(verbintenis))
		{
			verbintenis.setRedenUitschrijving(null);
			setVerbintenisStatusLevend(verbintenis);

			checkEindDatumAndRevert(verbintenis);

			for (Bekostigingsperiode bekostigingsperiode : verbintenis.getBekostigingsperiodes())
				checkEindDatumAndRevert(bekostigingsperiode);

			for (BPVInschrijving bpvinschrijving : verbintenis.getBpvInschrijvingen())
			{
				if (matchBPVInschrijving(bpvinschrijving))
				{
					bpvinschrijving.setRedenUitschrijving(null);
					setBPVInschrijvingStatusLevend(bpvinschrijving);

					checkEindDatumAndRevert(bpvinschrijving);
				}
			}

			for (VerbintenisContract contract : verbintenis.getContracten())
				checkEindDatumAndRevert(contract);

			for (Plaatsing plaatsing : verbintenis.getPlaatsingen())
				checkEindDatumAndRevert(plaatsing);
		}
	}

	private boolean matchVerbintenis(Verbintenis verbintenis)
	{
		boolean datum = datumVanOverlijden.equals(verbintenis.getEinddatum());
		boolean reden = getRedenoverlijden().equals(verbintenis.getRedenUitschrijving());
		boolean status =
			(VerbintenisStatus.Afgemeld.equals(verbintenis.getStatus()) || VerbintenisStatus.Beeindigd
				.equals(verbintenis.getStatus()));

		return datum && reden && status;
	}

	private void setVerbintenisStatusOverleden(Verbintenis verbintenis)
	{
		if (verbintenis.getStatus()
			.tussen(VerbintenisStatus.Voorlopig, VerbintenisStatus.Afgedrukt))
			verbintenis.setStatus(VerbintenisStatus.Afgemeld);
		else if (verbintenis.getStatus().equals(VerbintenisStatus.Definitief))
			verbintenis.setStatus(VerbintenisStatus.Beeindigd);
	}

	private void setVerbintenisStatusLevend(Verbintenis verbintenis)
	{
		if (VerbintenisStatus.Afgemeld.equals(verbintenis.getStatus()))
			verbintenis.setStatus(VerbintenisStatus.Voorlopig);
		else if (VerbintenisStatus.Beeindigd.equals(verbintenis.getStatus()))
			verbintenis.setStatus(VerbintenisStatus.Definitief);
	}

	private boolean matchBPVInschrijving(BPVInschrijving bpvinschrijving)
	{
		boolean datum = datumVanOverlijden.equals(bpvinschrijving.getEinddatum());
		boolean reden = getRedenoverlijden().equals(bpvinschrijving.getRedenUitschrijving());
		boolean status =
			BPVStatus.Beëindigd.equals(bpvinschrijving.getStatus())
				|| BPVStatus.Afgemeld.equals(bpvinschrijving.getStatus());

		return datum && reden && status;
	}

	private void setBPVInschrijvingStatusOverleden(BPVInschrijving bpvinschrijving)
	{
		if (bpvinschrijving.getStatus()
			.tussen(BPVStatus.Voorlopig, BPVStatus.OvereenkomstAfgedrukt))
			bpvinschrijving.setStatus(BPVStatus.Afgemeld);
		else if (bpvinschrijving.getStatus().equals(BPVStatus.Definitief))
			bpvinschrijving.setStatus(BPVStatus.Beëindigd);
	}

	private void setBPVInschrijvingStatusLevend(BPVInschrijving bpvinschrijving)
	{
		if (BPVStatus.Afgemeld.equals(bpvinschrijving.getStatus()))
			bpvinschrijving.setStatus(BPVStatus.Voorlopig);
		else if (BPVStatus.Beëindigd.equals(bpvinschrijving.getStatus()))
			bpvinschrijving.setStatus(BPVStatus.Definitief);
	}

	private void checkEindDatumAndRevert(BeginEinddatumInstellingEntiteit entiteit)
	{
		if (datumVanOverlijden.equals(entiteit.getEinddatum()))
		{
			entiteit.setEinddatum(null);
			entiteit.update();
		}
	}

	private void checkEindDatumAndRevert(BeginEinddatumLandelijkOfInstellingEntiteit entiteit)
	{
		if (datumVanOverlijden.equals(entiteit.getEinddatum()))
		{
			entiteit.setEinddatum(null);
			entiteit.update();
		}
	}

	private void checkEindDatumAndUpdate(BeginEinddatumInstellingEntiteit entiteit)
	{
		if (entiteit.getEinddatum() == null || entiteit.getEinddatum().after(datumVanOverlijden))
		{
			entiteit.setEinddatum(TimeUtil.getInstance().addDays(datumVanOverlijden, -1));
			entiteit.update();
		}
	}

	private void checkEindDatumAndUpdate(BeginEinddatumLandelijkOfInstellingEntiteit entiteit)
	{
		if (entiteit.getEinddatum() == null || entiteit.getEinddatum().after(datumVanOverlijden))
		{
			entiteit.setEinddatum(TimeUtil.getInstance().addDays(datumVanOverlijden, -1));
			entiteit.update();
		}
	}

	private RedenUitschrijving getRedenoverlijden()
	{
		return RedenUitschrijving.getOverledenReden();
	}

	private Deelnemer getDeelnemer()
	{
		return deelnemerModel.getObject();
	}
}
