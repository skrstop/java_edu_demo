package edu.jphoebe.demo.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.FileInputStream;

/**
 * @author 蒋时华
 * @date 2023-04-24 18:05:29
 */
public class PdfTest {

    public static void main(String[] args) throws Exception {

        PDDocument doc = PDDocument.load(new FileInputStream("/Users/jphoebe/Downloads/23056.pdf"));
        PDFTextStripper text = new PDFTextStripper();
        String FinalText = text.getText(doc);
        System.out.println(FinalText);
        doc.close();


    }

}
