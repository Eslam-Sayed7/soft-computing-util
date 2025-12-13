package softcomputing.genetic;

import java.util.Scanner;
import java.util.logging.Level;

import softcomputing.utils.AppLogger;
import softcomputing.utils.TestCases;

public class SoftUtilApplication {

    private static final AppLogger logger = AppLogger.getLogger(SoftUtilApplication.class);

    public static void main(String[] args) {

        AppLogger.configure(Level.INFO);

        logger.info("=== SoftUtil Genetic Algorithm Examples ===\n");
        logger.info("Choose chromosome type:");
        logger.info("1. Binary Chromosome");
        logger.info("2. Integer Chromosome");
        logger.info("3. FloatingPoint Chromosome");
        logger.info("4. IntChromosome , SP-Crossover ,  IntMutation , GReplacement");
        logger.info("5. BinChromosome , Uniform-Crossover , BinMutation , SSReplacement");
        logger.info("6. FloatChromosome , RnkSel, 2Pt-Crossover , NotUniMutation , EliteReplacement");
        logger.info("===========================================");
        System.out.print("\nEnter your choice (1, 2, 3, 4, 5, 6): ");

        try (Scanner scanner = new Scanner(System.in)) {
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> TestCases.runBinaryExample();
                case 2 -> TestCases.runIntegerExample();
                case 3 -> TestCases.runFloatingPointExample();
                case 4 -> TestCases.runInteger_SinglePCrsv_IntMut_FlReplacement();
                case 5 -> TestCases.runBin_UniformCrsv_BinMut_SSReplacement();
                case 6 -> TestCases.runFloating_RnkSele_2CrsOv_ElitRep();
                default -> logger.warn("Invalid choice. Please run again and select 1, 2, or 3.");
            }
        }
    }
}
