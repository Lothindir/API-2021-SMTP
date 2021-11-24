package ch.heigvd.mail;

import ch.heigvd.prank.Prank;

public class Mail {
    private final Person sender;
    private final Prank prank;

    public Mail(Person sender, Prank prank) {
        this.sender = sender;
        this.prank = prank;
    }

    public Prank getPrank() {
        return prank;
    }

    public Person getSender() {
        return sender;
    }

}
