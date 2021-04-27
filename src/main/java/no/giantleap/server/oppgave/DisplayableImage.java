package no.giantleap.server.oppgave;

import java.awt.image.BufferedImage;

class DisplayableImage {
    final BufferedImage image;
    final ScoredDimension parent;
    final String description;

    DisplayableImage(BufferedImage image, ScoredDimension parent, String description) {
        this.image = image;
        this.parent = parent;
        this.description = description;
    }
}
