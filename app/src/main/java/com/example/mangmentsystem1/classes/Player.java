package com.example.mangmentsystem1.classes;

import java.io.Serializable;

public class Player implements Serializable {

    private String key;
    private String name;
    private String teamKey;
    private String teamName;
    private String age;
    private int goals;
    private int assists;
    private String appearances;
    private String redCards;
    private String yellowCards;
    private String uri;
    private String shirtNumber;


    public  Player(){}


    public Player(String name, String teamKey, String teamName, String uri,String age,String shirtNumber) {
        this.name = name;
        this.teamKey = teamKey;
        this.teamName = teamName;
        this.uri = uri;
        this.age= age;
        this.goals =0;
        this.assists=0;
        this.appearances="0";
        this.redCards="0";
        this.yellowCards="0";
        this.shirtNumber=shirtNumber;


    }
    public Player(String name, String teamKey, String teamName,String age,String shirtNumber) {
        this.name = name;
        this.teamKey = teamKey;
        this.teamName = teamName;
        this.uri = "https://firebasestorage.googleapis.com/v0/b/mangmentsystem1.appspot.com/o/imagessss%2F1632230618821.jpg?alt=media&token=fc0036e9-826e-4554-859a-00d6f75030c0";
        this.age= age;
        this.goals =0;
        this.assists=0;
        this.appearances="0";
        this.redCards="0";
        this.yellowCards="0";
        this.shirtNumber=shirtNumber;


    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamKey() {
        return teamKey;
    }

    public void setTeamKey(String teamKey) {
        this.teamKey = teamKey;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public String getAppearances() {
        return appearances;
    }

    public void setAppearances(String appearances) {
        this.appearances = appearances;
    }

    public String getRedCards() {
        return redCards;
    }

    public void setRedCards(String redCards) {
        this.redCards = redCards;
    }

    public String getYellowCards() {
        return yellowCards;
    }

    public void setYellowCards(String yellowCards) {
        this.yellowCards = yellowCards;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getShirtNumber() {
        return shirtNumber;
    }

    public void setShirtNumber(String shirtNumber) {
        this.shirtNumber = shirtNumber;
    }
}
