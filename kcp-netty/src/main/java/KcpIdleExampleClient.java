import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import kcp.KcpChannelConfig;
import kcp.KcpClient;
import kcp.KcpListener;
import kcp.Ukcp;

import java.net.InetSocketAddress;

/**
 * Created by JinMiao
 * 2019-07-10.
 */
public class KcpIdleExampleClient implements KcpListener {

    public static void main(String[] args) {
        KcpClient kcpClient = new KcpClient();
        kcpClient.init(Runtime.getRuntime().availableProcessors());

        KcpChannelConfig kcpChannelConfig = new KcpChannelConfig();
        kcpChannelConfig.setFastresend(2);
        kcpChannelConfig.setSndwnd(1024);
        kcpChannelConfig.setRcvwnd(1024);
        kcpChannelConfig.setMtu(1400);
        //kcpChannelConfig.setFecDataShardCount(10);
        //kcpChannelConfig.setFecParityShardCount(3);
        kcpChannelConfig.setAckNoDelay(false);
        kcpChannelConfig.setInterval(40);
        kcpChannelConfig.setNocwnd(true);
        kcpChannelConfig.setCrc32Check(true);
        //kcpChannelConfig.setTimeoutMillis(10000);

        for (int i = 0; i < 3000; i++) {
            if(i%1000==0){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            KcpIdleExampleClient kcpIdleExampleClient = new KcpIdleExampleClient();
            kcpClient.connect(new InetSocketAddress("127.0.0.1", 10020), kcpChannelConfig, kcpIdleExampleClient);
        }

    }
    int i =0;

    @Override
    public void onConnected(Ukcp ukcp) {
        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer(124);
        byteBuf.writeInt(i++);
        byte[] bytes = new byte[120];
        byteBuf.writeBytes(bytes);
        ukcp.write(byteBuf);
        byteBuf.release();
    }
    //int j =0;

    @Override
    public void handleReceive(ByteBuf byteBuf, Ukcp ukcp) {
        //ukcp.write(byteBuf);
        //int id = byteBuf.getInt(0);
        ////if(j-id%10!=0){
        ////    System.out.println("id"+id +"  j" +j);
        ////}
        //
        //j++;
        //if(j%100000==0){
        //    System.out.println(Snmp.snmp.toString());
        //    System.out.println("收到了 返回回去"+j);
        //}
    }

    @Override
    public void handleException(Throwable ex, Ukcp kcp) {
        ex.printStackTrace();
    }

    @Override
    public void handleClose(Ukcp kcp) {
        System.out.println("连接断开了");
    }


}