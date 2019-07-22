import com.backblaze.erasure.fec.Snmp;
import io.netty.buffer.ByteBuf;
import kcp.KcpChannelConfig;
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

        KcpChannelConfig kcpChannelConfig = new KcpChannelConfig();
        kcpChannelConfig.setFastresend(2);
        kcpChannelConfig.setSndwnd(512);
        kcpChannelConfig.setRcvwnd(512);
        kcpChannelConfig.setMtu(1400);
        //kcpChannelConfig.setFecDataShardCount(10);
        //kcpChannelConfig.setFecParityShardCount(3);
        kcpChannelConfig.setAckNoDelay(false);
        kcpChannelConfig.setInterval(40);
        kcpChannelConfig.setNocwnd(true);
        kcpChannelConfig.setCrc32Check(true);
        kcpChannelConfig.setTimeoutMillis(10000);
        kcpChannelConfig.setAutoSetConv(true);
        KcpServer kcpServer = new KcpServer();
        kcpServer.init(Runtime.getRuntime().availableProcessors(), kcpRttExampleServer, kcpChannelConfig,10003);
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
