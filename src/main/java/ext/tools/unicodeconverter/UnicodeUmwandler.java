/*
 * Created on 23.04.2004
 *
 */
package ext.tools.unicodeconverter;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Ein Wandler, um Unicode-Codes fuer Java herauszufinden.
 * <table border="0">
 * <tr>
 * <th>Letter</th>
 * <th>Codepoint bzw. Hex</th>
 * </tr>
 * <tr>
 * <td>&uuml;</td>
 * <td>fc</td>
 * </tr>
 * <tr>
 * <td>&Uuml;</td>
 * <td>dc</td>
 * </tr>
 * <tr>
 * <td>&auml;</td>
 * <td>e4</td>
 * </tr>
 * <tr>
 * <td>&Auml;</td>
 * <td>c4</td>
 * </tr>
 * <tr>
 * <td>&ouml;</td>
 * <td>f6</td>
 * </tr>
 * <tr>
 * <td>&Ouml;</td>
 * <td>d6</td>
 * </tr>
 * <tr>
 * <td>&szlig;</td>
 * <td>df</td>
 * </tr>
 * </table>
 * 
 * @author dirk
 * @version $Revision: 1.7 $
 * 
 */
public class UnicodeUmwandler extends JFrame {
    /**
     * Hexadezimale Werte
     */
    public static final char HEX_WERTE[] = { '0', '1', '2', '3', '4', '5', '6',
        '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    private static final long serialVersionUID = 4657806856819445560L;
    
    public static String holeEncodedString(final String text, final int ZIELENC)
        throws UnsupportedEncodingException {
        String rc = new String(text.getBytes("ISO-8859-1"), "UTF-8");
        return rc;
    }
    
    /**
     * Wandelt einen UTF8-String in einen Java-String um.
     * 
     * @param alt
     *            der UTF8-String
     * @return der Java-String
     */
    public static String holeStringAusUTF8(final String alt) {
        if (alt == null) {
            return "";
        }
        if (alt.length() <= 0) {
            return "";
        }
        char[] zeichen = alt.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < zeichen.length; i++) {
            int cp = Character.codePointAt(zeichen, i);
            // Leerzeichen werden falsch interpretiert, wenn vor ihnen ein
            // Unicode-Char ist
            if (Character.isWhitespace(cp)) {
                sb.append(' ');
                continue;
            }
            /*
             * Bei UTF8 werden 2 Byte gebraucht. isUniIdentStart sagt aus, ob
             * vorherein solches Zeichen kam.Bei normalen Buchstaben wird TRUE
             * zurueckgeliefert, beiUnicodes nur das 1. Byte TRUE, das 2. ist
             * dann FALSE.Dieses Verhalten wird hier genutzt:
             */
            if (!Character.isUnicodeIdentifierStart(cp)) {
                byte[] b2 = new byte[2];
                b2[0] = (byte) zeichen[i - 1];
                b2[1] = (byte) zeichen[i];
                try {
                    String tmp = new String(b2, "UTF-8");
                    sb.deleteCharAt(sb.length() - 1);
                    sb.append(tmp);
                } catch (java.io.UnsupportedEncodingException e) {
                }
            } else {
                sb.append(zeichen[i]);
            }
        }
        return sb.toString();
    }
    
    /**
     * Liefert die Unicode-Nummer des Buchstaben auf index in s zurueck.
     * 
     * @param s
     *            der String, der den Buchstaben enthaelt, dessen codePoint
     *            gesucht ist
     * @param index
     *            der index des Buchstaben. Der 1. Buchstabe hat index=0, der 2.
     *            hat index=1 usw.
     * @return der codePoint. Durch NumKonverter.holeHexVonInt() kann die
     *         hexadezimale Version erstellt werden.
     */
    public static int holeUnicodeDarstellung(final String s, final int index) {
        int rc = 0;
        rc = Character.codePointAt(s.toCharArray(), index);
        return rc;
    }
    
    public static void main(final String[] args) {
        new UnicodeUmwandler();
    }
    
    private JTextArea jta = null;
    
    private JTextField tf = null;
    
    private JButton wandelbutton = null;
    
    /**
     * @throws java.awt.HeadlessException
     *             if an error occurred
     */
    public UnicodeUmwandler() throws HeadlessException {
        super("Unicode-Wandler");
        JPanel p = new JPanel();
        tf = new JTextField(5);
        wandelbutton = new JButton("Umwandeln");
        jta = new JTextArea(8, 30);
        jta.setEditable(false);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(p, BorderLayout.CENTER);
        p.setLayout(new GridBagLayout());
        p.add(tf);
        p.add(wandelbutton);
        JScrollPane sp = new JScrollPane(jta);
        p.add(sp);
        p.add(new JLabel("\u65e5\u672c\u8a9e\u6587\u5b57\u5217"));
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        wandelbutton.addActionListener(new ActionListener() {
            /**
             * (non-Javadoc)
             * 
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             */
            @Override
            public void actionPerformed(final ActionEvent e) {
                String text = tf.getText();
                jta.setText("Japanische Zeichen: \u65e5\u672c\u8a9e\u6587\u5b57\u5217\n");
                for (int i = 0; i < text.length(); i++) {
                    char c = text.charAt(i);
                    // Character c2=new Character(c);
                    jta.append("Analysiere Letter " + c + ":\n");
                    /*
                     * if(!Character.isLetterOrDigit(c)){
                     * jta.append("- kein Letter oder Zahl\n"); continue; }
                     */
                    char feld[] = new char[1];
                    feld[0] = c;
                    int codepoint = Character.codePointAt(feld, 0);
                    jta.append("- CP: " + codepoint + " bzw. "
                        + holeHexVonInt(codepoint) + "\n");
                }
            }
        });
        wandelbutton.setMnemonic('U');
        setResizable(false);
        setVisible(true);
    }
    
    public byte[] holeByteArrayVonInt(final int wert) {
        byte[] rc = new byte[4];
        rc[0] = (byte) (wert & 0xFF);
        rc[1] = (byte) ((wert & 0xFF00) >> 8);
        rc[2] = (byte) ((wert & 0xFF0000) >> 16);
        rc[3] = (byte) ((wert & 0xFF000000) >> 24);
        return rc;
    }
    
    public String holeHexVonInt(final int wert) {
        StringBuffer sb = new StringBuffer();
        byte bytes[] = holeByteArrayVonInt(wert);
        // Wir fangen von hinten an und arbeiten uns vor:
        for (int i = bytes.length - 1; i >= 0; i--) {
            byte aktByte = bytes[i];
            byte lowByte = (byte) (aktByte & 0xF);
            byte highByte = (byte) ((aktByte & 0xF0) >> 4);
            sb.append(HEX_WERTE[highByte]);
            sb.append(HEX_WERTE[lowByte]);
        }
        return sb.toString();
    }
}
