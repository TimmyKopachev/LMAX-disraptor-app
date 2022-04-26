package org.lmax.disruptor.demo;

import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SingleTradeConsumer implements EventHandler<TradeEvent> {

  @Override
  public void onEvent(TradeEvent tradeEvent, long sequence, boolean endOfBatch) throws Exception {
    var trade = tradeEvent.getTrade();
    log.info("Trade UUID:{} and price:{}", trade.getUUID(), trade.getPrice());
  }
}
