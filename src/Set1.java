import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Set1 {

  private static final byte[] BASE64_TABLE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes();

  // Challenge 1
  public static byte[] hex2base64(byte[] hex) {
    int len = (hex.length)/3*4;
    if (hex.length/3*3 != hex.length) {
      len = ((hex.length)/3 + 1)*4;
    }
    byte[] base64 = new byte[len];
    int buflen = 0;
    int bits = 0;
    int j = 0;
    for (int i = 0; i < hex.length; i++) {
      bits = (bits << 8);
      bits = bits | (hex[i] & 0x0FF);
      buflen += 8;
      while (buflen >= 6) {
        base64[j] = (byte)((bits >> (buflen - 6)) & 0x3F);
        bits = (bits & (0x0FFF >> (buflen - 6)));
        buflen -= 6;
        j++;
      }
    }
    while (buflen > 0) {
      base64[j] = (byte)((bits >> (buflen - 6)) & 0x3F);
      bits = (bits & (0x0FFF >> (buflen - 6)));
      buflen -= 6;
      j++;
    }
    byte[] base64Real = new byte[len];
    for (int i = 0; i < base64.length; i++) {
      if (i < j) {
        base64Real[i] = BASE64_TABLE[base64[i]];
      } else {
        base64Real[i] = '=';
      }
    }
    return base64Real;
  }

  // Challenge 2
  public static byte[] xor(byte[] set1, byte[] set2) {
    byte[] result = new byte[set1.length];
    for (int i = 0; i < set1.length; i++) {
      result[i] = (byte)(set1[i] ^ set2[i]);
    }
    return result;
  }

  public static boolean isAsciiPrintable(char ch) {
    return ch >= 32 && ch < 127;
  }

  // Challenge 3 (answer is 0x58)
  public static byte bruteForceXor(byte[] encoded) {
    byte[] decoded = new byte[encoded.length];
    byte[] testDecoded = new byte[encoded.length];
    int score = -1;
    byte ch = 0;
    for (int i = 0; i < 0x0FF; i++) {
      int proposedScore = 0;
      for (int j = 0; j < encoded.length; j++) {
        testDecoded[j] = (byte)(encoded[j] ^ i);
        if ("aeiou ".contains((char)testDecoded[j] + "")) {
          proposedScore++;
        }
      }
      if (proposedScore > score) {
        decoded = testDecoded.clone();
        score = proposedScore;
        ch = (byte)i;
      }
    }
    //System.out.printf("%x -> %s\n", ch, new String(decoded));
    return ch;
  }

  // Challenge 4 line 170 key is 0x35

  // Challenge 5
  public static byte[] repeatXor(byte[] data, byte[] key) {
    byte[] longkey = new byte[data.length];
    for (int i = 0; i < longkey.length; i++) {
      longkey[i] = key[i % key.length];
    }
    return xor(data, longkey);
  }

  // Challenge 6
  public static int hammingDistance(byte[] set1, byte[] set2) {
    int distance = 0;
    for (int i = 0; i < set1.length; i++) {
      distance += Integer.bitCount(set1[i] ^ set2[i]);
    }
    return distance;
  }

  // Challenge 6
  // Key length: 29
  // Key: 5465726d696e61746f7220583a204272696e6720746865206e6f697365
  public static byte[] breakRepeatedKeyXor(byte[] data) {
    List<Double> distances = new ArrayList<>();
    Map<Integer, Double> keySizeMap = new HashMap<>();
    for (int possibleKeySize = 2; possibleKeySize < 40; possibleKeySize++) {
      byte[][] d = new byte[4][];
      for (int i = 0; i < 4; i++) {
        d[i] = Arrays.copyOfRange(data, i*possibleKeySize, (i+1)*possibleKeySize);
      }
      double distance = (hammingDistance(d[0],d[1]) +
          hammingDistance(d[0],d[2]) +
          hammingDistance(d[0],d[3]) +
          hammingDistance(d[1],d[2]) +
          hammingDistance(d[1],d[3]) +
          hammingDistance(d[2],d[3]))/possibleKeySize;
      keySizeMap.put(possibleKeySize, distance);
      distances.add(distance);
    }

    Collections.sort(distances);
    //System.out.println(distances);
    Set<Integer> possibleKeySizes = new HashSet<>();
    for (int i = 0; i < 4; i++) {
      double distance = distances.get(i);
      for (Integer keysize : keySizeMap.keySet()) {
        if (keySizeMap.get(keysize) == distance) {
          possibleKeySizes.add(keysize);
        }
      }
    }
    //System.out.printf("Possible key sizes: %s\n", possibleKeySizes);

    List<byte[]> possibleKeys = new ArrayList<>();
    for (int keysize : possibleKeySizes) {
      int blockCount = data.length / keysize + 1;
      byte[][] blocks1 = new byte[blockCount][];
      for (int i = 0; i < blockCount; i++) {
        if ((i+1)*keysize < data.length) {
          blocks1[i] = Arrays.copyOfRange(data, i * keysize, (i + 1) * keysize);
        } else {
          blocks1[i] = new byte[keysize];
          for (int j = i * keysize, k = 0; j < data.length; j++, k++) {
            blocks1[i][k] = data[j];
          }
        }
      }

      byte[][] blocks2 = new byte[keysize][];
      for (int i = 0; i < keysize; i++) {
        blocks2[i] = new byte[blockCount];
        for (int j = 0; j < blockCount; j++) {
          blocks2[i][j] = blocks1[j][i];
        }
      }

      byte[] key = new byte[keysize];
      for (int i = 0; i < keysize; i++) {
        byte ch = bruteForceXor(blocks2[i]);
        key[i] = ch;
      }
      possibleKeys.add(key);
    }

    byte[] decoded = null;
    byte[] finalKey = null;
    int score = -1;
    for (byte[] key : possibleKeys) {
      byte[] result = repeatXor(data, key);
      int proposedScore = 0;
      for (int i = 0; i < result.length; i++) {
        if ("aeiou ".contains((char) result[i] + "")) {
          proposedScore++;
        }
      }
      if (proposedScore > score) {
        decoded = result;
        score = proposedScore;
        finalKey = key;
      }
      //System.out.printf("\n=====\n(%d) %s -> %s\n=====\n", key.length, Util.bytes2hex(key), new String(result));
    }

    return finalKey;
  }
}
