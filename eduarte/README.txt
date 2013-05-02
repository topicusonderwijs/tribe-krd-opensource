=== Installatie van Eduarte ===

Dit project is niet zelfstandig te gebruiken. Je dient ook cobra naast dit 
project uitgecheckt te hebben:

	/cobra
	/eduarte
	/eduarte/core
	/eduarte/module-krd
	/eduarte/module-*
	/eduarte/util
	/eduarte/web
	/eduarte/webservices

== Eclipse projecten aanmaken ==

Eerst dien je in de eduarte project folder mvn eclipse:eclipse te draaien, 
daarna kan je elk project apart importeren in Eclipse (of maak gebruik van de
Maven Eclipse plugin (m2e) om de projecten te importeren.

== Database instellingen ==

In het eduarte-web project staat een hibernate.properties.example bestand. Dit
bestand moet je kopieren in dezelfde directory en "hibernate.properties" noemen.
De gegevens in dit bestand moet je aanpassen zodat je de juiste database 
gebruikt. De folder heet: web/src/test/resources

== Fout bij starten ==

Bij het starten van de webapplicatie kan je een vervelende foutmelding krijgen,
namelijk dat CXF niet gestart kan worden vanwege een API versie conflict. Om
dit op te lossen dien je in je JAVA_HOME/lib/endorsed folder de volgende JAR te
zetten:

 * javax/xml/bind/jaxb-api/2.1/jaxb-api-2.1.jar
 
Een standaard installatie van je JVM bevat de 2.0 versie van de API, en Eduarte
maakt gebruik van de 2.1 versie. Je kan deze API vinden in je locale Maven 
repository.

== Opstart opties ==

De applicatie dient in het Nederlands te draaien, voor eclipse heb je ook een minimale 
permgen space nodig. Plaats de onderstaande regel in je VM args van het opstart item (Start.java):

-Duser.country=NL -Duser.language=nl -Xmx512m -Xms256m -XX:PermSize=128m
