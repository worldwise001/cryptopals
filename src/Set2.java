import java.util.Arrays;
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
        return key;
    }

    public static byte[] encryptionOracle(byte[] input) {
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
}
