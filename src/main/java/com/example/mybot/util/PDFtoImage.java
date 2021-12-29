package com.example.mybot.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFtoImage {
    public static String generateImageFromPDF(String filename) throws IOException {
        PDDocument document = PDDocument.load(new File(filename));
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        int size = filename.length();
        FileOutputStream image = new FileOutputStream(filename.substring(0,size-4) + "_image.png");
        for (int page = 0; page < document.getNumberOfPages(); ++page) {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(
                    page, 300, ImageType.RGB);
            ImageIO.write(bim, "PNG", image);
        }
        document.close();
        image.close();
        System.out.println(filename.substring(0, size-4) + "_image.png");
        return filename.substring(0, size-4) + "_image.png";
    }

    public static String generateImageFromPDF(String filename, Integer numberOfPage) {
        int size = filename.length();
        try {
            PDDocument document = PDDocument.load(new File(filename));
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            FileOutputStream image = new FileOutputStream(filename.substring(0,size-4) + "_image.png");
            for (int page = 0; page < numberOfPage+1; ++page) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(
                        page, 300, ImageType.RGB);
                ImageIO.write(bim, "PNG", image);
            }
            document.close();
            image.close();
            System.out.println(filename.substring(0, size-4) + "_image.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filename.substring(0, size-4) + "_image.png";
    }
}
