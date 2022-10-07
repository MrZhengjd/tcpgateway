package com.game.gateway;


import com.game.domain.registerservice.RegisterService;
import com.game.gateway.config.GateWayConfig;
import com.game.gateway.config.GatewayConstant;
import com.game.gateway.server.GameServerBoot;
import com.game.infrustructure.registerservice.RegisterServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

//import com.game.chat.util.ConsulUtil;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.cloud.config.server.EnableConfigServer;


@SpringBootApplication(scanBasePackages ="com.game")
//@EnableDiscoveryClient


public class GatewayApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(GatewayApplication.class, args);
//        ApplicationContext context = SpringApplication.run(GatewayStarterApplication.class,args);
//        IdGenerator idGenerator = IdGeneratorFactory.getDefaultGenerator();
//        RegisterServiceComponent bean = context.getBean(RegisterServiceComponent.class);
//        bean.autoRefreshData();
        GateWayConfig config = context.getBean(GateWayConfig.class);
        RegisterService registerService = context.getBean(RegisterServiceImpl.class);
        registerService.refreshData(config.getModules());
//        DynamicRegisterGameService dynamicRegisterGameService = context.getBean(DynamicRegisterGameService.class);
//        dynamicRegisterGameService.init();
        GameServerBoot boot = context.getBean(GameServerBoot.class);
//        DefaultMQProducer producer = run.getBean(DefaultMQProducer.class);
//        try {
//            producer.start();
//        } catch (MQClientException e) {
//            e.printStackTrace();
//        }
//        RedisPlayerRoomService service = context.getBean(RedisPlayerRoomService.class);
//        MqProduce produce = MqProduceFactory.getDefaultMqProduce();
//        produce.start();
        String host = "127.0.0.1";
        int port = 12365;
        boot.startServer(host, port, GatewayConstant.GATE_WAY_MODULE);
//        EventAnnotationManager manager = new EventAnnotationManager();
//        manager.init(context);
////        System.out.println("welcome-----------");
//        Long playerId = 0l;
//        Integer roomId = 100000;
//        DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();
//        CacheManager.saveData(service.getJsonRedisManager());
//        long timeMillis = System.currentTimeMillis();
//        byte[] info = service.getJsonRedisManager().getObjectHash1("test","first");
//        System.out.println("finist time info"+(System.currentTimeMillis() - timeMillis));
//        String result = dataSerialize.deserialize(info,String.class);
//        System.out.println("finist time str"+(System.currentTimeMillis() - timeMillis));
//        JSONObject data = dataSerialize.deserialize(info,JSONObject.class);
//        Gson gson = dataSerialize.deserialize(info,Gson.class);
//        System.out.println("finist time "+(System.currentTimeMillis() - timeMillis));
//        System.out.println("file name "+gson.get("fileName"));
//        System.out.println(data);
//        for (int i = 0;i < 100000;i++){
//            RoomServerVo roomServerVo = RoomServerVo.getInstance(roomId,1);
//            service.putInfo(playerId,roomServerVo);
//        }

//        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
//        Message message = null;
//        AtomicInteger count = new AtomicInteger(0);
//        executorService.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                int inc = count.getAndIncrement();
//                if (inc > 100){
//                    executorService.shutdown();
//                }
//                System.out.println("info "+service.getIncr());
////                ChannelMap.rotateSendMessage(message);
//            }
//        },10l,880l, TimeUnit.MILLISECONDS);
//        ConsulUtil.registerTcp(host,port,"chat");
//        ServiceDiscover serviceDiscover = run.getBean(ServiceDiscover.class);
//        serviceDiscover.testService();
//        DefaultRegistProvider registProvider = run.getBean(DefaultRegistProvider.class);
//        String serviceName = "chat";
//        String serverPath = ZOOKEEPER_CENTER.concat("/").concat(serviceName).concat("/").concat(PROVIDER);
////        Long serverId = idGenerator.generateId();
//        Long serverId = 1l;
//        ServerData sd = new ServerData(serviceName,host, port,serverId.intValue());
//        try {
//            registProvider.regist(new ZookeeperRegistContext(serverId, serverPath, sd));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        DefaultBalanceProvider defaultBalanceProvider = run.getBean(DefaultBalanceProvider.class);
//        ServerData balanceItem = defaultBalanceProvider.getBalanceItem(serviceName);
//        System.out.println(balanceItem);
//        String testserviceName = "message";
//        ServerData data =  defaultBalanceProvider.getBalanceItem(testserviceName);
//        String test = "test";
//        Message mqMessage = new Message();
//        mqMessage.setTopic(testserviceName+"-"+data.getBalance());
//        mqMessage.setBody(test.getBytes());
//        mqMessage.setTags("undo");
//        try {
//            SendResult send = producer.send(mqMessage);
//            System.out.println("send status "+send.getSendStatus());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("here is send message to mq");
    }

}
