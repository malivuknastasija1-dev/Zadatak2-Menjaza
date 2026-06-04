-----------------------------------------------------------------------------------------------------------------------------------------------------
28.5.2026.

- Kreiran serverski deo (konzolna aplikacija) - pokrenuti **klasu MenjazaServer**.
- Naredni korak je kreiranje klijentskog dela (GUI).

* AI alat je koriscen za proveru funkcionalnosti pojedinih linija koda, zbog same Java sintakse.
* Kod je napisan u skladu sa primerima sa vezbi i predavanja. :)
-----------------------------------------------------------------------------------------------------------------------------------------------------
30.5.2026.

- Kreiran klijentski deo aplikacije - pocetni GUI u okviru **klase MenjazaClient**.
- Naredni korak je kreiranje HashMap-e za graficki prikaz duplikata/slicica koje nedostaju u okviru GUI-ja.

* AI alat je koriscen za proveru funkcionalnosti pojedinih linija koda, zbog same Java sintakse.
* Kod je napisan u skladu sa primerima sa vezbi i predavanja. :)
-----------------------------------------------------------------------------------------------------------------------------------------------------
31.5.2026.

- Kreirana su dva panela za duplikate i slicice koje fale, kao i odgovarajuca dugma sa specificnim funkcionalnostima.
- Naredni korak je uspostavljanje veze izmedju servera i klijenta - **uvezivanje igraca i omogucavanje razmene slicica**.

* AI alat je koriscen za proveru funkcionalnosti pojedinih linija koda, zbog same Java sintakse.
* Kod je napisan u skladu sa primerima sa vezbi i predavanja. :)
-----------------------------------------------------------------------------------------------------------------------------------------------------
1.6.2026.

- Kreiran je deo za poredjenje inicijalnih setova slicica, kao i ComboBox-evi u kojima se ispisuje koliko razmena je moguce ostvariti sa ostalim igracima.
- Naredni korak je uspostavljanje veze za slanje zahteva menjaze igracima.

* AI alat je koriscen za proveru funkcionalnosti pojedinih linija koda, zbog same Java sintakse.
* Kod je napisan u skladu sa primerima sa vezbi i predavanja. :)
-----------------------------------------------------------------------------------------------------------------------------------------------------
2.6.2026.

- Kreiran je deo za prihvatanje zahteva za razmenu.

* AI alat je koriscen za proveru funkcionalnosti pojedinih linija koda, zbog same Java sintakse.
* Kod je napisan u skladu sa primerima sa vezbi i predavanja. :)
-----------------------------------------------------------------------------------------------------------------------------------------------------
**Primer rada aplikacije "Menjaza"**
Konektovala su se 4 igraca i svakom od njih je stigao inicijalni set slicica, kao i spisak slicica pogodnih za moguce razmene. Igrac4 zeli da se menja sa korisnikom Igrac2, i broj u zagradi oznacava koliko razmena slicica je moguce ostvariti. Igrac4 selektuje jednog svog duplikata, i jednu slicicu koja mu fali, potom posalje zahtev ka Igrac2. Igrac2 moze da prihvati ili odbije zahtev. Ako prihvati, automatski se azuriraju paneli sa duplikatima i slicicama koje nedostaju kod oba igraca, ali ako odbije, nista se ne desava i igra se redovno nastavlja. Oba igraca imaju i mogucnost da rucno obrisu slicice. Nakon izlaska iz igre nekog od igraca, sistem se osvezava i razmena je moguca samo sa aktivnim igracima. Primer aplikacije je prikazan na narednoj slici:
