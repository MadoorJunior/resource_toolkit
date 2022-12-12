package com.madoor.resource_toolkit.util;

import com.aspose.slides.Presentation;
import com.aspose.words.Document;
import com.aspose.words.ImportFormatMode;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.madoor.resource_toolkit.config.MinioProperties;
import com.madoor.resource_toolkit.exception.ResourceTypeException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hslf.usermodel.HSLFShape;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFTextShape;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.sl.usermodel.TextShape;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.xml.soap.Text;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DocProcessor {
    private final MinioProperties minioProperties;
    public List<Keyword> getKeywords(MultipartFile file) throws ResourceTypeException, IOException {
        String content = extractText(file);
        String title = file.getOriginalFilename();
        KeyWordComputer<DicAnalysis> kwc = new KeyWordComputer<>(30);
        return kwc.computeArticleTfidf(title, content);
    }
    public List<Keyword> getKeywords(MultipartFile file,String desc) throws ResourceTypeException, IOException {
        String title = file.getOriginalFilename();
        KeyWordComputer<DicAnalysis> kwc = new KeyWordComputer<>(30);
        return kwc.computeArticleTfidf(title, desc);
    }
    public String extractText(MultipartFile file) throws ResourceTypeException, IOException {
        String originalFilename = file.getOriginalFilename();
        ZipSecureFile.setMinInflateRatio(0);
        if (originalFilename.endsWith(".docx")){
            XWPFDocument document = new XWPFDocument(file.getInputStream());//使用XWPF组件XWPFDocument类获取文档内容
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            return extractor.getText();
        } else if (originalFilename.endsWith(".doc")) {
            try{
                HWPFDocument hwpfDocument = new HWPFDocument(file.getInputStream());
                return hwpfDocument.getText().toString();
            }catch (Exception e){
                XWPFDocument document = new XWPFDocument(file.getInputStream());//使用XWPF组件XWPFDocument类获取文档内容
                XWPFWordExtractor extractor = new XWPFWordExtractor(document);
                return extractor.getText();
            }
        } else if (originalFilename.endsWith(".pptx")) {
            XMLSlideShow ppt = new XMLSlideShow(file.getInputStream());
            StringBuilder stringBuilder = new StringBuilder();
            for (XSLFSlide slide : ppt.getSlides()) {
                for (XSLFShape sh : slide.getShapes()) {
                    if (sh instanceof XSLFTextShape) {
                        XSLFTextShape shape = (XSLFTextShape) sh;
                        stringBuilder.append(shape.getText().trim());
                    }
                }
            }
            return stringBuilder.toString();
        } else if (originalFilename.endsWith(".ppt")) {
            HSLFSlideShow ppt = new HSLFSlideShow(file.getInputStream());
            StringBuilder stringBuilder = new StringBuilder();
            for (HSLFSlide slide : ppt.getSlides()) {
                for (HSLFShape shape : slide.getShapes()) {
                    if (shape instanceof TextShape){
                        stringBuilder.append(((HSLFTextShape) shape).getText());
                    }
                }
            }
            return stringBuilder.toString();
        } else if (originalFilename.endsWith(".pdf")) {
            PDDocument document = PDDocument.load(file.getInputStream());
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            document.close();
            return text;
        } else {
            throw new ResourceTypeException("格式错误，请上传docx、pptx、pdf格式文件");
        }
    }
    public void uploadWordPreview(MultipartFile file,String name,Integer id) throws Exception {
        String s = "<License><Data><Products><Product>Aspose.Total for Java</Product><Product>Aspose.Words for Java</Product></Products><EditionType>Enterprise</EditionType><SubscriptionExpiry>20991231</SubscriptionExpiry><LicenseExpiry>20991231</LicenseExpiry><SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber></Data><Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature></License>";
        ByteArrayInputStream licenseStream = new ByteArrayInputStream(s.getBytes());
        License license = new License();
        license.setLicense(licenseStream);

        InputStream inputStream = file.getInputStream();
        Document doc = new Document(inputStream);
        Document document = new Document();
        document.removeAllChildren();
        document.appendDocument(doc, ImportFormatMode.USE_DESTINATION_STYLES);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.save(byteArrayOutputStream, SaveFormat.PDF);
        ByteArrayInputStream is = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        MinioUtil.uploadPreview(minioProperties.getBucket(),name,is);

        is.reset();
        uploadThumbImg(is,id);

        licenseStream.close();
        inputStream.close();
        is.close();
        byteArrayOutputStream.close();
    }
    public void uploadSlidePreview(MultipartFile file,String name,Integer id) throws Exception {
        final String s ="<License>\n" +
                "  <Data>\n" +
                "    <Products>\n" +
                "      <Product>Aspose.Total for Java</Product>      \n" +
                "    </Products>\n" +
                "    <EditionType>Enterprise</EditionType>\n" +
                "    <SubscriptionExpiry>20991231</SubscriptionExpiry>\n" +
                "    <LicenseExpiry>20991231</LicenseExpiry>\n" +
                "    <SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>\n" +
                "  </Data>\n" +
                "  <Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature>\n" +
                "</License>";
        InputStream licenseStream = new ByteArrayInputStream(s.getBytes());
        com.aspose.slides.License license = new com.aspose.slides.License();
        license.setLicense(licenseStream);

        InputStream inputStream = file.getInputStream();
        Presentation presentation = new Presentation(inputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        presentation.save(byteArrayOutputStream,com.aspose.slides.SaveFormat.Pdf);
        ByteArrayInputStream is = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        MinioUtil.uploadPreview(minioProperties.getBucket(),name,is);

        is.reset();
        uploadThumbImg(is,id);

        licenseStream.close();
        inputStream.close();
        is.close();
        byteArrayOutputStream.close();
    }
    public void uploadThumbImg(InputStream inputStream,Integer id) throws Exception {
        PDDocument pdfDocument = PDDocument.load(inputStream);
        PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);

        // 提取的页码
        int pageNumber = 0;
        // 以300 dpi 读取存入 BufferedImage 对象
        int dpi = 30;
        BufferedImage buffImage = pdfRenderer.renderImageWithDPI(pageNumber, dpi, ImageType.RGB);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(buffImage, "png", outputStream);
        InputStream input = new ByteArrayInputStream(outputStream.toByteArray());
        // 上传minio
        MinioUtil.uploadThumbImg(minioProperties.getBucket(), "thumb/cover/"+ id +".png",input);
        // 关闭文档
        pdfDocument.close();
    }
}