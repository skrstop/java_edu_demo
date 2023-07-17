package edu.jphoebe.demo.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author 蒋时华
 * @date 2022-09-16 18:32:59
 */
@Component
public class KafkaConsumer {

    //kafka的监听器，topic为"zhTest"，消费者组为"zhTestGroup"
//    @KafkaListener(topics = "zhTest", groupId = "zhTestGroup")

    @KafkaListener(groupId = "zhTestGroup"
            , topicPartitions = {@TopicPartition(topic = "zhTest", partitions = {"0", "1"})}
            , autoStartup = "#{${kafka.consume.enable}}")
    public void listenZhugeGroup(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String value = record.value();
        System.out.println(record.offset() + "  ||  " + value);
//        System.out.println(record);
        //手动提交offset
        ack.acknowledge();
    }

    /*//配置多个消费组
    @KafkaListener(topics = "zhTest",groupId = "zhTestGroup2")
    public void listenTulingGroup(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String value = record.value();
        System.out.println(value);
        System.out.println(record);
        ack.acknowledge();
    }*/
}
