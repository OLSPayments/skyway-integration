package com.ols.skyway.jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "isomsg")
public class ISOMessage {

  List<Field> fields = new ArrayList<Field>();

  @XmlElement(name="field")
  public List<Field> getFields() {
    return fields;
  }

  public void setFields(List<Field> queueus) {
    this.fields = queueus;
  }
  
  public static class Field {
    private String name;
    private String value;
    
    public Field() {
      super();
    }
    public Field(String name, String value) {
      super();
      this.name = name;
      this.value = value;
    }
    @XmlAttribute(name="id")
    public String getName() {
      return name;
    }
    public void setName(String name) {
      this.name = name;
    }
    @XmlAttribute(name="value")
    public String getValue() {
      return value;
    }
    public void setValue(String value) {
      this.value = value;
    }
  }
}