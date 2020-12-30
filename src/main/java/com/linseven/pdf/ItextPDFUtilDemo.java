package main.java.com.linseven.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.pdfcleanup.PdfCleanUpLocation;
import com.itextpdf.text.pdf.pdfcleanup.PdfCleanUpProcessor;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2020/12/23 15:44
 */
public class ItextPDFUtilDemo {
    //输入的模版路径
    public static String inputFilePath = "D:\\faxcover\\01.pdf";
    //插入的文件路径
    public static String imgFilePath = "C:\\Users\\Administrator\\Desktop\\test图.png";
    //输出路径
    public static String out = "d:\\3.pdf";
    public static String keyword = "{username}";//测试标记

    //插入图片的例子
    public static void main(String[] args) throws IOException, DocumentException {
        //查找位置
        float[] position= ItextPDFUtil.getAddImagePositionXY(inputFilePath,keyword);
        //Read file using PdfReader
        PdfReader pdfReader = new PdfReader(inputFilePath);
        System.out.println("x:"+position[1]+" y:"+position[2]);

        //Modify file using PdfReader
        PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(out));

       /* Image image = Image.getInstance(imgFilePath);
        //Fixed Positioning
//        image.scaleToFit(100, 50);
        image.scaleAbsolute(100, 50);
        //Scale to new height and new width of image
        image.setAbsolutePosition(position[1], position[2]);

        System.out.println("pages:"+pdfReader.getNumberOfPages());*/
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI,
                BaseFont.EMBEDDED);
        PdfContentByte content = pdfStamper.getOverContent(1);
        /*List<PdfCleanUpLocation> cleanUpLocations = new ArrayList<PdfCleanUpLocation>();
        Rectangle rectangle = new Rectangle(position[1]-20,position[2]-20, position[1]+100, position[2]+390);
        rectangle.setBorder(0);
        rectangle.setBorderColor(BaseColor.WHITE);
        PdfCleanUpLocation pdfCleanUpLocation = new PdfCleanUpLocation(1,rectangle );
        cleanUpLocations.add(pdfCleanUpLocation);

        PdfCleanUpProcessor cleaner = new PdfCleanUpProcessor(cleanUpLocations, pdfStamper);
        cleaner.cleanUp();*/
        content.moveTo(100,200);
        content.beginText();
        content.setFontAndSize(bf,14);
        content.showText("demo");
        content.showTextAligned(PdfContentByte.ALIGN_CENTER,"demo",100,100,0);
        content.endText();
        pdfStamper.close();

    }

    //修改文字的示例
    public static void main1(String[] args) {
        //1.给定文件
        File pdfFile = new File(inputFilePath);
        File imgFile = new File(imgFilePath);

        //2.定义一个byte数组，长度为文件的长度
        byte[] pdfData = new byte[(int) pdfFile.length()];

        //3.IO流读取文件内容到byte数组
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(pdfFile);
            inputStream.read(pdfData);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }

        //4.指定关键字


        String replace = " 王文博 先生";

        //5.调用方法，给定关键字和文件
        List<float[]> positions = null;
        try {
            positions = ItextPDFUtil.findKeywordPostions(pdfData, keyword);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PdfReader pdfReader = null;
        PdfStamper stamper = null;
        try {
            pdfReader = new PdfReader(pdfData);
            stamper = new PdfStamper(pdfReader, new FileOutputStream(out));

            if (positions != null) {
                for (int i = 0; i < positions.size(); i++) {
                    float[] position = positions.get(i);

                    PdfContentByte canvas = stamper.getOverContent((int) position[0]);
                    //修改背景色
                    canvas.saveState();
                    BaseColor baseColor = new BaseColor(23,71,158);
//                    canvas.setColorFill(BaseColor.WHITE);
                    canvas.setColorFill(baseColor);
                    // canvas.setColorFill(BaseColor.BLUE);
                    // 以左下点为原点，x轴的值，y轴的值，总宽度，总高度：
//                     canvas.rectangle(mode.getX() - 1, mode.getY(),
//                     mode.getWidth() + 2, mode.getHeight());
                    canvas.rectangle(position[1]-1, position[2]-1, position[3]+1, position[4]+4);

                   /* PdfGState gs = new PdfGState();
                    gs.setFillOpacity(0.3f);// 设置透明度为0.8
                    canvas.setGState(gs);*/

                    canvas.fill();
                    canvas.restoreState();
                    //替换关键字
                    canvas.beginText();

                    BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
                    canvas.setFontAndSize(bf, 13);
                    canvas.setTextMatrix(position[1]+3, position[2]);/*修正背景与文本的相对位置*/
                    //下面两行代码再次设置，设设置的字体的颜色
                    BaseColor baseColor1 = new BaseColor(255,255,255);
                    canvas.setColorFill(baseColor1);

                    canvas.showText(replace);
                    canvas.endText();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (stamper != null)
                try {
                    stamper.close();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (pdfReader != null) {
                pdfReader.close();
            }
        }

        //6.返回值类型是  List<float[]> 每个list元素代表一个匹配的位置，分别为 float[0]所在页码  float[1]所在x轴 float[2]所在y轴 float[3]关键字宽度 floatt[4]关键字高度
        System.out.println("total:" + positions.size());
        if (positions != null && positions.size() > 0)

        {
            for (float[] position : positions) {
                System.out.print("pageNum: " + (int) position[0]);
                System.out.print("\tx: " + position[1]);
                System.out.println("\ty: " + position[2]);
                System.out.println("\tw: " + position[3]);
                System.out.println("\th: " + position[4]);
            }
        }
    }

}
