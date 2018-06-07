package com.ols.skyway.jaxb;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "payload")
public class AddStoreResponse {

  @XmlElement(name = "store")
  Store store;

  public Store getStore() {
    return store;
  }

  public static class Store {

    @XmlElement(name = "credentials")
    Credentials credentials;

    public Credentials getAccessCredentials() {
      return credentials;
    }
  }

  public static class Credentials {

    String accessId;
    String accessKey;

    public String getAccessId() {
      return accessId;
    }

    public void setAccessId(String accessId) {
      this.accessId = accessId;
    }

    public String getAccessKey() {
      return accessKey;
    }

    public void setAccessKey(String accessKey) {
      this.accessKey = accessKey;
    }
  }
}

