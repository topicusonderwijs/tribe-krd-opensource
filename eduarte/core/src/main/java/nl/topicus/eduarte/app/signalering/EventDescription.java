package nl.topicus.eduarte.app.signalering;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nl.topicus.eduarte.entities.signalering.Event;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventDescription
{
	String name();

	EventAbonnementType[] abonnementTypes();

	Class< ? extends EventAbonnementConfiguration< ? >> configuratie() default NoEventAbonnementConfiguration.class;

	Class< ? extends EventHandler< ? extends Event>> handler();
}
