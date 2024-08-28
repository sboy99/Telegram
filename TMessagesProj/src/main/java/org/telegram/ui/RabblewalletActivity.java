// hint : this is rabble wallet activity. When wallet icon click in chat screen for user then showing this screen . 
// Here i create a dummy screen when you eneter this screen then you are showing a meessage that is your information.
// function : userinformation for rabble wallet , access token and refresh token generation when expired , balence show when you click wallet icon 

package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RabblewalletActivity extends BaseFragment {

    private ImageView searchBtn;
    @Override
    public View createView(Context context) {

        fragmentView = LayoutInflater.from(context).inflate(R.layout.fragment_rabble_wallet, null);
        searchBtn = fragmentView.findViewById(R.id.searchBtn);

        actionBar.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);

        getWalletInfo(context);

        if (AndroidUtilities.isTablet()) {
            actionBar.setOccupyStatusBar(false);
        }
        actionBar.setAllowOverlayTitle(true);
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        searchBtn.setOnClickListener(v -> {
            getBalanceInfo(context);
        });

        return fragmentView;
    }

    public void getWalletInfo(Context context) {
        String url = "https://api-test.pluto.buidl.so/wallets/me";

        SharedPreferences sharedPreferences = context.getSharedPreferences("RabbleWallet", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accessToken", null);

        if (accessToken == null) {
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String prettyResponse = "";
                        try {
                            prettyResponse = response.toString(4);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        showDialog(context, prettyResponse);

//                        // Handle the response
//                        try {
//                            String id = response.getString("id");
//                            String telegramId = response.getString("telegramId");
//                            String evmWalletAddress = response.getString("evmWalletAddress");
//                            String solanaWalletAddress = response.getString("solanaWalletAddress");
//                            String createdAt = response.getString("createdAt");
//
//                            // Handle the retrieved data as needed
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                            refreshthetoken(context,"getWalletInfo");  // Call the refresh token method
                        }
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }


    public void getBalanceInfo(Context context) {
        String url = "https://api-test.pluto.buidl.so/wallets/balance";

        // Retrieve the access token from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("RabbleWallet", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accessToken", null);

        if (accessToken == null) {
            return;
        }

        // Create a GET request with the required network parameter
        Uri.Builder builder = Uri.parse(url).buildUpon();
        builder.appendQueryParameter("network", "ETHEREUM_MAINNET");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, builder.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String prettyResponse = "";
                        try {
                            prettyResponse = response.toString(4);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        showDialog(context, prettyResponse);

                        // Handle the response
//                        try {
//                            double ethBalance = response.optDouble("ETH", 0);
//                            Double usdcBalance = response.optDouble("USDC");
//                            Double usdtBalance = response.optDouble("USDT");
//                            Double opBalance = response.optDouble("OP");
//                            double maticBalance = response.optDouble("MATIC", 0);
//
//                            // Further processing of balance data if required
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle 401 Unauthorized response
                        if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                            refreshthetoken(context,"getBalanceInfo");  // Call the refresh token method
                        }
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }


    public void refreshthetoken(Context context,String funcionname) {
        String url = "https://api-test.pluto.buidl.so/auth/refresh";

        // Retrieve the refresh token from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("RabbleWallet", Context.MODE_PRIVATE);
        String refreshToken = sharedPreferences.getString("refreshToken", null);

        if (refreshToken == null) {
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PATCH, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String newAccessToken = response.getString("accessToken");
                            String newRefreshToken = response.getString("refreshToken");

                            // Save the new tokens in SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("accessToken", newAccessToken);
                            editor.putString("refreshToken", newRefreshToken);
                            editor.apply();

                            // Retry the original request after refreshing the token
                            if(Objects.equals(funcionname, "getWalletInfo")) {
                                getWalletInfo(context);
                            }
                            else if(Objects.equals(funcionname, "getBalanceInfo")) {
                                getBalanceInfo(context);
                            }
//
//                            String prettyResponse = "";
//                            try {
//                                prettyResponse = response.toString(4);
//                            } catch (JSONException e) {
//                                throw new RuntimeException(e);
//                            }
//                            showDialog(context, prettyResponse);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showDialog(context, error.toString());
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + refreshToken);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    public void showDialog(Context context, String dialogTxt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(dialogTxt)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

}
