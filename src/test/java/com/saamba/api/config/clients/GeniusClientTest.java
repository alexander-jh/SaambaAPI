package com.saamba.api.config.clients;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "/application-test.properties")
public class GeniusClientTest {

    @Value("${test.genius.title}")
    private String title;

    @Value("${test.genius.path}")
    private String path;

    @Value("${test.genius.artist}")
    private String artist;

    @Autowired
    private GeniusClient geniusClient;

    @Test
    public void getUrlTest() {
        List<String> list = new ArrayList<>();
        list.add(artist);
        String expected = "https://api.genius.com/search?q=" + title + " " + artist;
        String actual = geniusClient.getUrl(title, list);
        assertThat(actual.equals(expected)).isTrue();
    }

    @Test
    public void getApiPathTest() {
        List<String> list = new ArrayList<>();
        list.add(artist);
        String actual = geniusClient.getApiPath(title, list);
        assertThat(actual.equals(path)).isTrue();
    }

//    @Test
//    public void parseLyricsTest() {
//        String expected = "Nobody pray for meIt been that day for meWay (Yeah, yeah)Ayy, I remember syrup sandwiches and crime allowancesFinesse a nigga with some counterfeits, but now I'm countin thisParmesan where my accountant lives, in fact I'm downin' thisDUSS with my boo bae tastes like Kool-Aid for the analystsGirl, I can buy your ass the world with my paystubOoh, that pussy good, won't you sit it on my taste bloods?I get way too petty once you let me do the extrasPull up on your block, then break it down: we playin' TetrisAM to the PM, PM to the AM, funkPiss out your per diem, you just gotta hate 'em, funkIf I quit your BM, I still ride Mercedes, funkIf I quit this season, I still be the greatest, funkMy left stroke just went viralRight stroke put lil' baby in a spiralSoprano C, we like to keep it on a high noteIt's levels to it, you and I knowBitch, be humble (Hol' up, bitch)Sit down (Hol' up, lil, hol up, lil' bitch)Be humble (Hol up, bitch)Sit down (Hol' up, sit down, lil', sit down, lil' bitch)Be humble (Hol up, hol' up)Bitch, sit down (Hol' up, hol' up, lil' bitch)Be humble (Lil' bitch, hol' up, bitch)Sit down (Hol' up, hol' up, hol' up, hol' up)Be humble (Hol' up, hol' up)Sit down (Hol' up, hol' up, lil', hol' up, lil' bitch)Be humble (Hol' up, bitch)Sit down (Hol' up, sit down, lil', sit down, lil' bitch)Be humble (Hol' up, hol' up)Bitch, sit down (Hol' up, hol' up, lil' bitch)Be humble (Lil' bitch, hol' up, bitch)Sit down (Hol' up, hol' up, hol' up, hol' up)Who that nigga thinkin' that he frontin' on Man-Man? (Man-Man)Get the fuck off my stage, I'm the Sandman (Sandman) Get the fuck off my dick, that ain't right I make a play fucking up your whole life I'm so fuckin' sick and tired of the PhotoshopShow me somethin' natural like afro on Richard PryorShow me somethin' natural like ass with some stretch marks Still will take you down right on your mama's couch in Polo socks Ayy, this shit way too crazy, ayy, you do not amaze me, ayyI blew cool from AC, ayy, Obama just paged me, ayyI don't fabricate it, ayy, most of y'all be fakin', ayyI stay modest 'bout it, ayy, she elaborate it, ayy This that Grey Poupon, that Evian, that TED Talk, ayy Watch my soul speak, you let the meds talk, ayyIf I kill a nigga, it won't be the alcohol, ayyI'm the realest nigga after all Bitch, be humble (Hol' up, bitch)Sit down (Hol' up, lil', hol' up, lil' bitch)Be humble (Hol' up, bitch)Sit down (Hol' up, sit down, lil', sit down, lil' bitch)Be humble (Hol' up, hol' up)Bitch, sit down (Hol' up, hol' up, lil' bitch)Be humble (Lil' bitch, hol' up, bitch)Sit down (Hol' up, hol' up, hol' up, hol' up)Be humble (Hol' up, hol' up)Sit down (Hol' up, hol' up, lil', hol' up, lil' bitch)Be humble (Hol' up, bitch)Sit down (Hol' up, sit down, lil', sit down, lil' bitch)Be humble (Hol' up, hol' up)Bitch, sit down (Hol' up, hol' up, lil' bitch)Be humble (Lil' bitch, hol' up, bitch)Sit down (Hol' up, hol' up, hol' up, hol";
//        String actual = geniusClient.parseLyrics(path);
//        assertThat(actual.equals(expected)).isTrue();
//    }

    @Test
    public void parseLyricsTestEmpty() {
        String actual = geniusClient.parseLyrics(path + "33333333");
        assertThat(actual.equals("")).isTrue();
    }

    @Test
    public void getLyricsTest() {
        List<String> list = new ArrayList<>();
        list.add(artist);
        String actual = geniusClient.getLyrics(title, list);
        System.out.println(actual);
        assertThat(actual.length() > 0).isTrue();
    }

    @Test
    public void getLyricsTestEmpty() {
        List<String> list = new ArrayList<>();
        String actual = geniusClient.getLyrics("", list);
        assertThat(actual.equals("")).isTrue();
    }
}
