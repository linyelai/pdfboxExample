package main.java.com.linseven.pdf;
        import java.io.*;
        import java.util.ArrayList;
        import java.util.Iterator;
        import java.util.List;
        import java.util.Stack;

        import com.itextpdf.text.Font;
        import org.apache.pdfbox.contentstream.operator.Operator;
        import org.apache.pdfbox.cos.*;
        import org.apache.pdfbox.pdfparser.PDFObjectStreamParser;
        import org.apache.pdfbox.pdfparser.PDFStreamParser;
        import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
        import org.apache.pdfbox.pdmodel.PDDocument;
        import org.apache.pdfbox.pdmodel.PDPage;
        import org.apache.pdfbox.pdmodel.PDPageContentStream;
        import org.apache.pdfbox.pdmodel.PDPageTree;
        import org.apache.pdfbox.pdmodel.common.PDStream;
        import org.apache.pdfbox.pdmodel.font.PDFont;
        import org.apache.pdfbox.pdmodel.font.PDFontFactory;
        import org.apache.pdfbox.pdmodel.font.PDType1Font;
        import org.apache.pdfbox.pdmodel.graphics.state.PDGraphicsState;
        import org.apache.pdfbox.pdmodel.graphics.state.PDTextState;
        import org.apache.pdfbox.text.PDFTextStripper;
        import org.apache.pdfbox.util.Matrix;
        import org.apache.pdfbox.util.Vector;


/**
 * http://pdfbox.apache.org/
 *
 * @author fish
 *
 */
public class PDFReader {

    public PDFReader() throws IOException {
        //createHelloPDF();
       // readPDF();
        //editPDF();
    }

    public void removePDFText() throws IOException {
        PDDocument document = PDDocument.load(new File("D:/fw2.pdf"));
        removeText(document,0,206.655f,1350.839f,"d:/remove.pdf");
    }

