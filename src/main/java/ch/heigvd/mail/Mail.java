package ch.heigvd.mail;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

@Getter @Setter
public class Mail {
    private String from;
    private List<String> to;
    private List<String> cc;
    private String subject;
    private String body;

    public Mail(String message) {
        parseMessage(message);
    }

    private void parseMessage(String message) {
        String[] strs = message.split("\n");
        if(strs[0].contains("Subject:")) {
            subject = strs[0];
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < strs.length; ++i) {
            sb.append(strs[i]).append("\n");
        }
        body = sb.toString();
    }
}
