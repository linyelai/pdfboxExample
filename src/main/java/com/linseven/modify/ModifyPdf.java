package main.java.com.linseven.modify;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.ProtectionPolicy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2021/6/4 14:57
 */
public class ModifyPdf {

    public static void main(String[] args) throws IOException {

        PDDocument document = PDDocument.load(new File("D:\\360安全浏览器下载\\4fa96f96047b40f0bc640879261ed5aa.pdf"));
        FileOutputStream fileOutputStream = new FileOutputStream("D://modifyReadOnly.pdf");
        document.setAllSecurityToBeRemoved(true);
        document.getCurrentAccessPermission().setCanModify(true);
        boolean isReadOnly = document.getCurrentAccessPermission().isReadOnly();
        document.save(fileOutputStream);

    }
}
