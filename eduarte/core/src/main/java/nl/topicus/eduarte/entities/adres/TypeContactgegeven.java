package nl.topicus.eduarte.entities.adres;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * Een onderwijsinstelling kan zelf allerlei soorten contactgegevens vastleggen, zoals
 * bijvoorbeeld thuistelefoon, mobieltelefoon etc. Het systeem kent zelf ook een aantal
 * 'globale' types. Deze worden gebruikt om op bepaalde plekken in de applicatie relevante
 * informatie te kunnen tonen.
 * 
 * @author loite
 */
@XmlType
@XmlEnum(String.class)
public enum TypeContactgegeven
{
	/**
	 * Overig (onbekend)
	 */
	Overig,
	/**
	 */
	Telefoon,
	/**
	 * Faxnummer
	 */
	Fax,
	/**
	 * Mobieltelefoon
	 */
	Mobieltelefoon,
	/**
	 * Emailadres
	 */
	Email
	{
		@Override
		public String toString()
		{
			return "E-mail";
		}
	},
	/**
	 * Homepage
	 */
	Homepage;

}
