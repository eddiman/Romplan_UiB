package com.pensive.android.romplanuib.io;

import com.pensive.android.romplanuib.io.util.CalActivityComparator;
import com.pensive.android.romplanuib.io.util.ParserInterface;
import com.pensive.android.romplanuib.models.CalActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Edvard Bjørgen & Fredrik Heimsæter
 * @version 1.0
 */
public class CalActivityParser implements ParserInterface {


    private String buildingCode;
    private String roomCode;
    private Document document;
    private List<Node> nodes;
    private List<String> dates = new ArrayList<String>(5);

    /**
     *
     * @param url containing activities
     * @param buildingCode the building code for the building where the room is located
     * @param roomCode the room code
     */
    public CalActivityParser(String url, String buildingCode, String roomCode) {
        try{
            this.document = getDocumentFromURL(url);
            this.buildingCode = buildingCode;
            this.roomCode = roomCode;
            nodes = new ArrayList<Node>();
            docToLists();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Fetches the document from the specified URL
     * @param url the url to fetch
     * @return the document
     * @throws IOException
     */
    private Document getDocumentFromURL(String url) throws IOException {
        return Jsoup.connect(url).get();
    }

    /**
     * Short method for converting the document object to convert the document
     * into a list of nodes
     */
    @Override
    public void docToLists() {
        nodesToList(document, null, nodes);
    }

    /**
     * Recursive method for listing all nodes hierarchically below paramnode
     *
     * @param paramnode
     *            The start point of the method, this node and all nodes will be
     *            added to the input list
     * @param parent
     *            Unused
     * @param nodeList
     *            The list to add the nodes to
     * @return
     */
    @Override
    public List<Node> nodesToList(Node paramnode, Node parent,
                                  List<Node> nodeList) {

        List<Node> children = paramnode.childNodes();
        if (!children.isEmpty()) {
            for (Node node : children) {
                nodesToList(node, paramnode, nodeList);
            }
        }

        nodeList.add(paramnode);

        return nodeList;
    }

    /**
     *
     * @return Returns the node containing the calendar table
     */
    private Node getCalendarNode() {
        for (Node node : nodes) {
            if (node instanceof Element
                    && node.attr("class").equals("calendar")) {
                return node;
            }
        }
        return null;
    }

    /**
     *
     * @return Returns a list object containing all the nodes from the document
     */
    @Override
    public List<Node> getNodeList() {
        return nodes;
    }

    /**
     *
     * @return Returns a list of activities containing all the activities found
     *         in the document
     */
    @Override
    public List<CalActivity> getCalActivityList() {

        List<CalActivity> activityList = new ArrayList<>();

        List<Node> rowList = (getCalendarNode().childNode(0).childNodes());
        // TODO
        dates = parseDates(rowList.get(1));

        for (int row = 3; row < rowList.size(); row += 2) {
            List<Node> cells = rowList.get(row).childNodes();

            for (int cellnumber = 2; cellnumber < cells.size(); cellnumber++) {
                Node cell = cells.get(cellnumber);
                List<Node> cellNodes = new ArrayList<>();
                nodesToList(cell, null, cellNodes);

                CalActivity activity = extractCalActivity(cellNodes, cell, cellnumber / 2);

                if (activity != null)
                    activityList.add(activity);
            }
        }

        activityList = sortActivities(activityList);

        return activityList;
    }


    /**
     * Returns a sorted version of the list of activities
     * @param activityList The list to be sorted
     * @return The sorted list
     */
    private List<CalActivity> sortActivities(List<CalActivity> activityList) {
        CalActivityComparator comparator = new CalActivityComparator();

        Collections.sort(activityList, comparator);
        return activityList;
    }

    /**
     * Private method used to locate and store the dates each CalActivity occurs on
     *
     * @param dateRow
     *            The parent of the nodes containing the dates
     * @return An arraylist of strings containing the dates
     */
    private List<String> parseDates(Node dateRow) {
        ArrayList<String> datestrings = new ArrayList<String>();
        List<Node> dates = new ArrayList<Node>(dateRow.childNodes());
        for (int cellnumber = 1; cellnumber < dates.size(); cellnumber++) {
            datestrings.add(dates.get(cellnumber).childNode(0).toString());
        }

        return datestrings;
    }

    /**
     * Internal method for extracting an activity from a list of nodes
     *
     * @param nodes
     *            The list of nodes containing the activity
     * @param activityNode
     *            The parent of the nodes containing the activity
     * @param day
     *            The day of the week this activity is taking place
     * @return
     */
    private CalActivity extractCalActivity(List<Node> nodes, Node activityNode,
                                     int day) {

        String emneString = null;
        String descString = null;
        String timeString = null;

        for (Node currnode : nodes) {

            String classString = currnode.attr("class");

            if (classString.equals("emne"))
                emneString = currnode.childNode(0).toString();
            else if (classString.equals("time"))
                timeString = currnode.childNode(0).toString();
            else if (classString.equals("item_desc"))
                if(currnode.childNodeSize() < 1){
                    descString = "";
                }else{
                    descString = currnode.childNode(0).toString();
                }

            if (descString != null && timeString != null && emneString != null) {
                CalActivity activity = new CalActivity(activityNode, emneString,
                        timeString, descString, buildingCode, roomCode, dates.get(day - 1));

                return activity;
            }
        }
        return null;
    }

    /**
     *
     * @return returns an arraylist of strings containing the dates for the
     *         current week
     */
    @Override
    public List<String> getDateStringList() {
        return dates;
    }


}
