package main.java.com.linseven.pdf;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2020/12/23 15:42
 */
public class ItextPDFUtil {
    public static final float defaultHeight = 16;
    public static final float fixHeight = 10;




    /**
     * @description: 查找插入签名图片的最终位置，因为是插入签名图片，所以之前的关键字应只会出现一次
     * 这里直接使用了第一次查找到关键字的位置，并返回该关键字之后的坐标位置
     * @return: float[0]:页码，float[1]:最后一个字的x坐标，float[2]:最后一个字的y坐标
     */
    public static float[] getAddImagePositionXY(String pdfName, String keyword) throws IOException {
        float[] temp = new float[3];
        List<float[]> positions = findKeywordPostions(pdfName, keyword);
        temp[0] = positions.get(0)[0];
        temp[1] = positions.get(0)[1] + positions.get(0)[3];
        temp[2] = positions.get(0)[2] - (positions.get(0)[3] / 2);

        return temp;
    }

    /**
     * findKeywordPostions
     *  返回查找到关键字的首个文字的左上角坐标值
     *
     * @param pdfName
     * @param keyword
     * @return List<float [ ]> : float[0]:pageNum float[1]:x float[2]:y
     * @throws IOException
     */
    public static List<float[]> findKeywordPostions(String pdfName, String keyword) throws IOException {
        File pdfFile = new File(pdfName);
        byte[] pdfData = new byte[(int) pdfFile.length()];
        FileInputStream inputStream = new FileInputStream(pdfFile);
        //从输入流中读取pdfData.length个字节到字节数组中，返回读入缓冲区的总字节数，若到达文件末尾，则返回-1
        inputStream.read(pdfData);
        inputStream.close();

        List<float[]> result = new ArrayList<>();
        List<PdfPageContentPositions> pdfPageContentPositions = getPdfContentPostionsList(pdfData);


        for (PdfPageContentPositions pdfPageContentPosition : pdfPageContentPositions) {
            List<float[]> charPositions = findPositions(keyword, pdfPageContentPosition);
            if (charPositions == null || charPositions.size() < 1) {
                continue;
            }
            result.addAll(charPositions);
        }
        return result;
    }



    /**
     * findKeywordPostions
     *
     * @param pdfData 通过IO流 PDF文件转化的byte数组
     * @param keyword 关键字
     * @return List<float [ ]> : float[0]:pageNum float[1]:x float[2]:y
     * @throws IOException
     */
    public static List<float[]> findKeywordPostions(byte[] pdfData, String keyword) throws IOException {
        List<float[]> result = new ArrayList<float[]>();
        List<PdfPageContentPositions> pdfPageContentPositions = getPdfContentPostionsList(pdfData);


        for (PdfPageContentPositions pdfPageContentPosition : pdfPageContentPositions) {
            List<float[]> charPositions = findPositions(keyword, pdfPageContentPosition);
            if (charPositions == null || charPositions.size() < 1) {
                continue;
            }
            result.addAll(charPositions);
        }
        return result;
    }


    private static List<PdfPageContentPositions> getPdfContentPostionsList(byte[] pdfData) throws IOException {
        PdfReader reader = new PdfReader(pdfData);

        List<PdfPageContentPositions> result = new ArrayList<PdfPageContentPositions>();

        int pages = reader.getNumberOfPages();
        for (int pageNum = 1; pageNum <= pages; pageNum++) {
            float width = reader.getPageSize(pageNum).getWidth();
            float height = reader.getPageSize(pageNum).getHeight();


            PdfRenderListener pdfRenderListener = new PdfRenderListener(pageNum, width, height);


            //解析pdf，定位位置
            PdfContentStreamProcessor processor = new PdfContentStreamProcessor(pdfRenderListener);
            PdfDictionary pageDic = reader.getPageN(pageNum);
            PdfDictionary resourcesDic = pageDic.getAsDict(PdfName.RESOURCES);
            try {
                processor.processContent(ContentByteUtils.getContentBytesForPage(reader, pageNum), resourcesDic);
            } catch (IOException e) {
                reader.close();
                throw e;
            }


            List<String> content = pdfRenderListener.getContent();
            List<CharPosition> charPositions = pdfRenderListener.getcharPositions();


            List<float[]> positionsList = new ArrayList<float[]>();
            for (CharPosition charPosition : charPositions) {
                float[] positions = new float[]{charPosition.getPageNum(), charPosition.getX(), charPosition.getY(), charPosition.getWidth(), charPosition.getHeight()};
                positionsList.add(positions);
            }


            PdfPageContentPositions pdfPageContentPositions = new PdfPageContentPositions();
            pdfPageContentPositions.setContent(content);
            pdfPageContentPositions.setPostions(positionsList);


            result.add(pdfPageContentPositions);
        }
        reader.close();
        return result;
    }


