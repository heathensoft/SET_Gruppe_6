## Hva vi skal forsøke å utvikle:




Siden et smart lås system er veldig omfattende må vi velge deler av systemet vi kan fokusere på. Siden vi har hatt java-programmering og databaser på skolen er det fordelaktig for oss å velge deler av systemet som omhandler dette. Vi har ikke hatt mobil-utvikling eller grensesnitt utvikling på skolen, så dette blir sekundært.

Vi har fått tips av Ida om at vi kan importere et grensesnitt API inn i programmet slik at vi kan simulere en mobil App. Men det er ikke prioritet på dette stadiet.



## Grov oversikt av systemet:

* En server på bedrift-siden: En database med ALLE brukere. Dette er personer som har lastet ned Appen og registrert seg med navn, mobil nr., adresse ol. 
Alle “Eiere” er også registret her (Disse blir registrert når de kjøper et system, med låser og en lokal wifi HUB) .



* En HUB for systemet: Inkluderer også en database. Denne databasen har oversikt over alle brukere av det lokale systemet, med rettigheter bestemt av Eier. I tillegg har HUB databasen en logg over bruk. F.eks. Kari Traa låste opp dør 3 kl.17.55 19/09/2024. HUB’en har også en oversikt over hvilke dører som er låst, batteristatus og feilmeldinger. HUB'en kan være en fysisk HUB eller software innstallert i en eksisterende smarthus løsning (Integrert).


* Mobil Appen: Hvor Eier eller Brukere som har rettighet kan søke databasen over alle brukere og legge til / fjerne brukere fra det lokale systemet. F.eks. Geir vil finne Kari og legge henne til i systemet slik at hun kan benytte systemet med “Beboer” rettigheter. Eller Kari søker etter en venninne å gi henne “Gjest” rettigheter for i morgen fra kl.12 til 15. Mobil Appen har også en oversikt over Alle dører i systemet. Denne informasjonen er hentet fra HUB’en. Og Brukere med rettighet kan låse opp / igjen dører fra avstand.



* Låsene / Dørene: Disse simulerer vi. De har status Låst / Låst opp. Batteri-nivå, Mekanisk feil / skade. Vi trenger ikke tenke så mye på låsene på dette stadiet. Vi er mest opptatt av Serverene. Registering av nye brukere. Søke på brukere. Legge til fjerne brukere fra den lokale HUB’en. 



## Detaljer Kladd (BrainStorming)


***Systemet / HUB (System klient siden / Smart Huset)***

* En eller flere Smartlåser. Batteridrevne (Enkel installasjon), med innebygd Blåtann, Wifi og QR-Scanner.

* En lokal sentral enhet med oppkobling til Internett.

* Låsene er enten koblet opp mot en eksisterende smarthus-løsning eller en lokal sentral enhet produsert av oss (Via. Wifi). Kun kalt “HUB” fra nå av.

* Denne HUB’en  har heletiden en oversikt over enhetene koblet til systemet.

* Samt en oversikt over brukere, gjester osv. Den logger / lagrer også hvem som låste / låste opp en enhet med tidspunkt for hendelsen.

* Genererte digitale-nøkler blir også lastet ned og cachet her med tidsintervallet nøkkelen er gjeldene for.

* HUB’en kommuniserer med alle lås-enhetene med korte mellomrom for å sjekke status og om noe er blitt sendt fra lås-enhetene. F.eks. En QR-Kode har blitt skannet eller en Bruker er oppdaget innen lås-enhetens blåtann rekkevidde osv.

* HUB’en prosesserer denne informasjonen og sender raskt svar tilbake.

* HUB’en kommuniserer også med en sentral database på bedrifts siden. Og sjekker med jevne mellomrom for endringer den trenger å vite om. Og kan også sende nødvendig data tilbake til sentral databasen.

* HUB’en er med andre ord mellomleddet mellom enhetene og internett.

* Laste ned og installere systemoppdateringer.



#### Sentral Serveren (Bedrifts siden)

* En Database med alle registrerte Brukere, Systemer (HUB) og Eiere.

* Generere og Serve QR-Koder (Midlertidige digitale nøkler)

* Gi beskjed om og serve system oppdateringer.

* Ta imot og lagre evt. feilmeldinger fra lokale systemer.

* Opprette / Slette Brukere.

* Unik Bruker må opprettes (Telefon nummer, email, adresse ol.) for å kunne benytte Mobil-appen.

* Alle brukere (Bortsett fra Eiere?) kan slette denne informasjonen når som helst, men kan da ikke lenger benytte appens funksjonaliteter.

* Bruker kan ha flere telefoner.

* Eiere (kunder som har kjøpt vårt smart hus system) blir registrert som Eier ved kjøp av systemet. Låsene blir da også lagt til i sentral databasen registrert på Eier.

* Trenger ikke vite alt som foregår i de enkelte systemene. Hva sentral databasen trenger å lagre er en problemstilling i seg selv.



#### Mobil Appen

* Enten utvikles fra scratch eller kanskje smartlås app funksjonaliteten vi trenger kan bli lagt til i en eksisterende smart-hus app? Det eneste jeg vet om mobil app utvikling er at Android Studio IDE kan bli brukt. Hvordan cross-plattform fungerer er jeg usikker på (IOS, nettbrett etc.)

* Lastes ned fra app-store eller lignende.

* Noen av App funksjonalitetene kan kun benyttes når telefonen er koblet til internett. Andre kan også bli brukt “offline”. F.eks. Hvis Bruker ønsker å låse opp en lås fra avstand eller gi midlertidig tilgang til en “Gjest” krever Internett. Men Brukere med f.eks. “Eier / Beboer” rettigheter skal kunne bruke Appen for å låse opp døra på stedet uavhengig av Internett.

* Eier og Beboere med rettigheter for det, skal kunne generere QR-Koder (Midlertidige digitale nøkler) for gjestebrukere.

* Eier og Beboere har egne “permanente” QR koder.

* Eier og Beboere skal kunne se en “total oversikt” med status over låsene i sitt system. Enten via. hjemmets wifi (Direkte) eller via. Internett på mobil.

* Eier (og Beboere med rettigheter) skal kunne låse / låse opp dører fra avstand via. denne oversikts menyen.

* Ved å gi Appen tilgang (Og mobilen har blåtann på) kan en pop-up dukke opp når Eier / Beboer er i nærheten av en lås. Her kreves det kanskje ikke at systemet er koblet til Internett (Så lenge låsen gjenkjenner bruker. Cachet i selve låsen).

* Eier av et system skal kunne tildele andre brukere rettigheter for sitt system. F.eks. rettigheten til å generere midlertidige nøkler ol.



#### Smartlås enheten

Komponenter:

* Strøm-forsyning: Batterier. For enkel installasjon (Kan også gjøres selv). Batteri-nivået måles, og enheten gir beskjed i god tid før det går tomt.

* Elektro-mekanisk lås. Må også kunne låses opp med fysisk nøkkel i nødstilfelle. Denne må være produsert med fokus på sikkerhet.

* ESP32 programmerbar chip med Wi-fi og Bluetooth Low Energi. Dette er hjernen i enheten. Programmeres med Arduino IDE og har mange funksjonaliteter. Blant annet protokoller for Point-to-point kommunikasjon med blåtann og Wi-fi kommunikasjon.

* Intern QR-Scanner

* Mulig en ekstra minne-brikke 


