public class Main {

  public static void main(String[] args) {
      byte[] bytes = AES.encrypt("marine sub green".getBytes(), "YELLOW SUBMARINE".getBytes());
      for (int i = 0; i < bytes.length; i++) {
          System.out.printf("%02x ", bytes[i]);
      }
  }
}
