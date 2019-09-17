import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    private static final double AGRESSIVE_RETURN = 9.4324;
    private static final double AGRESSIVE_RISK = 15.675;
    private static final double CONSERVATIVE_RETURN = 6.189;
    private static final double CONSERVATIVE_RISK = 6.3438;
    private static final int SIMULATION_COUNT = 10000;

    public static void main(String[] args) {

        double initialCapital = 100000.0;

        //aggressive calculations
        double[] agressiveSimulationResults = getSimulationResults(initialCapital, AGRESSIVE_RETURN / 100, AGRESSIVE_RISK/ 100, SIMULATION_COUNT);
        double agressiveMedian = getMedian(agressiveSimulationResults, SIMULATION_COUNT);
        double agressive90thPercentile = getPercentile(agressiveSimulationResults, 90);
        double agressive10thPercentile = getPercentile(agressiveSimulationResults, 10);

        //conservative calculations
        double[] conservativeSimulationResults = getSimulationResults(initialCapital, CONSERVATIVE_RETURN / 100, CONSERVATIVE_RISK/ 100, SIMULATION_COUNT);
        double conservtiveMedian = getMedian(conservativeSimulationResults, SIMULATION_COUNT);
        double conservative90thPercentile = getPercentile(conservativeSimulationResults, 90);
        double conservative10thPercentile = getPercentile(conservativeSimulationResults, 10);

        String leftAlignFormat = "| %-20s | %-15f | %-15f | %-15f |%n";

        System.out.format("+----------------------+------------------+---------------+-------------------+%n");
        System.out.println("|   Portfolio Type     | Median 20th Year | 10% Best Case | 10% Worst Case   |");
        System.out.format("+----------------------+------------------+---------------+-------------------+%n");
        System.out.format(leftAlignFormat, "A-AGRESSIVE" ,  agressiveMedian, agressive90thPercentile, agressive10thPercentile);
        System.out.format(leftAlignFormat, "I-VERY CONSERVATIVE" , conservtiveMedian, conservative90thPercentile, conservative10thPercentile);
        System.out.format("+----------------------+------------------+---------------+-------------------+%n");

    }

    public static double[] getSimulationResults(double initialCapital, double mean, double standardDeviation, int simulationCount ){

        Random random = new Random();
        double resultCapital = initialCapital;

        NormalDistribution distribution = new NormalDistribution(mean, standardDeviation);

        double[] results = new double[simulationCount];
        for (int j = 0; j < simulationCount; j++) {
            for (int i = 0; i < 20; i++) {
                double probability = random.nextDouble();
                double netProfit = distribution.inverseCumulativeProbability(probability);
                resultCapital = resultCapital * (1 + netProfit);
                resultCapital = resultCapital * 0.965;

            }
            results[j] = resultCapital;
            resultCapital = initialCapital;
        }
        return results;
    }

    public static double getMedian(double[] simulationResults, int simulationCount) {

        Arrays.sort(simulationResults);
        int mid = simulationCount / 2;
        int mod = (simulationCount + 1) % 2;
        return (simulationResults[mid] + simulationResults[mid + mod]) / 2;

    }

    public static double getPercentile(double[] simulationResults, int nthPercentile) {

        Percentile percentile = new Percentile();
        percentile.setData(simulationResults);

        return percentile.evaluate(nthPercentile);
    }
}
