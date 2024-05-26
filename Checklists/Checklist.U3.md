# Übung 3
Erstellen Sie die Simulationen für den Beleg. Jede Simulation soll eine eigene main-Methode haben.

Für die zufällig zu erzeugenden Mediadateien kann auch eine Liste mit verschiedenen Instanzen oder Eigenschaften erzeugt werden, aus der zufällig ausgewählt wird.

Weitere Informationen stehen im Anforderungsdokument unter der Überschrift Simulation.


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
- [ ] keine weiteren Bibliotheken außer JUnit5, Mockito und JavaFX (und deren Abhängigkeiten)
- [ ] keine Umlaute, Sonderzeichen, etc. in Datei- und Pfadnamen
- [ ] kompilierbar
- [ ] Trennung zwischen Test- und Produktiv-Code
- [ ] main-Methoden nur im default package des module belegProg3
- [ ] ausführbar
- [ ] Simulation 1
- [ ] Trennung zwischen GL und Simulationslogik
- [ ] kritische Bereiche/atomare Operationen verwendet
- [ ] Aktionen der threads produzieren Ausgaben auf der Konsole

### empfohlene Realisierungen als Vorbereitung auf den Beleg
werden überprüft (aber nicht bewertet), wenn hier in der vorgegebenen Reihenfolge als bearbeitet angegeben
- [ ] keine Verwendung von Thread.sleep o.Ä. bzw. nur mit 0-Werten
- [ ] Änderungen an der Geschäftslogik produzieren Ausgaben auf der Konsole
- [ ] deterministische Funktionalität testbar
- [ ] Simulation 2
- [ ] mindestens je ein Test für alle in der Simulation verwendeten Methoden die auf die Geschäftslogik zugreifen
- [ ] alle Tests sind deterministisch
- [ ] Simulation 3

