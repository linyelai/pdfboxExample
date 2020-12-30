package com.linseven.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2020/11/20 15:47
 */
public class Application {
    public static void main(String[] args) throws IOException {

        PDDocument document = new PDDocument();

        PDPage my_page = new PDPage();
        float width = my_page.getMediaBox().getWidth();
        PDPageContentStream contentStream = new PDPageContentStream(document, my_page);
        contentStream.beginText();
        contentStream.newLineAtOffset(25, 700);
        contentStream.setFont( PDType1Font.TIMES_ROMAN, 12.0f );
        contentStream.setLeading(14.5f);
        contentStream.showText("h");
        contentStream.close();


        document.addPage(my_page);
        /*PDPage secondPage = new PDPage();
        document.addPage(secondPage);*/
        /*PDPageContentStream lineContentStream = new PDPageContentStream(document, secondPage);
        lineContentStream.setStrokingColor(Color.BLACK);
        lineContentStream.setStrokingColor(1,1,1);
        lineContentStream.moveTo(0,0);
        lineContentStream.lineTo(700,700);
        lineContentStream.lineTo(0,0);
        lineContentStream.stroke();
        PDImageXObject pdImage = PDImageXObject.createFromFile("D:/images/cocosign-logo.png",document);
        lineContentStream.drawImage(pdImage,200,200,200,200);
        lineContentStream.close();
*/

        PDDocumentInformation pdd = document.getDocumentInformation();
        //Setting the author of the document
        pdd.setAuthor("Tyrion");
        // Setting the title of the document
        pdd.setTitle("Sample document");
        //Setting the creator of the document 
        pdd.setCreator("Tyrion");
        //Setting the subject of the document
        pdd.setSubject("Example document");
        //Setting the created date of the document
        Calendar date = new GregorianCalendar();
        date.set(2020, 11, 20);
        pdd.setCreationDate(date);
        //Setting the modified date of the document
        date.set(2020, 11, 20);
        pdd.setModificationDate(date);
        //Setting keywords for the document
        pdd.setKeywords("sample, first example, my pdf");
        //Saving the document
        document.save("d:/my_doc.pdf");
        System.out.println("PDF created");
        //Closing the document
        document.close();

    }


    public void showText(PDPageContentStream contentStream,String text,float fontSize,float containerWidth){

    }
}
