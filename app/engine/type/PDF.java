package engine.type;

import models.Page;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import utils.Logs;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by pavelkuzmin on 27/09/2016.
 */
public class PDF {

    public static Page read(String url) {

        Logs.debug("Read PDF");

        PDFTextStripper pdfStripper = null;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;

        File file = new File("file.pdf");

        String title = "";
        String text = "";

        try {

            FileUtils.copyURLToFile(new URL(url), file);

            PDDocument doc = PDDocument.load(file);


            text = new PDFTextStripper().getText(doc);
            String[] lines = text.split(System.getProperty("line.separator"));

            title = lines[0];
            Logs.debug(title);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return new Page(url, title, text, "", null);
    }
}
