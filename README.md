## Software Engineering & Testing - Group 6 Project Repo

Test...

### Oppsett

Hvis noe er uklart eller dere ikke får det til. Se slides fra forelesningen om Version Control, eller
spør hverandre på Discord. 

1. [Gå til Dev-Branchen på Github](https://github.com/heathensoft/SET_Gruppe_6/tree/dev)
2. Trykk på "Code" knappen og kopier URL under Clone
3. I Intellij trykk på New Project from Version Control, lim inn og trykk på Clone
4. Check om dere får kjørt "TestApp" (Skal bare være å trykke på "Run")

### Workflow (Guide)

En "Oppgave" er en user story eller funksjonalitet fra backloggen.
En Sprint med Backlogg over Oppgaver som skal utføres er satt opp som et "Project" på Github.

***Slik jeg forstår det til nå:*** (Vi finner ut av ting mens vi holder på)

1. Velg en Oppgave fra sprint backloggen som dere vil utføre
2. Legg dere til med et grovt estimat og dra Oppgaven over til "In Progress"
2. Lag en ny branch med et passende navn til Oppgaven
3. Programmer denne funksjonaliten.
4. commit det dere gjør til den nye branchen etterhvert som dere jobber med den.
5. Når dere er ferdig pusher dere endringene dere har gjort over på dev-branchen.
6. Her kan det oppstå konfliker (Men vi tar det som det kommer)
7. Så kan dere slette branchen dere er på.
8. Pull fra dev-branchen (Slik at dere får med endringer andre har gjort imellomtiden)
8. Repeter

Ikke tenk på testing enda hvis dere er usikre. Dere kan godt sette opp tester før dere pusher også, men vi bruker nok hovedsaklig dev-branchen for testing.

Når hele Sprinten er klar (Eller det vi rekker å bli ferdig med) og vi har satt opp tester der det er nødvendig.
Pusher jeg dev-branchen over på main-branchen.

***Oppgave Branch (Programmer en Oppgave fra Sprinten) -> Dev Branch (Testing og Justeringer) -> Main Branch (Når vi er fornøyd)***