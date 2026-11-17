package com.example.autoswipe;
import android.app.*;import android.os.*;import android.content.*;import android.provider.Settings;import android.net.Uri;import android.view.*;import android.widget.*;
public class MainActivity extends Activity {
 EditText interval,duration,count; Spinner direction;
 public void onCreate(Bundle b){super.onCreate(b);LinearLayout l=new LinearLayout(this);l.setOrientation(1);l.setPadding(30,30,30,30);ScrollView s=new ScrollView(this);s.addView(l);setContentView(s);
 TextView title=new TextView(this);title.setText("自动滑动助手");title.setTextSize(26);l.addView(title);
 direction=new Spinner(this);direction.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,new String[]{"向上滑动","向下滑动"}));l.addView(direction);
 interval=field(l,"滑动间隔（毫秒）","3000");duration=field(l,"滑动时长（毫秒）","500");count=field(l,"次数（0=无限）","100");
 button(l,"1 开启无障碍服务",()->startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
 button(l,"2 授权悬浮窗",()->startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:"+getPackageName()))));
 button(l,"3 开始滑动",()->{try{int i=Integer.parseInt(interval.getText().toString()),d=Integer.parseInt(duration.getText().toString()),c=Integer.parseInt(count.getText().toString());if(i<300||i>60000||d<100||d>3000||c<0||c>1000000)throw new Exception();getSharedPreferences("settings",0).edit().putInt("interval",i).putInt("duration",d).putInt("count",c).putInt("direction",direction.getSelectedItemPosition()).apply();if(SwipeAccessibilityService.instance==null){toast("请先开启无障碍服务");return;}if(!Settings.canDrawOverlays(this)){toast("请先授权悬浮窗");return;}SwipeAccessibilityService.instance.start();}catch(Exception e){toast("参数错误，请检查范围");}});
 button(l,"停止滑动",()->{if(SwipeAccessibilityService.instance!=null)SwipeAccessibilityService.instance.stop();});
 }
 EditText field(LinearLayout l,String label,String val){TextView t=new TextView(this);t.setText(label);l.addView(t);EditText e=new EditText(this);e.setInputType(2);e.setText(val);l.addView(e);return e;}
 void button(LinearLayout l,String name,Runnable r){Button b=new Button(this);b.setText(name);l.addView(b);b.setOnClickListener(v->r.run());}
 void toast(String t){Toast.makeText(this,t,Toast.LENGTH_LONG).show();}
}