    private static List<float[]> findPositions(String keyword, PdfPageContentPositions pdfPageContentPositions) {


        List<float[]> result = new ArrayList<float[]>();


        List<String> content = pdfPageContentPositions.getContent();
        List<float[]> charPositions = pdfPageContentPositions.getPositions();


        for (int pos = 0; pos < content.size(); pos++) {

            if(keyword.equals(content.get(pos))){
                float[] postions = charPositions.get(pos);
                result.add(postions);
            }

        }
        return result;
    }


    private static class PdfPageContentPositions {
        private List<String> content;
        private List<float[]> positions;


        public List<String> getContent() {
            return content;
        }


        public void setContent(List<String> content) {
            this.content = content;
        }


        public List<float[]> getPositions() {
            return positions;
        }


        public void setPostions(List<float[]> positions) {
            this.positions = positions;
        }
    }


    private static class PdfRenderListener implements RenderListener {
        private int pageNum;
        private float pageWidth;
        private float pageHeight;
        private StringBuilder contentBuilder = new StringBuilder();
        List<String> textList = new ArrayList<>();
        private List<CharPosition> charPositions = new ArrayList<CharPosition>();


        public PdfRenderListener(int pageNum, float pageWidth, float pageHeight) {
            this.pageNum = pageNum;
            this.pageWidth = pageWidth;
            this.pageHeight = pageHeight;
        }


        public void beginTextBlock() {
        }


        public void renderText(TextRenderInfo renderInfo) {
            List<TextRenderInfo> characterRenderInfos = renderInfo.getCharacterRenderInfos();
            StringBuffer text = new StringBuffer();
            for (int i=0;i<characterRenderInfos.size();i++) {
                TextRenderInfo textRenderInfo = characterRenderInfos.get(i);
                String word = textRenderInfo.getText();
                if (word.length() > 1) {
                    word = word.substring(word.length() - 1, word.length());
                }
                Rectangle2D.Float rectangle = textRenderInfo.getAscentLine().getBoundingRectange();

                float x = (float) rectangle.getX();
                float y = (float) rectangle.getY();
//                float x = (float)rectangle.getCenterX();
//                float y = (float)rectangle.getCenterY();
//                double x = rectangle.getMinX();
//                double y = rectangle.getMaxY();


                //这两个是关键字在所在页面的XY轴的百分比
                float xPercent = Math.round(x / pageWidth * 10000) / 10000f;
                float yPercent = Math.round((1 - y / pageHeight) * 10000) / 10000f;


//                CharPosition charPosition = new CharPosition(pageNum, xPercent, yPercent);
                if(i==0){
                CharPosition charPosition = new CharPosition(pageNum, x, y - fixHeight, (float) rectangle.getWidth(), (float) (rectangle.getHeight() == 0 ? defaultHeight : rectangle.getHeight()));
                charPositions.add(charPosition);
                }
                text.append(word);
            }
            textList.add(text.toString());
        }


        public void endTextBlock() {
        }


        public void renderImage(ImageRenderInfo renderInfo) {
        }


        public List<String> getContent() {
            return textList;
        }


        public List<CharPosition> getcharPositions() {
            return charPositions;
        }
    }


    private static class CharPosition {
        private int pageNum = 0;
        private float x = 0;
        private float y = 0;
        private float width;
        private float height;


        public CharPosition(int pageNum, float x, float y, float width, float height) {
            this.pageNum = pageNum;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }


        public int getPageNum() {
            return pageNum;
        }


        public float getX() {
            return x;
        }


        public float getY() {
            return y;
        }

        public float getWidth() {
            return width;
        }

        public float getHeight() {
            return height;
        }

        @Override
        public String toString() {
            return "CharPosition{" +
                    "pageNum=" + pageNum +
                    ", x=" + x +
                    ", y=" + y +
                    ", width=" + width +
                    ", height=" + height +
                    '}';
        }
    }
}
