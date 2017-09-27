package com.example.demo.Controller;

import com.example.demo.Dao.MainDao;
import com.example.demo.model.Group;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@LineMessageHandler
public class MainController {
    @Autowired
    private LineMessagingClient lineMessagingClient;

    private static String AccessToken = "u/jyVKXsD5N/OfmNIvEjnI+NffMIhzcFFjIZ3Whm4Gu9/LTL4y7WjWhWehHjYIO+aG6QUKw5991HFzs7i8c1PAZP07r1LIGun6o8X53yZflIk/Th0W8JkY9G/2IpWkL59subrXO5cOQCxJqjemzHvwdB04t89/1O/w1cDnyilFU=";

    @EventMapping
    public TextMessage handleJoinNewGroup(JoinEvent joinEvent) {
        TextMessage replyMsg = null;
        Source source = joinEvent.getSource();
        String id = getId(source);
        MainDao.CreateTableData(id);
        replyMsg = new TextMessage("Nuwun yo aku wes entuk join grup iki\n" +
                "Silakan ketik /woy untuk melihat fitur-fitur yang ada.");
        return replyMsg;
    }

    @EventMapping
    public TextMessage handleNewFollower(FollowEvent followEvent){
        TextMessage textMessage = null;
        Source source = followEvent.getSource();
        String id = getId(source);
        MainDao.CreateTableData(id);
        textMessage = new TextMessage("Nuwun yo aku wes di-add dadi friend\n" +
                "Silakan ketik /woy untuk melihat fitur-fitur yang ada.");
        return textMessage;
    }

