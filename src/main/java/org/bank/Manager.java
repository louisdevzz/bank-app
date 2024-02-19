package org.bank;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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
        User accountBank = new User("test","test","test");
        this.users.add(accountBank);
        Bank numberBank = new Bank("Test");
        this.banks.add(numberBank);
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
        for(User u: this.users){
            if(u.existAccount(username,password)){
                System.out.println("login success!!!");
            }
        }
    }

    public void updateInformationAccountBank(String sex, String yearOfBirth, String address, String numberPhone, String identificationCard){
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try{


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
        for (Bank b : this.banks) {
            if (b.getNumberAccount().compareTo(numberAccount) == 0) {
                b.setNumberMoney(amount);
                System.out.println("Deposit success into "+numberAccount + " with the money of " + Long.toString(amount));
            }
        }
    }
    public void showAcouuntBank(){
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                MongoDatabase database = mongoClient.getDatabase("bank");
                MongoCollection<Document> col = database.getCollection("bankData");
                Document doc = col.find().first();
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
