import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Set2 {
    // Don't do this at home
    private static Random rand = new Random();

    // Challenge 9
    public static byte[] pkcs7pad(byte[] data, int length) {
        byte[] result = Arrays.copyOf(data, length);
        byte pad = (byte)(length - data.length);
        for (int i = 0; i < pad; i++) {
            result[result.length-1-i] = pad;
        }
        return result;
    }

    // Challenge 10 is AES.decryptCBC

    // Challenge 11
    public static byte[] randomKey() {
        byte[] key = new byte[16];
        rand.nextBytes(key);
        for (int i = 0; i < key.length; i++) {
            System.out.printf("%02x, ", key[i]);
        }
        System.out.println();
        return key;
    }

    public static byte[] encryptionOracle11(byte[] input) {
        int start = rand.nextInt(5) + 5;
        int end = rand.nextInt(5) + 5;
        byte[] key = randomKey();
        byte[] data = new byte[input.length + start + end];
        for (int i = 0; i < data.length; i++) {
            if (i >= start && i < (input.length + start)) {
                data[i] = input[i-start];
            } else {
                data[i] = (byte)rand.nextInt();
            }
        }
        if (rand.nextBoolean()) {
            System.out.println("ECB");
            return AES.encryptECB(data, key);
        } else {
            byte[] iv = randomKey();
            System.out.println("CBC");
            return AES.encryptCBC(data, key, iv);
        }
    }

    public static void detectEncryption(byte[] input) {
        String hexString = "";
        for (int i = 0; i < input.length; i++) {
            hexString += String.format("%02x", input[i]);
        }
        int dupes = Set1.countDupes(hexString);
        if (dupes > 1) {
            System.out.println("ECB");
        } else {
            System.out.println("CBC");
        }
    }

    // Challenge 12
    private static byte[] key12 = new byte[]{0x59, 0x24, 0x35, (byte)0xe3, (byte)0x83, (byte)0xa1, (byte)0xe0, (byte)0xe5, (byte)0xe3, 0x7d, (byte)0xaf, 0x52, (byte)0xb1, 0x77, 0x5e, 0x77};
    public static byte[] encryptionOracle12(byte[] input) {
        byte[] key = key12;
        return AES.encryptECB(input, key);
    }

    public static String attemptDecryptECB(String mystery) {
        String decrypted = "";
        String start = "aaaaaaaaaaaaaaa";
        mystery += "aaaaaaaaaaaaaaaa";
        while (mystery.length() > 16)
        {
            String input = start + mystery;
            byte[] enc = encryptionOracle12(input.getBytes());
            byte[] encsh = Arrays.copyOf(enc, 16);
            for (int i = 0; i < 256; i++) {
                input = start + (char)i + mystery;
                byte[] p = encryptionOracle12(input.getBytes());
                byte[] psh = Arrays.copyOf(p, 16);
                if (Arrays.equals(encsh, psh)) {
                    decrypted += (char)i;
                    start = start.substring(1) + (char)i;
                    mystery = mystery.substring(1);
                    break;
                }
            }
        }
        return decrypted;
    }
}
