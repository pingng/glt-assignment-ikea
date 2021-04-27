package no.giantleap.server.oppgave;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;
import static no.giantleap.server.oppgave.GoogleCloudVisionUtils.*;
import static no.giantleap.server.oppgave.MiscUtils.*;

public class Main {
    private static final int DISPLAY_IMAGE_COUNT = 3;

    public static void main(String[] args) {
        checkGoogleCredentialFileSetup();

        BitSet rawPixels = load("./src/main/resources/bildefil.txt");
//        BitSet rawPixels = load("./src/main/resources/example_10x10.txt");

        List<DisplayableImage> topScoredImages = evalTopScoredImages(rawPixels, DISPLAY_IMAGE_COUNT);

        GuiUtils.displayInGui(topScoredImages);
    }

    private static List<DisplayableImage> evalTopScoredImages(BitSet rawPixels, int imageCount) {
        System.out.println("1) Finn potensielle bilder ved å telle pixler i horisontale og vertikale linjer...\n" +
                "   (hovedkoden ligger i ImageEvaluator.calcScoreOfDimension())");

        List<DisplayableImage> topScoredImages = evalTopSimpleScoredImages(rawPixels, imageCount);

        if (isGoogleCredentialEnvFound()) {
            System.out.println("2) Gi potensielle bilder til Google Vision og få score på evt objekter på bildet (tar noen sekunder)...");
            return evalTopObjectRecognitionScoreUsingGoogleVision(topScoredImages);
        }
        return topScoredImages;
    }

    private static List<DisplayableImage> evalTopSimpleScoredImages(BitSet rawPixels, int imageCount) {
        int totalPixels = rawPixels.length();
        int minimumWidthAndHeight = scaledSqrtOf(totalPixels, 0.05f);

        DimensionCalculator dimCalculator = new DimensionCalculator(totalPixels, minimumWidthAndHeight);
        ImageEvaluator imageEvaluator = new ImageEvaluator(rawPixels);

        return IntStream.range(1, totalPixels)
                .parallel()
                .filter(dimCalculator::isValidAsWidth)
                .mapToObj(dimCalculator::createDimensionFromWidth)
                .map(imageEvaluator::calcScoreOfDimension)
                .sorted(highToLow(ScoredDimension::getScore))
                .limit(imageCount)
                .map(ScoredDimension::createImage)
                .collect(toList());
    }

    private static List<DisplayableImage> evalTopObjectRecognitionScoreUsingGoogleVision(List<DisplayableImage> topScoredImages) {
        try {
            return topScoredImages.stream()
                    .map(Main::callGoogleVisionWithImage)
                    .sorted(highToLow(WhatGoogleSaysAboutImage::getHighestRecognizedObjectScore))
                    .map(WhatGoogleSaysAboutImage::createDisplayableImage)
                    .collect(toList());
        } catch (Exception e) {
            e.printStackTrace();

            GuiUtils.showWarning("Google failed me, showing result from simple scoring.");
            return topScoredImages;
        }
    }

    private static WhatGoogleSaysAboutImage callGoogleVisionWithImage(DisplayableImage displayableImage) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(displayableImage.image, "png", output);
            return new WhatGoogleSaysAboutImage(displayableImage, callGoogleCloudVisionApi(output.toByteArray()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
