package aic.g3t1.consumer.sink.notifications;

import aic.g3t1.common.model.notification.TaxiNotification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

abstract class NotifySink extends BaseRichBolt {
    protected OutputCollector collector;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private HttpClient httpClient;

    protected void sendNotification(TaxiNotification notification, String endpoint) {
        try {
            var request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(notification)))
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
        } catch (Exception e) {
            System.err.println("Error POSTing notification request");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) { }

    @Override
    public void prepare(Map<String, Object> topoConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        this.prepareAdditional();
    }

    protected void prepareAdditional() {}
}
