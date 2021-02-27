package com.demo.backend.websocket;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.backend.model.AdminUser;
import com.demo.backend.model.Chat;
import com.demo.backend.model.ChatMessage;
import com.demo.backend.service.ChatService;
import com.demo.backend.service.SiteService;
import com.demo.backend.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@ServerEndpoint("/ws/chat/{userType}")  // ? paramStr={usertoken/md5(userid+add_time+last_login)}&paramId={user_id/admin_id}
@Component
public class ChatSocket {

//    http://www.easyswoole.com/wstool.html

    private static UserService userService;
    private static SiteService siteService;
    private static ChatService chatService;

    private static final CopyOnWriteArraySet<ChatSocket> webSocketSet = new CopyOnWriteArraySet<ChatSocket>();

    //连接会话，通过它来给客户端发送数据
    private Session session;

    //定义链接用户的类型  管理员 、 用户
    private byte userType = WS_SERVER;

    private User user = null;
    private AdminUser adminUser = null;

    @Autowired
    public void setUserService(UserService userService){
        ChatSocket.userService = userService;
    }

    @Autowired
    public void setSiteService(SiteService siteService)
    {
        ChatSocket.siteService = siteService;
    }

    @Autowired
    public void setChatService(ChatService chatService)
    {
        ChatSocket.chatService = chatService;
    }
//ws://127.0.0.1:8088/admin/chat/1?paramStr=eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MiIsImlhdCI6MTU5NTkwMzk5NSwic3ViIjoiNTIifQ.uYz6-9IZjLCeICe06JDV7NmATfsQI_xtcWGnyQj6jm0&paramId=1
//ws://127.0.0.1:8088/admin/chat/1?paramStr=eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwIiwiaWF0IjoxNTczMTEzODYyLCJzdWIiOiIwIiwiZXhwIjoxNTczNzE4NjYyfQ._1keEXFv9csDPL-fXHV-xMY-XYAUO0XcWvCRMgz6G_0&paramId=2
//ws://127.0.0.1:8088/admin/chat/0?paramStr=e32e7321834f5dc3c62d9793fc78389e&paramId=1
    @OnOpen
    public void onOpen(Session session,@PathParam("userType") byte type){

        try {

            String token = "";
            long userId = 0;

            String query = session.getQueryString();

            if (null == query)
                throw new Exception("params empty");


            if(query.contains("paramStr") && query.contains("paramId")){

                String pattern = "paramStr=(.*?)&paramId=(.*)";
                Pattern r = Pattern.compile(pattern);

                Matcher matcher = r.matcher(query);
                if(matcher.find())
                {
                    token = matcher.group(1);
                    userId = Long.parseLong(matcher.group(2));
                }
            }

            if(type == WS_USER)//用户
            {
                User user_ = userService.findUserByToken(token);

                if(null == user_ || user_.getUserId() != userId)
                    throw new Exception("u_error");

                this.user = user_;
            }else
            {

                AdminUser admin = siteService.findAdminUserByUserId((short)userId);
                if(admin != null && admin.getIsDelete() == 0 && admin.getStatus() == 1 && MD5Utils.getMD5(admin.getUserId()+""+admin.getAddTime()+""+admin.getLastLogin()).equals(token))
                {
                    this.adminUser = admin;
                }else throw new Exception("m_error");
            }

            this.session = session;

            if(type != 1 && type != 0)
                throw new Exception("error");
            this.userType = type;
            //连接的都加入，区分用户
            webSocketSet.add(this);

        }catch (Exception e)
        {
            e.printStackTrace();
            try {
                session.getBasicRemote().sendText(e.getMessage());
                session.close();
            }catch (IOException e1)
            {
                e.printStackTrace();
            }
        }

    }

    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
    }

    @OnMessage
    public void onMessage(String message) {

//        {"to_id":1,"chat_id":0,"to_name":"user111","from_id":2,"from_name":"user222","mtype":1,"is_admin":0,"type":0,"content":"you two, user222"}

        System.out.println("消息:" + message);
        //解析message
        JSONObject jsonObject = JSON.parseObject(message);

        String content = jsonObject.getString("content");

        if(!content.equals(""))
        {
            long chatId = jsonObject.getLong("chat_id");
            long fromId = jsonObject.getLong("from_id");
            String fromName = jsonObject.getString("from_name");
            long toId = jsonObject.getLong("to_id");
            String toName = jsonObject.getString("to_name");
            byte mtype = jsonObject.getByte("mtype");
            byte isAdmin = jsonObject.getByte("is_admin");
            byte type = jsonObject.getByte("type");  // 0 user-user   1 admin-user
            //mtype  0   1  2

            boolean exist = false;

            for (ChatSocket item : webSocketSet) {
                try {
                    //user send
                    if(isAdmin == 0)
                    {
                        //user - user 发给用户的
                        if(type == 0)
                        {
                            if(item.userType == WS_USER && item.user != null && item.user.getUserId() == fromId)
                            {
                                item.sendMessage(message);

                            }else if(item.userType == WS_USER && item.user != null && item.user.getUserId() == toId)
                            {
                                item.sendMessage(message);
                                saveToDb(chatId, fromId,toId,isAdmin, type,mtype,content);
                                exist = true;
                            }
                        }else       //admin - user    发给管理员的
                        {
                            if(item.userType == WS_USER && item.user != null && item.user.getUserId() == fromId)
                            {
                                item.sendMessage(message);
                            }else if(item.userType == WS_SERVER && item.adminUser != null && item.adminUser.getUserId() == toId)
                            {
                                item.sendMessage(message);
                                saveToDb(chatId, fromId,toId,isAdmin, type,mtype,content);
                                exist = true;
                            }
                        }

                        //admin send
                    }else if(isAdmin == 1)
                    {
                        if(item.userType == WS_SERVER && item.adminUser != null && item.adminUser.getUserId() == fromId)
                        {
                            item.sendMessage(message);

                        }else if(item.userType == WS_USER && item.user != null && item.user.getUserId() == toId)
                        {
                            item.sendMessage(message);
                            saveToDb(chatId, fromId,toId, isAdmin,type,mtype,content);
                            exist = true;
                        }
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            //对方没在线
            if(!exist)
            {
                saveToDb(chatId, fromId,toId, isAdmin,type,mtype,content);
            }
        }
    }

    private void saveToDb(long chatId, long fromId, long toId, byte isAdmin, byte type, byte mtype, String content){
        //找到聊天框
        Chat chat = chatService.findChatByChatId(chatId);

        if(chat == null)
        {
            chat = chatService.findChatByFromAndToAndType(fromId, toId, type);
        }

        byte toUnread = 1;
        if(chat == null)
        {
            chat = new Chat();
            chat.setFromUid(fromId);
            chat.setToUid(toId);
            chat.setChatType(type);
        }else
        {
            toUnread += chat.getToUnread();
        }

        chat.setFromUnread((byte)0);
        chat.setToUnread(toUnread);
        chat.setUpdateTime(TimeUtil.getTimeStamp());
        chat.setLastMsg(content);
        chat = chatService.saveOrUpdateChat(chat);

        //聊天框具体消息
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatId(chat.getChatId());
        chatMessage.setFromUid(fromId);
        chatMessage.setIsAdmin(isAdmin);
        chatMessage.setToUid(toId);

        chatMessage.setMtype(mtype);
        chatMessage.setContent(content);
        chatService.saveChatMessage(chatMessage);

    }

    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }


    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

}
