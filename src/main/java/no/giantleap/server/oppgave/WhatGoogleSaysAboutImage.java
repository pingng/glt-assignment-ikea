package no.giantleap.server.oppgave;

import static java.util.stream.Collectors.joining;

class WhatGoogleSaysAboutImage {
    final DisplayableImage displayableImage;
    final GoogleCloudVisionUtils.WhatGoogleSays whatGoogleSays;

    WhatGoogleSaysAboutImage(DisplayableImage displayableImage, GoogleCloudVisionUtils.WhatGoogleSays whatGoogleSays) {
        this.whatGoogleSays = whatGoogleSays;
        this.displayableImage = displayableImage;
    }

    public float getHighestRecognizedObjectScore() {
        return whatGoogleSays.recognizedObjects.stream()
                .map(recognizedObject -> recognizedObject.score)
                .max(Float::compareTo)
                .orElse(-1f);
    }

    DisplayableImage createDisplayableImage() {
        String desc = "Google Vision API" +
                "\nObjects: " + whatGoogleSays.recognizedObjects.stream()
                .map(ro -> ro.name + " (" + ro.score + ")")
                .collect(joining(", ")) +
                "\nTexts: " + String.join(" ", whatGoogleSays.texts);

        return new DisplayableImage(displayableImage.image, displayableImage.parent,
                desc);
    }
}
