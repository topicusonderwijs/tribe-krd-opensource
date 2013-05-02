/*
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.app.security.actions;

import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;

import org.apache.wicket.security.actions.WaspAction;

/**
 * Actie om aan te geven dat iemand daadwerkelijk traject uitvoerende is, maw deze persoon
 * heeft direct een permissie gekregen op het niveau uitvoerend.
 * 
 * @author marrink
 */
@RechtenSoorten(RechtenSoort.INSTELLING)
@Module(EduArteModuleKey.DEELNEMER_BEGELEIDING)
@Description("Uitvoerende")
public interface Uitvoerend extends WaspAction
{
}
