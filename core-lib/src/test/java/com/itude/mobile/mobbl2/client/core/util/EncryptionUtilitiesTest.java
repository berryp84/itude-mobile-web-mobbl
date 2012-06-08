package com.itude.mobile.mobbl2.client.core.util;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class EncryptionUtilitiesTest extends TestCase
{

  private static final String TESTSTRING   = "toplinealex";
  private static final String ENCODINGTYPE = "windows-1252";

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

  public void testEncrypt() throws UnsupportedEncodingException
  {
    byte[] result = EncryptionUtilities.encrypt(TESTSTRING.toUpperCase(), TESTSTRING.toUpperCase());
    assertEquals("ÝÕåòÈïÇÊ_Ð", ByteUtil.encodeBytesToString(result, ENCODINGTYPE).trim());
  }

  @Test
  public void testEncrypt2() throws UnsupportedEncodingException
  {
    byte[] result = EncryptionUtilities.encrypt(TESTSTRING.toUpperCase(), TESTSTRING.toUpperCase());
    assertEquals("DDD5E5F2C8EFC7CA5FD00F", EncryptionUtilities.byte2string(result).toUpperCase());
    assertEquals("w53DlcOlw7LDiMOvw4fDil/DkA8=", Base64.encodeBase64String(ByteUtil.encodeBytes(result, ENCODINGTYPE)).trim());
  }

  @Test
  public void testEncryptError() throws UnsupportedEncodingException
  {
    byte[] result = EncryptionUtilities.encrypt("binckbank01".toUpperCase(), "binckbank01".toUpperCase());
    byte[] ba = new byte[]{2, 55, 24, -50, -76, 56, -115, 99, 14, -105, 68};
    assertEquals(Arrays.toString(ba), Arrays.toString(result));
    assertEquals(ByteUtil.encodeBytesToString(ba, ENCODINGTYPE), ByteUtil.encodeBytesToString(result, ENCODINGTYPE));

    assertEquals("023718CEB4388D630E9744", EncryptionUtilities.byte2string(result).toUpperCase());

    assertEquals(EncryptionUtilities.byte2string((ByteUtil.encodeBytes(ba, ENCODINGTYPE))),
                 EncryptionUtilities.byte2string((ByteUtil.encodeBytes(result, ENCODINGTYPE))));

    byte[] ba2 = new byte[]{2, 55, 24, -61, -114, -62, -76, 56, -62, -115, 99, 14, -30, -128, -108, 68};
    assertEquals(Arrays.toString(ba2), Arrays.toString(ByteUtil.encodeBytes(ba, ENCODINGTYPE)));

    assertTrue(Arrays.equals(ba2, ByteUtil.encodeBytes(ba, ENCODINGTYPE)));
    assertTrue(Arrays.equals(ba2, ByteUtil.encodeBytes(result, ENCODINGTYPE)));

    assertEquals("023718c38ec2b438c28d630ee2809444", EncryptionUtilities.byte2string((ByteUtil.encodeBytes(result, ENCODINGTYPE))));
    assertEquals("AjcYw47CtDjCjWMO4oCURA==", Base64.encodeBase64String(ByteUtil.encodeBytes(result, ENCODINGTYPE)).trim());
  }
}
