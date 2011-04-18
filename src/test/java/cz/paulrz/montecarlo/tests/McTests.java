package cz.paulrz.montecarlo.tests;

import cz.paulrz.montecarlo.GeometricBrownianMotionProcess;
import cz.paulrz.montecarlo.LogArrivedPointValuation;
import cz.paulrz.montecarlo.MonteCarloModel;
import cz.paulrz.montecarlo.SimpleAccumulator;
import junit.framework.TestCase;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.random.GaussianRandomGenerator;
import org.apache.commons.math.random.JDKRandomGenerator;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;

/**
 * User: paul
 * Date: 18/4/11
 * Time: 14:29 PM
 */
public class McTests extends TestCase {
    private GeometricBrownianMotionProcess process;
    private MonteCarloModel mcm;
    private SimpleAccumulator summary;

    // as GBM is a log-normal process, Logs of arrival points work better

    // GBM mean is x0*exp(mu*t), but log(x_t/x_0) = (mu-sigma^2/2)*t
    private final static double expectedMean = -0.5;
    // GBM variance = x0^2 * exp(2*mu*t) * (exp(sigma^2*t)-1) but log variance
    // is sigma^2*t
    private final static double expectedStdDev = 1.0;

    public McTests() {
        // mean should be 1.0, and stddev = sqrt(e-1)
        process = new GeometricBrownianMotionProcess(1.0, 0, 1.0);
        GaussianRandomGenerator grg = new GaussianRandomGenerator(
                new JDKRandomGenerator());
        LogArrivedPointValuation apv = new LogArrivedPointValuation();
        summary = new SimpleAccumulator();
        mcm = new MonteCarloModel<Double>(grg, process, 1.0, 100, apv, summary);
    }

    public void testMeanAndVariance() throws FunctionEvaluationException {
        int iters = mcm.addSamples(10000, 1e-5, 100);
        double mean = summary.stats.getMean();
        double stddev = summary.stats.getStandardDeviation();

        assertEquals(expectedMean, mean, 0.01);
        assertEquals(expectedStdDev, stddev, 0.05);
        System.out.println(iters);
    }

}