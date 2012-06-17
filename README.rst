====================
BlueTrack
====================
By Tim Schmiedl, Milos Babic


Mit BlueTrack werden Routen, die zu Fuß, mit Fahrrad oder mit Auto zurückgelegt werden, per GPS aufgenommen.
Diese Tracks werden mit vielen weiteren Informationen wie z.B Anzahl der Schritte bei Joggern oder Durchschnittsgeschwindigkeit in der Datenbank abgelegt,
welche dann jederzeit zur späteren Analyse verwendet werden können.
Jogger und Fahrradfahrer können somit ihre Trainingserfolge beobachten.

Ziele
====

Mussziele:

- Protokollierung der Standorte, zurückgelegte Strecken und Höhen für jede Trainingseinheit.	
- Trainingseinheiten speicherbar und für spätere Analyse wiederverwendbar.
- Grafisch ansprechende Statistiken in der App mit grafischer Darstellung der zurückgelgten Strecke (mithilfe von Googlemaps).
- Schrittzähler implementieren.


Kannziele:

- Googlemaps verbinden um weitere Rad/joggingwege zu finden.
- Statistik auf Computer übertragen.
- Liveanzeige der Strecke.
- Anpassung der App an unterschiedliche Auflösungen (Tablet).


Features
====
- Modi: Joggen, Fahrrad und Auto fahren.
- Live-Diagramme über Geschwindigkeit, Höhenunterschied, Distanz zwischen Messpunkten und Länge der Schritten.
- Statistik samt Live-Diagramme zu jedem aufgenommenem Track abrufbar.
- Tracking ist pausierbar
- Genauigkeit der Darstellung der zurückgelegten Strecke auf GoogleMap einstellbar.
- Eintragen von Rating und Notiz zu den einzelnen Tracks.
- Sprachpakete: Deutsch und Englisch.
- automatisiertes Senden von Absturzberichten (ACRA - https://code.google.com/p/acra/)


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

Beschreibung der Architektur der App. 
Pakete, Trackmanager, Manifest etc.

UML:
	UseCase
	Klassendiagramm
	evtl. Dom�nklassen, Paket

Datenbank:
	ER-Diagramm


Problem, Schwierigkeiten
====
was ist schwierig gewesen, mit welchen Problemen hatten wir nicht gerechnet
(z.B. ungenaues GPS, Schrittz�hler siehe Abhandlung)


Weiterentwicklung
====
kurz sagen, dass wir noch nach beenden des Projekts weiterzuentwickeln
Ideen was dann noch kommen kann


Statistiken
====
Lines of Code, Icons...


Bugs, unvollst�ndige Dinge
====
falls es so was noch geben sollte
