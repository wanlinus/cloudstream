package cn.wanli.cloudstream;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;


@SpringBootApplication
public class CloudstreamApplication implements CommandLineRunner {
    public static final String IFAAS_TARGET = "ifaas-target";

    @Autowired
    private OutputInter outputInter;

    public static void main(String[] args) {
        SpringApplication.run(CloudstreamApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        A a = new A("name", "password0");
        outputInter.ifaasTarget().send(MessageBuilder.withPayload(JSON.toJSONString(a)).build());
    }
}

@Service
@EnableBinding({InPutInter.class, OutputInter.class})
class MsgService {

    @StreamListener(CloudstreamApplication.IFAAS_TARGET)
    public void input(Message<String> msg) {
        System.out.println("header: " + msg.getHeaders());
        System.out.println("playload: " + msg.getPayload());
    }
}

interface OutputInter {
    @Output(CloudstreamApplication.IFAAS_TARGET)
    MessageChannel ifaasTarget();
}

interface InPutInter {
    @Input(CloudstreamApplication.IFAAS_TARGET)
    SubscribableChannel ifaasInput();
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class A {
    private String name;
    private String password;
}