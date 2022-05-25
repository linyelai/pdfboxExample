package main.java.com.linseven.pdf;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.form.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2021/3/1 19:51
 */
public class RemoveWaterMark {

    private PDDocument document;
    private Map<Integer, List<TextView>> textView  = new HashMap<>();
    public int fieldNum = 0;
    private List<PDRectangle> pdRectangleList = new ArrayList<>();

    private List<View> fields = new ArrayList<>();

    public void load(String filePath) throws IOException {
        document = PDDocument.load(new File(filePath));
    }


    public void removeText(int pageIndex,float x,float y) throws IOException {

        PDPage firstPage = (PDPage)document.getDocumentCatalog().getPages().get(pageIndex);
        PDFStreamParser parser = new PDFStreamParser(firstPage);
        parser.parse();
        List tokens = parser.getTokens();
        boolean start = false;
        List<Integer> removeIndex = new ArrayList();
        int k=0;
        for (int j =tokens.size()-1; k<6; j--)
        {
            Object next = tokens.get(j);
            if (next instanceof Operator)
            {
                Operator op = (Operator) next;
                // Tj and TJ are the two operators that display strings in a PDF
            }

            if(next instanceof Operator &&  ((Operator) next).getName().equals("Tj")){
                removeIndex.add(j);
                removeIndex.add(j-1);
                k++;
                continue;
            }


        }
        List newToken = new ArrayList();
        for(int i=0;i<tokens.size();i++){
            if(removeIndex.contains(i)){
                continue;
            }
            newToken.add(tokens.get(i));
        }
        // now that the tokens are updated we will replace the page content stream.
        PDStream updatedStream = new PDStream(document);
        OutputStream out = updatedStream.createOutputStream();
        ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
        tokenWriter.writeTokens(newToken);
        out.close();
        firstPage.setContents(updatedStream);

    }


    public void updateText(int pageIndex,float x,float y,String content,String targetPath) throws IOException {

         removeText(pageIndex,x,y);


    }
    public void save(String targetPath) throws IOException {
        document.save(targetPath); //Output file name
        document.close();
    }

    public void getFormField(int pageIndex) throws IOException {
        PDDocumentCatalog pdDocumentCatalog = document.getDocumentCatalog();
        PDAcroForm pdAcroForm = pdDocumentCatalog.getAcroForm();
        List<PDField> fieldList = pdAcroForm.getFields();
        for(PDField f:fieldList){
            listField(f);
        }
      /*  COSDictionary cosDictionary = pdAcroForm.getCOSObject();
        COSArray fields = (COSArray)cosDictionary.getItem("Fields");
        Iterator<COSBase> iterator = fields.iterator();
        while(iterator.hasNext()){

            COSObject field = (COSObject)iterator.next();
            getForm((COSDictionary) field.getObject());
        }*/
        if(fields !=null &&fields.size()>0){
            for(View field:fields){
                if(field instanceof  CheckBox){
                    CheckBox checkBox = (CheckBox)field ;
                    System.out.print("type:"+checkBox.getType());
                    System.out.print(",x:"+checkBox.getX());
                    System.out.print(",y:"+checkBox.getY());
                    System.out.print(",width:"+checkBox.getWidth());
                    System.out.print(",height:"+checkBox.getHeight());
                    System.out.print(",pageNo:"+checkBox.getPageNo());
                    System.out.print(",color:"+checkBox.getColor());
                    System.out.println(",check:"+checkBox.isCheck());
                }else if(field instanceof  TextField){
                    TextField textField = (TextField)field ;
                    System.out.print("type:"+textField.getType());
                    System.out.print(",x:"+textField.getX());
                    System.out.print(",y:"+textField.getY());
                    System.out.print(",width:"+textField.getWidth());
                    System.out.print(",height:"+textField.getHeight());
                    System.out.print(",pageNo:"+textField.getPageNo());
                    System.out.print(",color:"+textField.getColor());
                    System.out.println(",text:"+textField.getText());
                } else if(field instanceof  Radio){
                    Radio radio = (Radio)field ;
                    System.out.print("type:"+radio.getType());
                    System.out.print(",x:"+radio.getX());
                    System.out.print(",y:"+radio.getY());
                    System.out.print(",width:"+radio.getWidth());
                    System.out.print(",height:"+radio.getHeight());
                    System.out.print(",pageNo:"+radio.getPageNo());
                    System.out.print(",color:"+radio.getColor());
                    System.out.println(",value:"+radio.getSelectValue());
                }
            }
        }


    }
    public void listField(PDField pdField) throws IOException {

        if(pdField instanceof PDNonTerminalField){
            PDNonTerminalField pdNonTerminalField = (PDNonTerminalField)pdField;
            List<PDField>  pdFields = pdNonTerminalField.getChildren();
            for(PDField f:pdFields){
                listField(f);

            }

        }else{
            List<PDAnnotationWidget> widgets = pdField.getWidgets();
            PDAnnotationWidget widget  = widgets.get(0);
            PDPage pdPage = widget.getPage();
            int page = document.getPages().indexOf(pdPage);
            PDRectangle pdRectangle = widget.getRectangle();
            float x = pdRectangle.getLowerLeftX();
            float y = pdRectangle.getLowerLeftY();
            float width = pdRectangle.getUpperRightX()-x;
            float height = pdRectangle.getUpperRightY()-y;
            if(pdField instanceof PDCheckBox){
                PDCheckBox  checkBox  = (PDCheckBox)pdField;
                CheckBox field = new CheckBox();
                field.setCheck(checkBox.isChecked());
                field.setX(x);
                field.setY(y);
                field.setType("CheckBox");
                field.setPageNo(page);
                field.setWidth(width);
                field.setHeight(height);

                fields.add(field);
            }else if(pdField instanceof PDTextField){
                PDTextField pdTextField = (PDTextField)pdField;
                COSDictionary cosObject11 = pdTextField.getCOSObject();
                COSBase fontInfo = cosObject11.getItem(COSName.DA);

                if(widget.getColor()!=null) {
                    System.out.println(widget.getColor().toString());
                }
                pdTextField.setValue("hello");
                TextField field = new TextField();
                field.setX(x);
                field.setY(y);
                field.setHeight(height);
                field.setWidth(width);
                field.setPageNo(page);
                field.setText(pdTextField.getValue());
                field.setType("TextField");
                if(fontInfo != null && fontInfo instanceof COSString){
                    COSString fontStr = (COSString) fontInfo;
                    setFont(fontStr.getString(),field);
                }
                fields.add(field);


            }else if(pdField instanceof  PDRadioButton){
                PDRadioButton pdRadioButton = (PDRadioButton)pdField;
                Radio  field = new Radio();
                field.setX(x);
                field.setY(y);
                field.setHeight(height);
                field.setWidth(width);
                field.setPageNo(page);
                field.setSelectValue(pdRadioButton.getValue());
                field.setSelectIndex(pdRadioButton.getSelectedIndex());
                field.setType("Radio");
                fields.add(field);

            }

        }
    }
    /*public void getForm(COSDictionary cosDictionary){

        COSBase kids = cosDictionary.getItem("Kids");
        if(kids !=null && kids instanceof COSArray){
            COSArray cosArray = (COSArray)kids;
            Iterator<COSBase> iterator = cosArray.iterator();
            while(iterator.hasNext()){
                COSBase cosBase = iterator.next();
                if(cosBase instanceof  COSObject){
                    COSName cosName = COSName.RECT;
                    COSArray cosArray1 = (COSArray)((COSObject) cosBase).getItem(cosName);
                    if(cosArray1!=null){
                        PDRectangle pdRectangle = new PDRectangle(cosArray1);
                        pdRectangleList.add(pdRectangle);
                        fieldNum++;
                       // System.out.println("leftX:"+pdRectangle.getLowerLeftX()+"leftY:"+pdRectangle.getLowerLeftY()+"rightX:"+pdRectangle.getUpperRightX()+"rigthY:"+pdRectangle.getUpperRightY());
                    }
                    getForm((COSDictionary)((COSObject) cosBase).getObject());
                    COSString type = (COSString)((COSObject) cosBase).getItem(COSName.T);
                   // System.out.println(type.getString());
                }else {

                }
            }
        }
    }*/

