
import com.game.mj.eventdispatch.DynamicRegisterGameService;
import com.game.network.coder.TMessageDecoderPro;
import com.game.network.coder.TMessageEncoderPro;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author zheng
 */

public class ClientFactory {
    private static NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
//    private MessageDecoder messageDecoder = new MessageDecoder();
//    private MessageEncoder messageEncoder = new MessageEncoder();
//    private NettyClientHandler handler = new NettyClientHandler();
//    private IdleStateHandler stateHandler = new IdleStateHandler(60, 60, 100);
    private Bootstrap bootstrap;
    private ClientFactory() {
//        buildNewClient();
    }

    public static ClientFactory getInstance(){
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static ClientFactory INSTANCE = new ClientFactory();
    }

    public Bootstrap buildNewClient(DynamicRegisterGameService dynamicRegisterGameService){
        if (this.bootstrap != null){
            return this.bootstrap;
        }
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);

        bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
//                .option(ChannelOption.SO_REUSEADDR,true)
                .option(ChannelOption.TCP_NODELAY,true)
                .option(ChannelOption.SO_SNDBUF,Integer.MAX_VALUE)
                .option(ChannelOption.SO_RCVBUF,Integer.MAX_VALUE)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
        ;
//        bootstrap.bind(port);
        bootstrap.group(eventLoopGroup);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {

                socketChannel.pipeline().addLast(new TMessageEncoderPro());
                socketChannel.pipeline().addLast(new TMessageDecoderPro(dynamicRegisterGameService));
                socketChannel.pipeline().addLast(new IdleStateHandler(10, 10, 10));
                socketChannel.pipeline().addLast(new NettyClientHandler());

            }
        });
        this.bootstrap = bootstrap;
        return bootstrap;
    }

//    public ChannelFuture connect(String host, int port, int bindPort){
//        if (this.bootstrap == null){
//            this.bootstrap = buildNewClient();
//        }
//        bootstrap.bind(bindPort);
//        try {
//            return bootstrap.connect(host, port).sync();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            return null;
//        }
//
//    }
    public ChannelFuture connect(String host,int port,Bootstrap bootstrap,int sourcePort){
        if (this.bootstrap == null){
            System.out.println("bootstrap is empty ");
            return null;
        }
//        bootstrap.bind(sourcePort);
        try {
            return this.bootstrap.connect(host, port);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    public ChannelFuture connect(Bootstrap bootstrap,String host, int port, int bindPort){

        bootstrap.bind(bindPort);
        try {
            return bootstrap.connect(host, port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }

    }

}
