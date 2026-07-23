import java.security.SecureRandom;

public class PredictableSeed {
    void run() {
SecureRandom prng = new SecureRandom();
int randomData = 0;

// BAD: Using a constant value as a seed for a random number generator means all numbers it generates are predictable.
prng.setSeed(12345L);
randomData = prng.nextInt();

// BAD: System.currentTimeMillis() returns the system time which is predictable.
prng.setSeed(System.currentTimeMillis());
randomData = prng.nextInt();

// GOOD: SecureRandom implementations seed themselves securely by default.
prng = new SecureRandom();
randomData = prng.nextInt();
    }
}