    public void setFont(String fontStr,Text field){
        if(fontStr == null || fontStr.trim().equals("")){
            return;
        }
        String [] fontItems = fontStr.split(" ");
        for(int i=0;i<fontItems.length;i++  ){
            String fontItem = fontItems[i];
            if(i == 0){
                field.setFont(fontItem);
                continue;
            }
            if(fontItem.equals("Tf")){
                String fontSize = fontItems[i-1];
                field.setFontSize(Float.valueOf(fontSize));
            }else  if(fontItem.toLowerCase().equals("rg")){
                float red = Float.valueOf(fontItems[i-3]);
                float green = Float.valueOf(fontItems[i-2]);
                float blue = Float.valueOf(fontItems[i-1]);
                int redColor = (int)(red*255);
                int greenColor = (int)(green*255);
                int blueColor = (int)(blue*255);
                String redStr = Integer.toHexString(redColor);
                if(redStr.equals("0")){
                    redStr = "00";
                }
                String greenStr = Integer.toHexString(greenColor);
                if(greenStr.equals("0")){
                    greenStr = "00";
                }
                String blueStr = Integer.toHexString(blueColor);
                if(blueStr.equals("0")){
                    blueStr = "00";
                }
                String rgb = redStr+greenStr+blueStr;
                field.setColor(rgb);

            }
        }
    }

    public void draw() throws IOException {

        PDPage page = document.getPage(0);
        PDPageContentStream content = new PDPageContentStream(document, page,PDPageContentStream.AppendMode.APPEND,false);
        for(PDRectangle pdRectangle:pdRectangleList) {
            float x = pdRectangle.getLowerLeftX();
            float y = pdRectangle.getLowerLeftY();
            float width = pdRectangle.getUpperRightX() - x;
            float height = pdRectangle.getUpperRightY() - y;
            content.setNonStrokingColor(Color.DARK_GRAY);
            content.addRect(x, y, width, height);
        }
        content.fill();

        content.close();
        document.save("d:/test123.pdf");
        document.close();
    }
    public static void main(String[] args) throws IOException {

        RemoveWaterMark pdfEditor = new RemoveWaterMark();
        pdfEditor.load("d:/pdf/ppt2pdf.pdf");
        for(int i=0;i<1;i++){
            pdfEditor.updateText(i, 218.108f, 1369.264f, "i am so sad", "d:/pdf/remove-water-update.pdf");
        }
        pdfEditor.save("d:/pdf/remove-water-update1.pdf");
        System.out.println(pdfEditor.fieldNum);

    }
}
