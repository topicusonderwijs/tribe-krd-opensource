/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.security.authorization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.security.IPrincipalSource;
import nl.topicus.eduarte.app.security.EduArtePrincipal;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEntiteit;

import org.apache.wicket.security.actions.WaspAction;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class Recht extends OrganisatieEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "rol")
	@Index(name = "idx_Recht_rol")
	private Rol rol;

	@Column(nullable = false, length = 200)
	private String principalSourceClassName;

	@Column(nullable = false, length = 200)
	private String actionClassName;

	public Recht()
	{
	}

	public Recht(Rol rol,
			Class< ? extends IPrincipalSource<EduArtePrincipal>> principalSourceClass,
			Class< ? extends WaspAction> action)
	{
		setRol(rol);
		setPrincipalSourceClassName(principalSourceClass.getName());
		setActionClassName(action.getName());
	}

	public void setPrincipalSourceClassName(String principalSourceClassName)
	{
		this.principalSourceClassName = principalSourceClassName;
	}

	public String getPrincipalSourceClassName()
	{
		return principalSourceClassName;
	}

	public void setActionClassName(String actionClassName)
	{
		this.actionClassName = actionClassName;
	}

	public String getActionClassName()
	{
		return actionClassName;
	}

	public void setRol(Rol rol)
	{
		this.rol = rol;
	}

	public Rol getRol()
	{
		return rol;
	}

	@SuppressWarnings("unchecked")
	public Class< ? extends IPrincipalSource<EduArtePrincipal>> getPrincipalSourceClass()
			throws ClassNotFoundException
	{
		return (Class< ? extends IPrincipalSource<EduArtePrincipal>>) Class.forName(
			principalSourceClassName).asSubclass(IPrincipalSource.class);
	}

	public Class< ? extends WaspAction> getActionClass()
	{
		try
		{
			return Class.forName(actionClassName).asSubclass(WaspAction.class);
		}
		catch (ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}

	public EduArtePrincipal getPrincipal() throws ClassNotFoundException
	{
		return ReflectionUtil.invokeConstructor(getPrincipalSourceClass()).createPrincipal(
			getActionClass());
	}

	public boolean isPrincipal(EduArtePrincipal principal)
	{
		return principal.getSourceClass().getName().equals(getPrincipalSourceClassName())
			&& principal.getActionClass().equals(getActionClass());
	}
}
