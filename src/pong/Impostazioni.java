// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Consegna 2: le impostazioni. Scadenza: domenica 1 novembre 2026.
package pong;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Le impostazioni di una partita di Pong: quanti punti servono per vincere, con quale scarto
 * minimo, a che velocità va la palla. È un record (lezione 4): una volta create, non cambiano.
 *
 * IL PEZZO MANCANTE: il costruttore che rifiuta i valori sbagliati e il metodo leggi.
 * Ogni metodo ha il suo contratto, nel commento sopra. I test ufficiali (cartella test)
 * controllano i contratti.
 */
public record Impostazioni(int puntiPerVincere, int scartoMinimo, int velocitaPalla) {

    /** Le impostazioni standard: 11 punti per vincere, scarto minimo 2, velocità della palla 5. */
    public static final Impostazioni STANDARD = new Impostazioni(11, 2, 5);

    /**
     * Il costruttore (lezione 7: un record che nasce giusto).
     * Promette: se i tre valori sono nei loro intervalli, le impostazioni con quei valori.
     * Lancia IllegalArgumentException se puntiPerVincere non è fra 1 e 99, se scartoMinimo
     * non è fra 1 e 10, o se velocitaPalla non è fra 1 e 20 (estremi compresi). Il messaggio
     * dice quale valore è sbagliato e perché.
     */
    public Impostazioni {      

        if( !puntiValidi(puntiPerVincere) ) throw new IllegalArgumentException("Punti per vincere sono fuori range");
        else if ( !scartoValido(scartoMinimo) ) throw new IllegalArgumentException("Scarto minimo è fuori range");
        else if ( !velocitaValida(velocitaPalla)) throw new IllegalArgumentException("Velocità palla è fuori range");
    
    }

    private static boolean puntiValidi(int punti)       { return punti >= 1 && punti <= 99; }
    private static boolean scartoValido(int scarto)     { return scarto >= 1 && scarto <= 10; }
    private static boolean velocitaValida(int velocita) { return velocita >= 1 && velocita <= 20; }


    /**
     * Legge le impostazioni dalle righe di un file, una impostazione per riga.
     * Pretende: righe non è null e non contiene null.
     * Promette:
     *  - le righe vuote (o di soli spazi) e quelle che, tolti gli spazi iniziali, cominciano
     *    con # sono ignorate;
     *  - ogni altra riga è nella forma  chiave = valore , con spazi qualsiasi attorno alla chiave,
     *    al segno = e al valore; le chiavi sono puntiPerVincere, scartoMinimo, velocitaPalla;
     *  - un'impostazione che nelle righe non c'è prende il valore standard;
     *  - il risultato sono le impostazioni lette.
     * Lancia IllegalArgumentException, con un messaggio che comincia con «riga N: » (N è il numero
     * della riga, contando da 1 tutte le righe, anche quelle vuote e i commenti), se in una riga:
     *  1. manca il segno = ;
     *  2. la chiave non è una delle tre;
     *  3. il valore non è un numero intero;
     *  4. la stessa chiave c'era già in una riga precedente;
     *  5. il valore è fuori dal suo intervallo (gli intervalli del costruttore).
     * Se ci sono errori in più righe, il messaggio è quello della riga più in alto.
     */
    public static Impostazioni leggi(List<String> righe) {
        
        int n = righe.size();
        String chiave = "";
        String valore = "";
        int puntiLetti= 0;
        int scartoLetto = 0;
        int velocitaLetta = 0;

        // valori standard delle impostazioni che non ci sono
        int punti = 11;
        int scarto = 2;
        int velocita = 5;

        boolean lettoPunti = false;
        boolean lettoScarto = false;
        boolean lettoVelocita = false;


    //////////////////////////////////////////////////////
        for( int i=0; i< n; i++){

        String riga = righe.get(i).trim();

        if( riga.trim().isEmpty() || riga.startsWith("#") ){
            continue;

        }else if( riga.indexOf('=') == -1 ){
                throw new IllegalArgumentException("riga " + (i+1) + ": manca il segno =");
            }else{
                int posizioneUguale = riga.indexOf('=');
                chiave = riga.substring( 0, posizioneUguale );
                chiave = chiave.trim();
                valore = riga.substring( posizioneUguale+1 );
                valore = valore.trim();

                switch (chiave) {
                    case "puntiPerVincere":

                        if( lettoPunti == true ) throw new IllegalArgumentException("riga " + (i+1) + ": chiave puntiPerVincere ripetuta");
                        try {
                            puntiLetti = Integer.parseInt(valore);
                        } catch (Exception e) {
                            throw new NumberFormatException ("riga " + (i+1) + ": non è un numero");
                        }
                        if( puntiValidi(puntiLetti) != true ){
                            throw new IllegalArgumentException("riga " + (i+1) + ": puntiPerVincere fuori intervallo ");
                        }
                        
                        punti = puntiLetti;
                        lettoPunti = true;
                        
                        break;
                    case "scartoMinimo":
                        
                        if( lettoScarto == true ) throw new IllegalArgumentException("riga " + (i+1) + ": chiave scartoMinimo ripetuta");
                        try {
                            scartoLetto = Integer.parseInt(valore);
                        } catch (Exception e) {
                            throw new NumberFormatException ("riga " + (i+1) + ": non è un numero");
                        }
                        if( scartoValido(scartoLetto) != true ){
                            throw new IllegalArgumentException("riga " + (i+1) + ": scartoMinimo fuori intervallo ");
                        }
                        
                        scarto = scartoLetto;
                        lettoScarto = true;

                        break;
                        case "velocitaPalla":
                        
                        if( lettoVelocita == true ) throw new IllegalArgumentException("riga " + (i+1) + ": chiave velocitaPalla ripetuta");
                        try {
                            velocitaLetta = Integer.parseInt(valore);
                        } catch (Exception e) {
                            throw new NumberFormatException ("riga " + (i+1) + ": non è un numero");
                        }
                        if( velocitaValida(velocitaLetta) != true ){
                            throw new IllegalArgumentException("riga " + (i+1) + ": velocitaPalla fuori intervallo ");
                        }
                        
                        velocita = velocitaLetta;
                        lettoVelocita = true;

                        break;
                    default:
                        throw new IllegalArgumentException("riga " + (i+1) + ": chiave sconosciuta");
                }
            }
        }
        return new Impostazioni(punti, scarto, velocita);
    }

    /**
     * Legge le impostazioni da un file di testo.
     * Promette: come leggi, con le righe del file.
     * Lancia IOException se il file non si può leggere (per esempio non esiste): non la cattura,
     * la lascia a chi chiama. Lancia IllegalArgumentException come leggi.
     */
    public static Impostazioni daFile(Path file) throws IOException {
        return leggi(Files.readAllLines(file));
    }


}
