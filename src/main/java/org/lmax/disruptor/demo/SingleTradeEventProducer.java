package org.lmax.disruptor.demo;

import com.lmax.disruptor.RingBuffer;
import java.math.BigDecimal;
import java.util.Date;
import lombok.SneakyThrows;
import org.lmax.disruptor.model.Trade;

public class SingleTradeEventProducer {

  public void startProducing(RingBuffer<TradeEvent> ringBuffer) {
    final Runnable producer = () -> produce(ringBuffer);
    new Thread(producer).start();
  }

  @SneakyThrows
  private void produce(RingBuffer<TradeEvent> ringBuffer) {
    long increment = 1;

    while (Boolean.TRUE) {
      final long sequence = ringBuffer.next();
      final TradeEvent tradeEvent = ringBuffer.get(sequence);
      tradeEvent.setTrade(generateTrade(increment));
      ringBuffer.publish(sequence);

      increment++;
      if (increment % 10 == 0) {
        Thread.sleep(5000);
      }
    }
  }

  private static Trade generateTrade(long increment) {
    return Trade.builder()
        .UUID(increment)
        .price(
            generateRandomBigDecimalFromRange(BigDecimal.valueOf(2500), BigDecimal.valueOf(10_000)))
        .date(new Date())
        .build();
  }

  public static BigDecimal generateRandomBigDecimalFromRange(BigDecimal min, BigDecimal max) {
    BigDecimal randomBigDecimal =
        min.add(BigDecimal.valueOf(Math.random()).multiply(max.subtract(min)));
    return randomBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
  }
}
