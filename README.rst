====================
BlueTrack
====================
By Tim Schmiedl, Milos Babic


Mit BlueTrack werden Routen, die zu Fuß, mit Fahrrad oder mit Auto zurückgelegt werden, per GPS aufgenommen.
Diese Tracks werden mit vielen weiteren Informationen wie z.B Anzahl der Schritte bei Joggern oder Durchschnittsgeschwindigkeit in der Datenbank abgelegt, welche dann jederzeit zur späteren Analyse verwendet werden können.
Jogger und Fahrradfahrer können somit ihre Trainingserfolge beobachten.
Weiterhin kann man sich die genau zurückgelegte Strecke später mithilfe von Google-Maps metergenau abzeigen lassen um so auch eventuell beim nächstem Mal neue Strecken zu erkunden.

Ziele
====

Mussziele:

- Protokollierung der Standorte, zurückgelegte Strecken und Höhen für jede Trainingseinheit.	
- Trainingseinheiten speicherbar und für spätere Analyse wiederverwendbar.
- Grafisch ansprechende Statistiken in der App mit grafischer Darstellung der zurückgelgten Strecke (mithilfe von Googlemaps).
- Schrittzähler implementieren.


Kannziele:

- Googlemaps verbinden um weitere Rad/Joggingwege zu finden.
- Statistik auf Computer übertragen.
- Liveanzeige der Strecke.
- Anpassung der App an unterschiedliche Auflösungen (Tablet).

Die Mussziele wurden alle eingehalten, auch die Kannziele (mit Ausnahme der Übertragung auf den Computer) wurden erfüllt.
Zur Übertragung an den Computer siehe Weiterentwicklung


Features
====
- Modi: Joggen, Fahrrad und Auto fahren.
- Live-Diagramme über Geschwindigkeit, Höhenunterschied, Distanz zwischen Messpunkten und Länge der Schritten.
- Statistik samt Live-Diagramme zu jedem aufgenommenem Track abrufbar.
- Tracking ist pausierbar
- Genauigkeit der Darstellung der zurückgelegten Strecke auf GoogleMap einstellbar.
- Eintragen von Rating und Notiz zu den einzelnen Tracks.
- Sprachpakete: Deutsch und Englisch.
- automatisiertes Senden von Absturzberichten (ACRA (Application Crash Report for Android)- https://code.google.com/p/acra/)


Benutzerhilfe
====
Die Bediehnung der App ist möglichst intuitiv gehalten. Um einen kompletten Überblich über alle Activities zu bekommen und
so ein Eindruck von der Navigation zu erhalten gibt es folgendes Diagramm.

--> Diagramm (mit Activities) <--


Programm-Architekur (mit UML) 
====
Um die App auch weiterentwickeln zu können, war uns eine solide und modulare Architektur sehr wichtig.
Zu den sicherlich wichtigsten Klassen gehören die Klassen "TrackManager" sowie "Track".
Die Funktion der Klassen ist vermutlich schon teilweise aus dem Klassendiagramm auszulesen.

- Klasse "Track"
	- stellt jeweils eine Laufstecke dar
	- wird vom Trackmanager während der Aufnahme mit Daten versorgt (Methode: addLocation, addSteps)
	- erstellt in Echtzeit Statistiken (Klasse "Statistics"), die dann auch grafisch angezeigt werden können (Klasse "GraphLiveView")
- Klasse "TrackManager"
	- verwaltet alles was mit Tracks zu tun hat (erstellen von Track(Live), auslesen aus der Datenbank)
	- beinhaltet LocationLister (versorgt Track mit GPS-Daten); SensorEventListener (erkennt Schritte aus den Sensordaten, und sendet Schritte and Track)
	- enthälst statische Methoden zum generieren von Tracks aus der Datenbank

(Beschreibung der Architektur der App. Pakete, Trackmanager, Manifest etc.)
UML:
	UseCase
	Klassendiagramm
	Paket

--> ER-Diagramm <--
Wie aus dem ER-Diagramm deutlich ist, besteht die Datenbank aus nur zwei Tabellen.
Die Tabelle "gps_track" enthält die wichtigsten Daten welche schließlich in den Statistiken angezeigt werden.
Die Tabelle "gps_location" beinhaltet alle Locations die je bei einem Tracking aufgenommen werden. Dies ist notwendig, da auf der GoogleMap der genaue Streckenverlauf augezeichnet wird. Außerdem sind die Locations essential um einen Track erneut zu generieren. Dis geschieht, indem die Locations genau wie bei der Live-Aufnahme mithilfe der "addLocation"-Methode zugespielt werden. Somit unterscheidet sich ein generierter Track nicht von einem Live-aufgenommen.


Problem, Schwierigkeiten
====
Die größten Probleme lagen vermutlich bei der Verwertung der (ungenauen) Daten von GPS sowie der Bewegungssensoren.

- GPS
	- die Locations, welche das GPS liefert sind im besten Fall im Radius von 5 m genau
	- zu viele Locations auf kleinem Raum liefern genauso schlechte Ergebnisse, wie zu wenig Locations
	- wenn der Standort nicht vor Beginn einer Aufnahme durch GPS festgelegt wurde, dauert es bis zu 2 min (Samsung Galaxy S plus) bis eine erste Locations gesendet wird
- Sensoren
	- Schritte müssen aus den Erschütterungen (dh. Beschleunigunssensoren) entlang der Y-Achse des Telefons ausgelesen werden. Die ist bei bei starken Erschütterungen - z.B. beim Joggen oder Rennen - relativ gut möglich, da die Hochpunkte der Y-Achsen-Beschleunigunssensoren relativ eindeutige Sinus-Wellenformen liefert. Doch bei normalem Laufen sind keine klaren Hochpunkte mehr zu erkennen, es ergibt sich eine ziemlich chaotisches Funktionskurve. Dadurch ergibt sich eine starke Ungenauigkeit der Schritte beim Laufen.
	- wird das Telefon recht locker in der Tasche gehalten ergibt sich ebenfalls mehr Inteferenzen und dadurch Ungenauigkeiten
	- wird das Telefon nicht aufrecht gehalten verschieben sich die Y und Z-Achse, was wiederum zu Ungenauigkeiten führt.


Weiterentwicklung
====
Aufgrund der unserer Meinung nach Ansprechenden Entwicklung der App haben wir uns schon vor einiger Zeit entschieden, die App auch nach Ablauf des Projektes weiter zu entwickeln. (Version 1.0 wird die abzugebende Version sein)
Wir werden die Entwicklung genau wie auch schon in unserem Projekt weiterhin mithilfe der Versionsverwaltung "git" und öffentlich zugänglich auf github.com entwickeln.

Hier sind einige der Punkte die wir für künftige Versionen geplant haben:

- integrieren der App in "Google Play"
- kostenlose Version (evtl. mit Werbung) + evtl. kostenplichtige Pro-Version (zusätzliche Features, z.B. Übertragung der Statistiken auf Computer, mehr Modi auswählbar, mehr Einstellungsmöglichkeiten) 
- Überarbeitung aller Icons (verbleibende nichtkonforme Icons an die Android Richtlinien anpassen)
- MapLiveView - d.h. Anzeige des aktuellen Tracks auch während der Aufnahme (RunningActivity)

Statistiken
====
Lines of Code, Icons...


Bugs, Issues
====
- ungenauer Schrittzähler (vor allem beim Laufen), ist aber nicht ohne massiven Aufwand verbesserbar
- ungenaue erste Location 
