package nl.topicus.eduarte.krd.jobs;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.util.ResourceUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.util.ZipUtils;
import nl.topicus.cobra.util.ZipUtils.ZipIterator;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.MedewerkerDataAccessHelper;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.TypeBijlage;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonBijlage;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krd.entities.PersoonFotosInlezenJobRun;
import nl.topicus.eduarte.krd.jobs.PersoonFotosInlezenJobDataMap.Voor;

import org.apache.wicket.util.lang.Bytes;
import org.quartz.JobExecutionContext;

/**
 * Importeert een of meer pasfotos van medewerkers of deelnemers. Pasfotos dienen
 * aangeleverd te worden als jpeg bestanden. De maximale hoogte x breedte is 320x240 (een
 * verhouding van 4:3). Is een foto groter dan deze dimensies, dan worden ze automatisch
 * verkleind naar dit formaat.
 */
@JobInfo(name = PersoonFotosInlezenJob.JOB_NAME)
@JobRunClass(PersoonFotosInlezenJobRun.class)
public class PersoonFotosInlezenJob extends EduArteJob
{
	/** De maximale grootte van de fotos als ze in de database gezet worden. */
	private static final long MAX_FOTO_SIZE = Bytes.kilobytes(50).bytes();

	/** De maximale breedte van een pasfoto zoals deze in de database gezet wordt. */
	private static final int MAX_FOTO_WIDTH = 240;

	/** De maximale hoogte van een pasfoto zoals deze in de database gezet wordt. */
	private static final int MAX_FOTO_HEIGHT = 320;

	public static final String JOB_NAME = "Pasfotos inlezen";

	private PersoonFotosInlezenJobRun jobrun;

	private Voor fotosVoor;

	private FileInputStream fis;

	private File file;

	private String bestandsnaam;

	private String samenvatting;

	private int aantalVerwacht = 0;

	private int aantalVerwerkt = 0;

	private int aantalFout = 0;

	@Override
	protected void executeJob(JobExecutionContext context) throws InterruptedException
	{
		jobrun = new PersoonFotosInlezenJobRun();
		fotosVoor = (Voor) context.getMergedJobDataMap().get("fotosVoor");

		file = (File) context.getMergedJobDataMap().get("bestand");
		bestandsnaam = (String) context.getMergedJobDataMap().get("bestandsnaam");

		try
		{
			fis = new FileInputStream(file);
		}
		catch (FileNotFoundException e1)
		{
			error("Tijdelijk bestand kan niet geopend worden.");
		}

		setStatus("Verwerken van de foto(s) voor " + fotosVoor.name().toLowerCase());

		try
		{
			verwerkUpload();
		}
		catch (IOException e)
		{
			log.error(e.getMessage(), e);
			error("Bestand %s is niet goed verwerkt: ", bestandsnaam, e.getMessage());
			samenvatting =
				"Bestand " + bestandsnaam + " is niet goed verwerkt: " + e.getMessage()
					+ " (klik hier voor meer informatie)";
		}

		jobrun.setGestartDoor(getMedewerker());
		jobrun.setRunStart(getDatumTijdOpgestart());
		jobrun.setRunEinde(TimeUtil.getInstance().currentDateTime());
		jobrun.setSamenvatting(samenvatting);
		jobrun.saveOrUpdate();
		jobrun.commit();
	}

	private void verwerkUpload() throws InterruptedException, IOException
	{
		String clientFilename = bestandsnaam;
		if (clientFilename.toLowerCase().endsWith(".zip"))
		{
			bepaalAantalFotos();
			fis = new FileInputStream(file);

			verwerkZip();
			info(clientFilename + " is verwerkt");
			String aantal = aantalVerwacht == 0 ? "geen" : String.valueOf(aantalVerwacht);
			samenvatting =
				String.format("Er zijn %s %sfotos geïmporteerd uit bestand %s.", aantal, fotosVoor
					.enkelvoud(), clientFilename);
			if (aantalVerwerkt != aantalVerwacht)
			{
				samenvatting +=
					" Niet alle fotos konden verwerkt worden. Klik voor meer informatie.";
			}
		}
		else
		{
			aantalVerwacht = 1;
			byte[] b = new byte[(int) file.length()];
			fis.read(b);
			verwerkFoto(clientFilename, b);
			if (aantalFout == 0)
				samenvatting = "Foto %s is geïmporteerd.";
			else
				samenvatting = "Foto %s kon niet geïmporteerd kon worden.";
		}
	}

