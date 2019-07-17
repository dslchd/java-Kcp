import com.backblaze.erasure.fec.Snmp;
import io.netty.buffer.ByteBuf;
import kcp.ChannelConfig;
import kcp.KcpListener;
import kcp.KcpServer;
import kcp.Ukcp;

/**
 *
 * Created by JinMiao
 * 2018/11/2.
 */
public class KcpRttExampleServer implements KcpListener {

    public static void main(String[] args) {

        KcpRttExampleServer kcpRttExampleServer = new KcpRttExampleServer();

        ChannelConfig channelConfig = new ChannelConfig();
        channelConfig.setFastresend(2);
        channelConfig.setSndwnd(512);
        channelConfig.setRcvwnd(512);
        channelConfig.setMtu(1400);
        //channelConfig.setFecDataShardCount(10);
        //channelConfig.setFecParityShardCount(3);
        channelConfig.setAckNoDelay(false);
        channelConfig.setInterval(40);
        channelConfig.setNocwnd(true);
        channelConfig.setCrc32Check(true);
        channelConfig.setTimeoutMillis(10000);
        channelConfig.setAutoSetConv(true);
        KcpServer kcpServer = new KcpServer();
        kcpServer.init(Runtime.getRuntime().availableProcessors(), kcpRttExampleServer,channelConfig,10003);
    }


    @Override
    public void onConnected(Ukcp ukcp) {
        System.out.println("有连接进来"+Thread.currentThread().getName()+ukcp.user().getRemoteAddress());
    }

    @Override
    public void handleReceive(ByteBuf buf, Ukcp kcp) {
        short curCount = buf.getShort(buf.readerIndex());
        System.out.println(Thread.currentThread().getName()+"  收到消息 "+curCount);
        kcp.write(buf);
        if (curCount == -1) {
            kcp.notifyCloseEvent();
        }
    }

    @Override
    public void handleException(Throwable ex, Ukcp kcp) {
        ex.printStackTrace();
    }

    @Override
    public void handleClose(Ukcp kcp) {
        System.out.println(Snmp.snmp.toString());
        Snmp.snmp  = new Snmp();
    }
}
