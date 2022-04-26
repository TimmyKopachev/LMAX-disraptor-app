package org.lmax.disruptor.demo;

import lombok.Data;
import org.lmax.disruptor.model.Trade;

@Data
public class TradeEvent {

  private Trade trade;
}
