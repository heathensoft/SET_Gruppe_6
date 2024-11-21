# SecureHome


* **Mahmad**
* **Frederik**

## Prototypen

* IDE: Intellij IDE
* Språk: Java (JDK 23)
* Build Tool: Gradle

Prototypen er "proof of concept" for hvordan klient og server applikasjoner kan
benytte vårt nettverks API til å kommunisere med hverandre.
Med ferdig definerte datatyper og vår "in-house" nettverksprotokoll, har vi tilrettelagt for en felles
standard for kommunikasjon.
Nettverks API'et ar agnostisk iforhold til database og utvikling av brukergrensesnitt.
Dette er helt opp til fremtidige utviklere av systemet.
Vi kaller det "proof of concept" da mye av det grunnleggende er implementert men mye funksjonalitet
fortsatt mangler.


## Hvordan kjøre prototypen

Under app-modulen i prosjekt strukturen:

```
Project/
├─ app/
│  ├─ Server.java
│  ├─ Client.java
├─ datatype/
├─ network/
│  ├─ core/
```
Er det to programfiler: Server og Client.

1. Start Server.java. Dette starter en server på port: 8080 og laster databasen fra fil.
2. Start Client.java. Dette starter et klient program som kobler opp mot server.
3. Når begge programmene kjører, skal to vinduer være åpne. Pass på at "Client Application" vinduet er i fokus.
4. Programmet er tekst basert så pass på at output feltet i IDE er synlig.

*Programmene burde kjøre på MAC men vi har kun testet og kjøre programmene på PC.*

Hvis "Client Application" vinduet har fokus, kan appen lese input.
Når klient starter og hvert tiende sekund sender klient en request til server for en oppdatert oversikt over
alle brukere og låser i systemet. Vi satt ti sekunder slik at output blir lettere å lese.

Klient programmet reagerer på følgende input:

| Input  | Funksjon           |
|--------|--------------------|
| Escape | Close              |
| P      | Pause Refresh      |
| 1      | Print Users        |
| 2      | Print Locks        |
| 3      | Toggle Random Lock |

* "Pause Refresh" stopper klient fra å bli automatisk oppdatert. Kan gjøre det lettere og lese output.
* "Print Users" printer ut en oversikt over alle brukere i systemet.
* "Print Locks" printer ut en oversikt over alle låsene i systemet.
* "Toggle Random Lock" velger en tilfeldig lås blandt oversikten og sender en request om å "toggle" open / close.

Alle endringer blir lagret i databasen. Starter du programmene på ny, kommer endringene med.
Det er annen funksjonalitet som vi har implementert, men som egner seg bedre med brukergrensesnitt.
F.eks. Legge til / fjerne bruker, endre bruker rolle m.m.
(Vi har testet å kjøre server og klient programmene på hver sin PC, og det fungerer.)
Poenget er at vi har en klient og server som kommuniserer med hverandre med vårt nettverks API.

## Hvordan teste prototypen

Trykk på Gradle (Elefant) ikonet oppe i høyre hjørnet i Intellij.
```
SET_Gruppe_6/
├─ tasks/
│  ├─ verification/
│  │  ├─ test

```
Naviger build strukturen ovenfor og dobbeltklikk på "test".
Dette vil kjøre tester for alle modulene: datatype, network, app.

Hvis du skulle få: "Test events were not received" må man kjøre tester for hver enkelt klasse isteden.
Vi er usikre på hvorfor dette skjer, men det kom først opp helt i slutten av prosjektet.

Kjøre tester på hver enkelt klasse:

Under prosjektstrukturen for hver modul er det en tilsvarende test-struktur. Åpne hver enkelt klasse
og trykk på run Test (I margen helt øverst ved klasse definisjonen)

## ALT UNDER ER FRA ET TIDLIGERE STADIET I PROSJEKTET
(Ikke nødvendig å lese, men viser tankegangen rundt oppstart)

### Hovedfokus i prosjektet (Teknisk implementasjon)


