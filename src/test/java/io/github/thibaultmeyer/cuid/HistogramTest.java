package io.github.thibaultmeyer.cuid;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.MethodName.class)
final class HistogramTest {

    @Test
    void histogramCUIDv1() {

        // Arrange
        final List<Double> data = Stream.generate(CUID::randomCUID1)
            .limit(1_000_000)
            .map(Object::hashCode)
            .map(o -> (double) o)
            .collect(Collectors.toList());

        // Act
        final Histogram histogram = new Histogram(data);
        final Map<Integer, Integer> frequencies = histogram.histogram(20);

        // Assert
        System.out.println("CUID v1: " + frequencies);
    }

    @Test
    void histogramCUIDv2() {

        // Arrange
        final List<Double> data = Stream.generate(CUID::randomCUID2)
            .limit(1_000_000)
            .map(Object::hashCode)
            .map(o -> (double) o)
            .collect(Collectors.toList());

        // Act
        final Histogram histogram = new Histogram(data);
        final Map<Integer, Integer> frequencies = histogram.histogram(20);

        // Assert
        System.out.println("CUID v2: " + frequencies);
    }

    public static final class Histogram {

        private final List<Double> data;

        public Histogram(List<Double> data) {

            this.data = data;
        }

        private static Integer findBin(Double datum, int bins, double min, double max) {

            final double binWidth = (max - min) / bins;
            if (datum >= max) {
                return bins - 1;
            } else if (datum <= min) {
                return 0;
            }
            return (int) Math.floor((datum - min) / binWidth);
        }

        public Map<Integer, Integer> histogram(int bins) {

            final DoubleSummaryStatistics statistics = this.data.stream()
                .mapToDouble(x -> x)
                .summaryStatistics();

            final double max = statistics.getMax();
            final double min = statistics.getMin();

            // Make sure that all bins are initialized
            final Map<Integer, Integer> histogram = IntStream.range(0, bins)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), x -> 0));

            histogram.putAll(
                this.data.stream().collect(
                    Collectors.groupingBy(
                        x -> findBin(x, bins, min, max),
                        Collectors.mapping(x -> 1, Collectors.summingInt(x -> x)))));

            return histogram;
        }
    }
}
