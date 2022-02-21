package test;

import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static Document getPage(String url) throws IOException{
        return Jsoup.connect(url).get();
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        String urlString = "https://vk.com/music/album/-2000795077_14795077_41675b6195b6b15bb9?act=album";
//        String urlString = "https://www.gismeteo.ru/";
        ArrayList<String> performer = new ArrayList<String>(); // исполнитель
        ArrayList<String> titleTrack = new ArrayList<String>(); // название трека

//        Map<String,ArrayList<String>>


        Document page = getPage(urlString);
//        System.out.println(page.toString());
        Elements trackNames = page.getElementsByClass("audio_row__title_inner _audio_row__title_inner");
//        trackNames.forEach(trackName -> titleTrack.add(trackName.text().replaceAll("\\?","i")));
        for (Element trackName : trackNames) {
            String buffer = trackName.text();
            buffer = buffer.replaceAll("\\u0306", "");
            titleTrack.add(buffer);
        }
        Elements trackPerformers = page.getElementsByClass("audio_row__performers");
        trackPerformers.forEach(trackPerformer -> performer.add(trackPerformer.text()));
        System.out.println(titleTrack);
        System.out.println(performer);
    }
}