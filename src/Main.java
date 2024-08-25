//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import net.sourceforge.tess4j.util.LoadLibs;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.tess4j.*;

public class Main {
    public static void main(String[] args) throws Exception {
        DetectionProgram prog = new DetectionProgram(args);
    }
}

class DetectionProgram {
    private Path base_path;
    private String extension;
    private String language;
    private ITesseract tesseract_instance;

    public DetectionProgram(String[] args) {
        // Loading libraries
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // After library has loaded, we can begin
        try {
            start(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(String[] args) throws Exception {
        // Initialising program
        init_arg(args);
        init_ocr();

        // Getting files
        String[] files = glob();

        // Detection loop
        int characters = 0;
        int words = 0;

        for (String file : files) {
            // Image detection
            Mat mat_img = open_image(file);
            BufferedImage processed_img = process_image(mat_img);
            String text = detect(processed_img);

            // Counting characters and words
            characters += text.length();
            if (!text.isEmpty()) {
                String[] split_text = text.split(" ");
                words += split_text.length;
            }
            System.out.printf("FINISHED:\t%s\n", file);
        }

        System.out.printf("\n\n\n-----------------------\nCharacters: %d\nWords: %d\n", characters, words);
    }

    private void init_arg(String[] args) throws Exception {
        // Initialising this.base_path
        if (args.length == 0) {
            throw new Exception("Invalid args");
        }

        String raw_path = args[0];
        this.base_path = Path.of(raw_path);

        // Initialising extension and language args
        this.extension = get_arg(args, "-e", "png");
        this.language = get_arg(args, "-l", "en");
    }

    private void init_ocr() {
        this.tesseract_instance = new Tesseract();
        this.tesseract_instance.setLanguage(language);
        File test_data = LoadLibs.extractTessResources("tessdata");
    }

    private static String get_arg(String[] args, String flag, String def) {
        // Search array for argument flag
        for (int i = 0; i < args.length; i++) {
            if (flag.equals(args[i])) {
                if (i + 1 < args.length) {      // Check to see if a future input is given
                    return args[i + 1];
                } else {
                    break;
                }
            }
        }

        return def;                             // If all else fails, return default
    }

    private String[] glob() {
        String glob = String.format("*.%s", this.extension);
        List<String> found_paths = new ArrayList<String>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(this.base_path, glob)) {
            for (Path entry : stream) {
                String filename = entry.getFileName().toString();
                found_paths.add(filename);

                System.out.printf("FOUND:\t%s\n", filename);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return found_paths.toArray(new String[0]);
    }

    private Mat open_image(String path) {
        // Creating full path of image
        String full_path = this.base_path + "\\" + path;

        // Reading raw bytes of path and putting them in a buffer
        byte[] bytes;

        try {
            bytes = Files.readAllBytes(Path.of(full_path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // Reading image from buffer
        return Imgcodecs.imdecode(new MatOfByte(bytes), 1);
    }

    private static BufferedImage mat_to_buffered_image(Mat mat) {
        BufferedImage t_buf_img;

        // Getting image metadata
        int width = mat.width();
        int height = mat.height();
        int channels = mat.channels();

        // Converting mat to byte[]
        byte[] sourcePixels = new byte[width * height * channels];
        mat.get(0, 0, sourcePixels);

        // Setting channel type (RGBA, RGB or Greyscale)
        if (channels > 3) {
            t_buf_img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        } else if (channels > 1) {
            t_buf_img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        } else {
            t_buf_img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        }

        // Copying over to t_buf_img
        byte[] targetPixels = ((DataBufferByte) t_buf_img.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);


        return t_buf_img;
    }

    private static BufferedImage process_image(Mat img) {
        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);             // Converting to grayscale
        Imgproc.threshold(img, img, 175, 255, Imgproc.THRESH_BINARY);        // Thresholding

//        Mat kernel = new Mat(3,3, CvType.CV_32F) {                       // Morphology
//            {
//                put(0,0,1);
//                put(0,1,1);
//                put(0,2,1);
//
//                put(1,0,1);
//                put(1,1,1);
//                put(1,2,1);
//
//                put(2,0,1);
//                put(2,1,1);
//                put(2,2,1);
//            }
//        };
//
//        Imgproc.morphologyEx(img, img, Imgproc.MORPH_DILATE, kernel);
//        Imgproc.morphologyEx(img, img, Imgproc.MORPH_CLOSE, kernel);
//        Imgproc.morphologyEx(img, img, Imgproc.MORPH_OPEN, kernel);

        // Other processing
//        Core.bitwise_and(img, img, new Mat(img.rows(), img.cols(), CvType.CV_32F, Scalar.all(0)));

        // Contours
//        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
//        Mat hierarchy = new Mat();
//
//        Imgproc.findContours(img, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
//        Imgproc.drawContours(img, contours, -1, new Scalar(0, 255, 0), 3);
//
//        System.out.print(contours);
//        System.out.print(hierarchy);

        // Showing image
//        HighGui.imshow("IMG", img);
//        HighGui.waitKey(0);


        return mat_to_buffered_image(img);
    }

    private String detect(BufferedImage img) {
        String text = "";

        try {
            text = this.tesseract_instance.doOCR(img);
            System.out.printf("TEXT:\t\"%s\"\n", text);
        } catch (TesseractException e) {
            throw new RuntimeException(e);
        }

        return text;
    }
}