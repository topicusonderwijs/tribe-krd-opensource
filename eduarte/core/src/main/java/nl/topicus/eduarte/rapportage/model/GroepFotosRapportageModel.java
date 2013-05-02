package nl.topicus.eduarte.rapportage.model;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.image.Silhouet;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.zoekfilters.DeelnemerZoekFilter;

import org.apache.wicket.model.IDetachable;

public class GroepFotosRapportageModel implements IDetachable
{
	private static final long serialVersionUID = 1L;

	private DeelnemerZoekFilter groepFilter;

	public GroepFotosRapportageModel(final Groep groep)
	{
		Asserts.assertNotEmpty("groep mag niet null zijn", groep);

		groepFilter = new DeelnemerZoekFilter();
		groepFilter.setGroep(groep);
		groepFilter.addOrderByProperty("persoon.achternaam");
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(groepFilter);
	}

	public List<DeelnemerFoto> list()
	{
		List<DeelnemerFoto> fotos = new ArrayList<DeelnemerFoto>();

		int deelnemerCount =
			DataAccessRegistry.getHelper(DeelnemerDataAccessHelper.class).listCount(groepFilter);

		for (int i = 0; i < deelnemerCount; i += DeelnemerFoto.BATCHSIZE)
			fotos.add(new DeelnemerFoto(groepFilter, i));

		return fotos;
	}

	public static class DeelnemerFoto
	{
		public final static int BATCHSIZE = 5;

		private DeelnemerZoekFilter groepFilter;

		private int index;

		private List<Deelnemer> deelnemers = null;

		public DeelnemerFoto(DeelnemerZoekFilter groepFilter, int index)
		{
			this.groepFilter = groepFilter;
			this.index = index;
		}

		public Image getFoto1()
		{
			init();
			return getDeelnemerImage(0);
		}

		public Image getFoto2()
		{
			init();
			return getDeelnemerImage(1);
		}

		public Image getFoto3()
		{
			init();
			return getDeelnemerImage(2);
		}

		public Image getFoto4()
		{
			init();
			return getDeelnemerImage(3);
		}

		public Image getFoto5()
		{
			init();
			return getDeelnemerImage(4);
		}

		public String getNaam1()
		{
			init();
			return getDeelnemerNaam(0);
		}

		public String getNaam2()
		{
			init();
			return getDeelnemerNaam(1);
		}

		public String getNaam3()
		{
			init();
			return getDeelnemerNaam(2);
		}

		public String getNaam4()
		{
			init();
			return getDeelnemerNaam(3);
		}

		public String getNaam5()
		{
			init();
			return getDeelnemerNaam(4);
		}

		private Image getDeelnemerImage(int deelnemerIndex)
		{
			Image curFoto = null;

			try
			{
				if (deelnemers.size() > deelnemerIndex)
				{
					Deelnemer deelnemer = deelnemers.get(deelnemerIndex);

					if (deelnemer.getPersoon().getAfbeelding() != null
						&& deelnemer.getPersoon().getAfbeelding() != null)
						curFoto =
							ImageIO.read(new ByteArrayInputStream(deelnemer.getPersoon()
								.getAfbeeldingBytes()));

					if (curFoto == null)
						curFoto = ImageIO.read(Silhouet.class.getResourceAsStream("smiley.jpg"));
				}
			}
			catch (IOException e)
			{
				// ignore, we kunnen hier toch niets doen.
			}

			return curFoto;
		}

		private String getDeelnemerNaam(int deelnemerIndex)
		{
			if (deelnemers.size() > deelnemerIndex)
				return deelnemers.get(deelnemerIndex).toString();

			return null;
		}

		private void init()
		{
			if (deelnemers == null)
			{
				deelnemers =
					DataAccessRegistry.getHelper(DeelnemerDataAccessHelper.class).list(groepFilter,
						index, BATCHSIZE);
			}
		}
	}
}
