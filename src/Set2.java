import java.util.Arrays;

public class Set2 {
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
}
