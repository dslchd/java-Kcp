package kcp;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * KCP channel config
 * Created by JinMiao
 * @author dslchd
 * 2018/9/20.
 */
@Builder
@ToString
@Getter
public class KcpChannelConfig {

    /**
     * 是否禁用noDelay小数据即时传输算法
     */
    private boolean nodelay;
    @Builder.Default
    private int interval = Kcp.IKCP_INTERVAL;
    private int fastresend;
    private boolean nocwnd;
    @Builder.Default
    private int sndwnd = Kcp.IKCP_WND_SND;
    @Builder.Default
    private int rcvwnd = Kcp.IKCP_WND_RCV;
    @Builder.Default
    private int mtu = Kcp.IKCP_MTU_DEF;
    @Builder.Default
    private int minRto = Kcp.IKCP_RTO_MIN;
    /**
     * 超时时间 超过一段时间没收到消息断开连接
     */
    private long timeoutMillis;
    /**
     * true kcp stream mode  TODO 有bug还未测试
     */
    private boolean stream;

    //下面为新增参数
    private int fecDataShardCount;
    private int fecParityShardCount;
    //收到包立刻回传ack包
    private boolean ackNoDelay;
    /**
     * 发送包立即调用flush 延迟低一些  cpu增加  如果interval值很小 建议关闭该参数
     */
    @Builder.Default
    private boolean fastFlush = true;
    //crc32校验
    private boolean crc32Check;

    private boolean autoSetConv;
}
