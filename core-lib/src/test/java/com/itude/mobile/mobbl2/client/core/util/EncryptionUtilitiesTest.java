package com.itude.mobile.mobbl2.client.core.util;

import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.junit.Test;

public class EncryptionUtilitiesTest extends TestCase
{

  private final String TESTSTRING   = "toplinealex";
  //  private final String TESTSTRING2  = "bladiebla3";
  private final String ENCODINGTYPE = "Windows-1252";

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

  //  @Test
  //  public void testEncryptMetBase() throws UnsupportedEncodingException
  //  {
  //    byte[] result = EncryptionUtilities.encrypt(TESTSTRING.toUpperCase(), TESTSTRING.toUpperCase());
  //    assertEquals("77+977+977+977+977+977+977+977+9X++/vQ8=", Base64.encodeBase64String(ByteUtil.encodeBytes(result, "UTF-8")).trim());
  //  }
  //
  //  @Test
  //  public void testEncryptMetBaseWindowsTyped() throws UnsupportedEncodingException
  //  {
  //    byte[] result = EncryptionUtilities.encrypt(TESTSTRING.toUpperCase(), TESTSTRING.toUpperCase());
  //    assertEquals("w53DlcOlw7LDiMOvw4fDil/DkA8=", Base64.encodeBase64String(ByteUtil.encodeBytes(result, ENCODINGTYPE)).trim());
  //  }
  //
  //  @Test
  //  public void testEncryptSleutel() throws UnsupportedEncodingException
  //  {
  //    byte[] result = EncryptionUtilities.encrypt(TESTSTRING.toUpperCase(), TESTSTRING2.toUpperCase());
  //    byte[] result2 = EncryptionUtilities.encrypt(TESTSTRING2.toUpperCase(), TESTSTRING2.toUpperCase());
  //    byte[] result3 = EncryptionUtilities.encrypt(TESTSTRING.toUpperCase().getBytes(), result2);
  //
  //    String hashA = Base64.encodeBase64String(ByteUtil.encodeBytes(result, ENCODINGTYPE));
  //    String hashB = Base64.encodeBase64String(ByteUtil.encodeBytes(result3, ENCODINGTYPE));
  //    assertEquals("w4vDlsO0w7rDiMOkw4DDh1LCpg==", hashA.trim());
  //    assertEquals("Qx7DtMOUKMO6LivDh3s=", hashB.trim());
  //  }
  //
  //  @Test
  //  public void testEncryptSleutel2() throws UnsupportedEncodingException
  //  {
  //    byte[] result = EncryptionUtilities.encrypt(TESTSTRING2.toUpperCase(), TESTSTRING.toUpperCase());
  //    byte[] result2 = EncryptionUtilities.encrypt(TESTSTRING.toUpperCase(), TESTSTRING.toUpperCase());
  //    byte[] result3 = EncryptionUtilities.encrypt(TESTSTRING2.toUpperCase().getBytes(), result2);
  //
  //    String hashA = Base64.encodeBase64String(ByteUtil.encodeBytes(result, ENCODINGTYPE));
  //    String hashB = Base64.encodeBase64String(ByteUtil.encodeBytes(result3, ENCODINGTYPE));
  //
  //    assertEquals("w5zigKFQYsKpUMKrwq3Dmcuccw==", hashA.trim());
  //    assertEquals("VR3DpcOcKMOxKSbDig0k", hashB.trim());
  //  }

}
