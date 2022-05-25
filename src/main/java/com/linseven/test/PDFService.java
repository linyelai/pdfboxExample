package main.java.com.linseven.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDFontFactory;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.encoding.GlyphList;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.*;
import org.apache.pdfbox.pdmodel.interactive.form.*;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.apache.pdfbox.util.Matrix;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.*;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2021/1/4 16:13
 */


public class PDFService {

    private final static String COLOR_PREFIX = "#";
    private final static float THRESHOLD = 5f;
    private final static int MARGIN = 4;
    private static  GlyphList glyphList;
    static {
        String path = "/org/apache/pdfbox/resources/glyphlist/additional.txt";
        InputStream input = GlyphList.class.getResourceAsStream(path);
        try {
            glyphList = new GlyphList(GlyphList.getAdobeGlyphList(), input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void removeText(PDDocument document,int pageNo,float x,float y,float fontSize,PDFont font,float originHeight,String text) throws IOException {

        PDPage pdPage = document.getPage(pageNo);
        PDFStreamParser parser = new PDFStreamParser(pdPage);
        parser.parse();
        List tokens = parser.getTokens();
        boolean start = false;
        List<Integer> removeIndex = new ArrayList();
        float lastx = 0;
        float lasty = 0;
        Matrix cmMatric =  new Matrix(1,0,0,1,0,0);
        Map<String,List<TextPosition>> textPositionMap = new HashMap<>();
        int index = -1;
        boolean isNewLine = true;
        float  lastTextWidth = 0;
        float leading = 0 ;
        PDFont pdFont = null;
        Matrix textMatrix = new Matrix(1,0,0,1,0,0);
        Matrix textLineMatrix = new Matrix(1,0,0,1,0,0);
        for (int j = 0; j < tokens.size(); j++)
        {
            Object next = tokens.get(j);
            if(next instanceof Operator && ((Operator) next).getName().toUpperCase().equals("TL")){
            cmMatric = null;
        }
            if(j==738|| j==35){

            }
            if (next instanceof Operator)
            {
                Operator op = (Operator) next;
                // Tj and TJ are the two operators that display strings in a PDF
                if(op.getName().equals("BT")){
                    start = true;
                    textMatrix = new Matrix(1,0,0,1,0,0);
                    textLineMatrix = new Matrix(1,0,0,1,0,0);
                    continue;
                }
                if(op.getName().equals("ET")){
                    start = false;
                    continue;
                }

            }
            if(start && next instanceof Operator &&  ((Operator) next).getName().equals("Tm")){

                COSFloat af =  null;
                COSFloat bf = null;
                COSFloat cf = null;
                COSFloat df = null;
                COSFloat ef = null;
                COSFloat ff = null;
                COSInteger a1 =  null;
                COSInteger b1 = null;
                COSInteger c1 = null;
                COSInteger d1 = null;
                COSInteger e1 = null;
                COSInteger f1 = null;
                if(tokens.get(j-1) instanceof  COSFloat) {
                    ff = (COSFloat) tokens.get(j - 1);
                }else{
                    f1 = (COSInteger) tokens.get(j - 1);
                }
                if(tokens.get(j-2) instanceof  COSFloat) {
                    ef = (COSFloat) tokens.get(j - 2);
                }else{
                    e1 = (COSInteger) tokens.get(j - 2);
                }
                if(tokens.get(j-3) instanceof  COSFloat) {
                    df = (COSFloat) tokens.get(j - 3);
                }else{
                    d1 = (COSInteger) tokens.get(j - 3);
                }
                if(tokens.get(j-4) instanceof  COSFloat) {
                    cf = (COSFloat) tokens.get(j - 4);
                }else{
                    c1 = (COSInteger) tokens.get(j - 4);
                }
                if(tokens.get(j-5) instanceof  COSFloat) {
                    bf = (COSFloat) tokens.get(j - 5);
                }else{
                    b1 = (COSInteger) tokens.get(j - 5);
                }
                if(tokens.get(j-6) instanceof  COSFloat) {
                    af = (COSFloat) tokens.get(j - 6);
                }else{
                    a1 = (COSInteger) tokens.get(j - 6);
                }
                float a = af==null?a1.floatValue():af.floatValue();
                float b = bf == null?b1.floatValue():bf.floatValue();
                float c = cf == null ?c1.floatValue():cf.floatValue();
                float d = df == null?d1.floatValue():df.floatValue();
                float e = ef == null?e1.floatValue():ef.floatValue();
                float f = ff == null ?f1.floatValue():ff.floatValue();
                if(d==1){
                    f = fontSize*f;
                }else if(d<0){
                    f = -f;
                }
                if(Math.abs(a)>1){
                    fontSize = Math.abs(a);
                }
                textMatrix = new Matrix(1,b,c,1,e,f);
                textLineMatrix = new Matrix(1,b,c,1,e,f);
                isNewLine = true;
                if(cmMatric != null){
                     textMatrix = textMatrix.multiply(cmMatric);
                     lastx = textMatrix.getTranslateX();
                     lasty = textMatrix.getTranslateY();
                }else{
                    lastx = textMatrix.getTranslateX();
                    lasty = textMatrix.getTranslateY();
                }
            } else if(next instanceof Operator && ((Operator) next).getName().equals("Tf")){
                COSName fontName = (COSName)tokens.get(j-2);
                //System.out.println("fontName:{}",fontName);
                pdFont = document.getPage(pageNo).getResources().getFont(fontName);
               if(fontName.getName().contains("C0") ||fontName.getName().contains("TT1") ||fontName.getName().contains("C2")){
                   pdFont = font;
               }

            }else if(start && next instanceof Operator && ((Operator) next).getName().equals("Td")){

                COSFloat yF =  null;
                COSInteger yInteger = null;
                COSFloat xF =  null;
                COSInteger xInteger = null;
                if(tokens.get(j-1) instanceof  COSFloat) {
                    yF = (COSFloat) tokens.get(j - 1);
                }else{
                    yInteger = (COSInteger) tokens.get(j - 1);
                }
                if(tokens.get(j-2) instanceof  COSFloat) {
                    xF = (COSFloat) tokens.get(j - 2);
                }else{
                    xInteger = (COSInteger) tokens.get(j - 2);
                }
                float ty ;
                float tx;
                if(yF !=null) {
                    ty = yF.floatValue();
                }else{
                    ty = yInteger.floatValue();
                }
                if(xF !=null) {
                    tx = xF.floatValue();
                }else{
                    tx = xInteger.floatValue();
                }
                if(Math.abs(textLineMatrix.getTranslateX())>0){
                    tx = tx * fontSize;
                }
                if(Math.abs(textLineMatrix.getTranslateY())>0){
                    ty = ty*fontSize;
                    leading = ty;
                }
                Matrix tdMatric = new Matrix(1,0,0,1,tx,ty);
                textMatrix = textMatrix.multiply(tdMatric);
                lastx = textMatrix.getTranslateX();
                lasty = textMatrix.getTranslateY();
                lastTextWidth =0;
                continue;
            }else if(start && next instanceof Operator && ((Operator) next).getName().equals("TD")){
                isNewLine = true;
                lastTextWidth =0;
                COSFloat yF =  null;
                COSInteger yInteger = null;
                COSFloat xF =  null;
                COSInteger xInteger = null;
                if(tokens.get(j-1) instanceof  COSFloat) {
                    yF = (COSFloat) tokens.get(j - 1);
                }else{
                    yInteger = (COSInteger) tokens.get(j - 1);
                }
                if(tokens.get(j-2) instanceof  COSFloat) {
                    xF = (COSFloat) tokens.get(j - 2);
                }else{
                    xInteger = (COSInteger) tokens.get(j - 2);
                }
                float ty ;
                float tx;
                if(yF !=null) {
                    ty = yF.floatValue();
                }else{
                    ty = yInteger.floatValue();
                }
                if(xF !=null) {
                    tx = xF.floatValue();
                }else{
                    tx = xInteger.floatValue();
                }
                if(Math.abs(textLineMatrix.getTranslateX())>0){
                    tx = tx * fontSize;
                }
                if(Math.abs(textLineMatrix.getTranslateY())>0){
                    ty = ty*fontSize;
                    leading = ty;
                }
                Matrix tdMatric = new Matrix(1,0,0,1,tx,ty);
                textMatrix = textMatrix.multiply(tdMatric);
                lastx = textMatrix.getTranslateX();
                lasty = textMatrix.getTranslateY()-fontSize;
                continue;
            }
            else if( next instanceof Operator && ((Operator) next).getName().equals("cm")){
                isNewLine = true;
                lastTextWidth =0;
                COSFloat af =  null;
                COSFloat bf = null;
                COSFloat cf = null;
                COSFloat df = null;
                COSFloat ef = null;
                COSFloat ff = null;
                COSInteger a1 =  null;
                COSInteger b1 = null;
                COSInteger c1 = null;
                COSInteger d1 = null;
                COSInteger e1 = null;
                COSInteger f1 = null;
                if(tokens.get(j-1) instanceof  COSFloat) {
                    ff = (COSFloat) tokens.get(j - 1);
                }else{
                    f1 = (COSInteger) tokens.get(j - 1);
                }
                if(tokens.get(j-2) instanceof  COSFloat) {
                    ef = (COSFloat) tokens.get(j - 2);
                }else{
                    e1 = (COSInteger) tokens.get(j - 2);
                }
                if(tokens.get(j-3) instanceof  COSFloat) {
                    df = (COSFloat) tokens.get(j - 3);
                }else{
                    d1 = (COSInteger) tokens.get(j - 3);
                }
                if(tokens.get(j-4) instanceof  COSFloat) {
                    cf = (COSFloat) tokens.get(j - 4);
                }else{
                    c1 = (COSInteger) tokens.get(j - 4);
                }
                if(tokens.get(j-5) instanceof  COSFloat) {
                    bf = (COSFloat) tokens.get(j - 5);
                }else{
                    b1 = (COSInteger) tokens.get(j - 5);
                }
                if(tokens.get(j-6) instanceof  COSFloat) {
                    af = (COSFloat) tokens.get(j - 6);
                }else{
                    a1 = (COSInteger) tokens.get(j - 6);
                }
                float a = af==null?a1.floatValue():af.floatValue();
                float b = bf == null?b1.floatValue():bf.floatValue();
                float c = cf == null ?c1.floatValue():cf.floatValue();
                float d = df == null?d1.floatValue():df.floatValue();
                float e = ef == null?e1.floatValue():ef.floatValue();
                float f = ff == null ?f1.floatValue():ff.floatValue();
                Matrix cmMatric1 = new Matrix(1,b,c,1,e,f);
                cmMatric.concatenate(cmMatric1);
                continue;
            }else if(start && next instanceof Operator && ((Operator) next).getName().toUpperCase().equals("T*")){
                Matrix tdMatrix = new Matrix(1,0,0,1,0,- Math.abs(leading));
                textMatrix = textMatrix.multiply(tdMatrix);
                lastx = textMatrix.getTranslateX();
                lasty = textMatrix.getTranslateY();
                isNewLine = true;
                lastTextWidth =0;
            }else if(start && next instanceof Operator && ( ((Operator) next).getName().equals("\'") ||((Operator) next).getName().equals("\"")) ){
                Matrix tdMatrix = new Matrix(1,0,0,1,0,-fontSize*1.1f);
                textMatrix = textMatrix.multiply(tdMatrix);
                lastx = textMatrix.getTranslateX();
                lasty = textMatrix.getTranslateY();

                COSString content = (COSString) tokens.get(j-1);
                String contentStr = content.getString();
                String key = "x:"+lastx+",y:"+lasty;
                if(!isNewLine){
                    lastx = lastx + lastTextWidth;
                    key = "x:"+lastx+",y:"+lasty;
                }
                List<TextPosition> texts = textPositionMap.get(key);
                if(texts == null){
                    texts = new ArrayList<>();
                }
                //log.info("content:{},j:{},x:{},y:{},x:{},y:{}",contentStr,j,lastx,lasty,x,y);
                TextPosition textPosition = new TextPosition();
                textPosition.setContent(contentStr);
                textPosition.setTokenIndex(j);
                 lastTextWidth = getTextWidth(content.getBytes(),pdFont);
                textPosition.setX(lastx);
                textPosition.setY(lasty);
                texts.add(textPosition);
                textPositionMap.put(key,texts);
                isNewLine = true;
                lastTextWidth =0;
            }
            else if(next instanceof Operator && ((Operator) next).getName().equals("Q")){
                cmMatric = new Matrix(1,0,0,1,0,0);
            }

            if(start && next instanceof Operator &&  (((Operator) next).getName().equals("Tj")) /*&& rightPosition*/){
                removeIndex.add(j);
                removeIndex.add(j-1);
                if(pdFont == null){
                    pdFont = font;
                }
                //start = false;
                //log.info("j:{}",j);
                COSString content = (COSString) tokens.get(j-1);
                String contentStr = content.getString();
                String key = "x:"+lastx+",y:"+lasty;
                if(!isNewLine){
                    lastx = lastx + lastTextWidth;
                    key = "x:"+lastx+",y:"+lasty;
                }
                List<TextPosition> texts = textPositionMap.get(key);
                if(texts == null){
                    texts = new ArrayList<>();
                }
                TextPosition textPosition = new TextPosition();
                textPosition.setContent(contentStr);
                textPosition.setTokenIndex(j);
                lastTextWidth = getTextWidth(content.getBytes(),pdFont);
                //获取字符长度

                textPosition.setX(lastx);
                textPosition.setY(lasty);
                texts.add(textPosition);
                textPositionMap.put(key,texts);
                continue;
            } else if(start && next instanceof Operator &&  (((Operator) next).getName().equals("TJ")) /*&& rightPosition*/){
                removeIndex.add(j);
                removeIndex.add(j-1);
                if(pdFont == null){
                    pdFont = font;
                }
                String key = "x:"+lastx+",y:"+lasty;

                if(!isNewLine){
                    lastx = lastx + lastTextWidth;
                    key = "x:"+lastx+",y:"+lasty;
                }
                List<TextPosition> texts = textPositionMap.get(key);
                if(texts == null){
                    texts = new ArrayList<>();
                }
                COSArray previous = (COSArray) tokens.get(j - 1);
                StringBuffer sb = new StringBuffer();
                for (int k = 0; k < previous.size(); k++)
                {
                    Object arrElement = previous.getObject(k);
                    if (arrElement instanceof COSString)
                    {
                        COSString cosString = (COSString) arrElement;
                        String string = cosString.getString();
                        cosString.setValue(string.getBytes("UTF-8"));
                        sb.append(cosString.getString());
                    }
                }
              //  log.info("content:{},j:{}",sb.toString(),j);
                TextPosition textPosition = new TextPosition();
                textPosition.setContent(sb.toString());
                textPosition.setTokenIndex(j);
                textPosition.setX(lastx);
                String contentStr = sb.toString();
                lastTextWidth = getTextWidth(contentStr.getBytes(),pdFont);
                textPosition.setY(lasty);
                texts.add(textPosition);
                textPositionMap.put(key,texts);
                continue;
            }

            if(start){
                continue;
            }

        }


        //
       Set<Map.Entry<String,List<TextPosition>>> entrySet =  textPositionMap.entrySet();
        Iterator<Map.Entry<String,List<TextPosition>>> iterator = entrySet.iterator();
        float xDiff = fontSize;
        float yDiff = fontSize;
        List<Integer> removeIndexList = new ArrayList<>();
        boolean found = false;
        while(iterator.hasNext()){
            Map.Entry<String,List<TextPosition>> entry = iterator.next();
            List<TextPosition> value = entry.getValue();

            if(value.size()>0){
                for(int i=0;i<value.size();i++){
                    TextPosition textPosition = value.get(i);
                    String content = textPosition.getContent();
                    //log.info("content:{},text:{}",content,text);
                    index = textPosition.getTokenIndex();
                    if(Math.abs(x-textPosition.getX())<=fontSize && Math.abs(y-textPosition.getY())<=fontSize){
                        if(content.equals(text)){
                            removeIndexList.clear();
                            removeIndexList.add(index);
                            found = true;
                            break;
                        }else {
                            removeIndexList.add(index);
                        }

                    }
                }
            }
            if(found){
                break;
            }
        }
         //log.info(removeIndexList.toString());
        if(removeIndexList.size()>0) {
          tokens.remove(removeIndexList.get(0)-1);
          tokens.remove(removeIndexList.get(0)-1);
        }else{
            //log.info("view‘s x:{},view's y:{}",x,y);
        }
        // now that the tokens are updated we will replace the page content stream.
        PDStream updatedStream = new PDStream(document);
        OutputStream out = updatedStream.createOutputStream();
        ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
        tokenWriter.writeTokens(tokens);
        out.close();
        pdPage.setContents(updatedStream);
        List<PDAnnotation> annotations = pdPage.getAnnotations();
        float pageHeight = pdPage.getBBox().getHeight();
        if(annotations !=null){
            for(PDAnnotation annotation:annotations){
                PDRectangle rect = annotation.getRectangle();
            }
        }

        return ;
    }



    public float getTextWidth(byte [] bytes,PDFont pdFont )  {

        float width = 0 ;
        String str = new String(bytes);
        System.out.println(str);
        try {
            for(byte b:bytes){
                width+=pdFont.getWidth((int)b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        width = width/1000;
        System.out.println(width);
        return width;
    }


     public static void main(String[] args) throws IOException {

        PDDocument pdDocument = PDDocument.load( new File("D://学习文档//4. 美团点评搜索平台化实践之路-张乐雷.pdf"));
        PDFService pdfService = new PDFService();
        pdfService.removeText(pdDocument,0,0,0,12f,PDType1Font.TIMES_ROMAN,12F,"text");


    }

}
