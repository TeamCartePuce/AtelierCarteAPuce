package Default;

import java.util.List;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
/**
 *
 * @author Guillaume
 */
public class Main {
    private static CardTerminal terminal;
    private static Card carte;
    private static int i;
    private static String texte=new String();
    
    static public List<CardTerminal> getTerminals() throws CardException {
        return TerminalFactory.getDefault().terminals().list();

    }
    
    static public String toString(byte[] byteTab){
        String texte="";
        String hexNombre;
        for(i=0;i<byteTab.length;i++){
                hexNombre="";
                hexNombre=Integer.toHexString(byteTab[i]);
                if(hexNombre.length()==1){
                    texte+=" 0"+hexNombre;
                }
                else{
                    texte+=" "+hexNombre;
                }
        }
        return texte;
    }
    
    public static void main(String[] args) throws CardException {
        List<CardTerminal> terminauxDispos = Main.getTerminals();
        //Premier terminal dispo
        terminal = terminauxDispos.get(0);
        System.out.println(terminal.toString());
        //Connexion à la carte
        carte = terminal.connect("T=0");
        //ATR (answer To Reset)
        System.out.println(toString(carte.getATR().getBytes()));
        
        CardChannel channel = carte.getBasicChannel();
        
        System.out.println("\n Numéro de série de la carte");
        CommandAPDU commande = new CommandAPDU(0x80,0xBE,0x00,0x00,0x04);
        ResponseAPDU r = channel.transmit(commande);
        System.out.println("reponse : " + toString(r.getBytes()));
        System.out.println(r);
        
        System.out.println("\n Test de PINO");
        byte[] PIN0= new byte[] {(byte) 0x00,(byte) 0x20,(byte) 0x00,(byte) 0x07,(byte) 0x04,(byte) 0xAA,(byte) 0xAA,(byte) 0xAA,(byte) 0xAA} ;
        CommandAPDU commande2 = new CommandAPDU(PIN0);
        ResponseAPDU r2 = channel.transmit(commande2);
        System.out.println(r2);
        System.out.println(Integer.toHexString(r2.getSW1()));
        
        carte.disconnect(false);
        
        

    }
}