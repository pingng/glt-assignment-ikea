package no.giantleap.server.oppgave;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public abstract class GuiUtils {
    private static final String[] PLACES = {"First and Best", "Second", "Third"};

    static void displayInGui(List<DisplayableImage> imagesSortedByMlObjectScore) {
        JTabbedPane tabbedPane = new JTabbedPane();
        for (int i = 0; i < imagesSortedByMlObjectScore.size(); i++) {
            DisplayableImage di = imagesSortedByMlObjectScore.get(i);

            JPanel p = createPanelWithImage(di.image, di.parent.dim.toString(), di.description);
            String title = i < PLACES.length ? PLACES[i] : "Priority " + (i + 1);

            tabbedPane.add(title, p);
        }

        JFrame f = new JFrame("Hva har Kai-Ronny kjøpt på Ikea?");
        f.getContentPane().add(tabbedPane);
        f.pack();
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    private static JPanel createPanelWithImage(BufferedImage img, String title, String description) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JTextArea(description), BorderLayout.NORTH);
        p.add(new JLabel(new ImageIcon(img)));
        return p;
    }

    public static void showWarning(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.WARNING_MESSAGE);
    }
}
