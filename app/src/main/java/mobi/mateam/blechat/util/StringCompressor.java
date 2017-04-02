package mobi.mateam.blechat.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class StringCompressor {
  public static byte[] compress(String text) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    try {
      OutputStream outputStream = new DeflaterOutputStream(byteArrayOutputStream);
      outputStream.write(text.getBytes("UTF-8"));
      outputStream.close();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    return byteArrayOutputStream.toByteArray();
  }

  public static String decompress(byte[] bytes) {
    InputStream in = new InflaterInputStream(new ByteArrayInputStream(bytes));
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    try {
      byte[] buffer = new byte[8192];
      int len;
      while((len = in.read(buffer))>0)
        byteArrayOutputStream.write(buffer, 0, len);
      return new String(byteArrayOutputStream.toByteArray(), "UTF-8");
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }
}
