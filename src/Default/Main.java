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
 * Code PIN0 actuelle : 4444
 */

public class Main {
	private static CardTerminal terminal;
	private static Card carte;
	private static int i;
	private static String texte = new String();

	static public List<CardTerminal> getTerminals() throws CardException {
		return TerminalFactory.getDefault().terminals().list();

	}

	static public String toString(byte[] byteTab) {
		String texte = "";
		String hexNombre;
		for (i = 0; i < byteTab.length; i++) {
			hexNombre = "";
			hexNombre = Integer.toHexString(byteTab[i]);
			if (hexNombre.length() == 1) {
				texte += " 0" + hexNombre;
			} else {
				texte += " " + hexNombre;
			}
		}
		return texte;
	}

	public static boolean responseToString(ResponseAPDU r) {
		String strResponse = Integer.toHexString(r.getSW());
		int intResponse = Integer.parseInt(strResponse);
		String response = null;
		boolean valid = false;

		switch (intResponse) {
		case 9000:
			valid = true;
			response = "Commande executée avec succès";
			break;

		case 6982:
			response = "Trop de tentative. Carte bloquée";
			break;

		case 6300:
			response = "Code secret invalide";
			break;

		case 6581:
			response = "Mode inconnu";
			break;

		case 6700:
			response = "Invalid Lc value";
			break;
		}

		System.out.println(response);

		return valid;
	}

	static public void setNewPin0(CardChannel channel, String actPin, String newPin) throws CardException {

		System.out.println("\n Put true PINO");

		int apin1 = Integer.parseInt(String.valueOf(actPin.charAt(0)));
		int apin2 = Integer.parseInt(String.valueOf(actPin.charAt(1)));
		int apin3 = Integer.parseInt(String.valueOf(actPin.charAt(2)));
		int apin4 = Integer.parseInt(String.valueOf(actPin.charAt(3)));

		byte[] truePin = new byte[] { (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x07, (byte) 0x04, (byte) apin1,
				(byte) apin2, (byte) apin3, (byte) apin4 };

		CommandAPDU commande2 = new CommandAPDU(truePin);
		ResponseAPDU r2 = channel.transmit(commande2);

		if (responseToString(r2) == true) {
			System.out.println("Code PIN bon");
		}

		System.out.println("\n Nouveau mdp");

		int npin1 = Integer.parseInt(String.valueOf(newPin.charAt(0)));
		int npin2 = Integer.parseInt(String.valueOf(newPin.charAt(1)));
		int npin3 = Integer.parseInt(String.valueOf(newPin.charAt(2)));
		int npin4 = Integer.parseInt(String.valueOf(newPin.charAt(3)));

		byte[] newPinn = new byte[] { (byte) 0x80, (byte) 0xDE, (byte) 0x00, (byte) 0x06, (byte) 0x04, (byte) npin1,
				(byte) npin2, (byte) npin3, (byte) npin4 };
		CommandAPDU commande = new CommandAPDU(newPinn);
		ResponseAPDU r = channel.transmit(commande);
		System.out.println("reponse : " + toString(r.getBytes()));

		if (responseToString(r) == true) {
			System.out.println("Code PIN bien modifie");
		}

	}

	static public void verifyPin0(CardChannel channel, String actPin) throws CardException {

		System.out.println("\n Verify PINO");
		int apin1 = Integer.parseInt(String.valueOf(actPin.charAt(0)));
		int apin2 = Integer.parseInt(String.valueOf(actPin.charAt(1)));
		int apin3 = Integer.parseInt(String.valueOf(actPin.charAt(2)));
		int apin4 = Integer.parseInt(String.valueOf(actPin.charAt(3)));

		byte[] truePin = new byte[] { (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x07, (byte) 0x04, (byte) apin1,
				(byte) apin2, (byte) apin3, (byte) apin4 };

		CommandAPDU commande2 = new CommandAPDU(truePin);
		ResponseAPDU r2 = channel.transmit(commande2);

		if (responseToString(r2) == true) {
			System.out.println("Code PIN bon");
		}

	}

	static public void addInfo(CardChannel channel, String actPin, String info) throws CardException {
		System.out.println("\n Put true PINO");

		int apin1 = Integer.parseInt(String.valueOf(actPin.charAt(0)));
		int apin2 = Integer.parseInt(String.valueOf(actPin.charAt(1)));
		int apin3 = Integer.parseInt(String.valueOf(actPin.charAt(2)));
		int apin4 = Integer.parseInt(String.valueOf(actPin.charAt(3)));

		byte[] truePin = new byte[] { (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x07, (byte) 0x04, (byte) apin1,
				(byte) apin2, (byte) apin3, (byte) apin4 };

		CommandAPDU commande2 = new CommandAPDU(truePin);
		ResponseAPDU r2 = channel.transmit(commande2);

		if (responseToString(r2) == true) {
			System.out.println("Code PIN bon");
		}

		System.out.println("\n Add info");
		int info1 = Integer.parseInt(String.valueOf(info.charAt(0)));
		int info2 = Integer.parseInt(String.valueOf(info.charAt(1)));
		int info3 = Integer.parseInt(String.valueOf(info.charAt(2)));
		int info4 = Integer.parseInt(String.valueOf(info.charAt(3)));

		byte[] binfo = new byte[] { (byte) 0x80, (byte) 0xDE, (byte) 0x00, (byte) 0x3C, (byte) 0x04, (byte) info1,
				(byte) info2, (byte) info3, (byte) info4 };

		CommandAPDU commande = new CommandAPDU(binfo);
		ResponseAPDU r = channel.transmit(commande);

		if (responseToString(r) == true) {
			System.out.println("Ajout info bon");
		}

	}

	static public void readInfo(CardChannel channel) throws CardException {
		System.out.println("\n Read info");
		byte[] PIN0 = new byte[] { (byte) 0x80, (byte) 0xBE, (byte) 0x00, (byte) 0x3C, (byte) 0x04 };

		CommandAPDU commande = new CommandAPDU(PIN0);
		ResponseAPDU r = channel.transmit(commande);

		if (responseToString(r) == true) {
			System.out.println("Lecture info bon : " + toString(r.getBytes()));
		}

	}

	public static void main(String[] args) throws CardException {
		List<CardTerminal> terminauxDispos = Main.getTerminals();
		// Premier terminal dispo
		terminal = terminauxDispos.get(0);
		// System.out.println(terminal.toString());
		// Connexion à la carte
		carte = terminal.connect("T=0");
		// ATR (answer To Reset)
		// System.out.println(toString(carte.getATR().getBytes()));

		CardChannel channel = carte.getBasicChannel();

		verifyPin0(channel,"5555");

		//setNewPin0(channel, "5555", "4444");

		//addInfo(channel, "4444","1111");

		//readInfo(channel);

		carte.disconnect(false);

	}
}
