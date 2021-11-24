package ch.heig.prank;

import ch.heig.mail.Group;

public class Prank {
    private final String message;
    private final Group group;

    public Prank(String message, Group group) {
        this.message = message;
        this.group = group;
    }

    public String getMessage() {
        return message;
    }

    public Group getGroup() {
        return group;
    }
}
