package test;

import com.backblaze.erasure.fec.Snmp;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import kcp.KcpChannelConfig;
import kcp.KcpClient;
import kcp.KcpListener;
import kcp.Ukcp;
import lombok.extern.slf4j.Slf4j;
import threadPool.thread.DisruptorExecutorPool;

import java.net.InetSocketAddress;

/**
 * 模拟帧同步
 * 50ms一帧
 * 20字节一个包
 * Created by JinMiao
 * 2019-06-25.
 */
@Slf4j
public class LockStepSynchronizationClient implements KcpListener {

    public static void main(String[] args) {
        String ip = "127.0.0.1";
        if (args.length > 0) {
            ip = args[0];
        }
        int number = 1000;
        if (args.length > 1) {
            number = Integer.parseInt(args[1]);
        }
        KcpClient kcpClient = new KcpClient();
        kcpClient.init(Runtime.getRuntime().availableProcessors());
        KcpChannelConfig kcpChannelConfig = KcpChannelConfig.builder().fastresend(2)
                .sndwnd(300).rcvwnd(300).mtu(500).interval(400).nocwnd(true).crc32Check(true).timeoutMillis(10000).build();
        LockStepSynchronizationClient lockStepSynchronizationClient = new LockStepSynchronizationClient();
        for (int i = 0; i < number; i++) {
            //模拟1000个用户链接
            kcpClient.connect(new InetSocketAddress(ip, 10009), kcpChannelConfig, lockStepSynchronizationClient);
        }

        DisruptorExecutorPool.scheduleWithFixedDelay(()->{
            log.info("每秒收包:{} M",Snmp.snmp.InBytes.longValue()/1024/1024*8);
            log.info("每秒发包:{} M",Snmp.snmp.OutBytes.longValue() / 1024 / 1024 * 8);
            Snmp.snmp=new Snmp();
        },1000);
    }


    @Override
    public void onConnected(Ukcp ukcp) {
        //模拟按键事件
        DisruptorExecutorPool.scheduleWithFixedDelay(() -> {
            ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer(20);
            byteBuf.writeBytes(new byte[20]);
            ukcp.write(byteBuf);
            byteBuf.release();
        }, 50);
    }

    @Override
    public void handleReceive(ByteBuf byteBuf, Ukcp ukcp) {
        //System.out.println("收到数据"+byteBuf.readableBytes());
    }

    @Override
    public void handleException(Throwable ex, Ukcp ukcp) {

    }

    @Override
    public void handleClose(Ukcp ukcp) {

    }
}