	private void bepaalAantalFotos() throws InterruptedException, IOException
	{
		aantalVerwacht = 0;
		ZipIterator teller = new ZipIterator()
		{
			@Override
			public void process(ZipInputStream stream, ZipEntry entry)
			{
				if (isFotobestand(entry))
					aantalVerwacht++;
			}
		};
		ZipUtils.itereer(fis, teller);
	}

	private void verwerkZip() throws InterruptedException, IOException
	{
		ZipIterator importer = new ZipIterator()
		{
			@Override
			public void process(ZipInputStream stream, ZipEntry entry) throws IOException,
					InterruptedException
			{
				if (isFotobestand(entry))
				{
					log.info("Verwerken foto upload {}", entry.getName());
					byte[] foto = ZipUtils.getEntry(stream, entry);
					verwerkFoto(entry.getName(), foto);
				}
			}
		};

		ZipUtils.itereer(fis, importer);
	}

	private boolean isFotobestand(ZipEntry entry)
	{
		String filename = stripDirectories(entry.getName());
		return !entry.isDirectory() && !filename.startsWith(".")
			&& filename.toLowerCase().endsWith(".jpg");
	}

	private void info(String message)
	{
		setStatus(message);
		jobrun.info(message);
	}

	private void error(String format, Object... params)
	{
		String message = String.format(format, params);
		setStatus(message);
		jobrun.error(message);
	}

	private void verwerkFoto(String filename, byte[] foto) throws InterruptedException, IOException
	{
		setProgress(aantalVerwerkt++, Math.max(1, aantalVerwacht));

		setStatus("Verwerken van foto " + bestandsnaam + "/" + filename);
		byte[] resizedFoto = resizeImage(filename, foto);

		if (resizedFoto == null)
		{
			aantalFout++;
			return;
		}

		if (resizedFoto.length > MAX_FOTO_SIZE)
		{
			aantalFout++;

			double grootte = Bytes.bytes(foto.length).kilobytes();
			double max = Bytes.bytes(MAX_FOTO_SIZE).kilobytes();
			error("%s heeft een grootte van %01.1fKiB, maximale grootte voor een pasfoto is %dKiB",
				filename, grootte, max);
		}
		else if (filename.toLowerCase().endsWith(".jpg"))
		{
			String identifier = stripIdentifier(filename);
			Persoon persoon = getPersoon(identifier);
			if (persoon != null)
			{
				koppelFotoAanPersoon(resizedFoto, persoon);
			}
			else
			{
				aantalFout++;
				String nummerOfAfkorting = fotosVoor == Voor.Deelnemers ? "nummer" : "afkorting";
				error(
					"Kon bestand %s niet importeren omdat %s met %s %s niet gevonden kon worden.",
					filename, fotosVoor.enkelvoud(), nummerOfAfkorting, identifier);
			}
		}
	}

	private String stripDirectories(String filename)
	{
		String currentFilename = filename;
		int p = currentFilename.lastIndexOf('/');
		if (p >= 0)
			currentFilename = currentFilename.substring(p + 1);
		p = currentFilename.lastIndexOf('\\');
		if (p >= 0)
			currentFilename = currentFilename.substring(p + 1);
		return currentFilename;
	}

