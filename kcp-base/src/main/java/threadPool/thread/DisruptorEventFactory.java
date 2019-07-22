package threadPool.thread;

import com.lmax.disruptor.EventFactory;

public class DisruptorEventFactory implements EventFactory<DisruptorHandler>
{

	public DisruptorHandler newInstance() {
		return new DisruptorHandler();
	}

}
