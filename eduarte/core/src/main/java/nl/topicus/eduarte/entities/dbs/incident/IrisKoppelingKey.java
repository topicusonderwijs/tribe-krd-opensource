/*
 * Copyright (c) 2010, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.dbs.incident;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;

import nl.topicus.cobra.crypto.RSAPrivateKey;
import nl.topicus.cobra.crypto.RSAPublicKey;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;

/**
 * Bevat de private en public key voor communicatie met de IRIS+ webservice
 * 
 * @author niesink
 */
@Entity
public class IrisKoppelingKey extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Lob
	@Column(nullable = false)
	private byte[] privateKeyInBytes;

	@Lob
	@Column(nullable = false)
	private byte[] publicKeyInBytes;

	@Transient
	@AutoForm(label = "Bestand", editorClass = FileUploadField.class)
	private FileUpload keyFileUpload;

	public IrisKoppelingKey()
	{
	}

	public void setPrivateKeyInBytes(byte[] privateKeyInBytes)
	{
		this.privateKeyInBytes = privateKeyInBytes;
	}

	public byte[] getPrivateKeyInBytes()
	{
		return privateKeyInBytes;
	}

	public RSAPrivateKey getPrivateKey()
	{
		return new RSAPrivateKey(getPrivateKeyInBytes());
	}

	public void setPrivateKey(RSAPrivateKey key)
	{
		setPrivateKeyInBytes(key.toByteArray());
	}

	public void setPublicKeyInBytes(byte[] publicKeyInBytes)
	{
		this.publicKeyInBytes = publicKeyInBytes;
	}

	public byte[] getPublicKeyInBytes()
	{
		return publicKeyInBytes;
	}

	public RSAPublicKey getPublicKey()
	{
		return new RSAPublicKey(getPublicKeyInBytes());
	}

	public void setPublicKey(RSAPublicKey key)
	{
		setPublicKeyInBytes(key.toByteArray());
	}

	public void setKeyFileUpload(FileUpload keyFileUpload)
	{
		this.keyFileUpload = keyFileUpload;
	}

	public FileUpload getKeyFileUpload()
	{
		return keyFileUpload;
	}
}
