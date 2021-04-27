package no.giantleap.server.oppgave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.BitSet;

class ScoredDimension {
    final long score;
    final Dimension dim;
    final BitSet rawPixels;

    ScoredDimension(long score, Dimension dim, BitSet rawPixels) {
        this.score = score;
        this.dim = dim;
        this.rawPixels = rawPixels;
    }

    long getScore() {
        return score;
    }

    DisplayableImage createImage() {
        return new DisplayableImage(createBufferedImage(rawPixels, dim),
                this, "Simple score: " + score);
    }

    private static BufferedImage createBufferedImage(BitSet rawPixels, Dimension dim) {
        BufferedImage img = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
        g.setColor(Color.BLACK);
        for (int y = 0; y < dim.height; y++) {
            for (int x = 0; x < dim.width; x++) {
                if (!rawPixels.get(MiscUtils.toOffset(x, y, dim.width))) {
                    g.drawRect(x, y, 0, 0);
                }
            }
        }
        g.dispose();
        return img;
    }
}
