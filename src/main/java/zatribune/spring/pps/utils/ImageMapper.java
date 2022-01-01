package zatribune.spring.pps.utils;


import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


/**
 * user.home  :User home directory
 * user.dir   :User's current working directory
 **/
@Component
public class ImageMapper {

    public byte[] getImageUnWrapped(String product) {
        byte[] temp = new byte[0];
        try {
            System.out.println(new File(System.getProperty("user.dir")
                    +PropertiesExtractor.FILE_SERVER_PATH
                    +product));
            BufferedImage bImage = ImageIO.read(
                    new File(System.getProperty("user.dir")
                            + PropertiesExtractor.FILE_SERVER_PATH
                             + product));

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "png", bos);
            temp = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }

   public void saveFile(String uploadDir, String fileName,
                  MultipartFile multipartFile) throws IOException {

       File file = new File(System.getProperty("user.dir")
               + PropertiesExtractor.FILE_SERVER_PATH
               + fileName);

       if (!file.exists()) {
           multipartFile.transferTo(file);
       }else {
           throw new IOException("File already exists with the name: " + fileName);
       }
   }
}
