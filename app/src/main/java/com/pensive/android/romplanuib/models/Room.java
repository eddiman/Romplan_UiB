package com.pensive.android.romplanuib.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Class for making a room object
 *
 * @author Gaute Gjerløw Remen, Fredrik Heimsæter
 * @version 3.0
 *
 */
public class Room extends Unit implements Serializable{
    @SerializedName("id")
    private String roomID;
    @SerializedName("typeid")
    private String roomType;
    @SerializedName("size")
    private int roomSize;
    @SerializedName("building")
    private String buildingID;

    public Room(String universityID, String areaID, String name) {
        super(universityID, areaID, name);
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

    public String getBuildingID() {
        return buildingID;
    }

    @Override
    public String toString() {
        return "Room{" +
                "universityID='" + this.getUniversityID() + '\'' +
                ", areaID='" + this.getAreaID() + '\'' +
                ", buildingID='" + buildingID + '\'' +
                ", roomID='" + roomID + '\'' +
                ", roomType='" + roomType + '\'' +
                ", name='" + this.getName() + '\'' +
                ", roomSize=" + roomSize +
                ", buildingID='" + buildingID + '\'' +
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
