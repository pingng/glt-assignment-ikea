package no.giantleap.server.oppgave;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.BitSet;
import java.util.Comparator;
import java.util.function.Function;

abstract class MiscUtils {

    static BitSet load(String file) {
        try (InputStream in = new BufferedInputStream(new FileInputStream(file))) {
            BitSet r = new BitSet();
            int offset = 0;
            int read;
            while ((read = in.read()) != -1) {
                if (read == '1') {
                    r.set(offset);
                }
                offset++;
            }
            return r;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static int toOffset(int x, int y, int w) {
        return y * w + x;
    }

    static int scaledSqrtOf(int totalPixels, float scale) {
        return (int) (Math.sqrt(totalPixels) * scale);
    }

    @SuppressWarnings("unchecked")
    static <T, U extends Comparable<? super U>> Comparator<T> highToLow(Function<? super T, ? extends U> keyExtractor) {
        return (Comparator<T>) Comparator.comparing(keyExtractor).reversed();
    }

    static boolean isGoogleCredentialEnvFound() {
        return System.getenv("GOOGLE_APPLICATION_CREDENTIALS") != null;
    }

    static void checkGoogleCredentialFileSetup() {
        if (!isGoogleCredentialEnvFound()) {
            System.out.println("------\n" +
                    " Sett env. variable:\n" +
                    "   GOOGLE_APPLICATION_CREDENTIALS=src/main/resources/glt-assigment-ikea-db5179f74921.json\n" +
                    "\n" +
                    " Credential-filen er for en Service Account laget for denne oppgaven (i min Google konto).\n" +
                    " Jeg sletter Service Accounten når vi er ferdig med oppgaven :)\n"+
                    "------\n");

            GuiUtils.showWarning("Hei!\n\n" +
                    "Løsningen støtter kall mot Google Vision API, men du må sette\n" +
                    "env variabel. Se i console.\n" +
                    "\n" +
                    "I mellomtiden kan du se resultat før kjøring mot Google\n" +
                    "(det funker jo)");
        }
    }
}
