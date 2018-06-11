package com.ols.skyway.example;

import com.ols.skyway.jaxb.ISOMessage;
import org.jpos.iso.ISOMsg;
import org.junit.Ignore;
import org.junit.Test;

public class TestImplementationExample {

  @Test
  public void testWorkflow() {
    ImplementationExample implementation = new ImplementationExample();
    // If this runs successfully, you won't be able to run it again.
    // To get the credentials you can leverage the listStores endpoint with the includeCreds parameter
    implementation.addStore("CodeStore");

    ISOMsg echoResponse = implementation.echo();

    ISOMsg apResponse = implementation.activateProduct("79936614516");
    ISOMsg cancelResponse = implementation.cancel("79936614516");

    ISOMsg dpResponse = implementation.deactivateProduct("79936614516");
    ISOMsg cancelDpResponse = implementation.cancelDeactivateProduct("79936614516");

    ISOMessage iqResponse = implementation.itemQualification("79936614516");
  }

  /*
  This test can be used after the store is created, the accessKey and accessId should be store level.
   */
  @Test
  @Ignore
  public void testISOXML() {
    String storeAccessKey = "";
    String storeAccessId = "";
    ImplementationExample implementation = new ImplementationExample("CodeStore", storeAccessKey, storeAccessId);

    ISOMsg echoResponse = implementation.echo();

    ISOMsg apResponse = implementation.activateProduct("79936614516");
    ISOMsg cancelResponse = implementation.cancel("79936614516");

    ISOMsg dpResponse = implementation.deactivateProduct("79936614516");
    ISOMsg cancelDpResponse = implementation.cancelDeactivateProduct("79936614516");

    ISOMessage iqResponse = implementation.itemQualification("79936614516");
  }

}
