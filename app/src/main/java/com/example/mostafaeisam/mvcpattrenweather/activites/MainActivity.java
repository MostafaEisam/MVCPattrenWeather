package com.example.mostafaeisam.mvcpattrenweather.activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mostafaeisam.mvcpattrenweather.R;
import com.example.mostafaeisam.mvcpattrenweather.responses.GetWeatherResponseModel;
import com.example.mostafaeisam.mvcpattrenweather.services.RequestListener;
import com.example.mostafaeisam.mvcpattrenweather.services.ServiceFactory;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RequestListener {
    @BindView(R.id.adView)
    AdView mAdView;
    @BindView(R.id.bt_syncWeatherData)
    Button mbtSyncWeatherData;
    @BindView(R.id.rv_weatherView)
    RelativeLayout mRvWeatherView;
    @BindView(R.id.tv_Location)
    TextView mTvLocation;
    @BindView(R.id.tv_lastUpdate)
    TextView mTvLastUpdate;
    @BindView(R.id.tv_weatherStatus)
    TextView mTvWeatherStatus;
    @BindView(R.id.tv_humidity)
    TextView mTvHumidity;
    @BindView(R.id.tv_temp)
    TextView mTvTemp;
    @BindView(R.id.iv_clouds)
    ImageView mIvClouds;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private SimpleDateFormat simpleDateFormat;
    private Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRvWeatherView.setVisibility(View.GONE);

        //Banner Ads
        MobileAds.initialize(this,
                "ca-app-pub-3940256099942544~3347511713");

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mbtSyncWeatherData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Controller controller = new Controller();
                controller.getCarsData();
                */
                progressBar.setVisibility(View.VISIBLE);
                getWeatherData();
                mbtSyncWeatherData.setVisibility(View.GONE);
                mRvWeatherView.setVisibility(View.VISIBLE);
            }
        });

    }

    public void getWeatherData (){
        ServiceFactory.getData(this,"http://api.apixu.com/v1/current.json?key=adbe3cafcb5743a4aa5104339181508&q=cairo",this);
    }

    @Override
    public void onSuccess(Object object) {
        progressBar.setVisibility(View.GONE);
        final GetWeatherResponseModel mWeatherResponse = new Gson().fromJson((String) object, GetWeatherResponseModel.class);

        mTvLocation.setText(mWeatherResponse.getLocation().getName());
        mTvTemp.setText(String.valueOf(mWeatherResponse.getCurrent().getTemp_c())+" °C");
        mTvHumidity.setText("Humidity : "+mWeatherResponse.getCurrent().getHumidity()+"%");
        mTvWeatherStatus.setText(mWeatherResponse.getCurrent().getCondition().getText());

        Long localtime_epoch  = Long.valueOf(mWeatherResponse.getCurrent().getLast_updated_epoch());
        simpleDateFormat= new SimpleDateFormat("dd MMMM yyyy  hh:mm:ss a");
        date            = new Date(localtime_epoch*1000);
        String Date        = simpleDateFormat.format(date);
        mTvLastUpdate.setText("Last Update : "+Date);

        Picasso.get()
                .load("http:"+mWeatherResponse.getCurrent().getCondition().getIcon())
                .fit()
                .into(mIvClouds);
    }

    @Override
    public void onFailure(int errorCode) {

    }
}
