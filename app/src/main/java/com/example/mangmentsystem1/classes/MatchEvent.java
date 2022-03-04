package com.example.mangmentsystem1.classes;

import java.io.Serializable;

public class MatchEvent implements Serializable {
    String event;
    String playerName;
    String matchKey;
    String eventKey;
    String time;
    String teamName;


    public MatchEvent(String event, String playerName, String matchKey,String time,String teamName) {
        this.event = event;
        this.playerName = playerName;
        this.matchKey = matchKey;
        this.time=time;
        this.teamName=teamName;
    }


    public MatchEvent() {
    }


    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getMatchKey() {
        return matchKey;
    }

    public void setMatchKey(String matchKey) {
        this.matchKey = matchKey;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
