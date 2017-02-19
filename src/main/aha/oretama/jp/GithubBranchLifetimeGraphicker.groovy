package aha.oretama.jp

import groovy.json.JsonSlurper
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartUtilities
import org.jfree.chart.JFreeChart
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.time.Day
import org.jfree.data.time.TimeSeries
import org.jfree.data.time.TimeSeriesCollection

/**
 * @author aha-oretama
 * @Date 2017/02/19
 */


@Grapes([
        @Grab(group = 'org.jfree', module = 'jfreechart', version = '1.0.19'),
        @GrabConfig(systemClassLoader = true)
])


TimeSeriesCollection data = new TimeSeriesCollection();
def series = new TimeSeries('pull request time to merge',Day)
data.addSeries(series)

JsonSlurper slurper = new JsonSlurper()
def json = slurper.parse(new File('./pullRequest.json'))

def format = '''yyyy-MM-dd'T'HH:mm:ssZ'''
json.each { pullRequest ->
    def merged = Date.parse(format, pullRequest.merged)
    def time = merged.minus(Date.parse(format, pullRequest.created)).doubleValue()
    series.add(new Day(merged), time)
}

JFreeChart chart = ChartFactory.createScatterPlot("Pull Request's time to merge",
        "date",
        "time to merge",
        data,
        PlotOrientation.VERTICAL,
        true,
        false,
        false);

File file = new File("./pullRequestGraph.png");
try {
    ChartUtilities.saveChartAsPNG(file, chart, 300, 300);
} catch (IOException e) {
    e.printStackTrace();
}