/* 
 * Harjoitustyˆ 2
 * Txtris-peli
 * Jenni Mansikka-aho
 * Luotu: 21.1.2013
 * Muokattu: 26.1.2013
 * 
 */

public class Txtris {

   /* Metodi t‰ytt‰‰ kaksiulotteisen taulukon t alkiot "kehyst‰en"
    * ja palauttaa t‰ytetyn taulukon.
    */
   public static char[][] taytaTaulukko() {
      // Luodaan taulukko.
      char[][] t = new char[22][12];

      // Katsotaan, ett‰ muistia on varattu.
      if (t != null) {
         // Alustetaan taulukko.
         for (int rivi = 0; rivi < t.length; rivi++) {
            for (int sarake = 0; sarake < t[0].length; sarake++){
               // Ensimm‰isell‰ ja viimeisell‰ rivill‰ tulostetaan kehysmerkki,
               // niin myˆs jokaisen rivin alussa ja lopussa.
               if (rivi == 0 || rivi == (t.length -1) || 
                  sarake == 0 || sarake == (t[0].length - 1))
                  t[rivi][sarake] = '.';
               else
                  t[rivi][sarake] = ' ';
            }
         }
      }
      return t;
   }
   // Metodi piirt‰‰ merkkitaulukkoon m paikkataulukon p asettamiin kohtiin merkit.
   public static boolean piirraTaulukkoon (char[][] m, int[][] p, char merkki) {
      
      boolean voikoPiirtaa = tutkiPaikkataulukkoa(m, p, false);
      // Jos kaikki paikkataulukon alkiot lˆytyv‰t merkkitaulukosta,
      // voidaan piirt‰‰.
      if (voikoPiirtaa) {
         for (int i = 0; i < p.length; i++) {
            // Paikan rivi-indeksin arvo.
            int paikanRivi = p[i][0];
            int paikanSarake = p[i][1];

            // Sijoitetaan merkki taulukon paikassa olevaan alkioon.
            m[paikanRivi][paikanSarake] = merkki;
         }
      }
      return voikoPiirtaa;
   }
   // Metodi tulostaa taulukon alkiot n‰ytˆlle.
   public static void tulostaTaulukko(char[][] t) {
      // Tulostetaan vain, jos taulukolle on varattu muistia.
      if (t != null) {
         // Tulostetaan.
         for (int rivi = 0; rivi < t.length; rivi++) {
            for (int sarake = 0; sarake < t[0].length; sarake++)
               System.out.print(t[rivi][sarake]);
            System.out.println();
         }
      }
   }
   // Metodi tutkii ovatko paikkataulukon p alkiot taulukossa t.
   // Lis‰tty parametri, joka ilmaisee tutkitaanko v‰lilyˆntien olemassaolo.
   public static boolean tutkiPaikkataulukkoa(char[][] taulu, int[][] paikat, boolean valilyonteja) {
      // Alustetaan lippu muistinvarauksen mukaan. Sulut mukan selvyyden vuoksi.
      boolean paikatOK = (taulu != null) && (paikat != null);
       
      // Tarkastellaan paikat yksi kerrallaan. Silmukka pys‰htyy heti, kun lˆytyy
      // kelvoton paikka.
      int i = 0;
      while (paikatOK && i < paikat.length) {
         // Asetetaan paikan indeksiarvot selvyyden vuoksi apumuuttujiin.
         // Paikkataulukon rivin ensimm‰inen alkio sis‰lt‰‰ paikan rivi-indeksin
         // arvon ja toisessa alkiossa on paikan sarakeindeksin arvo.
         int rivi = paikat[i][0];
         int sarake = paikat[i][1];
            
         // Tutkitaan onko paikka kunnollinen.
         paikatOK = tutkiTaulukkoa(rivi, sarake, taulu, valilyonteja);
         
         // Kasvatetaan laskuria.
         i++;
      }
      // Palautetaan tulos.
      return paikatOK;
   }
   // Metodi tutkii sijaitsevatko indeksipaikat i ja j taulukossa t. 
   // Valilyonnit-parametri kertoo tutkitaanko myˆs v‰lilyˆntej‰.
   public static boolean tutkiTaulukkoa(int i, int j, char[][] t, boolean valilyonnit) {
      // Paluuarvon muuttuja.
      boolean tulos = false;
      
      // Tarkistetaan, ett‰ on varattu muistia.
      if (t != null) {
         // Tutkitaan ovatko indeksipaikat sallitulla v‰lill‰.
         if (i >= 0 && i <= (t.length - 1) && j >= 0 && j <= (t[0].length - 1))
            tulos = true;
         // Tarkistetaan, ettei ole muita kuin v‰lilyˆntej‰.
         if (valilyonnit && t[i][j] != ' ')
            tulos = false;
      }
      return tulos;
   }
   // Metodi yritt‰‰ siirt‰‰ palikkaa alasp‰in ja palauttaa arvon false,
   // jos siirto ei onnistunut.
   public static boolean koneenVuoro(char[][] m, int[][] p, char merkki, int pisteet) {
      // Vanhat paikat talteen
      int[][] vanhatPaikat = kopioiTaulukko(p);
      
      // Muuttuja kertoo osuiko palikka pohjaan. 
      boolean onkoPohjalla = false;
      
      // Pyyhit‰‰n palikka vanhasta paikastaan.
      piirraTaulukkoon(m, p, HT2Apu.TAUSTA);
      
      // Siirret‰‰n paikkoja alasp‰in kasvattamalla paikkojen rivi-indeksien arvoja.
      for (int rivi = 0; rivi < p.length; rivi++)
         p[rivi][0] = p[rivi][0] + 1;
      
      // Jos uudella paikalla ei ole ruuhkaa, piirret‰‰n siihen palikka.
      if (tutkiPaikkataulukkoa(m, p, true)) {
         // Sijoitetaan merkki taulukon paikkoihin.
         piirraTaulukkoon(m, p, merkki);
      }
      else {
         onkoPohjalla = true;
         // Piirret‰‰n palikka edelliseen sijaintiinsa.
         piirraTaulukkoon(m, vanhatPaikat, merkki);
         // P‰ivitet‰‰n paikat.
         p = vanhatPaikat;
      }
      // Paluuarvo.
      return onkoPohjalla;
   }
   // Metodi kopioi toisen taulukon alkiot itseens‰.
   public static int[][] kopioiTaulukko(int[][] alkup) {
      // Luodaan taulukko.
      int[][] uusi = new int[alkup.length][alkup[0].length];
      
      // Katsotaan, ett‰ muistia on varattu.
      if (alkup != null) {
         // K‰yd‰‰n l‰pi taulukko.
         for (int rivi = 0; rivi < alkup.length; rivi++) {
            for (int sarake = 0; sarake < alkup[0].length; sarake++){
               // Kopioidaan taulukon alkiot toiseen.
               uusi[rivi][sarake] = alkup[rivi][sarake];
            }
         }
      }
      return uusi;
   }
   // Metodi yritt‰‰ siirt‰‰ palikkaa vasemmalle.
   public static int[][] siirraVasempaan(char[][] merkkitaulu, int[][] paikat) {
      // K‰ytet‰‰n apumuuttujaa
      int[][] vanhatPaikat = kopioiTaulukko(paikat);

      // Siirret‰‰n paikat vasemmalle.
      for (int rivi = 0; rivi < paikat.length;rivi++) {
         for (int sarake = 1; sarake < paikat[0].length; sarake++){
            paikat[rivi][sarake] = paikat[rivi][sarake] - 1; 
         }
      }
      // Jos palikka ei osu reunaan tms.
      if (tutkiPaikkataulukkoa(merkkitaulu, paikat, true)) {
         // Hyv‰ksyt‰‰n uudet paikat.
         vanhatPaikat = paikat;
      }
      else
         paikat = vanhatPaikat;

      return paikat;
   }
   // Metodi yritt‰‰ siirt‰‰ palikkaa oikealle.
   public static int[][] siirraOikeaan(char[][] merkkitaulu, int[][] paikat) {
      // K‰ytet‰‰n apumuuttujaa
      int [][] vanhatPaikat = kopioiTaulukko(paikat);

      // Siirret‰‰n paikat oikealle.
      for (int rivi = 0; rivi < paikat.length;rivi++) {
         for (int sarake = 1; sarake < paikat[0].length; sarake++){
            paikat[rivi][sarake] = paikat[rivi][sarake] + 1; 
         }
      }
      // Jos palikka ei osu reunaan tms.
      if (tutkiPaikkataulukkoa(merkkitaulu, paikat, true)) {
         // Hyv‰ksyt‰‰n siirretyt paikat.
         vanhatPaikat = paikat;
      }
      else
         paikat = vanhatPaikat;
               
      return paikat;
   }
   // Metodi yritt‰‰ kiert‰‰ palikkaa.
   public static int[][] kierraPalikkaa(char[][] merkkitaulu, int[][] paikat) {
      // Lasketaan kierretyn palikan paikat.
      int[][] uudetPaikat = HT2Apu.kierraPalikanPaikat(paikat);

      // Jos palikka ei osu reunaan tms.
      if (tutkiPaikkataulukkoa(merkkitaulu, uudetPaikat, true)) {
         // Hyv‰ksyt‰‰n uudet paikat.
         paikat = uudetPaikat;
      }
      else
         uudetPaikat = paikat;
      
      return paikat;
   }
   // Metodi pudottaa palikan.
   public static int[][] pudotaPalikka(char[][] merkkitaulu, int[][] paikat, boolean palikkaIlmassa) {
      // Silmukkaa l‰pi kunnes palikka on pohjalla.
      while (palikkaIlmassa) {
         // K‰ytet‰‰n apumuuttujaa.
         int [][] vanhatPaikat = kopioiTaulukko(paikat);

         // Siirret‰‰n paikkoja alasp‰in kasvattamalla paikkojen
         // rivi-indeksien arvoja.
         for (int rivi = 0; rivi < paikat.length; rivi++)
            paikat[rivi][0] = paikat[rivi][0] + 1;

         // Jos uusi paikka on varattu, lopetetaan silmukan kierto.
         if (!tutkiPaikkataulukkoa(merkkitaulu, paikat, true)) {
            // Piirret‰‰n vanhat paikat
            piirraTaulukkoon(merkkitaulu, vanhatPaikat, HT2Apu.PALIKKA);
            // Paikat p‰ivitet‰‰n edelliseen sijaintiin.
            paikat = vanhatPaikat;
            // Lippu nurin.
            palikkaIlmassa = false;
         }
      }
      return paikat;
   }
   public static void pelaaPelia(HT2Apu apulainen) {
      /*
       * Vakiot ja muuttujat.
       *
       */
      // Pelaajan syˆtt‰m‰t valinnat.
      final char VASEN = '<';
      final char OIKEA = '>';
      final char KIERRA = 'r';
      final char PUDOTA = 'd';
      final char LOPETA = 'q';
      
      // Pelaajalle annettava info.
      final String PISTEET = "Points: ";
      final String VALINNAT = "left (<), right (>), (r)otate, (d)rop or (q)uit?";
      final String HEIHEI = "Bye, see you soon.";
      
      // Pelaajan tekem‰ valinta.
      char valinta;
      
      // Pelaajan pisteet.
      int pisteet = 0;
      
      // P‰‰silmukan jatkamisehto.
      boolean jatketaan = true;
      
      // Onko palikka ilmassa vai ei.
      boolean palikkaIlmassa = false;
      
      // Lis‰tt‰vien pisteiden kerroin.
      int kerroin = 0;
       
      // Tulostetaan tervehdysteksti.
      System.out.println("***************");
      System.out.println("* T X T R I S *");
      System.out.println("***************");

      // Luodaan ja alustetaan kehystetty taulukko metodissa.
      char[][] merkkitaulu = taytaTaulukko();
      
      // Luodaan paikkataulukko palikalle.
      int[][] paikat = null;  
      
      // P‰‰silmukka.
      do {
         /*
          * Uuden palikan luominen ja sijoittaminen pelikent‰lle.
          *
          */
          
         // Sijoitetaan HT2Apu-luokan metodin paluuarvo muuttujaan.
         paikat = apulainen.annaPalikanPaikat();   

         // Tarkastellaan onko palikka ilmassa.
         if (tutkiPaikkataulukkoa (merkkitaulu, paikat, true)) 
            palikkaIlmassa = true;
         // Lopetetaan pelaaminen ja heitet‰‰n hyv‰stit.
         else {
            palikkaIlmassa = false;
            jatketaan = false;
            System.out.println(PISTEET + pisteet);
            tulostaTaulukko(merkkitaulu);
            System.out.println(HEIHEI);
            break;
         }
         // Poistetaan mahdolliset t‰ydet rivit ja alustetaan pisteiden kerroin.
         kerroin = HT2Apu.poistaRivit(merkkitaulu);
         // Lis‰t‰‰n saadut pisteet.
         pisteet += kerroin * 100;
         
         //Tulostetaan pistetieto.
         System.out.println(PISTEET + pisteet);
         // Sijoitetaan palikka pelikentt‰‰n ja tulostetaan kentt‰.
         piirraTaulukkoon(merkkitaulu, paikat, HT2Apu.PALIKKA);
         tulostaTaulukko(merkkitaulu);

         while (jatketaan && palikkaIlmassa) {
            // Tulostetaan k‰ytt‰j‰lle annettavat valinnat.
            System.out.println(VALINNAT);
            
            // Luetaan k‰ytt‰j‰n valinta.
            valinta = In.readChar();
            
            // Katkaistaan silmukka jos k‰ytt‰j‰ tahtoo lopettaa pelin.
            if (valinta == LOPETA) {
               // Tulostetaan pistetieto.
               System.out.println(PISTEET + pisteet);
               // Tulostetaan loppun‰kym‰.
               tulostaTaulukko(merkkitaulu);
               // Hyv‰stell‰‰n pelaaja.
               System.out.print(HEIHEI);
               // Lippu nurin.
               jatketaan = false;
               break;
            }
            /*
             * Palikan siirt‰minen ja kiert‰minen.
             *
             */  
            // Pyyhit‰‰n palikka vanhasta paikastaan.
            piirraTaulukkoon(merkkitaulu, paikat, HT2Apu.TAUSTA);
            
            if (valinta == VASEN) {
               // Siirret‰‰n palikkaa vasemmalle metodin avulla.
               paikat = siirraVasempaan(merkkitaulu, paikat);
            }
            if (valinta == OIKEA) {
               // Siirret‰‰n palikkaa oikealle metodin avulla.
               paikat = siirraOikeaan(merkkitaulu, paikat);
            }
            if (valinta == KIERRA) {
               // Kierret‰‰n palikkaa metodin avulla.
               paikat = kierraPalikkaa(merkkitaulu, paikat);
            }
            if (valinta == PUDOTA) {
               // Pudotetaan palikka metodin avulla.
               paikat = pudotaPalikka(merkkitaulu, paikat, palikkaIlmassa);
               // Lippu nurin.
               palikkaIlmassa = false;
            }
            // Yritet‰‰n siirt‰‰ palikkaa alasp‰in.
            // Sis‰lt‰‰ tapaukset, joissa valinta muu kuin jokin annetuista.
            else if (koneenVuoro(merkkitaulu, paikat, HT2Apu.PALIKKA, pisteet))
               // Lippu nurin, jos alasp‰insiirto ei onnistunut.
               palikkaIlmassa = false;
            // Tulostetaan alas siirretty palikka.
            else {
               System.out.println("Points: " + pisteet);
               // Tulostetaan tulos.
               tulostaTaulukko(merkkitaulu);
            }
         }
      }
      while(jatketaan);
   }
   public static void main(String[] args) {
      
      // Tosi, jos komentoriviparametri oli kunnossa.
      boolean argsOK = true;

      // Siemenluku.
      int siemen = 0;

      // Saatiin tasan yksi komentoriviparametri.
      if (args.length == 1) {
         // Yritet‰‰n muuntaa komentoriviparametri int-tyyppiseksi siemenluvuksi.
         try {
            siemen = Integer.parseInt(args[0]);
         }
         catch (NumberFormatException e) {
            // Komentoriviparametria ei voitu muuntaa int-tyyppiseksi luvuksi.
            argsOK = false;
         }
      }
      // Nolla tai kaksi tai useampia komentoriviparametreja.
      else
         argsOK = false;

      // Komentoriviparametri kunnossa.
      if (argsOK) {

         // Luodaan palikoiden tyyppej‰ arpova olio ja asetetaan siihen viite.
         // Olion rakentimelle annetaan komentoriviparametrina saatu luku.
         // (Ohjelmassa tulee k‰ytt‰‰ vain yht‰ HT2Apu-oliota, jotta palikoiden
         // j‰rjestys olisi oikea.)
         HT2Apu apulainen = new HT2Apu(siemen);

         // Pelataan peli‰.
         pelaaPelia(apulainen);
      }
      // Ohjeistetaan virheen tapahtuessa.
      else
         System.out.println("Usage: java Texris seed");
   }
}