package com.sen.QuestionnaireCore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Choice implements Serializable {
    public final String essential;
    public final String view;
    public static final Choice EMPTY = new Choice("", "");
    public static final List<Choice> EMPTY_LIST = new ArrayList<>();
    public Choice(String essential, String view) {
        this.essential = essential;
        this.view = view;
    }
}
