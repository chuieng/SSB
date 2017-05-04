package com.acecodelabo.singaporesavingsbonds;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Chui Eng on 30/4/2017.
 */

public class HomeFragment extends Fragment {


    private static final String url = "http://www.sgs.gov.sg/savingsbonds/Your-SSB/This-months-bond.aspx";
    private ProgressDialog progressDialog;
    private AdView mAdView;
    private TextView hyperLink;
    private Spanned text;
    private View v;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_home, container, false);

        new Description().execute();
        hyperLink = (TextView)v.findViewById(R.id.refLink);

        text = Html.fromHtml("Reference:" +
                "<a href='http://www.sgs.gov.sg/savingsbonds.aspx'>http://www.sgs.gov.sg/savingsbonds.aspx</a>");

        hyperLink.setMovementMethod(LinkMovementMethod.getInstance());
        hyperLink.setText(text);


        mAdView = (AdView) v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        // Inflate the layout for this fragment
        return v;
    }

    private class Description extends AsyncTask<Void, Void, Void> {
        String intRate;
        String bondId;
        String period;
        String issueDate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Description");
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(url).get();

                Elements intRateDescr = document.select(".scroll-table tr:nth-child(2) td:nth-child(11) span");
                intRate = intRateDescr.html();

                Elements tables = document.select(".small-constraint table");
                Element issuanceTable = tables.get(0);
                Elements rows = issuanceTable.select("tr");
                //bond id
                Element row = rows.get(0);
                Elements cols = row.select("td");
                bondId = cols.get(0).text();

                //issue date
                Element issueDateRow = rows.get(2);
                Elements issueDateCols = issueDateRow.select("td");
                issueDate = issueDateCols.get(0).text();

                //period
                Element periodRow = rows.get(6);
                Elements periodCols = periodRow.select("td");
                period = periodCols.get(0).text();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            TextView txtDesc = (TextView) v.findViewById(R.id.rateText);
            txtDesc.setText(intRate + "% p.a.");

            TextView bondIdText = (TextView) v.findViewById(R.id.bondId);
            bondIdText.setText(bondId);

            TextView issueDateText = (TextView) v.findViewById(R.id.issueDate);
            issueDateText.setText(issueDate);

            TextView periodText = (TextView) v.findViewById(R.id.period);
            periodText.setText(period);
            progressDialog.dismiss();
        }
    }
}