    @EventMapping
    public void handleTextSlash(MessageEvent<TextMessageContent> msg) {
        Group group = new Group();
        Source source = msg.getSource();
        PushMessage pushMessage;
        List<Message> messageList = new ArrayList<>();
        TextMessage textMessage = null;
        String id = getId(source);
        TemplateMessage templateMessage = null;
        String pesan = msg.getMessage().getText().toUpperCase();
        if (pesan.equals("/WOY")) {
            CarouselTemplate carouselTemplate = new CarouselTemplate(
                    Arrays.asList(
                            new CarouselColumn(null, "TUGAS", "Tambah/Lihat seputar Tugas", Arrays.asList(
                                    new PostbackAction("Tambah Tugas",
                                            "/ADD-TUGAS"),
                                    new PostbackAction("Lihat Tugas",
                                            "/SHOW-TUGAS")
                            )),
                            new CarouselColumn(null, "UJIAN", "Tambah/Lihat seputar Ujian", Arrays.asList(
                                    new PostbackAction("Tambah Ujian",
                                            "/ADD-UJIAN"),
                                    new PostbackAction("Lihat Ujian",
                                            "/SHOW-UJIAN")
                            ))
                    ));
            templateMessage = new TemplateMessage("LJ BOT mengirim pesan!", carouselTemplate);
//            TextMessage msgCommand = new TextMessage("Daftar command LJ BOT\n" +
//                    "1. /TUGAS [spasi] [deskripsi TUGAS]\n" +
//                    "2. /UJIAN [spasi] [deskripsi UJIAN]\n");
//            messageList.add(msgCommand);
//        } else if (msg.getMessage().getText().toUpperCase().substring(0,6).equals("/TUGAS")){
//            messageList.clear();
//            String deskripsi = msg.getMessage().getText().substring(7);
////            StickerMessage stickerSuksesTugas = new StickerMessage("1", "5");
////            messageList.add(stickerSuksesTugas);
//
////            //Add TUGAS ke database
//            group.setId("TUGAS" + "-" + deskripsi.substring(0,5));
//            group.setDeskripsi(deskripsi);
//            group.setTipe("tugas");
//            int status_insert = MainDao.Insert(groupId, group);
//            if(status_insert==1)
//                System.out.println("Berhasil masukkan tugas ke database");
//            else
//                System.out.println("Gagal masukkan ke database");
//            List<Group> groupList = MainDao.GetAll(groupId, "tugas");
//            StringBuilder Stringmsg = new StringBuilder();
//            int nomor=1;
//            for (Group groupp:groupList) {
//                Stringmsg.append(nomor + "." + "\nID : " + groupp.getId() + "\nTugas : " + groupp.getDeskripsi());
//                nomor++;
//            }
//            TextMessage msgAllTugas = new TextMessage("LIST TUGAS\n" + String.valueOf(Stringmsg));
//            messageList.add(msgAllTugas);
//        } else if(msg.getMessage().getText().substring(0,6).equals("/UJIAN")){
//            messageList.clear();
//            String deskripsi = msg.getMessage().getText().substring(7);
//            group.setId("UJIAN" + "-" + deskripsi.substring(0,5));
//            group.setDeskripsi(deskripsi);
//            group.setTipe("ujian");
//            int status_insert = MainDao.Insert(groupId, group);
//            if(status_insert==1)
//                System.out.println("Berhasil masukkan tugas ke database");
//            else
//                System.out.println("Gagal masukkan ke database");
//            List<Group> groupList = MainDao.GetAll(groupId, "ujian");
//            StringBuilder Stringmsg = new StringBuilder();
//            int nomor=1;
//            for (Group groupp:groupList) {
//                Stringmsg.append(nomor + "." + "\nID : " + groupp.getId() + "\nUjian : " + groupp.getDeskripsi());
//                nomor++;
//            }
//            TextMessage msgAllTugas = new TextMessage("LIST UJIAN\n" + String.valueOf(Stringmsg));
//            messageList.add(msgAllTugas);
//        }
//            pushMessage = new PushMessage(groupId, messageList);
//            Response<BotApiResponse> response =
//                    null;
//            try {
//                response = LineMessagingServiceBuilder
//                        .create(AccessToken)
//                        .build()
//                        .pushMessage(pushMessage)
//                        .execute();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            System.out.println(response.code() + " " + response.message());
            try {
                BotApiResponse apiResponse = lineMessagingClient
                        .replyMessage(new ReplyMessage(msg.getReplyToken(), templateMessage))
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        } else if(pesan.substring(0,6).equals("/TUGAS")){
            String desc = pesan.substring(7);
            group.setId("TUGAS-" + desc.substring(0,7));
            group.setDeskripsi(desc);
            group.setTipe("tugas");
            int status_insert = MainDao.Insert(id, group);
            if(status_insert==1){
                textMessage = new TextMessage("Tugas berhasil dicatat.");
                pushMessage = new PushMessage(id, textMessage);
            } else{
                textMessage = new TextMessage("Oops! Ada kesalahan sistem, tugas gagal dicatat");
                pushMessage = new PushMessage(id, textMessage);
            }
            KirimPesan(pushMessage);
        }
    }

    @EventMapping
    public void handlePostback(PostbackEvent event){
        Source source = event.getSource();
        PushMessage pushMessage;
        List<Message> messageList = new ArrayList<>();
        String data = event.getPostbackContent().getData();
        TextMessage textMessage = null;
        String id = getId(source);
        if(data.equals("/ADD-TUGAS")){
            messageList.clear();
            textMessage = new TextMessage("Kirim deskripsi tugas selengkap mungkin (makul, disuruh ngapain, deadline, dikumpul kemana, dll)");
            messageList.add(textMessage);
            textMessage = new TextMessage("Perhatian!\n" +
                    "Kirim deskripsi tugas dengan format !tugas [spasi] [deskripsi]\n" +
                    "Contoh : /tugas progdas bikin kalkulator deadline senin");
            messageList.add(textMessage);
        } else if(data.equals("/SHOW-TUGAS")){
            messageList.clear();
            List<Group> groupList = MainDao.GetAll(id, "tugas");
            StringBuilder sb = null;
            int nomor=1;
            for (Group item:groupList) {
                sb.append(nomor + ".\n" +
                        item.getId() + "\n" +
                        item.getDeskripsi() + "\n");
                nomor++;
            }
            textMessage = new TextMessage(String.valueOf(sb));
            messageList.add(textMessage);
        }
        pushMessage = new PushMessage(id, messageList);
        KirimPesan(pushMessage);
    }

//    @EventMapping
//    public void handleContent(MessageEvent<TextMessageContent> msg){
//        Source source = msg.getSource();
//        Group group = new Group();
//        String id = getId(source);
//        String pesan = msg.getMessage().getText();
//        System.out.println("Pesan : " + pesan);
//        System.out.println("Pesan substring : " + pesan.substring(0,6).toUpperCase());
//        PushMessage pushMessage;
//        TextMessage textMessage;
//        if(pesan.substring(0,6).toUpperCase().equals("/TUGAS")){
//            String desc = pesan.substring(7);
//            group.setId("TUGAS-" + desc.substring(0,5));
//            group.setDeskripsi(desc);
//            group.setTipe("tugas");
//            int status_insert = MainDao.Insert(id, group);
//            if(status_insert==1){
//                textMessage = new TextMessage("Tugas berhasil dicatat.");
//                pushMessage = new PushMessage(id, textMessage);
//            } else{
//                textMessage = new TextMessage("Oops! Ada kesalahan sistem, tugas gagal dicatat");
//                pushMessage = new PushMessage(id, textMessage);
//            }
//            KirimPesan(pushMessage);
//        }
//    }

    public void KirimPesan(PushMessage pushMessage){
        Response<BotApiResponse> response = null;
        try {
            response = LineMessagingServiceBuilder
                    .create(AccessToken)
                    .build()
                    .pushMessage(pushMessage)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response.code() + " " + response.message());
    }

    public String getId(Source source){
        String id=null;
        if (source instanceof GroupSource) {
            id = ((GroupSource) source).getGroupId();
        } else if (source instanceof RoomSource) {
            id = ((RoomSource) source).getRoomId();
        } else{
            id = source.getUserId();
        }
        return id;
    }
}
