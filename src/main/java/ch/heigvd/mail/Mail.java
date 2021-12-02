package ch.heigvd.mail;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Implementation of a Mail. It will run the prank campaign.
 *
 * @author Anthony Coke
 * @author Francesco Monti
 */
@Accessors(prefix = {"_"})
@Getter @Setter
public class Mail {
    private String _from;
    private List<String> _to;
    private List<String> _cc;
    private String _subject;
    private String _body;

    /**
     * Constructor with parameters, it calls the parseMessage.
     * @param message the message with its subject and body.
     */
    public Mail(String message) {
        parseMessage(message);
    }

    /**
     * Retrieves the subject and the body of the message to be sent.
     * @param message
     */
    private void parseMessage(String message) {
        String[] strs = message.split("\n");
        if(strs[0].contains("Subject:")) {
            _subject = strs[0];
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < strs.length; ++i) {
            sb.append(strs[i]).append("\n");
        }
        _body = sb.toString();
    }
}
