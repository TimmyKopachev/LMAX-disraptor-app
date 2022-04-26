package org.lmax.disruptor;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import java.util.concurrent.ThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.lmax.disruptor.demo.SingleTradeConsumer;
import org.lmax.disruptor.demo.SingleTradeEventProducer;
import org.lmax.disruptor.demo.TradeEvent;
import org.lmax.disruptor.demo.TradeEventFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@Slf4j
@SpringBootApplication
public class LMAXDisruptorRunner implements ApplicationRunner {

  public static void main(String[] args) {
    new SpringApplicationBuilder()
        .sources(LMAXDisruptorRunner.class)
        .bannerMode(Banner.Mode.OFF)
        .run(args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    ThreadFactory threadFactory = DaemonThreadFactory.INSTANCE;
    WaitStrategy waitStrategy = new BusySpinWaitStrategy();
    Disruptor<TradeEvent> disruptor =
        new Disruptor<>(
            new TradeEventFactory(), 16, threadFactory, ProducerType.SINGLE, waitStrategy);

    disruptor.handleEventsWith(
        new SingleTradeConsumer(), new SingleTradeConsumer(), new SingleTradeConsumer());
    RingBuffer<TradeEvent> ringBuffer = disruptor.start();

    new SingleTradeEventProducer().startProducing(ringBuffer);
  }
}
