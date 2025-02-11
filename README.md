# java-Kcp

[![Powered][2]][1]

基于netty版本实现的kcp(包含fec功能的实现)


[1]: https://github.com/skywind3000/kcp
[2]: http://skywind3000.github.io/word/images/kcp.svg


# 兼容性:
1. 兼容c版本kcp
2. fec基于 https://github.com/Backblaze/JavaReedSolomon 实现


# 优化:
1. 基于disruptor事件驱动,充分利用多核
2. 优化fastack逻辑，降低10%流量
3. 优化check函数。
4. 优化集合迭代器。
5. 包含fec,降低延迟
6. 附带crc32校验


# 使用方法以及参数
1. [server端示例](https://github.com/l42111996/java-Kcp/blob/master/kcp-netty/src/main/java/KcpRttExampleServer.java)
2. [client端实例](https://github.com/l42111996/java-Kcp/blob/master/kcp-netty/src/main/java/KcpRttExampleClient.java)
3. [最佳实践](https://github.com/skywind3000/kcp/wiki/KCP-Best-Practice)
4. [大量资料](https://github.com/skywind3000/kcp)


# 已完成测试
1. 单连接死循环收发包内存检测，性能测试  
配置:mbp 2.3 GHz Intel Core i9 16GRam
单连接 带fec 5W/s qps
单连接 不带fec  8W/s qps


# TODO 
1. fec结合directbuffer检测对应cpu最佳性能方案
2. 大量连接性能测试标准

   
# 相关资料

1. https://github.com/skywind3000/kcp 原版c版本的kcp
2. https://github.com/xtaci/kcp-go go版本kcp,有大量优化
3. https://github.com/Backblaze/JavaReedSolomon java版本fec
4. https://github.com/LMAX-Exchange/disruptor 高性能的线程间消息传递库
5. https://github.com/JCTools/JCTools 高性能并发库
6. https://github.com/szhnet/kcp-netty java版本的一个kcp
   
   
#交流

QQ:526167774
   
    