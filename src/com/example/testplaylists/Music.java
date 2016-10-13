package com.example.testplaylists;

public class Music {
	private String title;
	private String artist;
	private String album;
	private String year;
	private String duration;
	private String data;
	private String url;
	
	public Music() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Music(String title, String artist, String album, String year,
			String duration, String data,String url) {
		super();
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.year = year;
		this.duration = duration;
		this.data = data;
		this.url=url;
	}
	
	public Music(String title, String artist, String album, String year,
			String duration, String data) {
		super();
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.year = year;
		this.duration = duration;
		this.data = data;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString() {
		return "Music [title=" + title + ", artist=" + artist + ", album="
				+ album + ", year=" + year + ", duration=" + duration
				+ ", data=" + data + "]";
	}
	
//	@Override
//	public boolean equals(Object o) {
//		// TODO Auto-generated method stub
//		
//		if(this==o){
//			return true;
//		}
//		if(null==o){
//			return false;
//		}
//		if(this.getClass()!=o.getClass()){
//			return false;
//		}
//		Music music=(Music)o;
//		if(this.)
//		
//	}
}
