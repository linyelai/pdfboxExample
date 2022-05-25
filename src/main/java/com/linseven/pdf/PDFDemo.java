package main.java.com.linseven.pdf;

import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.awt.image.RenderedImage;
import java.io.*;
import java.util.*;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2020/12/21 17:48
 */
public class PDFDemo {
    public static void main(String[] args) throws IOException {


       // getObjectFromDoc();
         stripText();
        //getFont();
        //printContent();
    }

    public static void printContent() throws IOException {

        File file = new File("D:/SK03 P2.pdf");
        FileInputStream inputStream = new FileInputStream(file);
        int len = 0 ;
        byte [] buff = new byte[1024];
        while((len = inputStream.read(buff))>0){
            System.out.println(new String(buff,0,len));
        }
        PDDocument document = PDDocument.load(file);
        document.getDocumentCatalog().getAcroForm();
    }

 public static void getObjectFromDoc()throws IOException {
        File file = new File("D:/pdf/ppt2pdf.pdf");
        PDDocument document = PDDocument.load(file);
        PDPage pdPage = document.getPage(0);
        float pageHeight = pdPage.getBBox().getHeight();

        COSDictionary cosDictionary = pdPage.getCOSObject();
      //  print(cosDictionary);
        PDResources resources = pdPage.getResources();
     Iterator<PDStream> iterator =  pdPage.getContentStreams();
     InputStream inputStream = pdPage.getContents();
     int len = 0 ;
     byte [] buff = new byte[1024];
     StringBuffer stringBuffer = new StringBuffer();
     while((len=inputStream.read(buff)) >0){
         stringBuffer.append(new String(buff));
     }
     //pdPage.
     System.out.println(stringBuffer.toString());
     while(iterator.hasNext()){
        PDStream pdStream =  iterator.next();
     }

         List<RenderedImage>  list = getImagesFromResources(resources);
         System.out.println(list);
    }

    public static void getFont() throws IOException {
        File file = new File("D:/fw2.pdf");
        PDDocument document = PDDocument.load(file);
        PDPage pdPage = document.getPage(0);

        Iterable<COSName> fontNames =  pdPage.getResources().getFontNames();
       for(COSName cosName:fontNames){
           System.out.println(cosName.getName());
          PDFont font =  pdPage.getResources().getFont(cosName);
          String fontname = font.getName();
           System.out.println(fontname);
           byte [] data = font.encode("h");
           ByteArrayInputStream in = new ByteArrayInputStream(data);
           //in.read(data);
           int code = font.readCode(in);
           String subType = font.getSubType();
           System.out.println(new String(data)+subType);
           System.out.println(new String(data));
       }

    }


    private static List<RenderedImage> getImagesFromResources(PDResources resources) throws IOException {



        List<RenderedImage> images = new ArrayList<>();
        for (COSName xObjectName : resources.getXObjectNames()) {
            PDXObject xObject = resources.getXObject(xObjectName);
            if (xObject instanceof PDImageXObject) {
                images.add(((PDImageXObject) xObject).getImage());
            } else if (xObject instanceof PDFormXObject) {
                images.addAll(getImagesFromResources(((PDFormXObject) xObject).getResources()));
            }
        }
        //ProcSet procSet = null;
        COSName cosName = COSName.ACTUAL_TEXT;
        Set<Map.Entry<COSName, COSBase>> entries = resources.getCOSObject().entrySet();
        for(Map.Entry<COSName, COSBase> entry:entries){
            System.out.println(entry.getKey().getName());
            if("ProcSet".equals(entry.getKey().getName())){
                COSBase cosBase = entry.getValue();
                if(cosBase instanceof  COSArray){
                    COSArray cosArray = (COSArray)cosBase;
                    for(COSBase i:cosArray){
                        System.out.println(i.toString());
                    }
                }
            }
        }
        return images;
    }

    public static  void print(COSDictionary cosDictionary){

        Set<Map.Entry<COSName, COSBase>> entrySet = cosDictionary.entrySet();
        Iterator<Map.Entry<COSName, COSBase>> iterator = entrySet.iterator();
        while(iterator.hasNext()){
            Map.Entry<COSName, COSBase> entry = iterator.next();
            COSName cosName = entry.getKey();
            COSBase cosBase = entry.getValue();
            if(cosBase instanceof COSDictionary){
                System.out.println(cosName.getName());
                print((COSDictionary)cosBase);
            }else{
                System.out.println(cosName.getName());

            }

        }

    }

    public  static void stripText() throws IOException {

        File file = new File("D:\\SK03 P2.pdf");
        PDDocument document = PDDocument.load(file);
        float y = document.getPage(0).getBBox().getHeight()-16-682.74f;
        System.out.println("page height"+document.getPage(0).getBBox().getHeight()+"y:"+y);
         List<TextPosition> positions ;
        PDFTextStripper stripper = new PDFTextStripper()
        {
            private int currentPage = 0 ;
            @Override
            protected void startPage(PDPage page) throws IOException
            {
                startOfLine = true;
                super.startPage(page);

            }

            @Override
            protected void writeLineSeparator() throws IOException
            {
                startOfLine = true;
                super.writeLineSeparator();
            }

            @Override
            protected void writeString(String text, List<TextPosition> textPositions) throws IOException
            {
                if (startOfLine)
                {

                    TextPosition textPosition = textPositions.get(0);

                    writeString(String.format("[x:%s,y:%s,endx:%s,endy:%s]", textPosition.getX(),textPosition.getY(),textPosition.getEndX(), textPosition.getEndY(), text));
                    startOfLine = false;
                }
                super.writeString(text, textPositions);
                //positions = textPositions;

            }

            boolean startOfLine = true;
        };

        String text = stripper.getText(document);
        stripper.getGraphicsState().getTextState().getFont();
        System.out.println(text);

    }
}
