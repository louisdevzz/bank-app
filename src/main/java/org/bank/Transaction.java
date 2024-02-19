package org.bank;

import java.sql.Timestamp;


public class Transaction {
    private static int count = 0;
    private int id;
    private Long uid;
    private Timestamp createAt;
    private String description;

    public Transaction(Long account,String description){
        this.id = ++count;
        this.uid = account;
        this.createAt = new Timestamp(System.currentTimeMillis());
        this.description = description;
    }

    public Long getUid() {
        return uid;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "uid='" + uid + '\'' +
                ", createAt=" + createAt +
                ", description='" + description + '\'' +
                '}';
    }
}
