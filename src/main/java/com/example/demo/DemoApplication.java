package com.example.demo;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
@LineMessageHandler
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	public Document GetConnection() throws IOException {
		Document doc = Jsoup.connect("http://sarjana.jteti.ugm.ac.id/akademik").get();
		return doc;
	}
//	public Elements GetTable() throws IOException {
//		Document doc = GetConnection();
//		Elements tableRow = doc.select("table.table-pad > tbody > tr#4470");
//		return tableRow;
//	}
	public Elements GetReadableBody() throws IOException {
		Document doc = GetConnection();
		Elements tableRow = doc.select("table.table-pad > tbody > tr#4470");
		Elements indexBody = tableRow.select("td:eq(2)");
		return indexBody;
	}

	public String GetWebDtetiTitle(){
		String title="";
		try {
			Document doc = GetConnection();
			title = doc.title();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return title;
	}

	public String GetCategory(){
		String category="";
		try {
			Elements indexBody = GetReadableBody();
			for (Element row :indexBody){
				category = row.select("span.label").text();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return category;
	}

	public String GetDate(){
		String date="";
		try {
			Elements indexBody = GetReadableBody();
			for (Element row:indexBody){
				String[] body = row.text().split(" ");
				String title = row.select("b").text(); //judul pengumuman
				int titleSize = title.split(" ").length;
				int CategoryDanTanggal = titleSize + 4; //4 adalah kategori tambah tanggal, 4 adalah jumlah kata
				String description="";
				while (CategoryDanTanggal < body.length){
					description += body[CategoryDanTanggal] + " ";
					CategoryDanTanggal++;
				}
				//CategoryDanTanggal dimulai dari index ke-1 (yaitu suku kata tanggal)
				CategoryDanTanggal=1;
				//Kurang dari 4 karena indeks ke-4 sudah masuk description
				while (CategoryDanTanggal < 4){
					//suku kata ke-1 adalah tanggal, suku kata ke-2 adalah bulan, suku kata ke-3 adalah tahun, lalu stop looping
					date += body[CategoryDanTanggal] + " ";
					CategoryDanTanggal++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return date;
	}

	public String GetDescription(){
		String description="";
		try {
			Elements indexBody = GetReadableBody();
			for (Element row:indexBody){
				String[] body = row.text().split(" ");
				String title = row.select("b").text(); //judul pengumuman
				int titleSize = title.split(" ").length;
				int CategoryDanTanggal = titleSize + 4; //4 adalah kategori tambah tanggal, 4 adalah jumlah kata
				while (CategoryDanTanggal < body.length){
					description += body[CategoryDanTanggal] + " ";
					CategoryDanTanggal++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return description;
	}

	@EventMapping
	public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> msg){
		TextMessage replyMsg = null;
		if (msg.getMessage().getText().equals("judul")){
			replyMsg = new TextMessage(GetWebDtetiTitle());
		} else if(msg.getMessage().getText().equals("category")){
			replyMsg = new TextMessage(GetCategory());
		} else if(msg.getMessage().getText().equals("date")){
			replyMsg = new TextMessage(GetDate());
		} else if(msg.getMessage().getText().equals("deskripsi")){
			replyMsg = new TextMessage(GetDescription());
		} else{
			replyMsg = new TextMessage("kowe ngirim : " + msg.getMessage().getText());
		}
		return replyMsg;
	}

	@EventMapping
	public StickerMessage handleStickerMessageEvent(MessageEvent<StickerMessageContent> sticker){
		return new StickerMessage("1", "5");
	}
}
