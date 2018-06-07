package com.ols.skyway.example;

import com.ols.skyway.jaxb.AddStoreResponse;
import com.ols.skyway.jaxb.ISOMessage;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.XMLPackager;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Properties;

public class ImplementationExample {

  String acquirerId;
  String storeName;
  String accessKey, accessId;
  String masterAccessKey, masterAccessId;
  String encryptionFileName, encryptionKeyName;

  public ImplementationExample() {
    try {
      Properties properties = new Properties();
      properties.load(getClass().getClassLoader().getResourceAsStream("credentials.properties"));

      acquirerId = properties.getProperty("acquirerId");
      masterAccessKey = properties.getProperty("masterAccessKey");
      masterAccessId = properties.getProperty("masterAccessId");
      encryptionFileName = properties.getProperty("encryptionFileName");
      encryptionKeyName = properties.getProperty("encryptionKeyName");
    } catch (IOException e) { }
  }

  public ImplementationExample(String storeName, String accessKey, String accessId) {
    this();
    this.storeName = storeName;
    this.accessKey = accessKey;
    this.accessId = accessId;
  }

  public HttpResponse addStore(String storeName) {
    this.storeName = storeName;

    String uri = "/manage/stores/" + storeName;
    String dateTime = "2017-06-22T14:57:28.999Z";
    String contentType = "application/xml;charset=UTF-8";
    String accept = "application/xml;version=1";
    String sign = generateMasterSign(uri, dateTime, contentType, accept);

    HttpPost httpPost = new HttpPost("https://skywayp.olstest.com:444" + uri);
    httpPost.addHeader("X-InComm-DateTime", dateTime);
    httpPost.addHeader("Content-Type", contentType);
    httpPost.addHeader("Accept", accept);
    httpPost.addHeader("Authorization", sign);

    try {
      HttpResponse response = HttpClients.createDefault().execute(httpPost);
      if (response.getStatusLine().getStatusCode() == 200) {
        JAXBContext context = JAXBContext.newInstance(AddStoreResponse.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        AddStoreResponse addStoreResponse = (AddStoreResponse) unmarshaller.unmarshal(response.getEntity().getContent());
        this.accessId = addStoreResponse.getStore().getAccessCredentials().getAccessId();
        this.accessKey = addStoreResponse.getStore().getAccessCredentials().getAccessKey();
      }
      return response;
    } catch (IOException | JAXBException e) {
      return null;
    }
  }

  public ISOMsg activateProduct(String UPC) {
    String uri = "/isoxml/activateProduct";
    String dateTime = "2017-06-22T14:57:28.999Z";
    String contentType = "application/xml;charset=UTF-8";
    String accept = "application/xml;version=1";
    String sign = generateSign(accessKey, accessId, uri, dateTime, contentType, accept);
    String body = generateActivateProductXML(UPC);

    HttpPost httpPost = new HttpPost("https://skywayp.olstest.com:444" + uri);
    httpPost.addHeader("X-InComm-DateTime", dateTime);
    httpPost.addHeader("Content-Type", contentType);
    httpPost.addHeader("Accept", accept);
    httpPost.addHeader("Authorization", sign);

    try {
      httpPost.setEntity(new StringEntity(body));
      HttpResponse response = HttpClients.createDefault().execute(httpPost);

      // Uses jPos to unmarshal the message
      ISOMsg message = new ISOMsg();
      message.setPackager(new XMLPackager());
      message.unpack(response.getEntity().getContent());

      return message;

    } catch (IOException | ISOException e) {
      return null;
    }
  }

  public ISOMessage itemQualification(String UPC) {
    String uri = "/isoxml/itemQualification";
    String dateTime = "2017-06-22T14:57:28.999Z";
    String contentType = "application/xml;charset=UTF-8";
    String accept = "application/xml;version=1";
    String sign = generateSign(accessKey, accessId, uri, dateTime, contentType, accept);
    String body = generateItemQualificationXML(UPC);

    HttpPost httpPost = new HttpPost("https://skywayp.olstest.com:444" + uri);
    httpPost.addHeader("X-InComm-DateTime", dateTime);
    httpPost.addHeader("Content-Type", contentType);
    httpPost.addHeader("Accept", accept);
    httpPost.addHeader("Authorization", sign);

    try {
      httpPost.setEntity(new StringEntity(body));
      HttpResponse response = HttpClients.createDefault().execute(httpPost);

      // uses ISOMessage example JAXB object to unmarshal the message
      JAXBContext context = JAXBContext.newInstance(ISOMessage.class);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      ISOMessage message = (ISOMessage) unmarshaller.unmarshal(response.getEntity().getContent());

      return message;

    } catch (IOException | JAXBException e) {
      return null;
    }
  }

  private String generateMasterSign(String uri, String... headers) {
    return generateSign(masterAccessKey, masterAccessId, uri, headers);
  }

  private String generateSign(String accessKey, String accessId, String uri, String... headers) {
    try {
      // concat headers
      String headerJoin = String.join("", headers) + uri;

      // calculate keyed-hash signature
      Mac mac = Mac.getInstance("HmacSHA1");
      mac.init(new SecretKeySpec(accessKey.getBytes(), "HmacSHA1"));
      byte[] hmac = mac.doFinal(headerJoin.getBytes());

      // base 64-encode accessId & generate auth header sign

      return "InComm " +
          new String(Base64.getEncoder().encode(accessId.getBytes()), "UTF-8") +
          ":" +
          new String(Base64.getEncoder().encode(hmac), "UTF-8");
    } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
      return null;
    }
  }

