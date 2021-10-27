package com.example.appcentnewsapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.appcentnewsapp.R


fun View.closeKeyboard() {
        val inputMethodManager =
            this.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
    }

@BindingAdapter("android:loadImage")
 fun  loadImage(view:ImageView,url: String?) {
    val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
    Glide.with(view).applyDefaultRequestOptions(requestOptions)
        .load(url)
        .placeholder(R.drawable.appcentnewsapp)
        .thumbnail(0.25f)
        .into(view)
}
@SuppressLint("StringFormatInvalid")
fun Context.sendInvite(url:String ) {
    val shareIntent = Intent()
    shareIntent.action = Intent.ACTION_SEND
    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.invitation))
    val urlMessage = getString(R.string.invite_message, packageName) + url
    shareIntent.putExtra(
        Intent.EXTRA_TEXT,
        urlMessage
    )
    shareIntent.type = "text/*"
    startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
}

@BindingAdapter("android:loadWebPage")
fun startWebView(webView:WebView,url: String) {

    webView.webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            url: String?,
        ): Boolean {
            url?.let {
                webView.loadUrl(it)
            }
            return true
        }
    }
    with(webView) {
        requestFocus()
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.javaScriptEnabled = true
        settings.textSize = android.webkit.WebSettings.TextSize.SMALLEST
        settings.cacheMode = android.webkit.WebSettings.LOAD_NO_CACHE
        isVerticalScrollBarEnabled = true
        settings.setSupportMultipleWindows(true)
        settings.defaultTextEncodingName = "utf-8"
        loadUrl(url)
    }
}


fun ImageView.downloadFromUrl(url:String?,progressDrawable: CircularProgressDrawable){
    val options= RequestOptions()
        .placeholder(progressDrawable
        )
        .error(R.mipmap.ic_launcher)
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)

}
fun placeHolderProgressBar(context:Context):CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth=8f
        centerRadius=40f
        start()
    }
}
@BindingAdapter("android:getImage")
fun getImage(view:ImageView,url:String?) {
    if (url?.length != 0 && url != null) {
        view.downloadFromUrl(url, placeHolderProgressBar(view.context))
    }
}



