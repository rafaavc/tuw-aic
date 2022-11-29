package aic.g3t1.consumer.sink;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;
import java.util.stream.Collectors;

public class DebugSink extends BaseRichBolt {

    private OutputCollector collector;

    @Override
    public void prepare(Map<String, Object> topoConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        System.out.println(input.toString());
        System.out.println(input.getFields());
        System.out.println(input.getValues().stream().map(v -> v.toString() + " (" + v.getClass() + ")").collect(Collectors.toList()));
        collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) { }

}
