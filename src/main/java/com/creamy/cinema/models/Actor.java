package com.creamy.cinema.models;

public class Actor {
    private String name;
    private String role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object obj) {
        Actor actor = (Actor) obj;
        return name.equals(actor.getName()) && role.equals(actor.getRole());
    }
}