Det [tenkte systemet](https://github.com/heathensoft/SET_Gruppe_6/blob/dev/doc/Prjoject_Overview.md) har mye funksjonalitet som vi aldri rekker å gjennomføre som et semester-prosjekt.
Derfor tenker vi at vi fokuserer på kommunikasjon mellom en enkel Bruker App og en HUB for et system.

* En HUB (Server Program med Database)
* En App som kan kommunisere med HUB. (Helst et fungerende brukergrensesnitt)
* Låse / Låse opp dører "remotely" med App via. HUB
* Loggføre tidspunkt og Bruker for slike hendelser i HUB
* Legge til Brukere i et slikt system via. App.
* Databasen over alle brukerkontoer (Bedrift-Serveren) Simmulerer vi. (En liste i memory eller fil)
* Om vi kommer så langt, eventuelt generere digitale nøkler for brukere.

## Sprints 



### [Sprint 1](https://github.com/users/heathensoft/projects/4)

***Oppsett og forberedning***

* Opprette Github konto
* Opprette et Github test-prosjekt for å teste basic git kommandoer
* Valg av build tools / Intellij + Gradle prosject struktur
* Sette opp en "kanban" lignende oversikt over oppgaver som er små nok til å estimere og utføre.

***Grov plan***

Vårt prosjekt omhandler kommunikasjon med databaser via. internett, samt et brukergrenesnitt for å vise diverse informasjon.

For å kunne sende data, diverse requests og database queries har vi behov for å definere et grunnleggende sett med data-typer.
Der disse data-typene skal kunne lagres i databaser, vises i brukergrensesnitt og sendes over internett (Og blåtann).

Derfor tenker vi å dele opp prosjektet i moduler (pakker), hvor hver modul har forskjellig "funkjonelt ansvar".

1. ***Datatype-modulen*** er "grunn-layeret" i prosjektet, inneholder kun datatyper. Ingen "dependencies" til andre moduler.
2. ***Database-modulen*** (midlertidig navn) Er et API / Library som kan "consumes" av alle "Apper" uavhengig av platform. Skal også brukes av servere for kommunikasjon med og mellom Apper. inneholder funksjoner for database kommunikasjon, Server/Klient kommunikasjon, TCP-ptotokoller og Sikkerhet/Kryptering. Og bruker Datatype-modulen til å sende / motta data.
3. ***App-modulen***. Her tenker vi å simmulere en App som utfører tenkt funksjonalitet og viser det i et brukergrensesnitt. Bruker eksternt API for dette. Fokuset er først å fremst på de andre modulene. Men hadde vært ålreit å få til dette iløpet av prosjektet. App-modulen benytter Database-modul API'et. 

***Utførelse***

Uten at vi enda er helt sikre på hvilke datatyper vi behøver for å representere vår system (Smartlås-system) starter vi med noen små datatyper vi helt sikkert har bruk for.

Deretter ser vi på ulike metoder for å serialisere / deserialisere våre datatyper med. Siden vi ikke streamer video eller lignende (Sender ikke store mengder med data) kunne vi brukt Java sin "innebygde" serialiserings API, eller utviklet vårt eget.

Siden vi har begrenset med tid velger vi å ikke finne opp hjulet på nytt her. Json er et velkjent format som også er lesbart og derfor lett å debugge. Så vi går med Json.

***Så i Sprint 1 er fokuset først og fremst på prosjekt struktur. Da det gjør at vi slipper å refaktorere og gjøre om på dette senere.
Fra der lager vi noen få klasser for datatyper og implementerer metoder for å serialisere / deserialisere disse. Og til sist kjøre tester.***

***Ressurser***

Gradle Tutorial

[![IMAGE ALT TEXT](https://img.youtube.com/vi/-dtcEMLNmn0/0.jpg)](https://www.youtube.com/watch?v=-dtcEMLNmn0 "click to watch")

Eksempel: Json Simple

[![IMAGE ALT TEXT](https://img.youtube.com/vi/ywLKpHw1MjQ/0.jpg)](https://www.youtube.com/watch?v=ywLKpHw1MjQ "click to watch")


### [Sprint 2](https://github.com/users/heathensoft/projects/5)

***Grov plan***

Her vil vi ha en mer spesifik oversikt over hvilke Datatyper vi trenger. Vi vil fortsette å utvide Datatype-modulen med flere typer og
metoder for å serialisere disse etter behov.
Samtidig vil hovedfokuset være på ENTEN:
1. ***Database modellering og Java Database Connectivity (JDBC)***: (lagre hente data) som skal kunne representeres som DataTyper på Java siden.
2. ***Server / Klient kommunikasjon***: Slik at vi kan sende og motta data og requests mellom Servere, mobiler, smarhus-HUB ol.

Men det er heller ikke noe i veien for å jobbe i parallell. At gruppemedlemmer f.eks. tar ansvaret for å sette seg inn i hver sin del.
Mye kommer også ann på hva vi kommer til å gå igjennom i forelesning, slik av vi hele tiden forholder oss til kurset. 

***Ressurser...?***

Eksempler: Server / Klient kommunikasjon

[![IMAGE ALT TEXT](https://img.youtube.com/vi/-xKgxqG411c/0.jpg)](https://www.youtube.com/watch?v=-xKgxqG411c "click to watch")
[![IMAGE ALT TEXT](https://img.youtube.com/vi/hIc_9Wbn704/0.jpg)](https://www.youtube.com/watch?v=hIc_9Wbn704 "click to watch")

Eksempler: Java Database Connectivity

[![IMAGE ALT TEXT](https://img.youtube.com/vi/9ntKSLLDeSs/0.jpg)](https://www.youtube.com/watch?v=9ntKSLLDeSs "click to watch")
[![IMAGE ALT TEXT](https://img.youtube.com/vi/7v2OnUti2eM/0.jpg)](https://www.youtube.com/watch?v=7v2OnUti2eM "click to watch")


## Organisering / Workflow

### Prosjekt Struktur

***Foreløpig. Mulig slå sammen datatype / database senere***

* DataType-Modulen: Kode som de andre Modulene behøver for å representere enheter i systemet. Som F.eks. en Bruker eller en Lås osv. (Sprint 1) 
* Database-Modulen: Kode for kommunikasjon med Database (Sprint 2.)
* App-Modulen: Kode for brukergrensenitt (Simmulerer en Mobil App)


### Oppsett

Hvis noe er uklart eller dere ikke får det til. Se slides fra forelesningen om Version Control, eller
spør meg / hverandre på Discord. 

1. [Gå til Dev-Branchen på Github](https://github.com/heathensoft/SET_Gruppe_6/tree/dev)
2. Trykk på "Code" knappen og kopier URL under Clone
3. I Intellij trykk på New Project from Version Control, lim inn og trykk på Clone
4. Check om dere får kjørt "TestApp" (Skal bare være å trykke på "Run")

### Workflow (Guide)

En "Oppgave" er en user story eller funksjonalitet fra backloggen.
En Sprint med Backlogg over Oppgaver som skal utføres er satt opp som et "Project" på Github.

***Slik jeg forstår det til nå:*** (Vi finner ut av ting mens vi holder på)

1. Velg en Oppgave fra sprint backloggen som dere vil utføre
2. Legg dere til og dra Oppgaven over til "In Progress"
2. Lag en ny branch med et passende navn til Oppgaven
3. Programmer oppgaven.
4. commit det dere gjør til den nye branchen etterhvert som dere jobber med den.
5. Når dere er ferdig pusher dere endringene dere har gjort over på dev-branchen.
6. Her kan det oppstå konfliker (Men vi tar det som det kommer :)
7. Pull fra dev-branchen (Slik at dere får med endringer andre har gjort imellomtiden)
9. Repeter

Ikke tenk på testing enda hvis dere er usikre. Dere kan godt sette opp tester før dere pusher også, men vi bruker nok hovedsaklig dev-branchen for testing.

Når hele Sprinten er klar (Eller det vi rekker å bli ferdig med) og vi har satt opp tester der det er nødvendig.
Pusher jeg dev-branchen over på main-branchen.

***Oppgave Branch (Programmer en Oppgave fra Sprinten) -> Dev Branch (Testing og Justeringer) -> Main Branch (Når vi er fornøyd)***
