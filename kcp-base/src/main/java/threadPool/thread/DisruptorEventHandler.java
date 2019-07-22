package threadPool.thread;

import com.lmax.disruptor.EventHandler;

public class DisruptorEventHandler implements EventHandler<DisruptorHandler>{

	public void onEvent(DisruptorHandler event, long sequence,
                        boolean endOfBatch) {
		event.execute();
	}
}
