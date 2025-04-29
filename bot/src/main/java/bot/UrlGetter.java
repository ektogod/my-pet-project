package bot;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class UrlGetter {
    public String getUrl() throws IOException {
        Process process = Runtime.getRuntime().exec("C:\\Users\\Nikita\\AppData\\Roaming\\npm\\lt.cmd --port 8080");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String url = reader.readLine();
        return url.substring(url.indexOf(':') + 1);
    }
}

