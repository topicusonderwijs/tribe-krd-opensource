package nl.topicus.cobra.templates.documents.rtf;

import java.io.BufferedWriter;

import nl.topicus.cobra.templates.exceptions.TemplateException;

public interface IRtfElement extends Cloneable
{
	IRtfElement clone() throws CloneNotSupportedException;

	void write(BufferedWriter writer) throws TemplateException;
}
