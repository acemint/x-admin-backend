package com.satusehat.property;


public class SatuSehatPropertyHolder {

  private static volatile SatuSehatProperty INSTANCE;

  public static void initialize(SatuSehatProperty satuSehatProperty) {
    synchronized (SatuSehatPropertyHolder.class) {
      if (INSTANCE == null) {
        INSTANCE = satuSehatProperty;
      }
    }
  }

  public static SatuSehatProperty getInstance() {
    synchronized (SatuSehatPropertyHolder.class) {
      if (INSTANCE == null) {
        throw new IllegalStateException("Uninitialized instance " + SatuSehatPropertyHolder.class.getPackageName());
      }
    }
    return INSTANCE;
  }

}
