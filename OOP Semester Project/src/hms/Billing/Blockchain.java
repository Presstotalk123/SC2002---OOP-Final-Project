package hms.Billing;


import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a blockchain used for billing records in the hospital management system.
 * The blockchain consists of a list of {@link Block} objects, each containing billing data.
 * It ensures the integrity and immutability of billing records by linking blocks with hashes.
 */

public class Blockchain implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // Added serialVersionUID

    public List<Block> chain;

     /**
     * Constructs a new {@code Blockchain} and initializes it with a genesis block.
     * The genesis block is the first block in the chain and has no previous hash.
     */

    public Blockchain() {
        chain = new ArrayList<>();
        // Create the genesis block
        chain.add(new Block("Genesis Block", "0"));
    }

    /**
     * Retrieves the latest block added to the blockchain.
     *
     * @return The most recent {@link Block} in the blockchain.
     */
    
    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    /**
     * Adds a new block to the blockchain after setting its previous hash
     * to the hash of the latest block and recalculating its own hash.
     *
     * @param newBlock The new {@link Block} to be added to the blockchain.
     */
    
    public void addBlock(Block newBlock) {
        newBlock.previousHash = getLatestBlock().hash;
        newBlock.hash = newBlock.calculateHash();
        chain.add(newBlock);
    }

    /**
     * Validates the integrity of the blockchain by checking if all blocks'
     * hashes are correctly calculated and that each block's previous hash
     * matches the hash of the preceding block.
     *
     * @return {@code true} if the blockchain is valid; {@code false} otherwise.
     */

    
    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                return false;
            }

            if (!currentBlock.previousHash.equals(previousBlock.hash)) {
                return false;
            }
        }
        return true;
    }
}
