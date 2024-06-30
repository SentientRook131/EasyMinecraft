package com.sen.QuestionnaireCore;

import java.io.Serializable;

public class Choice implements Serializable {
    public final String essential;
    public final String view;
    public static final Choice EMPTY = new Choice("", "");
    public Choice(String essential, String view) {
        this.essential = essential;
        this.view = view;
    }
}
