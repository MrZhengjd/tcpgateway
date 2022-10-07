//import com.game.common.eventdispatch.EventAnnotationManager;
//import com.game.common.eventdispatch.EventDispatchService;
//import com.game.common.relation.role.PlayerRole;
//import com.game.common.relation.role.PlayerRoleManager;
//import com.game.common.relation.role.RoleManager;
//import com.game.common.relation.room.JoinRoomEvent;
//import com.game.common.relation.room.Room;
//import com.game.common.relation.room.RoomManager;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.ApplicationContext;
//
//import java.util.HashMap;
//
///**
// * @author zheng
// */
////@SpringBootTest
//public class TestModelRelation {
//    @Autowired
//    private ApplicationContext context;
////    @Test
//    public void testRelation(){
//        EventAnnotationManager instance = EventAnnotationManager.getInstance();
//        instance.init(context);
//        Room room = new Room();
//        RoomManager roomManager = new RoomManager(room);
//        for (int i = 0;i < 4; i++){
//            PlayerRole playerRole = new PlayerRole(new HashMap<>());
//            playerRole.setPlayerId(102343l);
////            PlayerRoleManager roleManager = new PlayerRoleManager(playerRole);
//            instance.sendEvent(new JoinRoomEvent(playerRole),room);
//        }
//    }
//
//}