    public void createHelloPDF() {
        PDDocument doc = null;
        PDPage page = null;

        try {
            doc = new PDDocument();
            page = new PDPage();

            doc.addPage(page);
            PDFont font = PDType1Font.HELVETICA_BOLD;
            PDPageContentStream content = new PDPageContentStream(doc, page);
            content.beginText();
            content.setFont(font, 12);
            content.moveTextPositionByAmount(100, 700);
            content.drawString("Hello");

            content.endText();
            content.close();
            doc.save("D:\\gloomyfish\\pdfwithText.pdf");
            doc.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void readPDF()
    {
        PDDocument helloDocument;
        try {
            helloDocument = PDDocument.load(new File(
                    "D:\\gloomyfish\\pdfwithText.pdf"));
            PDFTextStripper textStripper = new PDFTextStripper();
            System.out.println(textStripper.getText(helloDocument));
            helloDocument.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void editPDF()throws IOException {

        try {
            // pdfwithText
            float x = 200.181f;
            float y = 1286.104f;
            PDDocument helloDocument = PDDocument.load(new File("D:/faxcover/01.pdf"));
            // PDDocument helloDocument = PDDocument.load(new File("D:\\gloomyfish\\hello.pdf"));
            // int pageCount = helloDocument.getNumberOfPages();
            PDPage firstPage = (PDPage)helloDocument.getDocumentCatalog().getPages().get(0);
            // PDPageContentStream content = new PDPageContentStream(helloDocument, firstPage);
            PDStream contents = firstPage.getContentStreams().next();

            PDFStreamParser parser = new PDFStreamParser(contents.getStream());
            parser.parse();
            List tokens = parser.getTokens();
            Stack<PDGraphicsState> graphicsStack = new Stack<PDGraphicsState>();
            graphicsStack.push(new PDGraphicsState(firstPage.getCropBox()));
            PDGraphicsState state = graphicsStack.peek();
            PDTextState textState = state.getTextState();
            List<Point> points = new ArrayList<>();
            // get the current font
            PDFont font = textState.getFont();
            if (font == null)
            {

                font = PDFontFactory.createDefaultFont();
            }
            List result = new ArrayList();
            boolean start = false;
            List<Integer> remove = new ArrayList();

            for (int j = 0; j < tokens.size(); j++)
            {
                Object next = tokens.get(j);
                if (next instanceof Operator)
                {
                    Operator op = (Operator) next;
                    // Tj and TJ are the two operators that display strings in a PDF
                   /* if(op.getName().equals("BT")){
                        start = true;
                        remove.add(j);
                        continue;
                    }
                    if(op.getName().equals("ET")){
                        start = false;
                        remove.add(j);
                        continue;
                    }
                    if(start && op.getName().equals("Td")){

                        //取前面两个
                    }*/
                    if (op.getName().equals("Do")){
                        System.out.println("do..");
                    }

                    if (op.getName().equals("Tj"))
                    {
                        // Tj takes one operator and that is the string
                        // to display so lets update that operator
                        COSString previous = (COSString) tokens.get(j - 1);
                       // previous.setForceHexForm(true);
                        String string =  new String(previous.getString().getBytes("iso8859-1"));
                        //string = string.replaceFirst("username", "ahaaaaaaaaa");
                        //Word you want to change. Currently this code changes word "Solr" to "Solr123"
                        byte [] bytes = previous.getBytes();
                        //bytes = "abcdefghjklmnopstryxzwv".getBytes();
                        InputStream in = new ByteArrayInputStream(bytes);
                        while (in.available() > 0)
                        {
                            // decode a character
                            int before = in.available();
                            int code = font.readCode(in);
                            int codeLength = before - in.available();
                            String unicode = font.toUnicode(code);
                            byte [] abc = font.encode("h");
                            System.out.println(code+":"+unicode);

                        }
                        previous.setValue("(hello world ,hhhhhh)".getBytes());
                    }
                    else if (op.getName().equals("TJ"))
                    {
                        COSArray previous = (COSArray) tokens.get(j - 1);
                        for (int k = 0; k < previous.size(); k++)
                        {
                            Object arrElement = previous.getObject(k);
                            if (arrElement instanceof COSString)
                            {
                                COSString cosString = (COSString) arrElement;
                                String string = cosString.getString();
                                string = string.replaceFirst("Sender", "Hello World, fish");
                                System.out.println(string);
                                // Currently this code changes word "Solr" to "Solr123"
                                cosString.setValue(string.getBytes("UTF-8"));
                            }
                        }
                    }else{
                        result.add(next);
                    }
                }
                if(start){
                    remove.add(j);
                    continue;
                }
            }
            Iterator iterator = tokens.iterator();
            List newToken = new ArrayList();
            for(int i=0;i<tokens.size();i++){

                if(!remove.contains(i)){
                    newToken.add(tokens.get(i));
                }
            }
            // now that the tokens are updated we will replace the page content stream.
            PDStream updatedStream = new PDStream(helloDocument);
            OutputStream out = updatedStream.createOutputStream();
            ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
            tokenWriter.writeTokens(tokens);
            out.close();
            firstPage.setContents(updatedStream);
            helloDocument.save("D:\\11.pdf"); //Output file name
            helloDocument.close();
			/*PDFTextStripper textStripper = new PDFTextStripper();
			System.out.println(textStripper.getText(helloDocument));
			helloDocument.close();*/
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void removeText(PDDocument document ,int pageIndex,float x,float y,String targetPath) throws IOException {

        PDPageTree pdPageTree = document.getDocumentCatalog().getPages();
        for(PDPage firstPage: pdPageTree) {
            PDFStreamParser parser = new PDFStreamParser(firstPage);
            parser.parse();
            List tokens = parser.getTokens();
            for (int j = 0; j < tokens.size(); j++) {
                Object next = tokens.get(j);
                if (next instanceof Operator) {
                    Operator op = (Operator) next;
                    // Tj and TJ are the two operators that display strings in a PDF
                    if (op.getName().equals("Do")) {
                        System.out.println("do");
                    }
                }
                System.out.println(next.toString());

            }


        }
    }

    public static void main(String[] args) throws IOException {
        PDFReader reader = new PDFReader();
        reader.removePDFText();
    }
}