package nl.topicus.eduarte.krd.entities.bron;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;

import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.cobra.util.ResourceUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krd.bron.jobs.BronTerugkoppelbestandInlezenJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue("BronTerugkoppelbestandJobRun")
public class BronTerugkoppelbestandInlezenJobRun extends JobRun
{
	private static final long serialVersionUID = 1L;

	/**
	 * De inhoud van het ingelezen bestand, wordt opgeslagen in een door GZIP
	 * gecomprimeerd bestandsformaat.
	 */
	@Column(nullable = true)
	@Lob
	private byte[] contents;

	@RestrictedAccess(hasGetter = false, hasSetter = false)
	@Transient
	private transient byte[] uncompressedContents = null;

	@Column(nullable = true, length = 512)
	private String filename;

	/**
	 * Geeft de gecomprimeerde inhoud van het bestand terug.
	 */
	public byte[] getCompressedContents()
	{
		return contents;
	}

	/**
	 * Zet de inhoud van het bestand, wordt gecomprimeerd door deze methode.
	 */
	public void setContents(byte[] contents)
	{
		backupUncompressedData(contents);
		deflateUncompressedData();
	}

	private void backupUncompressedData(byte[] data)
	{
		try
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ResourceUtil.copy(bais, baos);
			this.uncompressedContents = baos.toByteArray();
		}
		catch (IOException e)
		{
			throw new RuntimeException("Kon de inhoud van het bestand '" + filename
				+ "' niet lezen uit de database: " + e.getMessage(), e);
		}
	}

	private void deflateUncompressedData()
	{
		try
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(uncompressedContents);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream gzipos = new GZIPOutputStream(baos);
			ResourceUtil.copy(bais, gzipos);
			this.contents = baos.toByteArray();
		}
		catch (IOException e)
		{
			throw new RuntimeException("Kon de inhoud van het bestand '" + filename
				+ "' niet lezen uit de database: " + e.getMessage(), e);
		}
	}

	/**
	 * Geeft de ongecomprimeerde inhoud van het bestand terug.
	 */
	public byte[] getContents()
	{
		if (uncompressedContents == null)
		{
			inflateCompressedData();
		}
		return uncompressedContents;
	}

	private void inflateCompressedData()
	{
		try
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(contents);
			GZIPInputStream gzipis = new GZIPInputStream(bais);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ResourceUtil.copy(gzipis, baos);
			uncompressedContents = baos.toByteArray();
		}
		catch (IOException e)
		{
			throw new RuntimeException("Kon de inhoud van het bestand '" + filename
				+ "' niet lezen uit de database: " + e.getMessage(), e);
		}
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = StringUtil.truncate(filename, 512, "");
	}

	@Override
	public Class< ? extends EduArteJob> getJobClass()
	{
		return BronTerugkoppelbestandInlezenJob.class;
	}
}
