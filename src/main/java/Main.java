import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {


    public static void main(String[] args) {
        Random random = new Random();
        double profit = 9.4324;
        double risk = 15.675;

//        double profit = 6.189;
//        double risk = 6.3438;

        double mean = profit / 100;
        double std = risk / 100;
        int simulationCount = 10000;

        double initialCapital = 100000.0;

        double resultCapital = initialCapital;

        NormalDistribution distribution = new NormalDistribution(mean, std);

        double[] results = new double[simulationCount];
        for (int j = 0; j < simulationCount; j ++) {
            for(int i = 0; i < 20; i ++) {
                double probability = random.nextDouble();
                double netProfit = distribution.inverseCumulativeProbability(probability);
                resultCapital = resultCapital * (1 + netProfit);
                resultCapital = resultCapital * 0.965;

            }
            results[j] = resultCapital;
            resultCapital = initialCapital;
        }

        Arrays.sort(results);
        int mid = simulationCount / 2;
        int mod = (simulationCount + 1) % 2;
        double median = (results[mid] + results[mid + mod]) / 2;

        Percentile percentile = new Percentile();
        percentile.setData(results);

        System.out.println(median);
        System.out.println(percentile.evaluate(90));
        System.out.println(percentile.evaluate(10));
    }
}
