====================
Post-Mortem-Analyse anhand von FBI Virtual Case File
====================
By Tim Schmiedl, Milos Babic



Post Mortem Analyse
====
Beispiel-Template http://art.cim3.org/pm_workshop/Project_Post_Mortem/Postmortem_Template--MSF.html


Ziele

- positive und negative Erfahrungen dokumentieren
  - Fehler erkennen 
	- Erfahrungen vertiefen
- Verbessern (Prozess, Führung, Vorgehensweise)

Nutzen & Schwierigkeiten

- PMA kann frei gestaltet werden
- Review von möglichst allen Mitgliedern


- je nach Genauigkeit & Größe des Projekts zeitintensiv
- Schwierig alle Mitglieder zusammen bekommen
- personelle Konflikte (Schuld zuschieben)


Ablauf

- Ramenbedinungen (Dauer der PMA, welches Endergebniss? Umfang? Wer?)
- Daten & Fakten zum Projekt 
- Analyse des Projektes, Erfahrung der Mitarbeiter im Projekt
- Aufarbeiten wichtigster Erfahrungen
- Abschlussbericht / Report

Methoden

- Interview
- Fragebögen
- Gruppendiskussion

FBI
====

Organisationsstruktur der FBI (2000)

- 12.500 Mitarbeiter
- 500 Büros
- 23 Abteilungen
- jede Abteilung beistzt eigenes IT-Budget -> keine einheitlichen Anwendungen und Datenbanken
- über 10.000 veraltete PCs, Scanner, Drucker und sonsitge Hardware
- unstabiles,langsames und unsicheres IT-Netzwerk

Arbeitsprozess bei FBI-Ermittlungen

1. Agent dokumentiert mithilfe von standardisierten Forms.
2. Berichte werden an Vorgesetzten überreicht.
3. Bericht wird nach Überprüfung in ACS eingetragen.
	
=> mühsame Papierarbeit

Automated Case Support (ACS)

- Verwaltungssoftware für Ermittlungsfälle der FBI
- sehr veraltetes System:
	- veraltete Datenbank
	- nur einfach Suchfunktionen
	- langsame Übertragung von Informationen

=> Bei Agenten unbeliebt.

=> Fazit: Projekt Trilogy.

Porjekt Trilogy

- Modernisierung der gesamten IT-Infrastruktur der FBI
- Projekt besteht aus 3 Unterprojekte
	1. neue Hardware( PCs, Drucker, Scanner etc)
	2. neues IT-Netzwerk (WAN und LAN)
	3. Virtual Case File
- Dauer: 2000 - 2003
- Budget: 380 Mio $


Virtual Case File
====

Was ist VCF?

- Verwaltungssoftware für FBI-rmittlungsfälle
- ersetzt veraltetes ACS
- WebInterface
- Zugriff und Verteilung von Informationen schnell und zuverlässig
- Auftragnehmer: Science Applications International Corp. (SAIC)
- Dauer: 3 Jahre
- Budget: $ 120 Mio.
- Vertrag: contract-plus
- Projektleiter: Special Agent Larry Depew (FBI)



Projektverlauf von VCF:

- September 2000: Start von Trilogy
- Oktober 2001: Start Virtual Case File
- Dezember 2001: Neuausrichtung VCF
- Januar 2002: weitere $78 Mio für Trilogy
- Februar 2002: Joint Application Development Sessions mit Depew
- November 2002: 
	- Matthew Patton (IT-Security Experte) verlässt Projekt.
	- SAIC und FBI haben Anforderungskatalog erstellt.
- Dezember 2002: weitere $ 123 Mio für Trilogy
- September 2003: GAO empfihelt FBI ein Blueprint für Trilogy
- Dezember 2003: 
	- Zalmai Azmi wird CIO
	- SAIC liefert VCF aus
- Juni 2004: FBI gibt SAIC neuen Auftrag: Initial Operatin Capability
- April 2005: FBI legt VCF still
- Mai 2005: neues Softwarprojekt Sentinel

- Endstand:
	- 700.000 Lines of Code
	- Dauer: 4 Jahre
	- Kosten: $ 170 Mio.



Analyse:

- fehlender Blueprint (Enterprise Architektur)
- Vertrag (Cost-plus-award-fee)
- Projektleitung:
	- unerfahrener Projektmanager (Special Agent Depew)
	- kein CIO --> ständig wechselnde Personal
	- Matthew Patton
- ProjektVerlauf:
	- Weboberfläche --> komplett neues system
	- Überspezifizierte Anforderungsheft
	- (SAIC) 8 Teams parallel wegen Zeitdruck --> inkompatibel
	- ständige Änderungswünsche der Agenten
	- schlechte Kommunikation Developer <--> Agenten
	- flash cutover (kein Plan B)
	- FBI sehr von sich überzeugt
- Ende:
	- Streit über Abweichungen von Anforderungen
	- Schlichtung bzw. neutraler Beobachter

Fazit:

