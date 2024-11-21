package hms.Billing;

import java.io.Serial;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Date;

/**
 * Represents a block in the blockchain used for billing records.
 * Each block contains billing data, a hash of its contents, a reference to the previous block's hash,
 * and a timestamp. The hash is calculated using SHA-256 to ensure data integrity.
 */

public class Block implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // Added serialVersionUID
    
    public String hash;
    public String previousHash;
    private String data; // Billing data
    private long timeStamp;

    /**
     * Constructs a new {@code Block} with the specified data and previous hash.
     * The block's hash is calculated upon creation.
     *
     * @param data         The billing data to store in the block.
     * @param previousHash The hash of the previous block in the chain.
     */

    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    /**
     * Calculates the hash of the block based on its contents.
     * Uses SHA-256 hashing algorithm on the concatenation of the previous hash, timestamp, and data.
     *
     * @return The calculated hash as a hexadecimal string.
     */

    public String calculateHash() {
        String input = previousHash + Long.toString(timeStamp) + data;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the billing data stored in the block.
     *
     * @return The billing data as a string.
     */


    public String getData() {
        return data;
    }
}
