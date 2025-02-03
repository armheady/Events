package com.northcoders.jv_events_platform_frontend.ui.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.northcoders.jv_events_platform_frontend.R
import com.northcoders.jv_events_platform_frontend.api.ApiClient

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.back_button).setOnClickListener {
            activity?.onBackPressed()
        }

        view.findViewById<Button>(R.id.google_login_button).setOnClickListener {
            val apiClient = ApiClient.getInstance()
            val loginUrl = apiClient.getLoginUrl()  // This will now include device_id and device_name
            
            val webView = WebView(requireContext())
            webView.settings.apply {
                javaScriptEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
            }

            // Handle redirect URL in WebView
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (url?.startsWith("your-redirect-scheme://") == true) {
                        // Handle OAuth callback here
                        // Extract tokens/data from URL
                        return true
                    }
                    return false
                }
            }

            // Create and show dialog with WebView
            AlertDialog.Builder(requireContext())
                .setView(webView)
                .setCancelable(true)
                .create()
                .also { dialog ->
                    webView.loadUrl(loginUrl)
                    dialog.show()
                }
        }
    }
}