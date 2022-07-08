package com.andyra.tugas7;

public class BarangModel {
   public int mdid;
   public String mdnama;
   public String mdjenis;
   public int mdstok;

    BarangModel(int atrid, String atrnama, String atrjenis, int atrstok){
       this.mdid = atrid;
       this.mdnama = atrnama;
       this.mdjenis = atrjenis;
       this.mdstok = atrstok;
   }
}
