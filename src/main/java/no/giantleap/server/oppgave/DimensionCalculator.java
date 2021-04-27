package no.giantleap.server.oppgave;

class DimensionCalculator {
    private final int totalPixels;
    private final int minimumSize;

    public DimensionCalculator(int totalPixels, int minimumSize) {
        this.totalPixels = totalPixels;
        this.minimumSize = minimumSize;
    }

    public boolean isValidAsWidth(int potentialWidth) {
        boolean divisible = totalPixels % potentialWidth == 0;
        if (divisible) {
            int potentialHeight = totalPixels / potentialWidth;
            return potentialWidth > minimumSize && potentialHeight > minimumSize;
        }
        return false;
    }

    public Dimension createDimensionFromWidth(int width) {
        if (isValidAsWidth(width))
            return new Dimension(width, totalPixels / width);

        throw new IllegalArgumentException("Not valid width: " + width);
    }
}
