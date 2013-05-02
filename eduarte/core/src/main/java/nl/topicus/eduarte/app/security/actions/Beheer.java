/*
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.app.security.actions;

import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;

import org.apache.wicket.security.actions.WaspAction;

/**
 * Actie om aan te geven dat iemand daadwerkelijk beheermedewerker is, maw deze persoon
 * heeft direct een permissie gekregen op het niveau beheer.
 * 
 * @author marrink
 */
@RechtenSoorten(RechtenSoort.BEHEER)
@Description("Beheer")
public interface Beheer extends WaspAction
{
}
