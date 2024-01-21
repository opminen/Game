package main;

import java.awt.*;

public class EventRect extends Rectangle {
    boolean eventDone = false;
    int eventRectDefaultX, eventRectDefaultY;

    public EventRect(int x, int y, int width, int height) {
        super(x, y, width, height);
    }
}
