package com.linseven.pdf;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.Vector;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2020/11/20 15:47
 */
public class Application {

}
  /*  public static void main(String[] args) throws IOException {

        PDDocument document = new PDDocument();

        PDPage my_page = new PDPage();
        float width = my_page.getMediaBox().getWidth();
        PDPageContentStream contentStream = new PDPageContentStream(document, my_page, PDPageContentStream.AppendMode.APPEND,false);
       *//* contentStream.beginText();
        contentStream.newLineAtOffset(25, 700);
        contentStream.setFont( PDType1Font.TIMES_ROMAN, 12.0f );
        contentStream.setLeading(14.5f);
        contentStream.showText("h");*//*
        PDExtendedGraphicsState graphicsState = new PDExtendedGraphicsState();
        // 设置透明度
        graphicsState.setNonStrokingAlphaConstant(0.2f);
        graphicsState.setAlphaSourceFlag(true);

        contentStream.setGraphicsStateParameters(graphicsState);
        contentStream.addRect(0, 0, 200, 100);
        // 设置不划线颜色
        contentStream.setNonStrokingColor(Color.GREEN);
        contentStream.fill();
        contentStream.close();
        PDPageContentStream contentStream1 = new PDPageContentStream(document, my_page,PDPageContentStream.AppendMode.APPEND,false);
        contentStream1.beginText();
        PDExtendedGraphicsState graphicsState1 = new PDExtendedGraphicsState();
        // 设置透明度
        graphicsState1.setAlphaSourceFlag(true);
        graphicsState1.setNonStrokingAlphaConstant(1.0f);
        contentStream1.setGraphicsStateParameters(graphicsState1);
        contentStream1.newLineAtOffset(0, 0);

        contentStream1.setFont( PDType1Font.TIMES_ROMAN, 12.0f );
        contentStream1.setNonStrokingColor(Color.BLACK);
        contentStream1.setLeading(14.5f);
        contentStream1.showText("hfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
        contentStream1.endText();
        contentStream1.close();
        document.addPage(my_page);
        *//*PDPage secondPage = new PDPage();
        document.addPage(secondPage);*//*
        *//*PDPageContentStream lineContentStream = new PDPageContentStream(document, secondPage);
        lineContentStream.setStrokingColor(Color.BLACK);
        lineContentStream.setStrokingColor(1,1,1);
        lineContentStream.moveTo(0,0);
        lineContentStream.lineTo(700,700);
        lineContentStream.lineTo(0,0);
        lineContentStream.stroke();
        PDImageXObject pdImage = PDImageXObject.createFromFile("D:/images/cocosign-logo.png",document);
        lineContentStream.drawImage(pdImage,200,200,200,200);
        lineContentStream.close();
*//*

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



    public void showTextPosition(PDDocument document){


//        PdfContentStreamEditor editor = new PdfContentStreamEditor(document, page) {
//            final StringBuilder recentChars = new StringBuilder();
//            java.util.List<Matrix> positions = new ArrayList<>();
//            java.util.List<Vector> displacements = new ArrayList<>();
//            float pageHeight = page.getMediaBox().getHeight();
//
//            @Override
//            protected void showGlyph(Matrix textRenderingMatrix, PDFont font, int code, Vector displacement)
//                    throws IOException {
//                String string = font.toUnicode(code);
//                if (string != null) {
//                    recentChars.append(string);
//                    positions.add(textRenderingMatrix);
//                    displacements.add(displacement);
//                    super.showGlyph(textRenderingMatrix, font, code, displacement);
//                }
//
//            }
//
//            @Override
//            protected void write(ContentStreamWriter contentStreamWriter, Operator operator, java.util.List<COSBase> operands) throws IOException {
//                String recentText = recentChars.toString();
//                recentChars.setLength(0);
//                String operatorString = operator.getName();
//                if (!positions.isEmpty() && TEXT_SHOWING_OPERATORS.contains(operatorString)) {
//                    PDFView view = getRemoveView(positions, pageHeight, recentText, pageViews);
//                    if (view != null) {
//                        deletedView.add(view);
//                        positions.clear();
//                        displacements.clear();
//                        return;
//                    } else if (operatorString.equals("TJ")) {
//                        COSArray cosArray = computeTjObject(positions, (COSArray) (operands.get(0)), recentText, pageHeight, pageViews, deletedView);
//                        if (cosArray != null) {
//                            java.util.List<COSBase> temp1 = new ArrayList<>();
//                            temp1.add(cosArray);
//                            super.write(contentStreamWriter, operator, temp1);
//                            positions.clear();
//                            displacements.clear();
//                            return;
//                        }
//                    }
//                }
//
//                super.write(contentStreamWriter, operator, operands);
//                positions.clear();
//                displacements.clear();
//
//            }
//
//            final List<String> TEXT_SHOWING_OPERATORS = Arrays.asList("Tj", "'", "\"", "TJ");
//        };


    }
}
*/