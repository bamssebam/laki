import java.util.Random; // Random-luokka käyttöön.

/*
 * Lausekielinen ohjelmointi, syksy 2012, toinen harjoitustyö.
 *
 * Apuluokka, joka osaa arpoa palikan paikat, kiertää palikan paikkoja 90
 * astetta vastapäivään ja poistaa kentästä rivit, joiden sisäalkioissa on
 * vain palikkamerkkiä.
 *
 * VAIN KURSSIN VASTUUOPETTAJA SAA MUUTTAA TÄTÄ LUOKKAA.
 *
 * ÄLÄ KOPIOI METODEJA TÄSTÄ LUOKASTA OMAAN OHJELMAASI.
 *
 * Jorma Laurikkala, Informaatiotieteiden yksikkö, Tampereen yliopisto,
 * jorma.laurikkala@uta.fi.
 *
 * Versio 1.0.
 *
 * Viimeksi muutettu 11.12.2012.
 *
 */

public class HT2Apu {

   /*__________________________________________________________________________
    *
    * 1. Julkiset luokkavakiot.
    *
    */

   // Kentän koko.
   public static final int RIVIENLKM = 22;
   public static final int SARAKKEIDENLKM = 12;

   // Palikoiden tyypit.
   public static final int IPALIKKA = 0;
   public static final int JPALIKKA = 1;
   public static final int LPALIKKA = 2;
   public static final int OPALIKKA = 3;
   public static final int SPALIKKA = 4;
   public static final int TPALIKKA = 5;
   public static final int ZPALIKKA = 6;

   // Kentän merkit.
   public static final char PALIKKA = 'X';
   public static final char REUNA = '.';
   public static final char TAUSTA = ' ';

   /*__________________________________________________________________________
    *
    * 2. Kätketyt attribuutit.
    *
    */

   // Maailmalta kätketty pseudosatunnaislukugeneraattori.
   private Random generaattori;

   /*__________________________________________________________________________
    *
    * 3. Vain tämän luokan käyttöön tarkoitetut kätketyt apumetodit.
    *
    */

   /* Paluuarvo on true, jos kentälle on varattu muistia oikea määrä,
    * muussa tapauksessa paluuarvo on false.
    */
   private static boolean onkoKenttaKunnossa(char[][] kentta) {
      return (kentta != null) && (kentta.length == RIVIENLKM)
      && (kentta[0].length == SARAKKEIDENLKM);
   }

   /* Paluuarvo on true, jos pisteille on varattu muistia oikea määrä,
    * muussa tapauksessa paluuarvo on false.
    */
   private static boolean ovatkoPisteetKunnossa(int[][] kentta) {
      return (kentta != null) && (kentta.length == 4) && (kentta[0].length == 2);
   }

   /* Tutkii koostuuko taulukon t rivi r merkistä m välillä [a, b]. Paluuarvo on
    * true, jos välillä on yhtä merkkiä. Paluuarvo on false, jos välillä on kahden
    * tai useamman merkin esiintymiä tai parametreissä on virhe.
    */
   private static boolean onkoSamaaMerkkia(char[][] t, int r, int a, int b, char m) {
      // Ollaan pessimistejä.
      boolean samaa = false;

      // Edetään vain, jos muistia on varattu.
      if (t != null) {
         // Rivien ja sarakkeiden lukumäärät.
         int rivlkm = t.length;
         int sarlkm = t[0].length;

         // Edetään lisää vain, jos muissa parametreissa on järkeä.
         if  (r >= 0 && r < rivlkm && a >= 0 && b < sarlkm && a <= b) {
            // Välin ensimmäinen merkki.
            char ekaMerkki = t[r][a];

            // Käännetään lippu ja yritetään saman tien kääntää se takaisin.
            samaa = true;
            int i = a;
            while (samaa && i <= b) {
               // Nykyisen alkion merkki apumuutujaan.
               char merkki = t[r][i];

               // Käännetään lippu, jos löytyy haettavasta merkistä eroava merkki.
               if (merkki != m)
                  samaa = false;

               // Siirrytään seuraavaan alkioon.
               i++;
            }
         }
      }

      // Palautetaan tulos.
      return samaa;
   }

   /* Palauttaa true-arvon, jos kentän rivin sisäalkioissa on vain palikkamerkkejä.
    * Paluuarvo on false, jos rivin sisäpaikoissa on kahden tai useamman merkin
    * esiintymiä tai parametreissa on virhe.
    */
   private static boolean onkoPoistettavaRivi(char[][] kentta, int rivi) {
      // Tarvitaan tarkistus, jotta voidaan käyttää huoletta length-attribuuttia.
      if (kentta != null)
         return onkoSamaaMerkkia(kentta, rivi, 1, kentta[0].length - 2, PALIKKA);
      else
         return false;
   }

   /*__________________________________________________________________________
    *
    * 4. Harjoitustyöohjelmasta kutsuttavat julkiset metodit.
    *
    */

