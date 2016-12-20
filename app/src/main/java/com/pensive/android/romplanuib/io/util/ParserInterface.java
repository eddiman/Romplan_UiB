package com.pensive.android.romplanuib.io.util;

import com.pensive.android.romplanuib.models.CalActivity;

import org.jsoup.nodes.Node;

import java.util.List;

/**
 * @author Edvard Bjørgen
 * @version 1.0
 */
public interface ParserInterface {


    /**
     * Initializes different classes, like builders and the document
     */
    public abstract void docToLists();

    /**
     * Recursive parser
     *
     * @param node
     *            the root node of the part of the document to be parsed
     * @param parent
     *            the parent is needed to add the content in the node without
     *            children
     * @param nodeList
     *            the nodeList to add the nodes to
     */
    public abstract List<Node> nodesToList(Node node, Node parent,
                                           List<Node> nodeList);

    public abstract List<Node> getNodeList();

    public abstract List<CalActivity> getCalActivityList();

    public abstract List<String> getDateStringList();

}
