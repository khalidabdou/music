package com.ringtones.compose.feature.admob

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.ringtones.compose.R
import com.ringtones.compose.utils.findActivity

var mInterstitialAd: InterstitialAd? = null
var countShow=-1
val showAd=5

// load the interstitial ad
fun loadInterstitial(context: Context) {
    InterstitialAd.load(
        context,
        context.getString(R.string.ad_id_interstitial),
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
                Log.d("MainActivity", adError.message)
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
                Log.d("MainActivity", "Ad was loaded.")
            }
        }
    )
}

// add the interstitial ad callbacks
fun addInterstitialCallbacks(context: Context) {
    mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            Log.d("MainActivity", "Ad failed to show.")
        }

        override fun onAdShowedFullScreenContent() {
            mInterstitialAd = null
            Log.d("MainActivity", "Ad showed fullscreen content.")

            loadInterstitial(context)
        }

        override fun onAdDismissedFullScreenContent() {
            Log.d("MainActivity", "Ad was dismissed.")
        }
    }
}

// show the interstitial ad
fun showInterstitial(context: Context) {
    countShow++
    if (mInterstitialAd != null) {
        if (countShow % showAd != 0)
            return
        val activity = context.findActivity()
        mInterstitialAd?.show(activity!!)
    } else {
        loadInterstitial(context)
        Log.d("MainActivity", "The interstitial ad wasn't ready yet.")
    }
}