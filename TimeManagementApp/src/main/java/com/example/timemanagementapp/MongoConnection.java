package com.example.timemanagementapp;


import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
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

    public static Document createEmployee(String rolle, String name, String password, int employeeId){
        return new Document("id", new ObjectId())
                .append("Rolle", rolle)
                .append("Name", name)
                .append("Password", password)
                .append("EmployeeId", employeeId)
                .append("Stempelzeiten", Arrays.asList());
    }
    public static void insertEmployee(Document document){
        try{
            MongoClient mongoClient = createConnectiontoDatabase();
            MongoDatabase _database = mongoClient.getDatabase(database);
            MongoCollection<Document> _collection = _database.getCollection(collection);
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
            MongoDatabase _database = mongoClient.getDatabase(database);
            MongoCollection<Document> _collection = _database.getCollection(collection);
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
        MongoDatabase _database = mongoClient.getDatabase(database);
        MongoCollection<Document> _collection = _database.getCollection(collection);
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
        MongoDatabase _database = mongoClient.getDatabase(database);
        MongoCollection<Document> _collection = _database.getCollection(collection);
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


    public static void main(String[] args) {
    }
}
