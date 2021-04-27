package no.giantleap.server.oppgave;

import java.util.BitSet;

import static no.giantleap.server.oppgave.MiscUtils.*;

class ImageEvaluator {
    private final BitSet rawPixels;

    public ImageEvaluator(BitSet rawPixels) {
        this.rawPixels = rawPixels;
    }

    /**
     * Tanken er at bilder med størst og sammenhengende svart område mer sannsynlig er et bilde.
     *
     * Her forenkles det ved å telle pixler i horisontale & vertikale linjer. I tillegg,
     * vil ikke første pixel på en linje gi noe poeng, slik at det er om å ha
     * færrest & lengst linjer.
     */
    ScoredDimension calcScoreOfDimension(Dimension dim) {
        // Horizontal
        long hScore = 0;
        for (int y = 0; y < dim.height; y++) {
            boolean lineHasStarted = false;
            for (int x = 0; x < dim.width; x++) {
                if (!rawPixels.get(toOffset(x, y, dim.width))) {
                    if (lineHasStarted) {
                        hScore++;
                    } else {
                        lineHasStarted = true;
                    }
                } else {
                    lineHasStarted = false;
                }
            }
        }

        // Vertical
        long vScore = 0;
        for (int x = 0; x < dim.width; x++) {
            boolean lineHasStarted = false;
            for (int y = 0; y < dim.height; y++) {
                if (!rawPixels.get(toOffset(x, y, dim.width))) {
                    if (lineHasStarted) {
                        vScore++;
                    } else {
                        lineHasStarted = true;
                    }
                } else {
                    lineHasStarted = false;
                }
            }
        }

        return new ScoredDimension(hScore + vScore, dim, rawPixels);
    }
}
