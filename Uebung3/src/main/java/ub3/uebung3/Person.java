package ub3.uebung3;

import java.io.Serializable;
import java.util.ArrayList;

class Person implements Serializable {
    private final String name;
    private final String gender;
    private final ArrayList<Integer> KekuleNr = new ArrayList<>();
    private Person mother;
    private Person father;
    private int generation;
    private final ArrayList<Person> children = new ArrayList<>();
    private final ArrayList<Person> siblings = new ArrayList<>();

    public ArrayList<Integer> getKekuleNr() {
        return KekuleNr;
    }

    public ArrayList<Person> getSiblings() {
        return siblings;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public String getName() {
        return name;
    }

    public Person getMother() {
        return mother;
    }

    public void setMother(Person mother) {
        this.mother = mother;
    }

    public Person getFather() {
        return father;
    }

    public void setFather(Person father) {
        this.father = father;
    }

    public ArrayList<Person> getChildren() {
        return children;
    }

    public String getGender() {
        return gender;
    }

    Person(String name, String gender) {
        this.name = name;
        this.gender = gender;
    }
}