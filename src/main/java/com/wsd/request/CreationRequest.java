package com.wsd.request;

import com.wsd.model.SequenceState;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

public class CreationRequest {
    @Valid
    @NotEmpty(message = "Field: \"sequence\" is empty.")
    @Size(max = 30, message = "Field: \"sequence\" cannot have more than 30 items.")
    private List<SequenceState> sequence;

    public List<SequenceState> getSequence() {
        return sequence;
    }

    @Override
    public String toString() {
        return "CreationRequest{" +
                "sequence=" + sequence +
                '}';
    }
}
