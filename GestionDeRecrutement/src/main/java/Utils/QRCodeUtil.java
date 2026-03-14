package Utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.util.Map;

public class QRCodeUtil {

    /**
     * Generates a JavaFX WritableImage containing a QR code for the given text.
     *
     * @param text   the content to encode
     * @param size   width and height in pixels
     * @return       a WritableImage ready to display in an ImageView
     */
    public static WritableImage generate(String text, int size) {
        QRCodeWriter writer = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = Map.of(
                EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M,
                EncodeHintType.MARGIN, 2
        );
        try {
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size, hints);
            WritableImage image = new WritableImage(size, size);
            PixelWriter pw = image.getPixelWriter();
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    pw.setColor(x, y, matrix.get(x, y)
                            ? javafx.scene.paint.Color.BLACK
                            : javafx.scene.paint.Color.WHITE);
                }
            }
            return image;
        } catch (WriterException e) {
            throw new RuntimeException("Erreur génération QR code", e);
        }
    }
}
