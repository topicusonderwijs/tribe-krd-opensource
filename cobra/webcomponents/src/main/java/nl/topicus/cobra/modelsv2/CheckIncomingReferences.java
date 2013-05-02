package nl.topicus.cobra.modelsv2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nl.topicus.cobra.entities.FieldPersistance;

/**
 * Fields geannoteerd met CheckIncommingReferences worden gebruikt door het
 * {@link IChangeRecordingModel} om te controleren of er andere entiteiten verwijzen naar
 * de entiteit. De meest logische plaats voor deze annotatie is op een OneToMany relatie,
 * waarbij de entiteiten in de lijst buiten het beheer van het model kunnen vallen. Het is
 * verstandig om deze annotatie te combineren met een {@link FieldPersistance} SKIP.
 * 
 * @author papegaaij
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface CheckIncomingReferences
{
}
