package org.loose.fis.av.model;

public class Programat {

    private String nume;
    private String data;
    private String CNP;

    public Programat(String nume, String data,String cod){
        this.nume = nume;
        this.data = data;
        CNP = cod;
    }

    public Programat(){}

    public String getNume() {
        return nume;
    }

    public String getData() {
        return data;
    }
    public void setData(String data){this.data=data;}

    public String getCNP(){
        return CNP;
    }
}
