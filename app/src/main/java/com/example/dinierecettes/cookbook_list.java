package com.example.dinierecettes;

public class cookbook_list {

    String cookbook_id,cookbook_name,cookbook_num_of_recipess;

    public cookbook_list(String cookbook_id, String cookbook_name, String cookbook_num_of_recipess) {
        this.cookbook_id = cookbook_id;
        this.cookbook_name = cookbook_name;
        this.cookbook_num_of_recipess = cookbook_num_of_recipess;
    }

    public String getCookbook_id() {
        return cookbook_id;
    }

    public String getCookbook_name() {
        return cookbook_name;
    }

    public String getCookbook_num_of_recipess() {
        return cookbook_num_of_recipess;
    }
}