	private byte[] resizeImage(String filename, byte[] bytes) throws IOException
	{
		BufferedImage originalImage = getImage(filename, bytes);

		int originalWidth = originalImage.getWidth();
		int originalHeight = originalImage.getHeight();

		// alleen fotos in verticale verhouding accepteren.
		if (originalWidth > originalHeight)
		{
			error("Foto %s is niet in portret stand. Niet ingelezen.", filename);
			return null;
		}
		if (originalWidth > MAX_FOTO_WIDTH || originalHeight > MAX_FOTO_HEIGHT)
		{
			final int newWidth;
			final int newHeight;

			// kies de as die het meest afwijkt van de gewenste verhouding om de foto
			// daarlangs te verkleinen
			if ((Double.valueOf(originalWidth) / MAX_FOTO_WIDTH) > (Double.valueOf(originalHeight) / MAX_FOTO_HEIGHT))
			{
				newWidth = MAX_FOTO_WIDTH;
				newHeight = (MAX_FOTO_WIDTH * originalHeight) / originalWidth;
			}
			else
			{
				newWidth = (MAX_FOTO_HEIGHT * originalWidth) / originalHeight;
				newHeight = MAX_FOTO_HEIGHT;
			}
			BufferedImage dimg = new BufferedImage(newWidth, newHeight, originalImage.getType());
			Graphics2D g = dimg.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(originalImage, 0, 0, newWidth, newHeight, 0, 0, originalWidth,
				originalHeight, null);
			g.dispose();
			return toImageData(dimg);
		}

		// no need for resizing
		return bytes;
	}

	private BufferedImage getImage(String filename, byte[] bytes) throws IOException
	{
		InputStream is = null;
		BufferedImage originalImage = null;

		try
		{
			is = new ByteArrayInputStream(bytes);
			originalImage = ImageIO.read(is);
			if (originalImage == null)
			{
				// als ImageIO.read() null teruggeeft, kon het bestand niet ingelezen
				// worden doordat het formaat niet herkend wordt.
				throw new IOException("Foto " + filename
					+ " heeft een onbekend bestandsformaat. Enkel jpeg (.jpg) wordt ondersteund.");
			}
		}
		finally
		{
			ResourceUtil.closeQuietly(is);
		}
		return originalImage;
	}

	private byte[] toImageData(BufferedImage image) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image, "jpeg", out);
		return out.toByteArray();
	}

	private void koppelFotoAanPersoon(byte[] foto, Persoon persoon)
	{
		PersoonBijlage afbeelding = persoon.getAfbeelding();
		if (afbeelding == null)
		{
			afbeelding = new PersoonBijlage();
			afbeelding.setPersoon(persoon);
			afbeelding.setBegindatum(persoon.getGeboortedatum() != null ? persoon
				.getGeboortedatum() : TimeUtil.getInstance().currentDate());
			persoon.setAfbeelding(afbeelding);
		}
		Bijlage bijlage = afbeelding.getBijlage();
		if (bijlage == null)
		{
			bijlage = new Bijlage();
			bijlage.setOmschrijving("pasfoto");
			bijlage.setBestandsnaam("pasfoto.jpg");
			bijlage.setTypeBijlage(TypeBijlage.Bestand);
			afbeelding.setBijlage(bijlage);
		}
		bijlage.setBestand(foto);
		bijlage.saveOrUpdate();
		afbeelding.saveOrUpdate();
		persoon.saveOrUpdate();
	}

	private Persoon getPersoon(String identifier)
	{
		switch (fotosVoor)
		{
			case Deelnemers:
				return getDeelnemerPersoon(identifier);
			case Medewerkers:
				return getMedewerkerPersoon(identifier);
		}
		return null;
	}

	private String stripIdentifier(String filename)
	{
		String currentFilename = stripDirectories(filename);
		String identifier = currentFilename.substring(0, currentFilename.length() - 4);
		return identifier;
	}

	private Persoon getDeelnemerPersoon(String identifier)
	{
		DeelnemerDataAccessHelper helper =
			DataAccessRegistry.getHelper(DeelnemerDataAccessHelper.class);
		Integer deelnemernummer = null;
		try
		{
			deelnemernummer = Integer.valueOf(identifier);
		}
		catch (NumberFormatException e)
		{
			return null;
		}
		Deelnemer deelnemer = helper.getByDeelnemernummer(deelnemernummer);
		if (deelnemer != null)
			return deelnemer.getPersoon();
		return null;
	}

	private Persoon getMedewerkerPersoon(String identifier)
	{
		MedewerkerDataAccessHelper helper =
			DataAccessRegistry.getHelper(MedewerkerDataAccessHelper.class);
		Medewerker medewerker = helper.batchGetByAfkorting(identifier);
		if (medewerker != null)
			return medewerker.getPersoon();
		return null;
	}
}
