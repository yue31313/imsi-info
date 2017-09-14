package com.example.aimsi;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

import android.content.Context;
import android.telephony.TelephonyManager;  

import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
    private TextView tv;   
    private Button btn;
    Context ctx;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);    

        tv = (TextView)findViewById(R.id.txtImsi);   
        btn = (Button)findViewById(R.id.btnImsi);  
        
        ctx=this.getApplicationContext();

        btn.setOnClickListener(new View.OnClickListener() {   

        public void onClick(View v) {   
            
            TelephonyManager tm = (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
            String sImei = tm.getDeviceId();       //取出IMEI
            String sImsi =tm.getSubscriberId();     //取出IMSI
            String sVer=tm.getDeviceSoftwareVersion();  //DeviceSoftwareVersion 
            String sMcc=sImsi.substring(0,3);  //MCC:Mobile Country Code
            String sMnc=sImsi.substring(3,5);  //MNC:Mobile Network Code
            String sMncD="";
            
            switch(Integer.parseInt(sMnc))
            {
            	case 00:sMncD="China Mobile:GSM 900/GSM 1800 UMTS (TD-SCDMA) 1880/2010\n";break;
            	case 01:sMncD="China Unicom:GSM 900/GSM 1800/ UMTS 2100\n";break;
            	case 02:sMncD="China Mobile:GSM 900/GSM 1800/ UMTS (TD-SCDMA) 1880/2010\n";break;
            	case 03:sMncD="China Telecom:CDMA 800/cdma evdo 2100\n";break;
            	case 05:sMncD="China Telecom\n";break;
            	case 06:sMncD="China Unicom:GSM 900/GSM 1800/UMTS 2100\n";break;
            	case 07:sMncD="China Mobile:GSM 900/GSM 1800/UMTS (TD-SCDMA) 1880/2010\n";break;
            	case 20:sMncD="China Unicom:GSM-R\n";break;
            	default:break;
            }
            
            StringBuffer inf = new StringBuffer();
            String tel = "";
    		switch(tm.getSimState()){       //getSimState()取得sim的状态  有下面6中状态  
    		case TelephonyManager.SIM_STATE_ABSENT :tel="PHONE CARD IS NOT EXISTS!";
    		case TelephonyManager.SIM_STATE_UNKNOWN :tel="PHONE CARD STATE UNKOWN!";
    		case TelephonyManager.SIM_STATE_NETWORK_LOCKED :tel="PHONE CARD NEED NETWORKPIN UNLOCK!";
    		case TelephonyManager.SIM_STATE_PIN_REQUIRED :tel="PHONE CARD NEED PIN UNLOCK!";
    		case TelephonyManager.SIM_STATE_PUK_REQUIRED :tel="PHONE CARD NEED PUK UNLOCK!";
    		case TelephonyManager.SIM_STATE_READY :break;
    		}
    		
    		if (String.valueOf(tel).equalsIgnoreCase("")){
    			tel = tm.getLine1Number();  //取出MSISDN，很可能为空
    		}
    		

    		String strNo="";
    		if (String.valueOf(tel).equalsIgnoreCase("")){
                strNo = "IMSI[International Mobile SubscriberIdentification Number]:\n"+sImsi+
                		" "+"\n"+"IMEI[International Mobile Equipment Identity]:\n"+sImei+"\n"+
                		"MCC[Mobile Country Code]:"+sMcc+"\nMNC[Mobile Network Code]:"+sMnc+"\n"+sMncD;  
    		}else{
    			strNo = "IMSI[International Mobile SubscriberIdentification Number]:\n"+sImsi+
    					" "+"\n"+"IMEI[International Mobile Equipment Identity]:\n"+sImei+"\n"+
    					"MCC[Mobile Country Code]:"+sMcc+"\nMNC[Mobile Network Code]:"+sMnc+"\n"+sMncD+"\n"+
    					"MSISDN[Mobile Subscriber International ISDN/PSTN number]:\n"+tel;   
    		}
    		
        	tv.setText(strNo); 
        	Log.i("LANHAI:",strNo);
        }   
        });
    } 


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}


/*
 国际移动用户识别码:IMSI：International Mobile SubscriberIdentification Number
 国际移动设备身份码:IMEI：International Mobile Equipment Identity
手机号码:MSISDN
MCC : Mobile Country Code
MNC : Mobile Network Code

 China - CN
 * MCC    MNC    Brand    Operator                Status        Bands (MHz)                                    References and notes
 * 460    00            China Mobile            Operational    GSM 900/GSM 1800 UMTS (TD-SCDMA) 1880/2010
 * 460    01            China Unicom            Operational    GSM 900/GSM 1800/ UMTS 2100                    CDMA network sold to China Telecom, WCDMA commercial trial started in May 2009 and in full commercial operation as of October 2009.
 * 460    02            China Mobile            Operational    GSM 900/GSM 1800/ UMTS (TD-SCDMA) 1880/2010    
 * 460    03            China Telecom            Operational    CDMA 800/cdma evdo 2100    
 * 460    05            China Telecom            Operational        
 * 460    06            China Unicom            Operational    GSM 900/GSM 1800/UMTS 2100    
 * 460    07            China Mobile            Operational    GSM 900/GSM 1800/UMTS (TD-SCDMA) 1880/2010    
 * 460    20            China Tietong            Operational    GSM-R    
 * NA    NA            China Telecom&China Unicom    Operational        


//将该功能放入一个单独的类中
    public class BasicInfo {
    	public BasicInfo (Context context)
    	{
    		this.context = context;
    	}

    	public String getPhoneNumber()
    	{
    		// 获取手机号 MSISDN，很可能为空
    		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    		StringBuffer inf = new StringBuffer();
    		switch(tm.getSimState()){ //getSimState()取得sim的状态  有下面6中状态  
    		case TelephonyManager.SIM_STATE_ABSENT :inf.append("无卡");return inf.toString();   
    		case TelephonyManager.SIM_STATE_UNKNOWN :inf.append("未知状态");return inf.toString(); 
    		case TelephonyManager.SIM_STATE_NETWORK_LOCKED :inf.append("需要NetworkPIN解锁");return inf.toString();  
    		case TelephonyManager.SIM_STATE_PIN_REQUIRED :inf.append("需要PIN解锁");return inf.toString();  
    		case TelephonyManager.SIM_STATE_PUK_REQUIRED :inf.append("需要PUK解锁");return inf.toString();  
    		case TelephonyManager.SIM_STATE_READY :break;  
    		}

    		String phoneNumber = tm.getLine1Number();
    		return phoneNumber;
    	}
    
}

 */