   // Luokan rakentaja.
   public HT2Apu(int siemen) {
      // Luodaan pseudosatunnaislukugeneraattori annetulla siemenluvulla.
      // Tietyllä siemenluvulla saadaan tietty sarja pseudosatunnaislukuja.
      generaattori = new Random(siemen);
   }

   /* Arpoo palikan tyypin ja palauttaa tyyppiä vastaavan palikan merkkien
    * paikat (indeksiarvoja) 4 x 2 -kokoisessa taulukossa, kun palikka on
    * kentällä lähtöasemassa. Taulukon jokaisella rivillä on yksittäisen paikan
    * rivi- ja sarakeindeksit. Näin kukin taulukon riveistä on kahden alkion
    * mittainen. Esimerkiksi rivi { 1, 4 } tarkoittaa sitä, että pisteen
    * rivi- ja sarakeindeksit pelikenttää kuvaavassa taulukossa ovat 1 ja 4.
    * Taulukossa on aina neljä riviä, koska kukin palikka koostu neljästä
    * merkistä.
    */
   public int[][] annaPalikanPaikat() {
      // Kolmiulotteisessa taulukko alkioiden paikoille. Yksittäisen alkion
      // paikat on annettu kaksiulotteisessa 4 x 2 -kokoisessa taulukossa.
      // Kunkin palikan viereen on merkitty a-, b-, c- ja d-kirjaimin palikan
      // pisteiden sijainti pelikentällä. a-kirjain vastaa palikan ensimmäistä
      // ja d-kirjain palikan viimeistä paikkaa. Pelikentästä näytetään kolme
      // ensimmäistä riviä. Huomaa, että palikan kaikki paikat esitetään
      // varsinaisessa pelissä yhdellä merkillä (PALIKKA).
      int[][][] palikoidenPaikat = {
         // I.
         { { 1, 4 }, // a                             // ............
           { 1, 5 }, // b                             // .   abcd   .
           { 1, 6 }, // c                             // .          .
           { 1, 7 }  // d
         },
         // J.
         { { 1, 4 },                                  // ............
           { 1, 5 },                                  // .   abc    .
           { 1, 6 },                                  // .     d    .
           { 2, 6 } },
         // L.
         { { 1, 4 },                                  // ............
           { 1, 5 },                                  // .   abc    .
           { 1, 6 },                                  // .   d      .
           { 2, 4 } },
         // O.
         { { 1, 4 },                                  // ............
           { 1, 5 },                                  // .   ab     .
           { 2, 4 },                                  // .   cd     .
           { 2, 5 } },
         // S.
         { { 1, 5 },                                  // ............
           { 1, 6 },                                  // .    ab    .
           { 2, 4 },                                  // .   cd     .
           { 2, 5 } },
         // T.
         { { 1, 4 },                                  // ............
           { 1, 5 },                                  // .   abc    .
           { 1, 6 },                                  // .    d     .
           { 2, 5 } },
         // Z.
         { { 1, 4 },                                  // ............
           { 1, 5 },                                  // .   ab     .
           { 2, 5 },                                  // .    cd    .
           { 2, 6 } }
      };

      // Palikoiden tyypit taulukossa.
      int[] palikoidenTyypit = { IPALIKKA, JPALIKKA, LPALIKKA, OPALIKKA,
      SPALIKKA, TPALIKKA, ZPALIKKA };

      // Arvotaan luku väliltä [0, 6].
      int tyyppi = generaattori.nextInt(7);

      // Palautetaan viite kaksiulotteiseen taulukkoon.
      return palikoidenPaikat[tyyppi];
   }

   /* Kiertää palikan paikkoja 90 astetta vastapäivään palikan toisen paikan
    * ympäri. Palikan merkkien paikat ovat 4 x 2 -kokoisessa taulukossa,
    * jonka jokaisella rivillä on yksittäisen paikan rivi- ja sarakeindeksit.
    * Metodi palauttaa palikan uudet paikat vanhan taulukon kokoisessa uudessa
    * taulukossa. Paluuarvo on null, jos taulukolle ei ole varattu oikeaa
    * määrää muistia.
    */
   public static int[][] kierraPalikanPaikat(int[][] paikat) {
      // Kierretään aina kuvion toisen paikan suhteen.
      final int KIERTOPAIKANINDEKSI = 1;

      // Viite uudet paikat sisältävään taulukkoon.
      int[][] uudetPaikat = null;

      // Taulukolle oli varattu oikea määrä muistia.
      if (ovatkoPisteetKunnossa(paikat)) {
         // Lasketaan paikkojen lukumäärä.
         int paikkoja = paikat.length;

         // Luodaan uusi taulukko-olio ja liitetään siihen viite.
         uudetPaikat = new int[paikkoja][2];

         // Asetetaan apumuuttujiin kiertopaikan indeksit.
         int origorivi = paikat[1][0];
         int origosarake = paikat[1][1];

         // Kiertopaikka ei muutu.
         uudetPaikat[KIERTOPAIKANINDEKSI][0] = origorivi;
         uudetPaikat[KIERTOPAIKANINDEKSI][1] = origosarake;

         // Kierretään paikat yksi kerrallaan.
         for (int rivi = 0; rivi < paikkoja; rivi++) {
            // Lasketaan uudet indeksiarvot.
            int vanhaRivi = paikat[rivi][0];
            int vanhaSarake = paikat[rivi][1];
            int uusiRivi = origorivi - (vanhaSarake - origosarake);
            int uusiSarake = origosarake + (vanhaRivi - origorivi);

            // Asetetaan uusi paikka taulukkoon, ellei kyseessä ole kiertopaikka.
            if (rivi != KIERTOPAIKANINDEKSI) {
               uudetPaikat[rivi][0] = uusiRivi;
               uudetPaikat[rivi][1] = uusiSarake;
            }
         }
      }

      // Palautetaan viite uuteen taulukkoon.
      return uudetPaikat;
   }

