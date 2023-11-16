package com.example.timemanagementapp;


import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MongoConnection {
    static String user = "admin";
    static String password = "admin";
    static String uri = "mongodb+srv://"+ user +":"+ password +"@cluster0.5nwws7b.mongodb.net/?retryWrites=true&w=majority";
    static String database = "EmployeeDB";
    static String collection = "Employees";
    //Verbindung zur Datenbank herstellen
    public static MongoClient createConnectiontoDatabase() {
        MongoClient mongoClient = MongoClients.create(uri);
        try {
            System.out.println("Verbindung zur Datenbank wurde hergestellt.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mongoClient;
    }

    public static MongoCollection getCollection(MongoClient mongoClient) {
        MongoDatabase _database = mongoClient.getDatabase(database);
        MongoCollection<Document> _collection = _database.getCollection(collection);

        return _collection;
    }

    public static Document createEmployee(String rolle, String name, String password){
        MongoCollection<Document> _collection = getCollection(createConnectiontoDatabase());

        return new Document("id", new ObjectId())
                .append("Rolle", rolle)
                .append("Name", name)
                .append("Password", password)
                .append("EmployeeId", emplyeeIDgenerator())
                .append("Stempelzeiten", Arrays.asList());
    }
    public static void insertEmployee(Document document){
        try{
            MongoClient mongoClient = createConnectiontoDatabase();
            MongoCollection<Document> _collection = getCollection(mongoClient);
            _collection.insertOne(document);
            System.out.println("Mitarbeiter hinzugefügt.");
            mongoClient.close();
            System.out.println("Verbindung zur Datenbank geschlossen.");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static void insertManyEmployees(List<Document> documents) {
        try {
            MongoClient mongoClient = createConnectiontoDatabase();
            MongoCollection<Document> _collection = getCollection(mongoClient);
            _collection.insertMany(documents);
            System.out.println("Mitarbeitende hinzugefügt.");
            mongoClient.close();
            System.out.println("Verbindung zur Datenbank geschlossen.");
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public static List<Document> getEmployeesFromDatabase() {
        MongoClient mongoClient = createConnectiontoDatabase();
        MongoCollection<Document> _collection = getCollection(mongoClient);
        List<Document> employeeList = new ArrayList<>();

        // Alle Dokumente aus der Sammlung abrufen
        FindIterable<Document> iterable = _collection.find();
        try (MongoCursor<Document> cursor = iterable.iterator()) {
            while (cursor.hasNext()) {
                employeeList.add(cursor.next());
            }
        }
        mongoClient.close();
        System.out.println("Verbindung zur Datenbank geschlossen.");
        return employeeList;
    }

    public static void addStempelzeitEintrag(int mitarbeiterID, String datum, String typ) {
        MongoClient mongoClient = createConnectiontoDatabase();
        MongoCollection<Document> _collection = getCollection(mongoClient);
        // Filter nach Mitarbeiter-ID
        Document filter = new Document("EmployeeId", mitarbeiterID);

        // Neue Stempelzeit erstellen
        Document neuerEintrag = new Document("Datum", datum).append("Typ", typ);

        // Update-Operation, um den neuen Eintrag in die Liste einzufügen
        Document update = new Document("$push", new Document("Stempelzeiten", neuerEintrag));

        // Aktualisierung durchführen
        _collection.updateOne(filter, update);

        System.out.println("Neuer Eintrag hinzugefügt");
        mongoClient.close();
        System.out.println("Verbindung zur Datenbank geschlossen.");
    }

    private static int emplyeeIDgenerator() {
        List<Document> employees = getEmployeesFromDatabase();
        int newEmployeeId = 1;
        for (Document employee : employees) {
            int existingEmployeeId = employee.getInteger("EmployeeId");

            // Check if the current employee ID is equal to the new one
            if (newEmployeeId == existingEmployeeId) {
                // Increment the new employee ID if a match is found
                newEmployeeId++;
            } else {
                // If a gap is found in the existing employee IDs, return the new ID
                break;
            }
        }
        return newEmployeeId;
    }
}
