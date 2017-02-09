public class Main {

  public static void main(String[] args) {
      // 22 b7 68 9e 59 c8 e2 9a 25 01 35 ad fd 50 85 21
      byte[] key = "YELLOW SUBMARINE".getBytes();
      byte[] bytes = AES.encrypt("marine sub green".getBytes(), key);
      for (int i = 0; i < bytes.length; i++) {
          System.out.printf("%02x ", bytes[i]);
      }
      System.out.println();
      byte[] decrypted = AES.decrypt(bytes, key);
      System.out.println(new String(decrypted));
  }
}
