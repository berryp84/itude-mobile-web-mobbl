package com.itude.mobile.mobbl2.client.core.util;

import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.junit.Test;

public class EncryptionUtilitiesTest extends TestCase
{

  private static final String TESTSTRING   = "toplinealex";
  private static final String ENCODINGTYPE = "Windows-1252";

  @Test
  public void testEncryptWiki1()
  {
    byte[] result = EncryptionUtilities.encrypt("Key", "Plaintext");
    assertEquals("BBF316E8D940AF0AD3", EncryptionUtilities.byte2string(result).toUpperCase());
  }

  @Test
  public void testEncryptWiki2()
  {
    byte[] result = EncryptionUtilities.encrypt("Wiki", "pedia");
    assertEquals("1021BF0420", EncryptionUtilities.byte2string(result).toUpperCase());
  }

  @Test
  public void testEncrypt() throws UnsupportedEncodingException
  {
    byte[] result = EncryptionUtilities.encrypt(TESTSTRING.toUpperCase(), TESTSTRING.toUpperCase());
    assertEquals("ÝÕåòÈïÇÊ_Ð", ByteUtil.encodeBytesToString(result, ENCODINGTYPE).trim());
  }
}
