package zatribune.spring.pps.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * <h3>Notes:</h3>
 * <ul>
 *     <li><code>user.home</code>: User home directory</li>
 *     <li><code>user.dir</code>: User's current working directory</li>
 * </ul>
 **/
@Slf4j
@Component
public class ImageMapper {

    @Value("${server.file-server.location}")
    private String serverPath;

    public byte[] getImageUnWrapped(String product) {
        byte[] temp = new byte[0];
        try {
            System.out.println(new File(System.getProperty("user.dir")
                    + serverPath
                    + product));
            BufferedImage bImage = ImageIO.read(
                    new File(System.getProperty("user.dir")
                            + serverPath
                            + product));

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "png", bos);
            temp = bos.toByteArray();
        } catch (IOException e) {
            log.error("Error while getting image unwrapped", e);
        }
        return temp;
    }

    public void saveFile(String uploadDir,
                         String fileName,
                         MultipartFile multipartFile) throws IOException {

        File file = new File(System.getProperty("user.dir")
                + serverPath
                + fileName);

        if (!file.exists()) {
            multipartFile.transferTo(file);
        } else {
            throw new IOException("File already exists with the name: " + fileName);
        }
    }

    public String generateTimestampedFilename(String fileName) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String formattedDateTime = now.format(formatter);
        return "file_" + formattedDateTime + "_" + fileName; // You can change the file extension as per your requirement
    }
}
