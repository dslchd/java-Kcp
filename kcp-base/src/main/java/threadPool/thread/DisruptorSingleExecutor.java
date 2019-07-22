package threadPool.thread;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import threadPool.task.ITask;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 基于 {@link #disruptor} 的单线程队列实现
 * @author King
 *
 */
public class DisruptorSingleExecutor implements IMessageExecutor {

	/**
	 * RingBuffer queue size
	 */
	private static final int RING_BUFFER_SIZE = 65536;
	
	// private WaitStrategy strategy = new BlockingWaitStrategy();
	
	private Disruptor<DisruptorHandler> disruptor = null;

	private RingBuffer<DisruptorHandler> buffer = null;
	
	private DisruptorEventFactory eventFactory = new DisruptorEventFactory();
	
	private static final DisruptorEventHandler handler = new DisruptorEventHandler();
	
	private AtomicBoolean isTop = new AtomicBoolean();
	

	/**线程名字**/
	private String threadName;

	private DisruptorThread currentThread;


	public DisruptorSingleExecutor(String threadName){
		this.threadName = threadName;
	}
	

	@SuppressWarnings("unchecked")
	public void start() {
		LoopThreadFactory loopThreadfactory = new LoopThreadFactory(this);
//		disruptor = new Disruptor<DisruptorHandler>(eventFactory, ringBufferSize, executor, ProducerType.MULTI, strategy);
		disruptor = new Disruptor<>(eventFactory, RING_BUFFER_SIZE, loopThreadfactory);
		buffer = disruptor.getRingBuffer();
		disruptor.handleEventsWith(DisruptorSingleExecutor.handler);
		disruptor.start();
	}
	

	
	/**主线程工厂**/
	private class LoopThreadFactory implements ThreadFactory {
		IMessageExecutor iMessageExecutor;

		public LoopThreadFactory(IMessageExecutor iMessageExecutor) {
			this.iMessageExecutor = iMessageExecutor;
		}

		public Thread newThread(Runnable r) {
			currentThread = new DisruptorThread(r,iMessageExecutor);
			currentThread.setName(threadName);
			return currentThread;
		}
	}
	

	static int num = 1;
	static long start = System.currentTimeMillis();
	static long lastNum = 0;


	public void stop() {
		if(isTop.get())
			return;
		disruptor.shutdown();

		isTop.set(true);
	}


	public static void main(String[] args) {
		DisruptorSingleExecutor disruptorSingleExecutor = new DisruptorSingleExecutor("aa");
		disruptorSingleExecutor.start();
		disruptorSingleExecutor.execute(() -> {
			System.out.println("hahaha");
		});


		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


	}

	public AtomicBoolean getIsTop() {
		return isTop;
	}
	

	public boolean isFull() {
		return !buffer.hasAvailableCapacity(1);
	}

	@Override
	public void execute(ITask iTask){
		Thread currentThread = Thread.currentThread();
		if(currentThread==this.currentThread){
			iTask.execute();
			return;
		}
		//		if(buffer.hasAvailableCapacity(1))
//		{
//			System.out.println("没有容量了");
//		}
		long next = buffer.next();
		DisruptorHandler testEvent = buffer.get(next);
		testEvent.setTask(iTask);
		buffer.publish(next);
	}
}
