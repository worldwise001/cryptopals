import java.util.Base64;

public class Main {

    private static final String TEXT =
            "Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkg" +
            "aGFpciBjYW4gYmxvdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBq" +
            "dXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUg" +
            "YnkK";

    public static void main(String[] args) {
        String text = new String(Base64.getDecoder().decode(TEXT));
        String dec = Set2.attemptDecryptECB(text);
        System.out.println(dec);
        System.out.println(text.equalsIgnoreCase(dec));
    }
}
