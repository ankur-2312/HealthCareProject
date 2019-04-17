package com.healthcareproject;


class ChatData {


    private String name;
    private String id;


    ChatData(String name, String id) {
        this.name = name;
        this.id = id;
    }

    /* this method returns the
    name*/
    String getName() {
        return name;
    }

    /*this method returns the id*/
    String getId() {
        return id;
    }

}
