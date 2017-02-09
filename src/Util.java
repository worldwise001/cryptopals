public class Util {
    public static byte[] hex2bytes(String hex) {
        byte[] result = new byte[hex.length()/2];
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte)Integer.parseInt(hex.substring(i*2, i*2+2), 16);
        }
        return result;
    }

    public static String bytes2hex(byte[] bytes) {
        String result = "";
        for (int i = 0; i < bytes.length; i++) {
            result += String.format("%02x", bytes[i]);
        }
        return result;
    }
}
