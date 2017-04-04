package mobi.mateam.blechat.util;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class StringCompressorTest {

  @Test public void compress() throws Exception {
    String test = "фывфывфывфывыфвыфвфывфыв3к3425435иавпир6тор6тדגכדגכעכגדע דגגדכדגכג דגדכ דגכ דגכ דגכ דגכ רעאכח עיחל יחת חל ןםץףפם.]Some new cool string for testing with different characters1234567890)(*&^%$#@!1231231231231231231234ewrsdfdbgfnhgnymui,ik8,i,i..p./p;o/p[/[/[/0p[/0.0.";
    byte[] testBytes = test.getBytes();

    byte[] afterCompression = StringCompressor.compress(test);

    assertTrue(testBytes.length>afterCompression.length);
  }

  @Test public void decompress() throws Exception {

  }
}