package test;

import com.backblaze.erasure.fec.Snmp;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import kcp.KcpChannelConfig;
import kcp.KcpListener;
import kcp.KcpServer;
import kcp.Ukcp;
import lombok.extern.slf4j.Slf4j;
import threadPool.thread.DisruptorExecutorPool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模拟帧同步测试吞吐和流量
 * 50ms一帧
 * Created by JinMiao
 * 2019-06-25.
 */
@Slf4j
public class LockStepSynchronizationServer implements KcpListener {

    private Map<Integer,Room> playerRooms = new ConcurrentHashMap<>();

    private DisruptorExecutorPool disruptorExecutorPool = new DisruptorExecutorPool();


    public static void main(String[] args) {
        LockStepSynchronizationServer lockStepSynchronizationServer = new LockStepSynchronizationServer();
        //config kcp params
        KcpChannelConfig kcpChannelConfig =  KcpChannelConfig.builder().fastresend(2)
        .sndwnd(300).rcvwnd(300).mtu(500).interval(400).nocwnd(true).crc32Check(true).timeoutMillis(10000).build();
        //看看config的值
        log.info("KcpChannelConfig:{}",kcpChannelConfig);
        KcpServer kcpServer = new KcpServer();
        kcpServer.init(1, lockStepSynchronizationServer, kcpChannelConfig, 10009);
        for (int i = 0; i < 1; i++) {
            lockStepSynchronizationServer.disruptorExecutorPool.createDisruptorProcessor("logic-"+i);
        }
        //打印发送与接收封包情况
        printReceiveAndSendPacket();
    }


    private static void printReceiveAndSendPacket(){
        DisruptorExecutorPool.scheduleWithFixedDelay(()->{
            log.info("每秒收包:{}",(Snmp.snmp.InBytes.longValue()/1024.0/1024.0*8.0)+" M");
            log.info("每秒发包:{}",(Snmp.snmp.InBytes.longValue()/1024.0/1024.0*8.0)+" M");
        },2000);
    }

    private synchronized void joinRoom(Player player){
        Room room = null;
        for (Room value : playerRooms.values()) {
            if(value.getPlayers().size()==8)
            {
                continue;
            }
            if(room==null){
                room = value;
                continue;
            }
            if(room.getPlayers().size()>value.getPlayers().size()){
                room = value;
            }
        }
        if(room==null){
            room = new Room();
            room.setiMessageExecutor(disruptorExecutorPool.getAutoDisruptorProcessor());
            DisruptorExecutorPool.scheduleWithFixedDelay(room,50);
        }
        playerRooms.put(player.getId(),room);
        room.getPlayers().put(player.getId(),player);
    }


    @Override
    public void onConnected(Ukcp ukcp) {
        System.out.println("有连接进来"+ukcp.user());
        Player player = new Player(ukcp);
        ukcp.user().setCache(player);
        joinRoom(player);
    }

    @Override
    public void handleReceive(ByteBuf byteBuf, Ukcp ukcp) {
        //System.out.println("收到消息"+ukcp.user());
        Player player = ukcp.user().getCache();
        Room room = playerRooms.get(player.getId());
        ByteBuf byteBufAllocator = ByteBufAllocator.DEFAULT.directBuffer(20);
        byteBuf.readBytes(byteBufAllocator);
        byteBufAllocator.readerIndex(0);
        byteBufAllocator.writerIndex(20);
        room.getiMessageExecutor().execute(() -> player.getMessages().add(byteBufAllocator));
    }

    @Override
    public void handleException(Throwable ex, Ukcp ukcp) {
        ex.printStackTrace();
    }

    @Override
    public void handleClose(Ukcp ukcp) {
        Player player = ukcp.user().getCache();
        playerRooms.remove(player.getId());
        log.info("连接断开了"+ukcp.user().getRemoteAddress());
    }
}
