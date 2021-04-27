package no.giantleap.server.oppgave;

class Dimension {
    final int width;
    final int height;

    Dimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return width + "x" + height;
    }
}
