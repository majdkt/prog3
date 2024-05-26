# Übung 2
Erstellen Sie ein prototypisches CLI für den Beleg mit 4 Befehlen (und ohne Modi): ein Befehl zum Einfügen einer (vordefinierten) Mediadatei, ein Befehl zum Anzeigen der Mediadateien, ein Befehl zum Ändern des Zugriffszählers einer Mediadatei und ein Befehl zum Entfernen einer Mediadatei.
Legen Sie dafür die entsprechenden Module an. (siehe auch https://www.jetbrains.com/help/idea/creating-and-managing-modules.html#add-new-module)

Der Persistenzmodus sowie die Ausführung als Client bzw. Server sind nicht Teil der Übung.

## Abgabeanforderungen
Die Abgabe hat als zip-Datei zu erfolgen, die ein lauffähiges IntelliJ-IDEA-Projekt enthält. Sie sollte die befüllte Checkliste im root des Projektes (neben der iml-Datei) enthalten in der der erreichte Stand bezüglich des Bewertungsschemas vermerkt ist.

Änderungen an der Checkliste sind grundsätzlich nicht zulässig. Davon ausgenommen ist das Befüllen der Checkboxen und ergänzende Anmerkungen die _kursiv gesetzt_ sind.

## Quellen
Zulässige Quellen sind suchmaschinen-indizierte Internetseiten. Werden mehr als drei zusammenhängende Anweisungen übernommen ist die Quelle in den Kommentaren anzugeben. Ausgeschlossen sind Quellen, die auch als Beleg oder Übungsaufgabe abgegeben werden oder wurden. Zulässig sind außerdem die über moodle bereitgestellten Materialien, diese können für die Übungsaufgaben und den Beleg ohne Quellenangabe verwendet werden.
Flüchtige Quellen, wie Sprachmodelle, sind per screen shot zu dokumentieren.

## Bewertung
1 Punkt für die Erfüllung des Pflichtteils

### Pflichtteil
- [ ] Quellen angegeben
- [ ] zip Archiv
- [ ] IntelliJ-Projekt (kein Gradle, Maven o.ä.)
- [ ] JUnit5 und Mockito als Testframeworks (soweit verwendet)
- [ ] keine weiteren Bibliotheken außer JavaFX
- [ ] keine Umlaute, Sonderzeichen, etc. in Datei- und Pfadnamen
- [ ] kompilierbar
- [ ] Trennung zwischen Test- und Produktiv-Code
- [ ] main-Methoden nur im default package des module belegProg3
- [ ] ausführbar
- [ ] Benutzeroberfläche und Geschäftslogik korrekt aufgeteilt (mindestens 2-Schichten-Architektur)
- [ ] prototypisches CLI (nicht notwendig, wenn umfangreicheres CLI realisiert ist)

### empfohlene Realisierungen als Vorbereitung auf den Beleg
werden überprüft (aber nicht bewertet), wenn hier in der vorgegebenen Reihenfolge als bearbeitet angegeben
- [ ] event-System für die Kommunikation vom CLI zur GL realisiert
- [ ] ein Beobachter realisiert
- [ ] event-System für die Kommunikation von der GL zum CLI realisiert
- [ ] angemessene Aufzählungstypen verwendet
- [ ] zwei Tests für Beobachter realisiert
- [ ] zwei Tests für listener realisiert
- [ ] nach MVC strukturiert
- [ ] vollständiger Befehlssatz des CLIs

