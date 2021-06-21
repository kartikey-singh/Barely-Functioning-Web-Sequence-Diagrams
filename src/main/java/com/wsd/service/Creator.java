package com.wsd.service;

import com.wsd.helper.DimensionAnalyser;
import com.wsd.model.Graph;
import com.wsd.model.SequenceState;
import com.wsd.request.CreationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class Creator {
    private static final Logger LOG = LoggerFactory.getLogger(Creator.class);

    public byte[] processRequest(CreationRequest creationRequest) throws IOException {
        List<SequenceState> sequenceStates = creationRequest.getSequence();
        Graph graph = new Graph(sequenceStates);
        LOG.info("Graph Created");
        DimensionAnalyser dimensionAnalyser = new DimensionAnalyser(graph);
        dimensionAnalyser.estimate();
        LOG.info("Estimation Done");
        VisualiserG2D visualiserG2D = new VisualiserG2D(graph);
        visualiserG2D.draw();
        LOG.info("Visualisation Done");
        return visualiserG2D.getImageByteArray();
    }
}
