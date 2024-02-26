package org.bank;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;

import org.bson.types.ObjectId;
import org.bson.conversions.Bson;
import static com.mongodb.client.model.Filters.eq;
import java.sql.Timestamp;
import java.util.*;
import org.json.*;
public class Manager {
    private ArrayList<Bank> banks;
    private ArrayList<User> users;
    private Map<String, ArrayList<Transaction>> trans;
    private ArrayList<String> information;
    private String connectionString = "mongodb+srv://louisdevzz04:vohuunhan1310@cluster0.zmwbg2i.mongodb.net/?retryWrites=true&w=majority";
    private ServerApi serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build();
    private MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(connectionString))
            .serverApi(serverApi)
            .build();
    public Manager(){
        this.banks = new ArrayList<Bank>();
        this.users = new ArrayList<User>();
        this.trans = new TreeMap<String, ArrayList<Transaction>>();
        this.information = new ArrayList<String>();
    }

    public void createBankAccount(String username, String password,String fullName){
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // create user login account bank
                MongoDatabase database = mongoClient.getDatabase("bank");
                MongoCollection<Document> collection = database.getCollection("users");
                collection.insertOne(new Document()
                        .append("_id",new ObjectId())
                        .append("fullName", fullName)
                        .append("username", username)
                        .append("password", password)
                        .append("createAt", new Timestamp(System.currentTimeMillis()))
                );
                information.add(fullName);
                // create account bank
                Bank bank = new Bank(fullName);
                MongoCollection<Document> col = database.getCollection("bankData");
                col.insertOne(new Document()
                        .append("_id",new ObjectId())
                        .append("numberAccount", bank.getNumberAccount())
                        .append("fullName", bank.getFullName())
                        .append("sex",bank.getSex())
                        .append("yearOfBirth", bank.getYearOfBirth())
                        .append("phoneNumber", bank.getNumberPhone())
                        .append("identificationCard", bank.getIdentificationCard())
                        .append("address", bank.getAddress())
                        .append("pin", bank.getPin())
                        .append("money", bank.getNumberMoney())
                        .append("createAt", bank.getCreateAt())
                );
                Transaction trns = new Transaction(bank.getNumberAccount(),"Create account with Name: "+fullName);
                MongoCollection<Document> trans = database.getCollection("transactions");
                trans.insertOne(new Document()
                        .append("_id", new ObjectId())
                        .append("uid", trns.getUid())
                        .append("description", trns.getDescription())
                        .append("createAt", trns.getCreateAt())
                );
                System.out.println("Create Account Bank Successful");
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }
    }

    public void loginAccount(String username, String password){
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                MongoDatabase database = mongoClient.getDatabase("bank");
                MongoCollection<Document> col = database.getCollection("users");
                Bson filter = Filters.and(Filters.eq("username",username),Filters.eq("password",password));
                Document doc = col.find(filter).first();
                if (doc == null) {
                    System.out.println("Account not found.");
                } else {
                    JSONObject account = new JSONObject(doc.toJson());
                    information.add(account.getString("fullName"));
                    System.out.println("Login Success!!!");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateInformationAccountBank(String sex, String yearOfBirth, String address, String numberPhone, String identificationCard){
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try{
                MongoDatabase database = mongoClient.getDatabase("bank");
                MongoCollection<Document> col = database.getCollection("bankData");
                Document doc = col.find(eq("fullName",information.getFirst())).first();
                Bson updates = Updates.combine(
                        Updates.set("sex", sex),
                        Updates.set("yearOfBirth",yearOfBirth),
                        Updates.set("address",address),
                        Updates.set("phoneNumber",numberPhone),
                        Updates.set("identificationCard",identificationCard)
                );
                UpdateOptions options = new UpdateOptions().upsert(true);
                if (doc!=null){
                    col.updateOne(doc,updates,options);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void transferMoney(Long numberAccountFrom,Long numberAccountTo,Long amount) {
        for (Bank b : this.banks) {
            if (b.getNumberAccount().compareTo(numberAccountTo) == 0) {
                b.setNumberMoney(amount);
                System.out.println("Transfer success from " + numberAccountFrom.toString() + " to " + numberAccountTo.toString() + " with the money of " + Long.toString(amount));
            }
        }
    }
    public void depositMoney(Long numberAccount,Long amount) {
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try{
                MongoDatabase database = mongoClient.getDatabase("bank");
                MongoCollection<Document> col = database.getCollection("bankData");
                Document doc = col.find(eq("numberAccount",numberAccount)).first();
                Bson updates = Updates.combine(
                        Updates.set("money", amount)
                );
                UpdateOptions options = new UpdateOptions().upsert(true);
                if (doc!=null){
                    col.updateOne(doc,updates,options);
                    System.out.println("Deposit success from " + numberAccount.toString()  + " with the money of " + Long.toString(amount));
                }else{
                    System.out.println("Account not found!!");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void showAcouuntBank(){
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                MongoDatabase database = mongoClient.getDatabase("bank");
                MongoCollection<Document> col = database.getCollection("bankData");
                Document doc = col.find(eq("fullName",information.getFirst())).first();
                if(doc!=null){
                   JSONObject account = new JSONObject(doc.toJson());
                   System.out.println("Information Account Bank! \nAccount Number: " + account.getLong("numberAccount") +"\n"+
                           "Full Name: " + account.getString("fullName")  +"\n"+
                           "sex: " + account.getString("sex")  +"\n"+
                           "Year of Birth: " + account.getString("yearOfBirth")  +"\n"+
                           "Address: " + account.getString("address") +"\n"+
                           "Phone Number: " + account.getString("phoneNumber")  +"\n"+
                           "Identification Card: " + account.getString("identificationCard")+"\n"+
                           "Code Pin: " + account.getInt("pin")+"\n"+
                           "Money: " + account.getLong("money"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public ArrayList<Bank> Bank(){
        return banks;
    }
    public  Map<String, ArrayList<Transaction>> Trans(){
        return trans;
    }
}
