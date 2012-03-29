package com.itude.mobile.mobbl2.client.core.util;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class EncryptionUtilitiesTest
{

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
    byte[] result = EncryptionUtilities.encrypt("TOPLINEALEX", "TOPLINEALEX");
    assertEquals("ÝÕåòÈïÇÊ_Ð", ByteUtil.encodeBytesToString(result, "Windows-1252").trim());
  }

  @Test
  public void testEncryptMetBase() throws UnsupportedEncodingException
  {
    byte[] result = EncryptionUtilities.encrypt("TOPLINEALEX", "TOPLINEALEX");
    assertEquals("w53DlcOlw7LDiMOvw4fDil/DkA8=", Base64.encodeBase64String(ByteUtil.encodeBytes(result, "Windows-1252")).trim());
  }

  @Test
  public void testEncryptSleutel() throws UnsupportedEncodingException
  {
    byte[] result = EncryptionUtilities.encrypt("toplinealex".toUpperCase(), "bladiebla3".toUpperCase());
    byte[] result2 = EncryptionUtilities.encrypt("bladiebla3".toUpperCase(), "bladiebla3".toUpperCase());
    byte[] result3 = EncryptionUtilities.encrypt("toplinealex".toUpperCase().getBytes(), result2);

    String hashA = Base64.encodeBase64String(ByteUtil.encodeBytes(result, "Windows-1252"));
    String hashB = Base64.encodeBase64String(ByteUtil.encodeBytes(result3, "Windows-1252"));
    assertEquals("w4vDlsO0w7rDiMOkw4DDh1LCpg==", hashA.trim());
    assertEquals("Qx7DtMOUKMO6LivDh3s=", hashB.trim());
  }

  @Test
  public void testEncryptSleutel2() throws UnsupportedEncodingException
  {
    byte[] result = EncryptionUtilities.encrypt("bladiebla3".toUpperCase(), "toplinealex".toUpperCase());
    byte[] result2 = EncryptionUtilities.encrypt("toplinealex".toUpperCase(), "toplinealex".toUpperCase());
    byte[] result3 = EncryptionUtilities.encrypt("bladiebla3".toUpperCase().getBytes(), result2);

    String hashA = Base64.encodeBase64String(ByteUtil.encodeBytes(result, "Windows-1252"));
    String hashB = Base64.encodeBase64String(ByteUtil.encodeBytes(result3, "Windows-1252"));

    assertEquals("w5zigKFQYsKpUMKrwq3Dmcuccw==", hashA.trim());
    assertEquals("VR3DpcOcKMOxKSbDig0k", hashB.trim());
  }

}
