import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FamilyTree {
    static boolean running = true;
    static ArrayList<Person> famList = new ArrayList<>();
    static ArrayList<Person> printList = new ArrayList<>();

    public static void main(String[] args) {
        initialize();
        setGenerations();
        setKekule();
        Scanner sc = new Scanner(System.in);
        System.out.println("FamilyTree:\ninput help to get started");
        while (running) {
            String helpString = "Commands:\n\t"
                    + "add, a: Add a Person.\n\t"
                    + "addrel, ar: Add a relationship between two Persons.\n\t"
                    + "save, sv: Save your familyTree to a file.\n\t"
                    + "load, l: Load a previously saved familytree from a file.\n\t"
                    + "search, s: You can search for father,mother,grandfather,grandmother,brother and sister.\n\t"
                    + "print, p: Output the current Familytree.\n\t"
                    + "printl, pl: Print a list version of your familyTree."
                    + "remove, rm: Remove a Person.\n\t"
                    + "filter, f: filter for generation or gender.\n\t"
                    + "quit, q: exit program.";
            switch (sc.next()) {

                case "help", "h" -> System.out.println(helpString);
                case "search", "s" -> {
                    System.out.println("What would you like to search for?(name,father,mother,grandfather,grandmother,brother,sister)");
                    switch (sc.next()) {
                        case "name", "n":
                            for (Person person3 : famList) {
                                if (person3.getName().equals(sc.next())) {
                                    System.out.println(person3.getName());
                                }
                            }
                        case "father", "f":
                            System.out.println("Input the name you're seaching for.");
                            for (Person person2 : famList) {
                                if (person2.getFather().getName().equals(sc.next())) {
                                    System.out.println(person2.getName() + " : " + person2.getFather().getName());
                                }
                            }
                            break;
                        case "mother", "m":
                            System.out.println("Input the name you're seaching for.");
                            for (Person person1 : famList) {
                                if (person1.getMother() != null && person1.getMother().getName().equals(sc.next())) {
                                    System.out.println(person1.getName() + " : " + person1.getMother().getName());
                                }
                            }
                            break;
                        case "grandmother", "gm":
                            System.out.println("Input the name you're seaching for.");
                            String name = sc.next();
                            for (Person element : famList) {
                                if (element.getMother() != null && element.getMother().getMother() != null && element.getMother().getMother().getName().equals(name)) {
                                    System.out.println(element.getName() + " : " + element.getMother().getMother().getName());
                                } else if (element.getFather() != null && element.getFather().getMother() != null && element.getFather().getMother().getName().equals(name)) {
                                    System.out.println(element.getName() + " : " + element.getFather().getMother().getName());
                                }
                            }
                            break;
                        case "grandfather", "gf":
                            System.out.println("Input the name you're seaching for.");
                            name = sc.next();
                            for (Person item : famList) {
                                if (item.getFather() != null && item.getFather().getFather() != null && item.getFather().getFather().getName().equals(name)) {
                                    System.out.println(item.getName() + " : " + item.getFather().getFather().getName());
                                } else if (item.getMother() != null && item.getMother().getFather() != null && item.getMother().getFather().getName().equals(name)) {
                                    System.out.println(item.getName() + " : " + item.getMother().getFather().getName());
                                }
                            }
                            break;
                        case "brother", "b":
                            System.out.println("Input the name you're seaching for.");
                            name = sc.next();
                            for (Person value : famList) {
                                if (!value.getSiblings().isEmpty() && value.getGender().equals("f")) {
                                    for (int j = 0; j < value.getSiblings().size(); j++) {
                                        if (value.getSiblings().get(j).getName().equals(name)) {
                                            System.out.println(value.getName() + " : " + value.getSiblings().get(j).getName());
                                        }
                                    }
                                }
                            }
                            break;
                        case "sister", "s":
                            System.out.println("Input the name you're seaching for.");
                            name = sc.next();
                            for (Person person : famList) {
                                if (!person.getSiblings().isEmpty() && person.getGender().equals("m")) {
                                    for (int j = 0; j < person.getSiblings().size(); j++) {
                                        if (person.getSiblings().get(j).getName().equals(name)) {
                                            System.out.println(person.getName() + " : " + person.getSiblings().get(j).getName());
                                        }
                                    }
                                }
                            }
                            break;

                        default:
                            System.out.println("No valid input.");
                            break;
                    }
                }
                case "load", "l" -> {
                    System.out.println("input path of the file you'd like to load.(default: " + System.getProperty("user.dir") + "\\familyTreeList)");
                    String path = sc.next();
                    if (path.equals("default")) {
                        deserialize(System.getProperty("user.dir") + "\\familyTreeList");
                    } else {
                        deserialize(path);
                    }
                }
                case "save", "sv" -> {
                    System.out.println("Input the path where you want to save your file.(default: " + System.getProperty("user.dir") + "\\familyTreeList)");
                    String path = sc.next();
                    if (path.equals("default")) {
                        try {
                            serialize(System.getProperty("user.dir") + "\\familyTreeList");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            serialize(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                case "filter", "f" -> {
                    System.out.println("What would you like to filter for?(generation,gender)");
                    switch (sc.next()) {
                        case "generation" -> {
                            System.out.println("Input generation you'd like to filter for");
                            int generation = sc.nextInt();
                            for (Person person : famList) {
                                if (person.getGeneration() == generation) {
                                    System.out.println(person.getName());
                                }
                            }
                        }
                        case "gender" -> {
                            System.out.println("Input gender you'd like to filter for.");
                            String gender = sc.next();
                            for (Person person : famList) {
                                if (person.getGender().equals(gender)) {
                                    System.out.println(person.getName());
                                }
                            }
                        }
                        default -> System.out.println("Wrong input.");
                    }
                }
                case "add", "a" -> {
                    System.out.println("Enter name:");
                    String name = sc.next();
                    System.out.println("Enter gender(m/f):");
                    switch (sc.next()) {
                        case "m" -> {
                            addPerson(name, "m");
                            System.out.println("Person got added. Use \"addrel\" to add relationship with other people.");
                        }
                        case "f" -> {
                            addPerson(name, "f");
                            System.out.println("Person got added. Use \"addrel\" to add relationship with other people.");
                        }
                        default -> System.out.println("something went wrong.");
                    }
                }
                case "addrel", "ar" -> {
                    System.out.println("Input the name of the child:");
                    String personName = sc.next();
                    Person child = findPerson(personName);
                    if (child == null) {
                        System.out.println("Person not found.");
                        break;
                    }
                    System.out.println("Input the name of the mother:");
                    personName = sc.next();
                    Person mother = findPerson(personName);
                    if (mother == null) {
                        System.out.println("Person not found.");
                        break;
                    }
                    System.out.println("Input the name of the father:");
                    personName = sc.next();
                    Person father = findPerson(personName);
                    if (father == null) {
                        System.out.println("Person not found.");
                        break;
                    }
                    if (addRelation(child, mother, father)) {
                        System.out.println("Relation added successfully.");
                    } else {
                        System.out.println("Something went wrong.");
                    }
                    setKekule();
                }
                case "print", "p" -> printFamilyTree();
                case "remove", "rm" -> {
                    System.out.println("Input the name of the person you want to remove from the familyTree.");
                    if (!removePerson(sc.next())) {
                        System.out.println("Person not found.");
                    } else {
                        System.out.println("Successfully removed.");
                    }
                }
                case "printl", "pl" -> printFamilyList();
                case "q", "quit" -> running = false;
                default -> System.out.println("No command recognised.");
            }
        }
    }

    private static void serialize(String path) throws IOException {
        FileOutputStream fOut = new FileOutputStream(path);
        ObjectOutputStream obOut = new ObjectOutputStream(fOut);
        obOut.writeObject(famList);
        obOut.close();
        fOut.close();
    }

    private static void deserialize(String path) {
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

    private static void printFamilyList() {
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

    private static void setGenerations() {
        for (Person person : famList) {
            person.setGeneration(1);
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

    private static void setKekule() {
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

    private static boolean removePerson(String name) {
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

    private static void printFamilyTree() {
        printList.clear();
        System.out.println("Printing tree:\n-----------------------------------");
        printFamilyTreeRec(famList.get(6), 5);
        System.out.println("-----------------------------------");
    }

    private static void printFamilyTreeRec(Person p, int gen) {

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

    private static boolean addRelation(Person child, Person mother, Person father) {
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
        return false;
    }

    private static Person findPerson(String name) {
        for (Person p : famList) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    private static void addPerson(String name, String gender) {
        if (listContainsNot(famList, name)) {
            famList.add(new Person(name, gender));
        }
    }

    private static boolean listContainsNot(ArrayList<Person> list, String name) {
        for (Person person : list) {
            if (person.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    private static void initialize() {
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