   /* Poistaa kentästä vain palikkamerkistä koostuvat sisärivit ja palauttaa
    * poistettujen rivien lukumäärän. Paluuarvo on nolla, jos yhtään riviä ei
    * poistettu tai muistia ei oltu varattu oikein.
    */
   public static int poistaRivit(char[][] kentta) {
      // Poistettujen rivien lukumäärä.
      int poistettuja = 0;

      // Taulukolle oli varattu oikea määrä muistia.
      if (onkoKenttaKunnossa(kentta)) {
         // Kentän rivien ja sarakkeiden lukumäärä.
         int rivlkm = kentta.length;
         int sarlkm = kentta[0].length;

         // Ensimmäinen tutkittava rivi on viimeinen sisärivi.
         int tutkittavaRivi = rivlkm - 2;

         // Käydään kentän sisärivit läpi lopusta alkuun.
         while (tutkittavaRivi > 0) {
            // Oletetaan, että ensimmäinen säilytettävä rivi on tutkittava rivi.
            int ekaSailytettavaRivi = tutkittavaRivi;

            // Päätellään pitääkö tutkittava rivi ja kenties muutama sitä seuraavista
            // riveistä sittenkin poistaa. Silmukan jälkeen ekaSailytettavaRivi
            // on ensimmäisen säilytettävän rivin indeksi.
            boolean poistettava;
            do {
               // Päätellään onko sisärivi pelkkää palikkaa.
               poistettava = onkoPoistettavaRivi(kentta, ekaSailytettavaRivi);

               // Siirrytään edelliselle riville ja piirretään viiva seinälle,
               // kun löytyy poistettava rivi.
               if (poistettava) {
                  ekaSailytettavaRivi--;
                  poistettuja++;
               }
            }
            while (poistettava);

            // Tutkittava rivi ja kenties sitä seuraavia rivejä on poistettava.
            if (tutkittavaRivi != ekaSailytettavaRivi) {
               // Kopioidaan rivit indeksien väliltä [1, ekaSailytettavaRivi] siten,
               // että rivi indeksillä ekaSailytettavaRivi korvaa rivin indeksillä
               // tutkittavaRivi, rivi indeksillä ekaSailytettavaRivi - 1 korvaa
               // rivin indeksillä tutkittavaRivi - 1 jne. Näin kentän rivit
               // siirtyvät alaspäin tutkittavaRivi - ekaSailytettavaRivi riviä
               // ja poistettaviksi tuomittujen rivien merkit katoavat.
               int korvattavaRivi = tutkittavaRivi;
               int korvaavaRivi = ekaSailytettavaRivi;
               while (korvaavaRivi > 0) {
                  // Kopioidaan korvaavan rivin alkioiden arvot korvattavan
                  // rivin alkioihin.
                  for (int sarake = 0; sarake < sarlkm; sarake++)
                     kentta[korvattavaRivi][sarake] = kentta[korvaavaRivi][sarake];

                  // Siirrytään edellisille riveille.
                  korvattavaRivi--;
                  korvaavaRivi--;
               }

               // Tyhjennetään kentän yläosaan kopioinnin jälkeen jäävät
               // ylimääräiset rivit.
               int tyhjattavia = tutkittavaRivi - ekaSailytettavaRivi;
               for (int tyhjaRivi = 1; tyhjaRivi <= tyhjattavia; tyhjaRivi++) {
                  // Asetetaan merkit rivin reunapaikkoihin.
                  kentta[tyhjaRivi][sarlkm - 1] = REUNA;
                  kentta[tyhjaRivi][0] = REUNA;

                  // Asetetaan merkit rivin sisäpaikkoihin.
                  for (int sarake = 1; sarake < sarlkm - 1; sarake++)
                     kentta[tyhjaRivi][sarake] = TAUSTA;
               }
            }

            // Siirrytään tutkimaan nykyistä riviä edeltävää riviä.
            tutkittavaRivi--;
         }
      }

      // Palautetaan poistettujen rivien lukumäärä.
      return poistettuja;
   }
}
