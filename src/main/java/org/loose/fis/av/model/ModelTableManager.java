package org.loose.fis.av.model;

public class ModelTableManager {
    String nume,data;
    public ModelTableManager(String nume,String data){
        this.nume=nume;
        this.data=data;
    }
    public String getNume(){return nume;}
    public String getData(){return data;}
}
