package com.albo.comics.marvel.vo.remote.comicsByCharacter;

public class ComicPerson {

    private String name;
    private String role;

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ComicPerson && obj != null) {
            {
                ComicPerson other = (ComicPerson) obj;
                return this.name.equals(other.name) && this.role.equals(other.role);
            }
        } else {
            return false;
        }
    }
}
