public class Main {

    public static void main(String[] args) {
        byte[] result = Set2.pkcs7pad("YELLOW SUBMARINE".getBytes(), 20);
        for (int i = 0; i < result.length; i++) {
            System.out.printf("%02x ", result[i]);
        }
    }
}
