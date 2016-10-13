package com.example.testplaylists;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private ListView mainList;
	private ListView secondList;
	private TextView btn_Return, songs, playlists;
	private List<Music> m_musicData = new ArrayList<Music>();
	private List<Music> s_musicData = new ArrayList<Music>();
	private MyMainAdapter m_mAdapter;
	private MySecondAdapter s_mAdapter;

	float DownX = 0;
	float DownY = 0;
	int position = 0;
	float UpX = 0;
	float distanceX = 0;

	// mainList是否被LongClick
	int ismLongClick = 0;

	// 按钮是否出现
	boolean isButtonVisible = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		//关联控件
		btn_Return = (TextView) findViewById(R.id.btn_return);
		btn_Return.setOnClickListener(this);
		songs = (TextView) findViewById(R.id.Songs);
		playlists = (TextView) findViewById(R.id.Playlists);
		mainList = (ListView) findViewById(R.id.f_listView);
		secondList = (ListView) findViewById(R.id.c_listView);

		// initData();
		m_musicData = scanAllAudioFiles();
		m_mAdapter = new MyMainAdapter();
		s_mAdapter = new MySecondAdapter();

		mainList.setAdapter(m_mAdapter);
		secondList.setAdapter(s_mAdapter);
		
		mainList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				ismLongClick = 1;
				mainList.setVisibility(View.GONE);
				secondList.setVisibility(View.VISIBLE);
				TranslateAnimation tran = new TranslateAnimation(840, 0, 0, 0);
				tran.setDuration(1000);
				secondList.startAnimation(tran);

				if (null == s_musicData) {
					return false;
				}

				if (!(s_musicData.contains(m_musicData.get(arg2)))) {
					s_musicData.add(m_musicData.get(arg2));
				} else {
					Toast.makeText(getApplicationContext(), "此歌曲已添加",
							Toast.LENGTH_LONG).show();
				}
				s_mAdapter.notifyDataSetChanged();
				btn_Return.setVisibility(View.VISIBLE);
				playlists.setVisibility(View.VISIBLE);
				songs.setVisibility(View.GONE);
				return true;
			}
		});

		mainList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (0 == ismLongClick) {
					isPlayDialog(arg2);
				}
			}
		});

		secondList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				isPlayDialog(arg2);
			}
		});

		//手势划出删除键
		secondList.setOnTouchListener(new OnTouchListener() {

			Wrapper wrapper4 = null;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				try {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						DownX = event.getX();
						DownY = event.getY();
						position = secondList.pointToPosition((int) DownX,
								(int) DownY);
						View view = secondList.getChildAt(position);
						wrapper4 = (Wrapper) view.getTag();
						break;
					case MotionEvent.ACTION_UP:
						UpX = event.getX();
						distanceX = UpX - DownX;
						break;
					default:
						break;
					}

					Log.i("dis", "" + distanceX);

					if (distanceX < -100) {
						wrapper4.getButDelet().setVisibility(View.VISIBLE);
						TranslateAnimation tran3 = new TranslateAnimation(840,
								0, 0, 0);
						tran3.setDuration(500);
						wrapper4.getButDelet().startAnimation(tran3);
						distanceX=0;
						return true;
					} else if (distanceX > 100) {
						wrapper4.getButDelet().setVisibility(View.GONE);
						TranslateAnimation tran4 = new TranslateAnimation(0,
								840, 0, 0);
						tran4.setDuration(500);
						wrapper4.getButDelet().startAnimation(tran4);
						distanceX=0;
						return true;
					} else {
						return false;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
		});

	}

	// test code
	private void initData() {

		Music music1 = new Music("Baby", "bieber", "hip-hop", "2010", "3:36",
				"4.3MB");
		m_musicData.add(music1);
		Music music2 = new Music("AA", "bieber", "hip-hop", "2010", "3:36",
				"4.3MB");
		m_musicData.add(music2);
		Music music3 = new Music("BB", "bieber", "hip-hop", "2010", "3:36",
				"4.3MB");
		m_musicData.add(music3);
	}

	//返回键的监听
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_return:
			btn_Return.setVisibility(View.GONE);
			mainList.setVisibility(View.VISIBLE);
			TranslateAnimation tran1 = new TranslateAnimation(0, 840, 0, 0);
			tran1.setDuration(1000);
			mainList.startAnimation(tran1);
			secondList.setVisibility(View.GONE);
			songs.setVisibility(View.VISIBLE);
			playlists.setVisibility(View.GONE);
			ismLongClick = 0;
			break;
		}
	}

	class MyMainAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null == m_musicData) {
				return 0;
			}
			return m_musicData.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			if (null == m_musicData) {
				return null;
			}
			return m_musicData.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			Wrapper wrapper1 = null;
			if (null == arg1) {
				arg1 = getLayoutInflater().inflate(R.layout.listitem, arg2,
						false);
				wrapper1 = new Wrapper(arg1);

			} else {
				wrapper1 = (Wrapper) arg1.getTag();
			}
			wrapper1.getTitle().setText(m_musicData.get(arg0).getTitle());
			wrapper1.getDuration().setText(m_musicData.get(arg0).getDuration());
			wrapper1.getArtist().setText(m_musicData.get(arg0).getArtist());
			wrapper1.getAlbum().setText(m_musicData.get(arg0).getAlbum());
			wrapper1.getYear().setText(m_musicData.get(arg0).getYear());
			wrapper1.getData().setText(m_musicData.get(arg0).getData());
			return arg1;

		}
	}

	class MySecondAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null == s_musicData) {
				return 0;
			}
			return s_musicData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (null == s_musicData) {
				return null;
			}
			return s_musicData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Wrapper wrapper2 = null;
			final int p = position;

			if (null == convertView) {
				convertView = getLayoutInflater().inflate(R.layout.listitem,
						parent, false);
				wrapper2 = new Wrapper(convertView);

			} else {
				wrapper2 = (Wrapper) convertView.getTag();
			}
			wrapper2.getTitle().setText(s_musicData.get(position).getTitle());
			wrapper2.getDuration().setText(
					s_musicData.get(position).getDuration());
			wrapper2.getArtist().setText(s_musicData.get(position).getArtist());
			wrapper2.getAlbum().setText(s_musicData.get(position).getAlbum());
			wrapper2.getYear().setText(s_musicData.get(position).getYear());
			wrapper2.getData().setText(s_musicData.get(position).getData());
			final Wrapper wrapper3 = wrapper2;

			wrapper3.getButDelet().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					s_musicData.remove(p);
					Log.d("data", s_musicData.toString());
					s_mAdapter.notifyDataSetChanged();
					wrapper3.getButDelet().setVisibility(View.GONE);
				}
			});

			return convertView;
		}
	}

	static class Wrapper {
		private TextView title;
		private TextView duration;
		private TextView artist;
		private TextView album;
		private TextView year;
		private TextView data;
		private Button btn_delet;
		View view;

		public RelativeLayout item_list;

		public Wrapper(View view) {
			this.view = view;
			if (title == null) {
				title = (TextView) view.findViewById(R.id.title);
			}
			if (duration == null) {
				duration = (TextView) view.findViewById(R.id.duration);
			}
			if (artist == null) {
				artist = (TextView) view.findViewById(R.id.artist);
			}
			if (album == null) {
				album = (TextView) view.findViewById(R.id.album);
			}
			if (year == null) {
				year = (TextView) view.findViewById(R.id.year);
			}
			if (data == null) {
				data = (TextView) view.findViewById(R.id.data);
			}
			if (btn_delet == null) {
				btn_delet = (Button) view.findViewById(R.id.button_delet);
			}
			view.setTag(this);
		}

		public TextView getTitle() {
			return title;
		}

		public TextView getDuration() {
			return duration;
		}

		public TextView getArtist() {
			return artist;
		}

		public TextView getAlbum() {
			return album;
		}

		public TextView getYear() {
			return year;
		}

		public TextView getData() {
			return data;
		}

		public Button getButDelet() {
			return btn_delet;
		}
	}

	//通过系统库查找音频文件
	public List<Music> scanAllAudioFiles() {
		List<Music> music = new ArrayList<Music>();
		// 查询媒体数据库
		Cursor cursor = getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		if (null == cursor) {
			return null;
		}
		// 遍历媒体数据库
		if (cursor.moveToFirst()) {

			while (!cursor.isAfterLast()) {

				// 歌曲编号
				int id = cursor.getInt(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
				// 歌曲标题
				String tilte = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
				// 歌曲的专辑名：MediaStore.Audio.Media.ALBUM
				String album = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
				// 歌曲的歌手名： MediaStore.Audio.Media.ARTIST
				String artist = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
				// 歌曲文件的路径 ：MediaStore.Audio.Media.DATA
				String url = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
				// 歌曲的总播放时长 ：MediaStore.Audio.Media.DURATION
				int duration = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
				// 歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
				Long size = cursor.getLong(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));

				if (size > 1024 * 800) {// 大于800K
					// HashMap<String, Object> map = new HashMap<String,
					// Object>();
					Music m_music = new Music();
					m_music.setTitle(tilte);
					m_music.setArtist(artist);
					m_music.setAlbum(album);
					m_music.setDuration(timeCast(duration));
					m_music.setData(sizeCast(size));
					m_music.setUrl(url);
					music.add(m_music);
				}
				cursor.moveToNext();
			}
		}

		return music;

	}

	//播放音频文件
	public void play(String url) {
		try {
			Intent it = new Intent(Intent.ACTION_VIEW);
			it.setDataAndType(Uri.parse(url), "audio/*");
			startActivity(it);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void isPlayDialog(int position) {
		final int p = position;
		AlertDialog.Builder isplayDialog = new AlertDialog.Builder(
				MainActivity.this);
		isplayDialog.setMessage("是否播放歌曲?");
		isplayDialog.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						play(m_musicData.get(p).getUrl());
					}
				});
		isplayDialog.setNegativeButton("取消", null);
		isplayDialog.show();
	}

	public String timeCast(int duration) {
		int musicTime = duration / 1000;
		return musicTime / 60 + ":" + musicTime % 60;
	}

	public String sizeCast(Long size) {
		return size / (1024 * 1024) + "MB";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

}
