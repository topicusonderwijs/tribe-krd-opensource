Hallo,

De afgelopen maanden zijn Maurice en ik druk bezig geweest met een verbeterde 
afhandeling van Hibernate objecten via models in Wicket. Het probleem hierbij 
is dat Hibernate objecten niet opgeslagen mogen worden in de sessie, aangezien 
de HibernateSession niet Serialiazable is. Het nieuwe model maakt het mogelijk 
om toch rechtstreeks met Hibernate objecten te werken, deze te bewerken en op 
te slaan in de de sessie. Aangezien dit een veel voorkomend probleem is, en 
veroorzaker is van veel vervelende fouten in DBS, wil ik jullie via deze mail 
op de hoogte brengen van de mogelijkheden van het nieuwe 
'ExtendedHibernateModel' en het laatste telg in de Hibernate model familie: 
het 'ChangeRecordingModel'.


- Werking -
Het begint allemaal bij het standaard HibernateModel. Dit model slaat voor 
Hibernate objecten bij een detach operatie het ID en de class van het object 
op. Bij het attachen van het model wordt het object, met een versie Hibernate 
sessie, opnieuw uit de database gehaald. Dit zorgt er echter wel voor dat alle 
wijzigingen op het object verloren gaan. Hier biedt het ExtendedHibernateModel 
uitkomst.

Het ExtendedHibernateModel zorgt ervoor dat voordat het model gedetached 
wordt, alle velden opgeslagen worden. Dmv. reflectie worden de velden van het 
object uitgelezen, en worden, waar nodig, nieuwe models voor de waarden van 
deze velden gemaakt. Of dit een extended of een standaard Hibernate model is 
wordt bepaald door de ModelManager. Deze geeft in feite de grens aan van de 
objecten die bewerkt kunnen worden.

Als laatste is er dan het ChangeRecordingModel. Dit model, dat extend van 
ExtendedHibernateModel, is in staat om in 1 operatie alle wijzigingen op een 
object, en alle ondergelegen objecten, op te slaan in de database. Het maakt 
hierbij niet uit wat voor wijzigingen er gemaakt zijn. Er kunnen nieuwe 
objecten aangemaakt zijn, lijsten kunnen veranderd zijn (toevoegen en/of 
verwijderen) en properties kunnen veranderd zijn.


- Gebruik -
In feite komt het gebruik hier op neer:
 - Maak een DefaultModelManager.
 - Gebruik deze ModelManager om een ChangeRecordingModel te maken.
 - Wijzig de objecten op wat voor manier dan ook, al dan niet via meerdere 
   request cycles en/of op meerdere pagina's.
 - Sla de wijzigingen op met een call naar 'saveObject()'.

Hierbij dient er op gelet te worden dat er altijd maar 1 ModelManager gebruikt 
wordt. Ook moet deze ModelManager gebruikt worden om alle betrokken Hibernate 
models te maken. De makkelijkste manier om dit op te lossen is door de 
operaties te laten verlopen via het Wicket PropertyModel en/of 
CompoundPropertyModel.

Bij het aanmaken van de DefaultModelManager moet een lijst met classes gegeven 
worden. Dit zijn de entiteit classes die door de manager beheerd moeten 
worden. De entiteiten mogen wel refereren naar entiteiten die niet door de 
manager beheerd worden, maar deze onbeheerde entiteiten mogen niet gewijzigd 
worden (dus ook geen nieuwe instances maken die nog niet in de database 
staan).

Verder is de volgorde van deze classes belangrijk. Deze dienen in delete-order 
gegeven te worden. Stel er is een Auto met Wielen. Elk Wiel heeft een 
referentie naar de Auto waar hij bij hoort (many-to-one). Als nu de Auto 
verwijderd wordt terwijl de Wielen er nog aan zitten, zal er een integrity 
constraint violation optreden. De Wielen dienen eerst verwijderd te worden en 
daarna pas de Auto. De delete-order is dus: Wiel, Auto. ChangeRecordingModel 
zal deze volgorde aanhouden voor het verwijderen van objecten en de omgekeerde 
volgorde voor het toevoegen.


- De code -
De models zijn te vinden in Cobra-trunk (onderwijs/cobra/trunk/cobra) in het 
package nl.topicus.cobra.modelsv2. Let er wel op dat, hoewel de classnames en 
API grotendeels gelijk is aan die van de models in nl.topicus.cobra.models, de 
werking is anders. De models zijn ook niet compatible en kunnen dus niet op 1 
pagina gecombineerd worden.


Ik hoop dat jullie hier iets aan hebben, en mochten er nog vragen/opmerkingen 
zijn, dan hoor ik het wel.

Groeten,
Emond
_______________________________________________
Ontwikkelaars mailing list
Ontwikkelaars@lists.topicus.nl
http://lists.topicus.nl/mailman/listinfo/ontwikkelaars