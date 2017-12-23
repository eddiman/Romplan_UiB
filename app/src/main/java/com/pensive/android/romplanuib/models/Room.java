package com.pensive.android.romplanuib.models;

import java.io.Serializable;

/**
 * Class for making a room object
 *
 * @author Gaute Gjerløw Remen, Fredrik Heimsæter
 * @version 2.0
 *
 */
public class Room extends Unit implements Serializable{
    private String roomID;
    private String roomType;
    private int roomSize;
    private String fsRoomId;
    private String mazeMapUrl;


    public Room(String areaID, String buildingID, String roomID, String name, String roomType, int roomSize, String fsRoomId, String mazeMapUrl) {

        super(areaID, buildingID, name);
        this.roomID = roomID;
        this.roomType = roomType;
        this.roomSize = roomSize;
        this.fsRoomId = fsRoomId;
        this.mazeMapUrl = mazeMapUrl;
    }



    public String getRoomID() {
        return roomID;
    }


    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(int roomSize) {
        this.roomSize = roomSize;
    }

    public String getFsRoomId() {
        return fsRoomId;
    }

    public String getMazeMapUrl() {
        return mazeMapUrl;
    }

    @Override
    public String toString() {
        return "Room{" +
                "areaID='" + this.getAreaID() + '\'' +
                ", buildingID='" + this.getBuildingID() + '\'' +
                ", name='" + this.getName() + '\'' +
                "roomID='" + roomID + '\'' +
                ", roomType='" + roomType + '\'' +
                ", roomSize=" + roomSize + '\'' +
                ", buildingAcronym=" + this.getBuildingAcronym() + '\'' +
                ", imageURL=" + this.getImageURL() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;

        if (roomSize != room.roomSize) return false;
        if (!roomID.equals(room.roomID)) return false;
        return roomType != null ? roomType.equals(room.roomType) : room.roomType == null;

    }

    @Override
    public int hashCode() {
        int result = roomID.hashCode();
        result = 31 * result + (roomType != null ? roomType.hashCode() : 0);
        result = 31 * result + roomSize;
        return result;
    }
}
