package client;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ClientScreenshotController {

    public ClientController clientController;
    @FXML
    private ImageView imageView;

    private BufferedImage img;

    private static Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }

        return new ImageView(wr).getImage();
    }

    public void captureImage() throws IOException {
        clientController.writeData("4");
        byte[] sizeAr = new byte[4];
        InputStream inputStream = clientController.s.getInputStream();
        inputStream.read(sizeAr);
        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
        byte[] imageAr = new byte[size];
        inputStream.read(imageAr);
        System.out.println('d');
        img = ImageIO.read(new ByteArrayInputStream(imageAr));
        File outfile = new File("image.jpg");
        ImageIO.write(img, "jpg", outfile);
        imageView.setImage(convertToFxImage(img));
    }

    public void copyToClipboard()
    {
        CopyImagetoClipBoard copyImagetoClipBoard = new CopyImagetoClipBoard(img);
    }
}