  private String generateActivateProductXML(String upc) {
    try {
      ISOMsg f125 = new ISOMsg(125);
      f125.set(0, "OFF");
      f125.set(2, "0006277006720279092");
      f125.set(14, "4912");
      ISOMsg message = new ISOMsg();
      message.setPackager(new XMLPackager());
      message.set(0, "0200");
      message.set(3, "189090");
      message.set(4, "000008000000");
      message.set(7, "20180221T124514Z");
      message.set(11, "591299165119");
      message.set(12, "124418-0500");
      message.set(13, "20180221");
      message.set(22, "030");
      message.set(32, acquirerId);
      message.set(37, "591299165119");
      message.set(41, "12345123");
      message.set(42, storeName);
      message.set(49, "USD");
      message.set(54, upc);
      message.set(f125);

      return new String(message.pack());

    } catch (ISOException e) {
      return null;
    }
  }

  private String generateItemQualificationXML(String upc) {
    try {
      ISOMsg f125 = new ISOMsg(125);
      f125.set(0, "RSA");
      f125.set(1, encryptionKeyName);
      f125.set(2, encrypt("0006277006720279092"));
      ISOMsg message = new ISOMsg();
      message.setPackager(new XMLPackager());
      message.set(0, "0200");
      message.set(3, "910000");
      message.set(4, "000000003600");
      message.set(7, "20180221T124514Z");
      message.set(11, "591299165119");
      message.set(12, "124418-0500");
      message.set(13, "20180221");
      message.set(17, "20180221");
      message.set(21, "0000006571153");
      message.set(22, "020");
      message.set(25, "00");
      message.set(26, "4121");
      message.set(32, acquirerId);
      message.set(37, "591299165119");
      message.set(41, acquirerId + "123"); // dummy lane number
      message.set(42, storeName);
      message.set(48, "MC");
      message.set(49, "USD");
      message.set(54, upc);
      message.set(59, "PI090001001Y0PI110002002Y176PI090003001N0PI110004002N176");
      message.set(111, "DV009020501.01PI0490214000312547652360303005051200000000024713040001PI0490214000301490039420303006051200000000025013040002PI0490214000688678547850303004051200000000035713040003PI0490214000784586397540303002051200000000206013040004");
      message.set(f125);

      return new String(message.pack());

    } catch (ISOException e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  private byte[] encrypt(String data) {

    try {
      X509EncodedKeySpec spec = new X509EncodedKeySpec(read(encryptionFileName));
      KeyFactory kf = KeyFactory.getInstance("RSA");

      Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
      cipher.init(Cipher.ENCRYPT_MODE, kf.generatePublic(spec));
      return Base64.getEncoder().encode((cipher.doFinal(data.getBytes())));

    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }

  private static byte[] read(String filename) throws IOException {
    File f = new File(filename);
    InputStream fileIn = new FileInputStream(f);
    if(fileIn == null)
      return null;

    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();

      int c;
      while((c = fileIn.read()) != -1) {
        out.write(c);
      }
      fileIn.close();

      return out.toByteArray();
    }
    catch (IOException e) {
      throw e;
    }
  }

}
