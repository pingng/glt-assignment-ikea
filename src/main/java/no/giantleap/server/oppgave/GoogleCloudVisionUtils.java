package no.giantleap.server.oppgave;

//import com.google.cloud.storage.*;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public abstract class GoogleCloudVisionUtils {
    static WhatGoogleSays callGoogleCloudVisionApi(byte[] data) throws IOException {
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

            // The path to the image file to annotate
//            String fileName = "C:\\Users\\pingn.GIANTLEAP\\Downloads\\oppg img.png";
            ByteString imgBytes = ByteString.copyFrom(data);

            // Builds the image annotation request
            List<AnnotateImageRequest> requests = new ArrayList<>();
            Image img = Image.newBuilder().setContent(imgBytes).build();
            AnnotateImageRequest request =
                    AnnotateImageRequest.newBuilder().addFeatures(
                            Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build()
                    )
                            .addFeatures(
                                    Feature.newBuilder().setType(Feature.Type.OBJECT_LOCALIZATION).build()
                            ).setImage(img).build();
            requests.add(request);

            // Performs label detection on the image file
            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            List<RecognizedObject> recognizedObjects = new ArrayList<>();
            Set<String> texts = new LinkedHashSet<>();
            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    throw new RuntimeException("Error: " + res.getError().getMessage());
                }

                recognizedObjects.addAll(res.getLocalizedObjectAnnotationsList().stream()
                        .map(ann -> new RecognizedObject(ann.getName(), ann.getScore()))
                        .collect(toList()));

                texts.addAll(res.getTextAnnotationsList().stream()
                        .map(EntityAnnotation::getDescription)
                        .map(String::trim)
                        .collect(toList()));
            }
            return new WhatGoogleSays(recognizedObjects, new ArrayList<>(texts));
        }
    }

    static class WhatGoogleSays {
        final List<RecognizedObject> recognizedObjects;
        final List<String> texts;

        WhatGoogleSays(List<RecognizedObject> recognizedObjects, List<String> texts) {

            this.recognizedObjects = recognizedObjects;
            this.texts = texts;
        }
    }

    static class RecognizedObject {
        final String name;
        final float score;

        RecognizedObject(String name, float score) {
            this.name = name;
            this.score = score;
        }

        @Override
        public String toString() {
            return name + " (" + score + ")";
        }
    }
}
