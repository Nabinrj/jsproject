package laundry;

import laundry.gui.LaundryFrame;
import javax.swing.SwingUtilities;

/** Application entry point for the Smart Laundry Facility concurrent simulation. */
public final class Main {
    private Main() { }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new LaundryFrame().setVisible(true)); }
}
