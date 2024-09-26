## Hva vi skal forsøke å utvikle:




Siden et smart lås system er veldig omfattende må vi velge deler av systemet vi kan fokusere på. Siden vi har hatt java-programmering og databaser på skolen er det fordelaktig for oss å velge deler av systemet som omhandler dette. Vi har ikke hatt mobil-utvikling eller grensesnitt utvikling på skolen, så dette blir sekundært.

Vi har fått tips av Ida om at vi kan importere et grensesnitt API inn i programmet slik at vi kan simulere en mobil App. Men det er ikke prioritet på dette stadiet.



### Grov oversikt av systemet:

* En server på bedrift-siden: En database med ALLE brukere. Dette er personer som har lastet ned Appen og registrert seg med navn, mobil nr., adresse ol. 
Alle “Eiere” er også registret her (Disse blir registrert når de kjøper et system, med låser og en lokal wifi HUB) .



* En HUB for systemet: Inkluderer også en database. Denne databasen har oversikt over alle brukere av det lokale systemet, med rettigheter bestemt av Eier. I tillegg har HUB databasen en logg over bruk. F.eks. Kari Traa låste opp dør 3 kl.17.55 19/09/2024. HUB’en har også en oversikt over hvilke dører som er låst, batteristatus og feilmeldinger. HUB'en kan være en fysisk HUB eller software innstallert i en eksisterende smarthus løsning (Integrert).


* Mobil Appen: Hvor Eier eller Brukere som har rettighet kan søke databasen over alle brukere og legge til / fjerne brukere fra det lokale systemet. F.eks. Geir vil finne Kari og legge henne til i systemet slik at hun kan benytte systemet med “Beboer” rettigheter. Eller Kari søker etter en venninne å gi henne “Gjest” rettigheter for i morgen fra kl.12 til 15. Mobil Appen har også en oversikt over Alle dører i systemet. Denne informasjonen er hentet fra HUB’en. Og Brukere med rettighet kan låse opp / igjen dører fra avstand.



* Låsene / Dørene: Disse simulerer vi. De har status Låst / Låst opp. Batteri-nivå, Mekanisk feil / skade. Vi trenger ikke tenke så mye på låsene på dette stadiet. Vi er mest opptatt av Serverene. Registering av nye brukere. Søke på brukere. Legge til fjerne brukere fra den lokale HUB’en. 




