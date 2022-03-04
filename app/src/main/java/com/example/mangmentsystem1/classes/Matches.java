package com.example.mangmentsystem1.classes;

import java.io.Serializable;

public class Matches implements Serializable {
    private String team1name;
    private String team2name;
    private String team1Key;
    private String team2Key;
    private String team1uri;
    private String team2uri;
    private String date;
    private String time;
    private String Key;
    private String team1goals;
    private String team2goals;
    private String team1redCards;
    private String team2redCards;
    private String team1yellowCards;
    private String team2yellowCards;


   public Matches(){};

    public Matches(String team1name, String team2name, String team1Key, String team2Key, String team1uri, String team2uri, String date,String time) {
        this.team1name = team1name;
        this.team2name = team2name;
        this.team1Key = team1Key;
        this.team2Key = team2Key;
        this.team1uri = team1uri;
        this.team2uri = team2uri;
        this.date = date;
        this.time = time;
        this.team1goals="0";
        this.team2goals="0";
        this.team1redCards="0";
        this.team2redCards="0";
        this.team1yellowCards="0";
        this.team2yellowCards="0";

    }
    public Matches(String team1name, String team2name, String team1Key, String team2Key, String date,String time) {
        this.team1name = team1name;
        this.team2name = team2name;
        this.team1Key = team1Key;
        this.team2Key = team2Key;
        this.team1uri = "https://firebasestorage.googleapis.com/v0/b/mangmentsystem1.appspot.com/o/imagessss%2F1632230618821.jpg?alt=media&token=fc0036e9-826e-4554-859a-00d6f75030c0";
        this.team2uri = "https://firebasestorage.googleapis.com/v0/b/mangmentsystem1.appspot.com/o/imagessss%2F1632230618821.jpg?alt=media&token=fc0036e9-826e-4554-859a-00d6f75030c0";
        this.date = date;
        this.time = time;
        this.team1goals="0";
        this.team2goals="0";
        this.team1redCards="0";
        this.team2redCards="0";
        this.team1yellowCards="0";
        this.team2yellowCards="0";

    }


    public String getTeam1name() {
        return team1name;
    }

    public void setTeam1name(String team1name) {
        this.team1name = team1name;
    }

    public String getTeam2name() {
        return team2name;
    }

    public void setTeam2name(String team2name) {
        this.team2name = team2name;
    }

    public String getTeam1Key() {
        return team1Key;
    }

    public void setTeam1Key(String team1Key) {
        this.team1Key = team1Key;
    }

    public String getTeam2Key() {
        return team2Key;
    }

    public void setTeam2Key(String team2Key) {
        this.team2Key = team2Key;
    }

    public String getTeam1uri() {
        return team1uri;
    }

    public void setTeam1uri(String team1uri) {
        this.team1uri = team1uri;
    }

    public String getTeam2uri() {
        return team2uri;
    }

    public void setTeam2uri(String team2uri) {
        this.team2uri = team2uri;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getTeam1goals() {
        return team1goals;
    }

    public void setTeam1goals(String team1goals) {
        this.team1goals = team1goals;
    }

    public String getTeam2goals() {
        return team2goals;
    }

    public void setTeam2goals(String team2goals) {
        this.team2goals = team2goals;
    }

    public String getTeam1redCards() {
        return team1redCards;
    }

    public void setTeam1redCards(String team1redCards) {
        this.team1redCards = team1redCards;
    }

    public String getTeam2redCards() {
        return team2redCards;
    }

    public void setTeam2redCards(String team2redCards) {
        this.team2redCards = team2redCards;
    }

    public String getTeam1yellowCards() {
        return team1yellowCards;
    }

    public void setTeam1yellowCards(String team1yellowCards) {
        this.team1yellowCards = team1yellowCards;
    }

    public String getTeam2yellowCards() {
        return team2yellowCards;
    }

    public void setTeam2yellowCards(String team2yellowCards) {
        this.team2yellowCards = team2yellowCards;
    }
}
