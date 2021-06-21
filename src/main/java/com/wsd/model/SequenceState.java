package com.wsd.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SequenceState {
    @NotBlank(message = "Field: \"from\" is blank.")
    @Size(max = 30, message = "Field: \"from\" cannot be more than 30 characters.")
    private String from;

    @NotBlank(message = "Field: \"to\" is blank.")
    @Size(max = 30, message = "Field: \"to\" cannot be more than 30 characters.")
    private String to;

    @NotBlank(message = "Field: \"text\" is blank.")
    @Size(max = 30, message = "Field: \"text\" cannot be more than 30 characters.")
    private String text;

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "SequenceState{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
