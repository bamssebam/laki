import java.util.Random; // Random-luokka k�ytt��n.

/*
 * Lausekielinen ohjelmointi, syksy 2012, toinen harjoitusty�.
 *
 * Apuluokka, joka osaa arpoa palikan paikat, kiert�� palikan paikkoja 90
 * astetta vastap�iv��n ja poistaa kent�st� rivit, joiden sis�alkioissa on
 * vain palikkamerkki�.
 *
 * VAIN KURSSIN VASTUUOPETTAJA SAA MUUTTAA T�T� LUOKKAA.
 *
 * �L� KOPIOI METODEJA T�ST� LUOKASTA OMAAN OHJELMAASI.
 *
 * Jorma Laurikkala, Informaatiotieteiden yksikk�, Tampereen yliopisto,
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

   // Kent�n koko.
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

   // Kent�n merkit.
   public static final char PALIKKA = 'X';
   public static final char REUNA = '.';
   public static final char TAUSTA = ' ';

   /*__________________________________________________________________________
    *
    * 2. K�tketyt attribuutit.
    *
    */

   // Maailmalta k�tketty pseudosatunnaislukugeneraattori.
   private Random generaattori;

   /*__________________________________________________________________________
    *
    * 3. Vain t�m�n luokan k�ytt��n tarkoitetut k�tketyt apumetodit.
    *
    */

   /* Paluuarvo on true, jos kent�lle on varattu muistia oikea m��r�,
    * muussa tapauksessa paluuarvo on false.
    */
   private static boolean onkoKenttaKunnossa(char[][] kentta) {
      return (kentta != null) && (kentta.length == RIVIENLKM)
      && (kentta[0].length == SARAKKEIDENLKM);
   }

   /* Paluuarvo on true, jos pisteille on varattu muistia oikea m��r�,
    * muussa tapauksessa paluuarvo on false.
    */
   private static boolean ovatkoPisteetKunnossa(int[][] kentta) {
      return (kentta != null) && (kentta.length == 4) && (kentta[0].length == 2);
   }

   /* Tutkii koostuuko taulukon t rivi r merkist� m v�lill� [a, b]. Paluuarvo on
    * true, jos v�lill� on yht� merkki�. Paluuarvo on false, jos v�lill� on kahden
    * tai useamman merkin esiintymi� tai parametreiss� on virhe.
    */
   private static boolean onkoSamaaMerkkia(char[][] t, int r, int a, int b, char m) {
      // Ollaan pessimistej�.
      boolean samaa = false;

      // Edet��n vain, jos muistia on varattu.
      if (t != null) {
         // Rivien ja sarakkeiden lukum��r�t.
         int rivlkm = t.length;
         int sarlkm = t[0].length;

         // Edet��n lis�� vain, jos muissa parametreissa on j�rke�.
         if  (r >= 0 && r < rivlkm && a >= 0 && b < sarlkm && a <= b) {
            // V�lin ensimm�inen merkki.
            char ekaMerkki = t[r][a];

            // K��nnet��n lippu ja yritet��n saman tien k��nt�� se takaisin.
            samaa = true;
            int i = a;
            while (samaa && i <= b) {
               // Nykyisen alkion merkki apumuutujaan.
               char merkki = t[r][i];

               // K��nnet��n lippu, jos l�ytyy haettavasta merkist� eroava merkki.
               if (merkki != m)
                  samaa = false;

               // Siirryt��n seuraavaan alkioon.
               i++;
            }
         }
      }

      // Palautetaan tulos.
      return samaa;
   }

   /* Palauttaa true-arvon, jos kent�n rivin sis�alkioissa on vain palikkamerkkej�.
    * Paluuarvo on false, jos rivin sis�paikoissa on kahden tai useamman merkin
    * esiintymi� tai parametreissa on virhe.
    */
   private static boolean onkoPoistettavaRivi(char[][] kentta, int rivi) {
      // Tarvitaan tarkistus, jotta voidaan k�ytt�� huoletta length-attribuuttia.
      if (kentta != null)
         return onkoSamaaMerkkia(kentta, rivi, 1, kentta[0].length - 2, PALIKKA);
      else
         return false;
   }

   /*__________________________________________________________________________
    *
    * 4. Harjoitusty�ohjelmasta kutsuttavat julkiset metodit.
    *
    */

   // Luokan rakentaja.
   public HT2Apu(int siemen) {
      // Luodaan pseudosatunnaislukugeneraattori annetulla siemenluvulla.
      // Tietyll� siemenluvulla saadaan tietty sarja pseudosatunnaislukuja.
      generaattori = new Random(siemen);
   }

   /* Arpoo palikan tyypin ja palauttaa tyyppi� vastaavan palikan merkkien
    * paikat (indeksiarvoja) 4 x 2 -kokoisessa taulukossa, kun palikka on
    * kent�ll� l�ht�asemassa. Taulukon jokaisella rivill� on yksitt�isen paikan
    * rivi- ja sarakeindeksit. N�in kukin taulukon riveist� on kahden alkion
    * mittainen. Esimerkiksi rivi { 1, 4 } tarkoittaa sit�, ett� pisteen
    * rivi- ja sarakeindeksit pelikentt�� kuvaavassa taulukossa ovat 1 ja 4.
    * Taulukossa on aina nelj� rivi�, koska kukin palikka koostu nelj�st�
    * merkist�.
    */
   public int[][] annaPalikanPaikat() {
      // Kolmiulotteisessa taulukko alkioiden paikoille. Yksitt�isen alkion
      // paikat on annettu kaksiulotteisessa 4 x 2 -kokoisessa taulukossa.
      // Kunkin palikan viereen on merkitty a-, b-, c- ja d-kirjaimin palikan
      // pisteiden sijainti pelikent�ll�. a-kirjain vastaa palikan ensimm�ist�
      // ja d-kirjain palikan viimeist� paikkaa. Pelikent�st� n�ytet��n kolme
      // ensimm�ist� rivi�. Huomaa, ett� palikan kaikki paikat esitet��n
      // varsinaisessa peliss� yhdell� merkill� (PALIKKA).
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

      // Arvotaan luku v�lilt� [0, 6].
      int tyyppi = generaattori.nextInt(7);

      // Palautetaan viite kaksiulotteiseen taulukkoon.
      return palikoidenPaikat[tyyppi];
   }

   /* Kiert�� palikan paikkoja 90 astetta vastap�iv��n palikan toisen paikan
    * ymp�ri. Palikan merkkien paikat ovat 4 x 2 -kokoisessa taulukossa,
    * jonka jokaisella rivill� on yksitt�isen paikan rivi- ja sarakeindeksit.
    * Metodi palauttaa palikan uudet paikat vanhan taulukon kokoisessa uudessa
    * taulukossa. Paluuarvo on null, jos taulukolle ei ole varattu oikeaa
    * m��r�� muistia.
    */
   public static int[][] kierraPalikanPaikat(int[][] paikat) {
      // Kierret��n aina kuvion toisen paikan suhteen.
      final int KIERTOPAIKANINDEKSI = 1;

      // Viite uudet paikat sis�lt�v��n taulukkoon.
      int[][] uudetPaikat = null;

      // Taulukolle oli varattu oikea m��r� muistia.
      if (ovatkoPisteetKunnossa(paikat)) {
         // Lasketaan paikkojen lukum��r�.
         int paikkoja = paikat.length;

         // Luodaan uusi taulukko-olio ja liitet��n siihen viite.
         uudetPaikat = new int[paikkoja][2];

         // Asetetaan apumuuttujiin kiertopaikan indeksit.
         int origorivi = paikat[1][0];
         int origosarake = paikat[1][1];

         // Kiertopaikka ei muutu.
         uudetPaikat[KIERTOPAIKANINDEKSI][0] = origorivi;
         uudetPaikat[KIERTOPAIKANINDEKSI][1] = origosarake;

         // Kierret��n paikat yksi kerrallaan.
         for (int rivi = 0; rivi < paikkoja; rivi++) {
            // Lasketaan uudet indeksiarvot.
            int vanhaRivi = paikat[rivi][0];
            int vanhaSarake = paikat[rivi][1];
            int uusiRivi = origorivi - (vanhaSarake - origosarake);
            int uusiSarake = origosarake + (vanhaRivi - origorivi);

            // Asetetaan uusi paikka taulukkoon, ellei kyseess� ole kiertopaikka.
            if (rivi != KIERTOPAIKANINDEKSI) {
               uudetPaikat[rivi][0] = uusiRivi;
               uudetPaikat[rivi][1] = uusiSarake;
            }
         }
      }

      // Palautetaan viite uuteen taulukkoon.
      return uudetPaikat;
   }

   /* Poistaa kent�st� vain palikkamerkist� koostuvat sis�rivit ja palauttaa
    * poistettujen rivien lukum��r�n. Paluuarvo on nolla, jos yht��n rivi� ei
    * poistettu tai muistia ei oltu varattu oikein.
    */
   public static int poistaRivit(char[][] kentta) {
      // Poistettujen rivien lukum��r�.
      int poistettuja = 0;

      // Taulukolle oli varattu oikea m��r� muistia.
      if (onkoKenttaKunnossa(kentta)) {
         // Kent�n rivien ja sarakkeiden lukum��r�.
         int rivlkm = kentta.length;
         int sarlkm = kentta[0].length;

         // Ensimm�inen tutkittava rivi on viimeinen sis�rivi.
         int tutkittavaRivi = rivlkm - 2;

         // K�yd��n kent�n sis�rivit l�pi lopusta alkuun.
         while (tutkittavaRivi > 0) {
            // Oletetaan, ett� ensimm�inen s�ilytett�v� rivi on tutkittava rivi.
            int ekaSailytettavaRivi = tutkittavaRivi;

            // P��tell��n pit��k� tutkittava rivi ja kenties muutama sit� seuraavista
            // riveist� sittenkin poistaa. Silmukan j�lkeen ekaSailytettavaRivi
            // on ensimm�isen s�ilytett�v�n rivin indeksi.
            boolean poistettava;
            do {
               // P��tell��n onko sis�rivi pelkk�� palikkaa.
               poistettava = onkoPoistettavaRivi(kentta, ekaSailytettavaRivi);

               // Siirryt��n edelliselle riville ja piirret��n viiva sein�lle,
               // kun l�ytyy poistettava rivi.
               if (poistettava) {
                  ekaSailytettavaRivi--;
                  poistettuja++;
               }
            }
            while (poistettava);

            // Tutkittava rivi ja kenties sit� seuraavia rivej� on poistettava.
            if (tutkittavaRivi != ekaSailytettavaRivi) {
               // Kopioidaan rivit indeksien v�lilt� [1, ekaSailytettavaRivi] siten,
               // ett� rivi indeksill� ekaSailytettavaRivi korvaa rivin indeksill�
               // tutkittavaRivi, rivi indeksill� ekaSailytettavaRivi - 1 korvaa
               // rivin indeksill� tutkittavaRivi - 1 jne. N�in kent�n rivit
               // siirtyv�t alasp�in tutkittavaRivi - ekaSailytettavaRivi rivi�
               // ja poistettaviksi tuomittujen rivien merkit katoavat.
               int korvattavaRivi = tutkittavaRivi;
               int korvaavaRivi = ekaSailytettavaRivi;
               while (korvaavaRivi > 0) {
                  // Kopioidaan korvaavan rivin alkioiden arvot korvattavan
                  // rivin alkioihin.
                  for (int sarake = 0; sarake < sarlkm; sarake++)
                     kentta[korvattavaRivi][sarake] = kentta[korvaavaRivi][sarake];

                  // Siirryt��n edellisille riveille.
                  korvattavaRivi--;
                  korvaavaRivi--;
               }

               // Tyhjennet��n kent�n yl�osaan kopioinnin j�lkeen j��v�t
               // ylim��r�iset rivit.
               int tyhjattavia = tutkittavaRivi - ekaSailytettavaRivi;
               for (int tyhjaRivi = 1; tyhjaRivi <= tyhjattavia; tyhjaRivi++) {
                  // Asetetaan merkit rivin reunapaikkoihin.
                  kentta[tyhjaRivi][sarlkm - 1] = REUNA;
                  kentta[tyhjaRivi][0] = REUNA;

                  // Asetetaan merkit rivin sis�paikkoihin.
                  for (int sarake = 1; sarake < sarlkm - 1; sarake++)
                     kentta[tyhjaRivi][sarake] = TAUSTA;
               }
            }

            // Siirryt��n tutkimaan nykyist� rivi� edelt�v�� rivi�.
            tutkittavaRivi--;
         }
      }

      // Palautetaan poistettujen rivien lukum��r�.
      return poistettuja;
   }
}
