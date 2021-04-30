package com.albo.comics.marvel.domain;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;

public class Character {

    public String name;
    public Integer age;
    public Set<Collaborator> collaborators;

    public Character() {

    }

    public Character(String name, Integer age) {
        this.name = name;
        this.age = age;
        this.collaborators = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));
        this.collaborators.add(new Collaborator(1L, "Wilson Moss", CollaboratorType.EDITOR));
        this.collaborators.add(new Collaborator(2L, "Andy Smith", CollaboratorType.EDITOR));

        this.collaborators.add(new Collaborator(3L, "Ed Brubaker", CollaboratorType.WRITER));
        this.collaborators.add(new Collaborator(4L, "Andy Smith", CollaboratorType.WRITER));

        this.collaborators.add(new Collaborator(5L, "Rico Renzi", CollaboratorType.COLORIST));
    }

}
