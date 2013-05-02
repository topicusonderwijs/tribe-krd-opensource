package nl.topicus.eduarte.krd.web.pages.beheer.bron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.IdBasedModelSelection;
import nl.topicus.eduarte.krd.bron.BronBatchBuilder;
import nl.topicus.eduarte.krd.bron.BronUtils;
import nl.topicus.eduarte.krd.dao.helpers.BronAanleverpuntDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.AbstractBronBatchVO;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchBVE;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchVOExamengegevens;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchVOInschrijvingen;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronExamenresultaatVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronVakGegegevensVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter.TypeMelding;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.IBronBatch;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.VerantwoordelijkeAanlevering;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public class BronBatchModel extends AbstractReadOnlyModel<List<IBronBatch>>
{
	private static final long serialVersionUID = 1L;

	private static final int START_MELDINGNUMMER = 1;

	private ModelManager manager = getManager();

	private String error = "";

	Map<IChangeRecordingModel<IBronBatch>, IdBasedModelSelection<IBronMelding>> batchesModel =
		new HashMap<IChangeRecordingModel<IBronBatch>, IdBasedModelSelection<IBronMelding>>();

	public BronBatchModel(BronMeldingZoekFilter filter)
	{
		BronBatchBuilder builder = new BronBatchBuilder(filter);
		VerantwoordelijkeAanlevering verantwoordelijke = VerantwoordelijkeAanlevering.Instelling;
		if (TypeMelding.Accountant == filter.getTypeMelding())
			verantwoordelijke = VerantwoordelijkeAanlevering.Accountant;
		if (builder.createBatches(verantwoordelijke))
		{
			for (IBronBatch batch : builder.getBatches())
			{
				IChangeRecordingModel<IBronBatch> batchModel =
					ModelFactory.getCompoundChangeRecordingModel(batch, manager);
				IdBasedModelSelection<IBronMelding> selection =
					new IdBasedModelSelection<IBronMelding>();
				for (Object curMelding : batch.getMeldingen())
				{
					IBronMelding melding = (IBronMelding) curMelding;
					if (!melding.isGeblokkeerd())
					{
						if (melding.bevatAlleenToevoegingen()
							|| melding.bevatSofiOfOnderwijsNummer())
							selection.add((IBronMelding) curMelding);
					}
				}
				batchesModel.put(batchModel, selection);
			}
		}
		else
			error = builder.getErrorMelding();

	}

	private ModelManager getManager()
	{
		return new DefaultModelManager(BronInschrijvingsgegevensVOMelding.class,
			BronExamenresultaatVOMelding.class, BronVakGegegevensVOMelding.class,
			BronBveAanleverRecord.class, BronAanleverMelding.class, BronBatchBVE.class,
			BronBatchVOInschrijvingen.class);
	}

	@Override
	public List<IBronBatch> getObject()
	{
		List<IBronBatch> batchList = new ArrayList<IBronBatch>();
		for (IChangeRecordingModel<IBronBatch> model : batchesModel.keySet())
		{
			batchList.add(model.getObject());
		}
		return batchList;
	}

	public String getError()
	{
		return error;
	}

	public IChangeRecordingModel<IBronBatch> getItem(int index)
	{
		int i = 0;
		for (IChangeRecordingModel<IBronBatch> model : batchesModel.keySet())
		{
			if (index == i)
				return model;
			i++;
		}
		return null;
	}

	public IdBasedModelSelection<IBronMelding> getSelection(IModel<IBronBatch> model)
	{
		return batchesModel.get(model);
	}

	public void updateBatch(IBronBatch batch, IdBasedModelSelection<IBronMelding> selectie)
	{
		List<BronAanleverMelding> uitgevinkteMeldingen = new ArrayList<BronAanleverMelding>();
		int meldingnr = START_MELDINGNUMMER;
		if (batch instanceof BronBatchBVE)
		{
			BronBatchBVE batchBVE = (BronBatchBVE) batch;
			for (BronAanleverMelding melding : batchBVE.getMeldingen())
			{
				if (!selectie.isSelected(melding))
				{
					uitgevinkteMeldingen.add(melding);
				}
				else
				{
					melding.setBronMeldingStatus(BronMeldingStatus.IN_BEHANDELING);
					melding.setMeldingnummer(meldingnr);
					meldingnr++;
				}
			}
			for (BronAanleverMelding melding : uitgevinkteMeldingen)
			{
				melding.setBatch(null);
				batchBVE.getMeldingen().remove(melding);
			}
			batchBVE.berekenControleTotalen();
		}
		if (batch instanceof BronBatchVOExamengegevens)
		{
			BronBatchVOExamengegevens batchVOExam = (BronBatchVOExamengegevens) batch;
			List<BronExamenresultaatVOMelding> uitegevinkteMeldingen =
				new ArrayList<BronExamenresultaatVOMelding>();
			for (BronExamenresultaatVOMelding melding : batchVOExam.getMeldingen())
			{
				if (!selectie.isSelected(melding))
				{
					uitegevinkteMeldingen.add(melding);
				}
				else
				{
					melding.setBronMeldingStatus(BronMeldingStatus.IN_BEHANDELING);
					melding.setMeldingNummer(meldingnr);
					meldingnr++;
				}
			}
			for (BronExamenresultaatVOMelding melding : uitegevinkteMeldingen)
			{
				melding.setBatch(null);
				batchVOExam.getMeldingen().remove(melding);
			}
			batchVOExam.berekenControleTotalen();
		}
		if (batch instanceof BronBatchVOInschrijvingen)
		{
			BronBatchVOInschrijvingen batchVOInsch = (BronBatchVOInschrijvingen) batch;
			List<BronInschrijvingsgegevensVOMelding> uitegevinkteMeldingen =
				new ArrayList<BronInschrijvingsgegevensVOMelding>();
			for (BronInschrijvingsgegevensVOMelding melding : batchVOInsch.getMeldingen())
			{
				if (!selectie.isSelected(melding))
				{
					uitegevinkteMeldingen.add(melding);
				}
				else
				{
					melding.setBronMeldingStatus(BronMeldingStatus.IN_BEHANDELING);
					melding.setMeldingNummer(meldingnr);
					meldingnr++;
				}
			}
			for (BronInschrijvingsgegevensVOMelding melding : uitegevinkteMeldingen)
			{
				melding.setBatch(null);
				batchVOInsch.getMeldingen().remove(melding);
			}
			batchVOInsch.berekenControleTotalen();
		}
	}

	@SuppressWarnings("unchecked")
	public void save()
	{
		for (IChangeRecordingModel<IBronBatch> model : batchesModel.keySet())
		{
			IBronBatch batch = model.getObject();
			updateBatch(batch, batchesModel.get(model));

			Integer aanleverPuntNummer = batch.getAanleverPuntNummer();
			BronAanleverpuntDataAccessHelper aanleverpunten =
				DataAccessRegistry.getHelper(BronAanleverpuntDataAccessHelper.class);
			BronAanleverpunt aanleverpunt = aanleverpunten.getAanleverpunt(aanleverPuntNummer);

			BronOnderwijssoort sector = batch.getOnderwijssoort();

			// We zetten het batchnummer opnieuw, aangezien er meerdere gebruikers
			// tegelijkertijd een batch kunnen maken. Hierdoor wordt het ophogen van het
			// nummer atomair, en is het schier onmogelijk om twee batches met hetzelfde
			// nummer te genereren. Nadeel is dat de gebruiker mogelijk een ander
			// batchnummer ziet dan daadwerkelijk wordt gegenereerd.

			int batchNummer = Integer.MIN_VALUE;
			switch (sector)
			{
				case BEROEPSONDERWIJS:
					batchNummer = aanleverpunt.incBatchNrBO();
					((BronBatchBVE) batch).setBatchNummer(batchNummer);
					break;
				case EDUCATIE:
					batchNummer = aanleverpunt.incBatchNrED();
					((BronBatchBVE) batch).setBatchNummer(batchNummer);
					break;
				case VAVO:
					batchNummer = aanleverpunt.incBatchNrVAVO();
					((BronBatchBVE) batch).setBatchNummer(batchNummer);
					break;
				case VOORTGEZETONDERWIJS:
					batchNummer = aanleverpunt.incBatchNrVO();
					((AbstractBronBatchVO< ? >) batch).setBatchNummer(batchNummer);
					break;
			}

			batch.berekenControleTotalen();
			batch.setBestand(BronBatchBuilder.writeBronBatch(batch));
			batch.setBestandsnaam(BronBatchBuilder.getFilename(batch));
			BronUtils.updateStatussenNaBatchAanmaken((List<IBronMelding>) batch.getMeldingen());

			model.saveObject();
			aanleverpunt.saveOrUpdate();
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
		}
	}

	@Override
	public void detach()
	{
		for (IChangeRecordingModel<IBronBatch> model : batchesModel.keySet())
		{
			ComponentUtil.detachQuietly(model);
		}
		super.detach();
	}

	public String getAantalMeldingen(IModel<IBronBatch> model, SoortMutatie soortMutatie)
	{
		IBronBatch batch = model.getObject();
		IdBasedModelSelection<IBronMelding> selection = getSelection(model);
		int aantal = 0;
		for (Object object : batch.getMeldingen())
		{
			IBronMelding melding = (IBronMelding) object;
			if (selection.isSelected(melding))
			{
				if (soortMutatie == null)
					aantal++;
				else if (melding instanceof BronAanleverMelding)
				{
					BronAanleverMelding bronAanleverMelding = (BronAanleverMelding) melding;
					for (BronBveAanleverRecord record : bronAanleverMelding.getMeldingen())
					{
						if (soortMutatie.equals(record.getMutatieSoort()))
							aantal++;
					}
				}
				else if (melding.getSoortMutaties().contains(soortMutatie))
					aantal++;
			}
		}
		return "" + aantal;
	}

}