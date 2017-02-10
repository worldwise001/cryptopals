import java.util.Random;

public class Main {
    private static final Random rand = new Random();
    public static void main(String[] args) {
        for (int tries = 0; tries < 10; tries++) {
            String randomText = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
            /*
            int length = rand.nextInt(256) + 128;
            for (int i = 0; i < length; i++) {
                randomText += String.format("%c", (rand.nextInt(26) + 'a'));
            }*/
            System.out.println(randomText);
            byte[] enc = Set2.encryptionOracle(randomText.getBytes());
            Set2.detectEncryption(enc);
        }
    }
}
