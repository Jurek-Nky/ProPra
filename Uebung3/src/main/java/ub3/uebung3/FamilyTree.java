package ub3.uebung3;

import java.io.*;
import java.util.ArrayList;

public class FamilyTree {
    boolean running = true;
    ArrayList<Person> famList = new ArrayList<>();
    ArrayList<Person> printList = new ArrayList<>();

    public FamilyTree() {
        try {
            initialize();
        } catch (RelationNotPossible e) {
            e.printStackTrace();
        }
        setGenerations();
        setKekule();
    }

    public ArrayList<Person> getFamList() {
        return famList;
    }

    public int getAllChildrenCount(Person person) {
        if (person.getChildren().isEmpty()) {
            return 1;
        } else {
            int count = 0;
            for (Person child : person.getChildren()) {
                count = count + getAllChildrenCount(child);
            }
            return count + 1;
        }
    }


    public void serialize(String path) throws IOException {
        FileOutputStream fOut = new FileOutputStream(path);
        ObjectOutputStream obOut = new ObjectOutputStream(fOut);
        obOut.writeObject(famList);
        obOut.close();
        fOut.close();
    }

    public void deserialize(String path) {
        FileInputStream fIn = null;
        try {
            fIn = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectInputStream obIn = null;
        try {
            obIn = new ObjectInputStream(fIn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert obIn != null;
            famList = (ArrayList<Person>) obIn.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            obIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert fIn != null;
            fIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printFamilyList() {
        for (Person p : famList) {
            StringBuilder out = new StringBuilder();
            out.append(p.getName());
            if (p.getFather() != null) {
                out.append(" Mother: ").append(p.getMother().getName()).append(",");
                out.append(" Father: ").append(p.getFather().getName()).append(",");
                out.append(" Siblings:[");
                for (int i = 0; i < p.getSiblings().size(); i++) {
                    out.append(p.getSiblings().get(i).getName()).append(", ");
                }
                out.append("],");

            }
            out.append(" Generation: ").append(p.getGeneration()).append(",");
            out.append(" Gender: ").append(p.getGender());
            out.append(" KekuleNrs:[");
            for (int i : p.getKekuleNr()) {
                out.append(i).append(",");
            }
            out.append("]");
            System.out.println(out);
        }
    }

    public void setGenerations() {
        for (Person person : famList) {
            person.setGeneration(0);
        }
        for (Person person : famList) {
            if (person.getMother() != null) {
                person.setGeneration(person.getMother().getGeneration() + 1);
            }
        }
        for (Person person : famList) {
            if (!person.getChildren().isEmpty()) {
                person.setGeneration(person.getChildren().get(0).getGeneration() - 1);
            }
        }
    }

    public void setKekule() {
        for (int i = 0; i < 3; i++) {


            for (Person person : famList) {
                if (person.getChildren().isEmpty() && !person.getKekuleNr().contains(1)) {
                    person.getKekuleNr().add(1);
                } else if (!person.getChildren().isEmpty() && person.getChildren().get(0).getChildren().isEmpty() && person.getGender().equals("m") && !person.getKekuleNr().contains(2)) {
                    person.getKekuleNr().add(2);
                } else if (!person.getChildren().isEmpty() && person.getChildren().get(0).getChildren().isEmpty() && person.getGender().equals("f") && !person.getKekuleNr().contains(3)) {
                    person.getKekuleNr().add(3);
                }
            }
            for (Person person : famList) {
                for (Person child : person.getChildren()) {
                    if (!person.getChildren().isEmpty() && !child.getChildren().isEmpty()) {              //hat enkel
                        for (int kNr : child.getKekuleNr()) {
                            if (person.getGender().equals("m")) {
                                int kek = kNr * 2;
                                if (!person.getKekuleNr().contains(kek)) {
                                    person.getKekuleNr().add(kek);
                                }
                            } else if (person.getGender().equals("f")) {
                                int kek = kNr * 2 + 1;
                                if (!person.getKekuleNr().contains(kek)) {
                                    person.getKekuleNr().add(kek);
                                }
                            }
                        }

                    }
                }
            }
        }

    }

    public boolean removePerson(String name) throws PersonNotFound {
        Person p = findPerson(name);
        if (p != null && p.getMother() != null && p.getFather() != null) {
            for (int i = 0; i < p.getMother().getChildren().size(); i++) {
                if (p.getMother().getChildren().get(i).getName().equals(name)) {
                    p.getMother().getChildren().remove(i);
                    break;
                }
            }
            for (int i = 0; i < p.getFather().getChildren().size(); i++) {
                if (p.getFather().getChildren().get(i).getName().equals(name)) {
                    p.getFather().getChildren().remove(i);
                    break;
                }
            }
            for (int i = 0; i < famList.size(); i++) {
                if (famList.get(i).getName().equals(name)) {
                    famList.remove(i);
                    break;
                }
            }
            for (int i = 0; i < p.getSiblings().size(); i++) {
                for (int j = 0; j < p.getSiblings().get(i).getSiblings().size(); j++) {
                    if (p.getSiblings().get(i).getSiblings().get(j).getName().equals(name)) {
                        p.getSiblings().get(i).getSiblings().remove(j);
                        break;
                    }
                }
            }
            return true;
        } else if (p != null && p.getFather() == null && p.getMother() == null) {
            for (int i = 0; i < famList.size(); i++) {
                if (famList.get(i).getName().equals(name)) {
                    famList.remove(i);
                    break;
                }
            }
        }
        return false;
    }

    public void printFamilyTree() {
        printList.clear();
        System.out.println("Printing tree:\n-----------------------------------");
        printFamilyTreeRec(famList.get(6), 5);
        System.out.println("-----------------------------------");
    }

    public void printFamilyTreeRec(Person p, int gen) {

        //mother
        if (p.getMother() != null) {
            if (listContainsNot(printList, p.getMother().getName())) {
                printFamilyTreeRec(p.getMother(), gen + 1);
            }
        }
        //#mother
        //self
        StringBuilder x = new StringBuilder();
        x.append("      ".repeat(Math.max(0, gen)));
        if (listContainsNot(printList, p.getName())) {
            System.out.println(x + p.getName());
            printList.add(p);
        }
        //#self
        //siblings
        for (int i = 0; i < p.getSiblings().size(); i++) {
            if (listContainsNot(printList, p.getSiblings().get(i).getName())) {
                printFamilyTreeRec(p.getSiblings().get(i), gen);
            }
        }
        //#siblings

        //father
        if (p.getFather() != null) {
            if (listContainsNot(printList, p.getFather().getName())) {
                printFamilyTreeRec(p.getFather(), gen + 1);
            }
        }
        //#father
        //children
        for (int i = 0; i < p.getChildren().size(); i++) {
            if (listContainsNot(printList, p.getChildren().get(i).getName())) {
                printFamilyTreeRec(p.getChildren().get(i), gen - 1);
            }
        }
        //#children


    }

    public boolean addRelation(Person child, Person mother, Person father) throws RelationNotPossible{
        if (child != null && mother != null && father != null && child.getMother() == null && child.getFather() == null) {
            child.setFather(father);
            child.setMother(mother);
            for (int i = 0; i < mother.getChildren().size(); i++) {
                child.getSiblings().add(mother.getChildren().get(i));
            }
            for (int i = 0; i < mother.getChildren().size(); i++) {
                mother.getChildren().get(i).getSiblings().add(child);
            }
            father.getChildren().add(child);
            mother.getChildren().add(child);
            return true;
        }
        throw new RelationNotPossible();
    }

    public Person findPerson(String name) throws PersonNotFound {
        for (Person p : famList) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        throw new PersonNotFound(name);
    }

    public void addPerson(String name, String gender) {
        if (listContainsNot(famList, name)) {
            famList.add(new Person(name, gender));
        }
    }

    public boolean listContainsNot(ArrayList<Person> list, String name) {
        for (Person person : list) {
            if (person.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    public void initialize() throws RelationNotPossible {
        /*
         * Creates a familyTree with the following sceme:
         *
         *                 helga___robert    mathilda_________thomas
         *                       |                    |     |
         *                     lara________________markus rebecca_________tim
         *                             |     |                   |     |
         *                           tobi simon                 jale marie______max
         *                                                                   |
         *                                                                michelle
         *
         *
         */
        famList.add(new Person("lara", "f"));
        famList.add(new Person("markus", "m"));
        famList.add(new Person("tobi", "m"));
        famList.add(new Person("simon", "m"));
        famList.add(new Person("thomas", "m"));
        famList.add(new Person("mathilda", "f"));
        famList.add(new Person("helga", "f"));
        famList.add(new Person("robert", "m"));
        famList.add(new Person("rebecca", "f"));
        famList.add(new Person("tim", "m"));
        famList.add(new Person("jale", "f"));
        famList.add(new Person("marie", "f"));
        famList.add(new Person("max", "m"));
        famList.add(new Person("michelle", "f"));
        addRelation(famList.get(3), famList.get(0), famList.get(1));
        addRelation(famList.get(2), famList.get(0), famList.get(1));
        addRelation(famList.get(0), famList.get(6), famList.get(7));
        addRelation(famList.get(1), famList.get(5), famList.get(4));
        addRelation(famList.get(8), famList.get(5), famList.get(4));
        addRelation(famList.get(10), famList.get(8), famList.get(9));
        addRelation(famList.get(11), famList.get(8), famList.get(9));
        addRelation(famList.get(13), famList.get(11), famList.get(12));
    }
}

class PersonNotFound extends Exception {
    public PersonNotFound(String name) {
        super(String.format("Person %s does not exist",name));
    }
}
class RelationNotPossible extends Exception{
    public RelationNotPossible(){
        super("This Relation is not possible");
    }
}

