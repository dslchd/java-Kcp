package com.backblaze.erasure.fec;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.LongAdder;


/**
 * Created by JinMiao
 * 2018/8/29.
 */
@Data
@NoArgsConstructor
public class Snmp {
    // bytes sent from upper level
    public LongAdder BytesSent = new LongAdder();
    // bytes received to upper level
    public LongAdder BytesReceived = new LongAdder();
    // max number of connections ever reached
    public LongAdder MaxConn = new LongAdder();
    // accumulated active open connections
    public LongAdder ActiveOpens = new LongAdder();
    // accumulated passive open connections
    public LongAdder PassiveOpens = new LongAdder();
    // current number of established connections
    public LongAdder CurrEstab = new LongAdder();
    // UDP read errors reported from net.PacketConn
    public LongAdder InErrs = new LongAdder();
    // checksum errors from CRC32
    public LongAdder InCsumErrors = new LongAdder();
    // packet iput errors reported from KCP
    public LongAdder KCPInErrors = new LongAdder();
    // incoming packets count
    public LongAdder InPkts = new LongAdder();
    // outgoing packets count
    public LongAdder OutPkts = new LongAdder();
    // incoming KCP segments
    public LongAdder InSegs = new LongAdder();
    // outgoing KCP segments
    public LongAdder OutSegs = new LongAdder();
    // UDP bytes received
    public LongAdder InBytes = new LongAdder();
    // UDP bytes sent
    public LongAdder OutBytes = new LongAdder();
    // accmulated retransmited segments
    public LongAdder RetransSegs = new LongAdder();
    // accmulated fast retransmitted segments
    public LongAdder FastRetransSegs = new LongAdder();
    // accmulated early retransmitted segments
    public LongAdder EarlyRetransSegs = new LongAdder();
    // number of segs infered as lost
    public LongAdder LostSegs = new LongAdder();
    // number of segs duplicated
    public LongAdder RepeatSegs = new LongAdder();
    // correct packets recovered from FEC
    public LongAdder FECRecovered = new LongAdder();
    // incorrect packets recovered from FEC
    public LongAdder FECErrs = new LongAdder();
    // 收到的 Data数量
    public LongAdder FECDataShards = new LongAdder();
    // 收到的 Parity数量
    public LongAdder FECParityShards = new LongAdder();
    // number of data shards that's not enough for recovery
    public LongAdder FECShortShards = new LongAdder();
    // number of data shards that's not enough for recovery
    public LongAdder FECRepeatDataShards = new LongAdder();

    public static volatile Snmp snmp = new  Snmp();

    @Override
    public String toString() {
        return "Snmp{" +
                "BytesSent=" + BytesSent +
                ", BytesReceived=" + BytesReceived +
                ", MaxConn=" + MaxConn +
                ", ActiveOpens=" + ActiveOpens +
                ", PassiveOpens=" + PassiveOpens +
                ", CurrEstab=" + CurrEstab +
                ", InErrs=" + InErrs +
                ", InCsumErrors=" + InCsumErrors +
                ", KCPInErrors=" + KCPInErrors +
                ", 收到包=" + InPkts +
                ", 发送包=" + OutPkts +
                ", InSegs=" + InSegs +
                ", OutSegs=" + OutSegs +
                ", 收到字节=" + InBytes +
                ", 发送字节=" + OutBytes +
                ", 总共重发数=" + RetransSegs +
                ", 快速重发数=" + FastRetransSegs +
                ", 空闲快速重发数=" + EarlyRetransSegs +
                ", 超时重发数=" + LostSegs +
                ", 收到重复包数量=" + RepeatSegs +
                ", fec恢复数=" + FECRecovered +
                ", fec恢复错误数=" + FECErrs +
                ", 收到fecData数=" + FECDataShards +
                ", 收到fecParity数=" + FECParityShards +
                ", fec缓存冗余淘汰data包数=" + FECShortShards +
                ", fec收到重复的数据包=" + FECRepeatDataShards +
                '}';
    }

}
