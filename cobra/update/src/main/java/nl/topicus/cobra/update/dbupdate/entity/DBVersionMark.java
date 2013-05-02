package nl.topicus.cobra.update.dbupdate.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Onderdeel van het nieuwe DBVersion tool
 * 
 * @author Chris Gunnink
 * @since 14 juli 2009
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"version", "mark"})})
public class DBVersionMark
{

	public enum MARK_TYPE
	{
		OPERATION,
		FINAL_TASK;
	}

	@Id()
	@GeneratedValue(generator = "hibernate_generator")
	/** generator die het doet op meerdere database omgevingen **/
	@GenericGenerator(name = "hibernate_generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
		@Parameter(name = "sequence_name", value = "HIBERNATE_SEQUENCE"),
		@Parameter(name = "increment_size", value = "1"),
		@Parameter(name = "optimizer", value = "none")})
	@AccessType("property")
	private long id;

	private Date creationDate;

	private String version;

	private String mark;

	private MARK_TYPE type;

	@SuppressWarnings("unused")
	private DBVersionMark()
	{
	}

	public DBVersionMark(String version, String mark, MARK_TYPE type)
	{
		this.version = version;
		this.mark = mark;
		this.type = type;
		creationDate = new Date();
	}

	public long getId()
	{
		return id;
	}

	public Date getCreationDate()
	{
		return creationDate;
	}

	public String getVersion()
	{
		return version;
	}

	public String getMark()
	{
		return mark;
	}

	public MARK_TYPE getType()
	{
		return type;
	}

	@SuppressWarnings("unused")
	private void setId(long id)
	{
		this.id = id;
	}

	@SuppressWarnings("unused")
	private void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public void setMark(String mark)
	{
		this.mark = mark;
	}

	public void setType(MARK_TYPE type)
	{
		this.type = type;
	}

	/**
	 * Belangrijk: er wordt gebruik gemaakt van de NaturalOrderComparator, die toString()
	 * op elk object gebruikt om deze te vergelijken. In alle gevallen bedoelen we de
	 * versie, dus deze moet als eerste worden teruggegeven door deze methode.
	 */
	@Override
	public String toString()
	{
		return version + " " + mark;
	}

}