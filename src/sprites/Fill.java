package sprites;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a fill.
 */
public class Fill {
    private static Map<String, Image> pathToImage = new TreeMap<>();
    private boolean isFillImage;
    private Color color;
    private Image image;
    /**
     * Constructor for color fill.
     *
     * @param color The fill color.
     */
    public Fill(Color color) {
        isFillImage = false;
        this.color = color;
    }
    /**
     * Constructor for image fill.
     *
     * @param image The fill image.
     */
    public Fill(Image image) {
        isFillImage = true;
        this.image = image;
    }
    /**
     * Constructor for image fill.
     *
     * @param imageFileName The image file name.
     */
    public Fill(String imageFileName) {
        Image img = null;

        try {
            //            File imageFile = new File(imageFileName);
            if (!pathToImage.containsKey(imageFileName)) {
                InputStream imageFile = ClassLoader.getSystemClassLoader().getResourceAsStream(imageFileName);
                img = ImageIO.read(imageFile);
                pathToImage.put(imageFileName, img);
            } else {
                img = pathToImage.get(imageFileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.image = img;
        isFillImage = true;
    }
    /**
     * Return true if fill is image.
     *
     * @return True if fill is image.
     */
    public boolean isImage() {
        return isFillImage;
    }
    /**
     * Return the fill color.
     *
     * @return The fill color.
     */
    public Color getColor() {
        return this.color;
    }
    /**
     * Return the fill image.
     *
     * @return The fill image.
     */
    public Image getImage() {
        return this.image;
    }
}
