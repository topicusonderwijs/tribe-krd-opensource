package nl.topicus.eduarte.entities.jobs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nl.topicus.eduarte.entities.jobs.logging.JobRun;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JobRunClass {
	Class< ? extends JobRun> value();
}
