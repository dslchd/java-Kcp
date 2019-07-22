import com.backblaze.erasure.fec.Snmp;
import io.netty.buffer.ByteBuf;
import kcp.KcpChannelConfig;
import kcp.KcpListener;
import kcp.KcpServer;
import kcp.Ukcp;

/**
 *
 * mbp 2.3 GHz Intel Core i9 16GRam 单连接 带fec 5W/s qps 单连接 不带fec 8W/s qps
 * Created by JinMiao
 * 2019-06-27.
 */
public class KcpPingPongExampleServer implements KcpListener {

    public static void main(String[] args) {

        KcpPingPongExampleServer kcpRttExampleServer = new KcpPingPongExampleServer();
//        KcpChannelConfig kcpChannelConfig = new KcpChannelConfig();
////        kcpChannelConfig.setFastresend(2);
////        kcpChannelConfig.setSndwnd(1024);
////        kcpChannelConfig.setRcvwnd(1024);
////        kcpChannelConfig.setMtu(1400);
////        kcpChannelConfig.setFecDataShardCount(10);
////        kcpChannelConfig.setFecParityShardCount(3);
////        kcpChannelConfig.setAckNoDelay(false);
////        kcpChannelConfig.setInterval(40);
////        kcpChannelConfig.setNocwnd(true);
////        kcpChannelConfig.setCrc32Check(true);
        //kcpChannelConfig.setTimeoutMillis(10000);
        KcpChannelConfig kcpChannelConfig =  KcpChannelConfig.builder().fastresend(2)
                .sndwnd(300).rcvwnd(300).mtu(500).interval(400).nocwnd(true).crc32Check(true).timeoutMillis(10000).build();
        KcpServer kcpServer = new KcpServer();
        kcpServer.init(Runtime.getRuntime().availableProcessors(), kcpRttExampleServer, kcpChannelConfig, 10001);
    }


    @Override
    public void onConnected(Ukcp ukcp) {
        System.out.println("有连接进来" + Thread.currentThread().getName() + ukcp.user().getRemoteAddress());
    }

    int i = 0;

    long start = System.currentTimeMillis();

    @Override
    public void handleReceive(ByteBuf buf, Ukcp kcp) {
        i++;
        long now = System.currentTimeMillis();
        if(now-start>1000){
            System.out.println("收到消息 time: "+(now-start) +"  message :" +i);
            start = now;
            i=0;
        }
        kcp.write(buf);
    }

    @Override
    public void handleException(Throwable ex, Ukcp kcp) {
        ex.printStackTrace();
    }

    @Override
    public void handleClose(Ukcp kcp) {
        System.out.println(Snmp.snmp.toString());
        Snmp.snmp= new Snmp();
        System.out.println("连接断开了");
    }
}