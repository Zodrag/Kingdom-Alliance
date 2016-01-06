package com.example.aaron.kingdomalliance;

import android.content.Intent;
import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication implements KingdomAlliance.KingdomAllianceCallback {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		KingdomAlliance kingdomAlliance = new KingdomAlliance();
		kingdomAlliance.setKingdomAllianceCallback(this);
		initialize(kingdomAlliance, config);
	}

	@Override
	public void onStartActivityLogin(){
		Intent i = new Intent(this, LoginActivity.class);
		startActivity(i);
	}
}
