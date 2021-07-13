package zatribune.spring.pps.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@PropertySource(name="customMapProperties", value="classpath:/application.properties")
@Component
public class PropertiesExtractor {
    public static String FILE_SERVER_PATH;


    public PropertiesExtractor(@Value("${server.file-server.location}")String c){
      FILE_SERVER_PATH=c;
    }

}
