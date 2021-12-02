/**
 * Authors  : Anthony Coke, Francesco Monti
 * Date     : 2021-11-28
 */
package ch.heigvd.mail;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
/**
 *
 */
public class Mail {
    private String _from;
    private List<String> _to;
    private List<String> _cc;
    private String _subject;
    private String _body;

    /**
     *
     * @param message
     */
    public Mail(String message) {
        parseMessage(message);
    }

    /**
     *
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
