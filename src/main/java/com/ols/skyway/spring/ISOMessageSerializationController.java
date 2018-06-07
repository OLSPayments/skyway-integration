package com.ols.skyway.spring;

import com.ols.skyway.jaxb.ISOMessage;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ISOMessageSerializationController {

  @RequestMapping(value = "/jaxb", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
  public ISOMessage jaxb(@RequestBody ISOMessage isoMessage) {
    return isoMessage;
  }

  @RequestMapping(value = "/custom", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
  public byte[] customDatabind(@iISOMsg ISOMsg isoMsg) throws ISOException {
    return isoMsg.pack();
  }

}
