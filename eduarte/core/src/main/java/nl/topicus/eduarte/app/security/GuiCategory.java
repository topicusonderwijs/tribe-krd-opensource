/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.app.security;


/**
 * @author marrink
 */
public final class GuiCategory
{
	/**
	 * Lezen
	 */
	public static final int RENDER = 0x2;

	/**
	 * Schrijven
	 */
	public static final int ENABLE = 0x4 | RENDER;

	public static final int DOCENT = 0x8;

	public static final int BEGELEIDER = 0x10;

	public static final int UITVOEREND = 0x20;

	public static final int VERANTWOORDELIJK = 0x40;

	/**
	 * Werkt op zelfde organisatie-eenheid-locatie, implies DOCENT | BEGELEIDER |
	 * UITVOEREND | VERANTWOORDELIJK
	 */
	public static final int ORGANISATIE_EENHEID =
		0x80 | DOCENT | BEGELEIDER | UITVOEREND | VERANTWOORDELIJK;

	/**
	 * Voor gehele instelling, implies ORGANISATIE_EENHEID
	 */
	public static final int INSTELLING = 0x100 | ORGANISATIE_EENHEID;

	/**
	 * Landelijk beheer.
	 */
	public static final int BEHEER = 0x200;

	/**
	 * Deelnemer voor deelnemerportaal.
	 */
	public static final int DEELNEMER = 0x400;
}
