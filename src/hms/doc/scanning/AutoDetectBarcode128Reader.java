package hms.doc.scanning;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import javax.imageio.ImageIO;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class AutoDetectBarcode128Reader {

    public static void main(String[] args) {
        String filePath = "TempImages/image1.png";
        readAndCropBarcode(null);
    }
 
    public static String readAndCropBarcode(BufferedImage bufferedImage) {
    	 Result result = null;
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            Vector<BarcodeFormat> formats = new Vector<BarcodeFormat>();
            formats.add(BarcodeFormat.CODE_128);
            hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);
            try {
                result = new MultiFormatReader().decode(bitmap, hints);
                System.out.println("✅ Barcode Text: " + result.getText());
                System.out.println("📄 Barcode Format: " + result.getBarcodeFormat());
                ResultPoint[] points = result.getResultPoints();

                if (points != null && points.length >= 2) {
                    float minX = points[0].getX();
                    float minY = points[0].getY();
                    float maxX = minX;
                    float maxY = minY;

                    for (ResultPoint point : points) {
                        if (point.getX() < minX) minX = point.getX();
                        if (point.getY() < minY) minY = point.getY();
                        if (point.getX() > maxX) maxX = point.getX();
                        if (point.getY() > maxY) maxY = point.getY();
                    }
                    int padding = 20;
                    int x = Math.max((int)minX - padding, 0);
                    int y = Math.max((int)minY - padding, 0);
                    int width = Math.min((int)(maxX - minX) + 2 * padding, bufferedImage.getWidth() - x);
                    int height = Math.min((int)(maxY - minY) + 2 * padding, bufferedImage.getHeight() - y);
                    BufferedImage cropped = bufferedImage.getSubimage(x, y, width, height);
                    String croppedText = decodeCroppedImage(cropped, hints);
                    if (croppedText != null) {
//                        System.out.println("✅ Barcode Text (from cropped): " + croppedText);
                    }
                }

            } catch (NotFoundException e) {
                System.err.println("❌ No barcode found in the image.");
            }

        } catch (Exception e) {
            System.err.println("⚠️ Error: " + e.getMessage());
            e.printStackTrace();
        }
        return result!=null?result.getText():"";
    } 

    private static String decodeCroppedImage(BufferedImage image, Hashtable<DecodeHintType, Object> hints) {
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap, hints);
            return result.getText();
        } catch (Exception e) {
            System.err.println("⚠️ Could not decode cropped image: " + e.getMessage());
            return null;
        }
    }
}
